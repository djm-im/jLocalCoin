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
import im.djm.exception.TxException;
import im.djm.exception.WrapperException;
import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.tx.Utxo;

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

	/**
	 * 
	 * @param blockChain
	 * @param walletAddress
	 * @param coinValue
	 * @return
	 */
	public Tx sendCoin(WalletAddress walletAddress, long coinValue) {
		if (coinValue < 1) {
			throw new TxException("Cannot send less or zero value for coin. Tried to send " + coinValue + ".");
		}

		List<Utxo> utxoList = this.blockChain.getUtxoFor(this.walletAddress);
		List<Utxo> spendOutputs = new ArrayList<>();
		long sum = 0;
		int index = 0;
		while (sum < coinValue && index < utxoList.size()) {
			Utxo utxo = utxoList.get(index);
			spendOutputs.add(utxo);
			index++;

			Tx tx = this.blockChain.getTxFromPool(utxo.getTxId());
			Output txOutput = tx.getOutput(utxo.getOutputIndexd());
			long outputCoinValue = txOutput.getCoinValue();
			sum += outputCoinValue;
		}

		if (sum < coinValue) {
			throw new TxException("Not enough coins for tx. Tried to send " + coinValue + ". Utxo is " + sum + ".");
		}

		Tx newTx = createNewTx(walletAddress, coinValue, sum, spendOutputs);
		this.blockChain.add(newTx, spendOutputs);

		return newTx;
	}

	private Tx createNewTx(WalletAddress walletAddress, long coinAmount, long sum, List<Utxo> spendOutputs) {
		Tx newTx = new Tx();
		this.fillInputs(newTx, spendOutputs);
		this.createOutput(walletAddress, coinAmount, newTx, sum);
		this.signTx(newTx);

		return newTx;
	}

	private void fillInputs(Tx tx, List<Utxo> prevTxOutputs) {
		prevTxOutputs.forEach(utxo -> {
			tx.addInput(utxo.getTxId(), utxo.getOutputIndexd());
		});
	}

	private void createOutput(WalletAddress walletAddress, long coinAmount, Tx tx, long sum) {
		tx.addOutput(walletAddress, coinAmount);
		if (sum > coinAmount) {
			tx.addOutput(this.walletAddress, sum - coinAmount);
		}
	}

	private void signTx(Tx newTx) {
		byte[] inputSign = newTx.getRawDataForSignature();
		byte[] signature = txSignature(inputSign);
		newTx.addSignature(signature);
	}

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
