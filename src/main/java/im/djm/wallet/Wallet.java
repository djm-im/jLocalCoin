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

	/**
	 * @param blockChain
	 * @throws NoSuchAlgorithmException
	 */
	public Wallet(BlockChain blockChain) throws NoSuchAlgorithmException {
		this.blockChain = blockChain;

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(512);
		KeyPair generateKeyPair = keyGen.generateKeyPair();

		this.walletAddress = new WalletAddress((RSAPublicKey) generateKeyPair.getPublic());
		this.privateKey = generateKeyPair.getPrivate();
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
	 * @param coinAmount
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public Tx sendCoin(WalletAddress walletAddress, long coinAmount)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		List<Utxo> utxoList = this.blockChain.getUtxoFor(this.walletAddress);
		List<Utxo> spendOutputs = new ArrayList<>();
		long sum = 0;
		int index = 0;
		while (sum < coinAmount && index < utxoList.size()) {
			Utxo utxo = utxoList.get(index);
			spendOutputs.add(utxo);
			index++;

			Tx tx = this.blockChain.getTxFromPool(utxo.getTxId());
			Output txOutput = tx.getOutput(utxo.getOutputIndexd());
			long coinValue = txOutput.getCoinValue();
			sum += coinValue;
		}

		if (sum < coinAmount) {
			throw new TxException("Not enough coins for tx. Tried to send " + coinAmount + ". Utxo is " + sum + ".");
		}

		Tx newTx = createNewTx(walletAddress, coinAmount, sum, spendOutputs);
		this.blockChain.add(newTx, spendOutputs);

		return newTx;
	}

	private Tx createNewTx(WalletAddress walletAddress, long coinAmount, long sum, List<Utxo> spendOutputs)
			throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		Tx newTx = new Tx();
		this.fillInputs(newTx, spendOutputs);
		this.createOutput(walletAddress, coinAmount, newTx, sum);
		this.signTx(newTx);

		return newTx;
	}

	private void fillInputs(Tx tx, List<Utxo> prevTxOutputs)
			throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		prevTxOutputs.forEach(utxo -> {
			tx.addInput(utxo.getTxId(), utxo.getOutputIndexd());
		});
	}

	private void createOutput(WalletAddress walletAddress, long coinAmount, Tx tx, long sum)
			throws NoSuchAlgorithmException {
		tx.addOutput(walletAddress, coinAmount);
		if (sum > coinAmount) {
			tx.addOutput(this.walletAddress, sum - coinAmount);
		}
	}

	private void signTx(Tx newTx) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		byte[] inputSign = newTx.getRawDataForSignature();
		byte[] signature = txSignature(inputSign);
		newTx.addSignature(signature);
	}

	private byte[] txSignature(byte[] rawDataToSign)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(this.privateKey);
		signature.update(rawDataToSign);

		return signature.sign();
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
