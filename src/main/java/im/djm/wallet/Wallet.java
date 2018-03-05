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

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.hash.TxHash;
import im.djm.exception.NullBlockChainException;
import im.djm.exception.TxException;
import im.djm.exception.WrapperException;
import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;

/**
 * @author djm.im
 */
public class Wallet {

	private WalletAddress walletAddress;

	private PrivateKey privateKey;

	private BlockChain blockChain;

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

	public Wallet setBlockchain(BlockChain blockChain) {
		this.blockChain = blockChain;
		return this;
	}

	public WalletAddress address() {
		return this.walletAddress;
	}

	public Tx send(List<Payment> payments) {
		// TODO
		// Use immutable payments ==> Java 9
		if (this.blockChain == null) {
			throw new NullBlockChainException("Cannot send coins: blockchain is not set.");
		}

		long totalSpent = totalSpent(payments);

		List<Utxo> utxoList = this.blockChain.getUtxoFor(this.walletAddress);

		List<Utxo> spentOutputs = new ArrayList<>();
		long senderBalance = 0;
		int index = 0;
		while (senderBalance < totalSpent && index < utxoList.size()) {
			Utxo utxo = utxoList.get(index);
			spentOutputs.add(utxo);

			senderBalance = senderBalance + getCoinValueFor(utxo);

			index++;
		}

		Tx newTx = createAndAddTx(payments, totalSpent, spentOutputs, senderBalance);
		return newTx;
	}

	private long getCoinValueFor(Utxo utxo) {
		TxHash txId = utxo.getTxId();
		Tx txFromPool = this.blockChain.getTxFromPool(txId);
		Output txOutput = txFromPool.getOutput(utxo.getOutputIndexd());
		long coinValue = txOutput.getCoinValue();

		return coinValue;
	}

	private long totalSpent(List<Payment> payments) {
		long totalSpent = 0;
		for (Payment payment : payments) {
			if (payment == null) {
				throw new NullPaymentException("Payment cannot be null.");
			}

			long coinValue = payment.getCoinValue();
			if (coinValue <= 0) {
				throw new TxException("Cannot send zero or less value for coin. Tried to send " + coinValue + ".");
			}

			totalSpent = totalSpent + coinValue;
		}

		return totalSpent;
	}

	private Tx createAndAddTx(List<Payment> payments, long totalSpent, List<Utxo> spentOutputs, long senderBalance) {
		if (senderBalance < totalSpent) {
			String errMsg = "Not enough coins for tx. Tried to send " + totalSpent + ". Utxo is " + senderBalance + ".";
			throw new TxException(errMsg);
		}

		Tx newTx = createNewTx(payments, spentOutputs, senderBalance, totalSpent);
		this.blockChain.add(newTx);

		return newTx;
	}

	private Tx createNewTx(List<Payment> payments, List<Utxo> spentOutputs, long senderBalance, long totalSpent) {
		Tx newTx = fillInputsAndSign(spentOutputs);

		List<Payment> allPayments = addChangePayment(payments, senderBalance, totalSpent);

		this.createOutputs(newTx, allPayments);

		return newTx;
	}

	private List<Payment> addChangePayment(List<Payment> payments, long senderBalance, long totalSpent) {
		if (senderBalance == totalSpent) {
			return payments;
		}

		// TODO
		// Throw exception if senderBalance < totalSpent
		// if (senderBalance > totalSpent) {
		Payment changePayment = this.createChangePayment(senderBalance, totalSpent);
		List<Payment> newPayments = Lists.newArrayList(payments);
		newPayments.add(changePayment);

		return newPayments;
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

	private void createOutputs(Tx tx, List<Payment> payments) {
		payments.forEach(payment -> {
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
		if (this.blockChain == null) {
			throw new NullBlockChainException("Cannot check balance: blockchain is not set.");
		}

		return this.blockChain.getBalance(this.walletAddress);
	}

	@Override
	public String toString() {
		return "{ Wallet : " + this.walletAddress + " }";
	}

}
