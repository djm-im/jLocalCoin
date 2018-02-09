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
import java.util.Map;
import java.util.stream.Collectors;

import im.djm.blockchain.BlockChain;
import im.djm.exception.TxException;
import im.djm.exception.WrapperException;
import im.djm.node.Payment;
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

	public Tx sendMultiCoins(Map<String, Payment> paymentMap) {
		long totalSpent = paymentMap.values().stream()
				.collect(Collectors.summarizingLong(payment -> payment.getCoinValue())).getSum();
		if (totalSpent < 1) {
			throw new TxException("Cannot send less or zero value for coin. Tried to send " + totalSpent + ".");
		}

		List<Utxo> utxoList = this.blockChain.getUtxoFor(this.walletAddress);
		List<Utxo> spentOutputs = new ArrayList<>();
		long sum = 0;
		int index = 0;
		while (sum < totalSpent && index < utxoList.size()) {
			Utxo utxo = utxoList.get(index);
			spentOutputs.add(utxo);
			index++;

			Tx tx = this.blockChain.getTxFromPool(utxo.getTxId());
			Output txOutput = tx.getOutput(utxo.getOutputIndexd());
			long outputCoinValue = txOutput.getCoinValue();
			sum += outputCoinValue;
		}

		if (sum < totalSpent) {
			throw new TxException("Not enough coins for tx. Tried to send " + totalSpent + ". Utxo is " + sum + ".");
		}

		Tx newTx = createMultiOutputTx(paymentMap, spentOutputs, sum, totalSpent);
		this.blockChain.add(newTx, spentOutputs);

		return newTx;
	}

	private Tx createMultiOutputTx(Map<String, Payment> paymentMap, List<Utxo> spendOutputs, long sum,
			long totalSpent) {
		Tx newTx = new Tx();
		this.fillInputs(newTx, spendOutputs);
		this.createMultipleOutput(newTx, paymentMap, sum, totalSpent);
		this.signTx(newTx);

		return newTx;
	}

	private void createMultipleOutput(Tx tx, Map<String, Payment> paymentMap, long sum, long totalSpent) {
		paymentMap.forEach((walletName, payment) -> {
			long coinValue = payment.getCoinValue();
			tx.addOutput(payment.getWalletAddress(), coinValue);
		});
		if (sum > totalSpent) {
			tx.addOutput(this.walletAddress, sum - totalSpent);
		}
	}

	public Tx sendCoin(WalletAddress recieverAddress, long coinValue) {
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

		Tx newTx = createNewTx(recieverAddress, coinValue, sum, spendOutputs);
		this.blockChain.add(newTx, spendOutputs);

		return newTx;
	}

	private Tx createNewTx(WalletAddress receiverAddress, long coinAmount, long sum, List<Utxo> spendOutputs) {
		Tx newTx = new Tx();
		this.fillInputs(newTx, spendOutputs);
		this.createOutput(newTx, receiverAddress, coinAmount, sum);
		this.signTx(newTx);

		return newTx;
	}

	private void fillInputs(Tx tx, List<Utxo> prevTxOutputs) {
		prevTxOutputs.forEach(utxo -> {
			tx.addInput(utxo.getTxId(), utxo.getOutputIndexd());
		});
	}

	private void createOutput(Tx tx, WalletAddress receiverAddress, long coinAmount, long sum) {
		tx.addOutput(receiverAddress, coinAmount);
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
