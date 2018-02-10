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

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.hash.TxHash;
import im.djm.exception.TxException;
import im.djm.exception.WrapperException;
import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;

/**
 * 
 * @author djm.im
 *
 */
public class Wallet {

	private WalletAddress walletAddress;

	private PrivateKey privateKey;

	private BlockChain blockChain;

	public Wallet(BlockChain blockChain) {
		this.blockChain = blockChain;

		KeyPair keyPair = createKeyPair();
		this.walletAddress = new WalletAddress((RSAPublicKey) keyPair.getPublic());

		this.privateKey = keyPair.getPrivate();
	}

	private KeyPair createKeyPair() {
		try {
			// TOOD
			// Generalize this method
			// "RSA" and 512 move to constants
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(512);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			return keyPair;
		} catch (NoSuchAlgorithmException ex) {
			throw new WrapperException("Tried to create a new wallet", ex);
		}
	}

	public void setBlockchain(BlockChain blockChain) {
		this.blockChain = blockChain;
	}

	public WalletAddress getWalletAddress() {
		return this.walletAddress;
	}

	public Tx send(List<Payment> payments) {
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
		this.blockChain.add(newTx, spentOutputs);
		return newTx;
	}

	private Tx createNewTx(List<Payment> payments, List<Utxo> spentOutputs, long senderBalance, long totalSpent) {
		Tx newTx = new Tx();
		this.fillInputs(newTx, spentOutputs);

		payments = this.adddChangeToOutputs(payments, senderBalance, totalSpent);
		this.createOutputs(newTx, payments);
		this.signTx(newTx);

		return newTx;
	}

	private List<Payment> adddChangeToOutputs(List<Payment> payments, long senderBalance, long totalSpent) {
		// TODO
		// Throw exception if senderBalance < totalSpent
		if (senderBalance > totalSpent) {
			long change = senderBalance - totalSpent;
			payments.add(new Payment(this.walletAddress, change));
		}

		return payments;
	}

	private void fillInputs(Tx tx, List<Utxo> prevTxOutputs) {
		prevTxOutputs.forEach(utxo -> {
			tx.addInput(utxo.getTxId(), utxo.getOutputIndexd());
		});
	}

	private void createOutputs(Tx tx, List<Payment> payments) {
		payments.forEach(payment -> {
			tx.addOutput(payment.getWalletAddress(), payment.getCoinValue());
		});
	}

	private void signTx(Tx newTx) {
		byte[] inputSign = newTx.getRawDataForSignature();
		byte[] signature = txSignature(inputSign);
		newTx.addSignature(signature);
	}

	// TODO
	// Make this method static
	private byte[] txSignature(byte[] rawDataToSign) {
		try {
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(this.privateKey);
			signature.update(rawDataToSign);

			return signature.sign();
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
			throw new WrapperException("txSignature", ex);
		}
	}

	/**
	 * 
	 * @param blockChain
	 * @return
	 *
	 * 		Return number of coins that Wallet address can spend
	 */
	public long balance() {
		return this.blockChain.getBalance(this.walletAddress);
	}

	@Override
	public String toString() {
		return "{ Wallet : " + this.walletAddress + " }";
	}
}
