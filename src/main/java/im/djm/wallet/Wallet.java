package im.djm.wallet;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import im.djm.blockchain.exception.WrapperException;
import im.djm.coin.tx.Output;
import im.djm.coin.tx.Tx;
import im.djm.coin.tx.TxException;
import im.djm.coin.txhash.TxHash;
import im.djm.coin.utxo.Utxo;
import im.djm.p2p.node.BlockChainNode;
import im.djm.p2p.node.NullBlockChainNodeException;

/**
 * @author djm.im
 */
public class Wallet {

	private WalletAddress walletAddress;

	private PrivateKey privateKey;

	private BlockChainNode blockChainNode;

	public static Wallet createNewWallet() {
		return new Wallet();
	}

	private Wallet() {
		KeyPair keyPair = createKeyPair();

		this.walletAddress = new WalletAddress((RSAPublicKey) keyPair.getPublic());

		this.privateKey = keyPair.getPrivate();
	}

	private KeyPair createKeyPair() {
		try {
			String keyPairAlgorithm = "RSA";
			int keysize = 512;

			return createKeyPair(keyPairAlgorithm, keysize);
		} catch (NoSuchAlgorithmException ex) {
			throw new WrapperException("Tried to create a new wallet", ex);
		}
	}

	private KeyPair createKeyPair(String keyPairAlgorithm, int keysize) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyPairAlgorithm);
		keyPairGenerator.initialize(keysize);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		return keyPair;
	}

	public Wallet setBlockchainNode(BlockChainNode blockChainNode) {
		this.blockChainNode = blockChainNode;

		return this;
	}

	public WalletAddress address() {
		return this.walletAddress;
	}

	public Tx send(List<Payment> payments) {
		if (this.blockChainNode == null) {
			throw new NullBlockChainNodeException("Cannot send coins: BlockChainNode is not set.");
		}

		PaymentList paymentList = new PaymentList(payments);

		Bill bill = createBill(paymentList);

		Tx newTx = createAndAddTx(paymentList, bill);

		return newTx;
	}

	private Bill createBill(PaymentList paymentList) {
		List<Utxo> utxoList = this.blockChainNode.getUtxoFor(this.walletAddress);

		List<Utxo> spentOutputs = new ArrayList<>();
		long senderBalance = 0;
		int index = 0;
		while (senderBalance < paymentList.total() && index < utxoList.size()) {
			Utxo utxo = utxoList.get(index);
			spentOutputs.add(utxo);

			senderBalance = senderBalance + getCoinValueFor(utxo);

			index++;
		}

		return new Bill(spentOutputs, senderBalance);
	}

	private long getCoinValueFor(Utxo utxo) {
		TxHash txId = utxo.getTxId();
		Tx txFromPool = this.blockChainNode.getTxFromPool(txId);
		Output txOutput = txFromPool.getOutput(utxo.getOutputIndexd());
		long coinValue = txOutput.getCoinValue();

		return coinValue;
	}

	private Tx createAndAddTx(PaymentList paymentList, Bill bill) {
		hasEnoughUtxo(paymentList, bill);

		Tx newTx = createNewTx(paymentList, bill);

		this.blockChainNode.sendCoin(newTx);

		return newTx;
	}

	private void hasEnoughUtxo(PaymentList paymentList, Bill bill) {
		long senderBalance = bill.getSenderBalance();
		long totalSpent = paymentList.total();

		if (senderBalance < totalSpent) {
			String errMsg = "Not enough coins for tx. Tried to send " + totalSpent + ". Utxo is " + senderBalance + ".";

			throw new TxException(errMsg);
		}
	}

	private Tx createNewTx(PaymentList paymentList, Bill bill) {
		Tx newTx = fillInputsAndSign(bill.getSpentOutputs());

		PaymentList paymentListWithChange = addChangePayment(paymentList, bill);

		this.createOutputs(newTx, paymentListWithChange);

		return newTx;
	}

	private PaymentList addChangePayment(PaymentList paymentList, Bill bill) {
		if (bill.getSenderBalance() == paymentList.total()) {
			return paymentList;
		}

		// if (senderBalance > totalSpent) {
		Payment changePayment = this.createChangePayment(paymentList.total(), bill);
		List<Payment> paymentsWithChange = Lists.newArrayList(paymentList.getPayments());
		paymentsWithChange.add(changePayment);

		return new PaymentList(paymentsWithChange);
	}

	private Tx fillInputsAndSign(List<Utxo> spentOutputs) {
		Tx newTx = this.fillInputs(spentOutputs);

		newTx = this.signTx(newTx);

		return newTx;
	}

	private Payment createChangePayment(long totalSpent, Bill bill) {
		long change = bill.getSenderBalance() - totalSpent;
		Payment changePayment = new Payment(this.walletAddress, change);

		return changePayment;
	}

	private Tx fillInputs(List<Utxo> txOutputs) {
		Tx tx = new Tx();
		txOutputs.forEach(utxo -> {
			tx.addInput(utxo.getTxId(), utxo.getOutputIndexd());
		});

		return tx;
	}

	private void createOutputs(Tx tx, PaymentList allPaymentList) {
		allPaymentList.getPayments().forEach(payment -> {
			tx.addOutput(payment.getWalletAddress(), payment.getCoinValue());
		});
	}

	private Tx signTx(Tx newTx) {
		byte[] inputSign = newTx.getRawDataForSignature();
		byte[] signature = txSignature(inputSign);

		newTx.addSignature(signature);

		return newTx;
	}

	private byte[] txSignature(byte[] rawDataToSign) {
		try {
			String signatureAlgorithm = "SHA256withRSA";

			return sign(rawDataToSign, signatureAlgorithm);
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
			throw new WrapperException("txSignature", ex);
		}
	}

	private byte[] sign(byte[] rawDataToSign, String signatureAlgorithm)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		Signature signature = Signature.getInstance(signatureAlgorithm);
		signature.initSign(this.privateKey);
		signature.update(rawDataToSign);

		return signature.sign();
	}

	public long balance() {
		if (this.blockChainNode == null) {
			throw new NullBlockChainNodeException("Cannot check balance: BlockChainNode is not set.");
		}

		return this.blockChainNode.getBalance(this.walletAddress);
	}

	@Override
	public String toString() {
		return "{ Wallet : " + this.walletAddress + " }";
	}

}
