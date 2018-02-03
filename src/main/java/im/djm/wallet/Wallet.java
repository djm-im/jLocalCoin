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
import im.djm.zMain.SoutUtil;

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

		SoutUtil.printlnParagraph("Get Utxo for " + this.walletAddress);
		List<Utxo> utxoList = this.blockChain.getUtxoFor(this.walletAddress);
		if (utxoList.isEmpty()) {
			SoutUtil.printlnParagraph("No Utxo: " + utxoList);

			// TODO
			// throw exception
			throw null;
		}

		SoutUtil.printlnParagraph("Yes Utxo: " + utxoList);

		long sum = 0;
		List<Utxo> spendOutputs = new ArrayList<>();
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

		return newTx;
	}

	private Tx createNewTx(WalletAddress walletAddress, long coinAmount, long sum, List<Utxo> spendOutputs)
			throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		Tx newTx = new Tx();
		this.fillInputs(newTx, spendOutputs);
		this.createOutput(walletAddress, coinAmount, newTx, sum);

		byte[] inputSign = newTx.getRawDataForSignature();
		byte[] signature = signTx(inputSign);
		newTx.addSignature(signature);
		this.blockChain.add(newTx, spendOutputs);

		return newTx;
	}

	private void fillInputs(Tx tx, List<Utxo> prevTxOutputs)
			throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		// TODO:
		// replace this loop by .forEach();
		for (int index = 0; index < prevTxOutputs.size(); index++) {
			Utxo utxo = prevTxOutputs.get(index);
			tx.addInput(utxo.getTxId(), utxo.getOutputIndexd());
		}
	}

	private byte[] signTx(byte[] rawDataToSign)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initSign(this.privateKey);

		sig.update(rawDataToSign);

		return sig.sign();
	}

	private void createOutput(WalletAddress walletAddress, long coinAmount, Tx tx, long sum)
			throws NoSuchAlgorithmException {

		tx.addOutput(walletAddress, coinAmount);
		if (sum > coinAmount) {
			tx.addOutput(this.walletAddress, sum - coinAmount);
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
