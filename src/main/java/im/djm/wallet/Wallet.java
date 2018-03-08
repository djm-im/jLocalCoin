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

import im.djm.exception.WrapperException;
import im.djm.node.BlockChainNode;
import im.djm.node.NullBlockChainNodeException;
import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.tx.TxException;
import im.djm.txhash.TxHash;
import im.djm.utxo.Utxo;

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
		PaymentList paymentList = new PaymentList(payments);

		// TODO
		// Use immutable payments ==> Java 9
		if (this.blockChainNode == null) {
			throw new NullBlockChainNodeException("Cannot send coins: BlockChainNode is not set.");
		}

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

		Tx newTx = createAndAddTx(paymentList, spentOutputs, senderBalance);
		return newTx;
	}

	private long getCoinValueFor(Utxo utxo) {
		TxHash txId = utxo.getTxId();
		Tx txFromPool = this.blockChainNode.getTxFromPool(txId);
		Output txOutput = txFromPool.getOutput(utxo.getOutputIndexd());
		long coinValue = txOutput.getCoinValue();

		return coinValue;
	}

	private Tx createAndAddTx(PaymentList paymentList, List<Utxo> spentOutputs, long senderBalance) {

		if (senderBalance < paymentList.total()) {
			String errMsg = "Not enough coins for tx. Tried to send " + paymentList.total() + ". Utxo is "
					+ senderBalance + ".";
			throw new TxException(errMsg);
		}

		Tx newTx = createNewTx(paymentList, spentOutputs, senderBalance);

		this.blockChainNode.sendCoin(newTx);

		return newTx;
	}

	private Tx createNewTx(PaymentList paymentList, List<Utxo> spentOutputs, long senderBalance) {
		Tx newTx = fillInputsAndSign(spentOutputs);

		PaymentList paymentListWithChange = addChangePayment(paymentList, senderBalance);

		this.createOutputs(newTx, paymentListWithChange);

		return newTx;
	}

	private PaymentList addChangePayment(PaymentList paymentList, long senderBalance) {
		if (senderBalance == paymentList.total()) {
			return paymentList;
		}

		// if (senderBalance > totalSpent) {
		Payment changePayment = this.createChangePayment(senderBalance, paymentList.total());
		List<Payment> paymentsWithChange = Lists.newArrayList(paymentList.getPayments());
		paymentsWithChange.add(changePayment);

		return new PaymentList(paymentsWithChange);
	}

	private Tx fillInputsAndSign(List<Utxo> spentOutputs) {
		Tx newTx = this.fillInputs(spentOutputs);

		newTx = this.signTx(newTx);

		return newTx;
	}

	private Payment createChangePayment(long senderBalance, long totalSpent) {
		long change = senderBalance - totalSpent;
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
