package im.djm.zMain;

import static im.djm.zMain.SoutUtil.printlnParagraph;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import im.djm.blockchain.BlockChain;
import im.djm.exception.TxException;
import im.djm.tx.Tx;
import im.djm.wallet.Wallet;

/**
 * 
 * @author djm.im
 *
 */
public class Main02 {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Wallet minerWallet = new Wallet(null);

		BlockChain blockChain = new BlockChain(minerWallet.getWalletAddress());
		minerWallet.setBlockchain(blockChain);
		blockChain.setMiner(minerWallet.getWalletAddress());

		Wallet w1 = new Wallet(blockChain);
		Wallet w2 = new Wallet(blockChain);
		Wallet w3 = new Wallet(blockChain);

		printlnParagraph("Wallets", "Miner " + minerWallet, "W1    " + w1, "W2    " + w2);

		Tx tx0 = minerWallet.sendCoin(w1.getWalletAddress(), 100);
		printlnParagraph("New tx added to blockchain. Tx0: " + tx0);
		printlnParagraph("Blockchain after tx0", blockChain.toString());

		printlnParagraph("Miner coins: " + minerWallet.balance(), "W1 coins: " + w1.balance(),
				"W2 coins: " + w2.balance());

		Tx tx1 = w1.sendCoin(w2.getWalletAddress(), 50);

		printlnParagraph("Tx1: " + tx1);
		printlnParagraph("Miner coins: " + minerWallet.balance(), "W1 coins: " + w1.balance(),
				"W2 coins: " + w2.balance(), "W3 coins: " + w3.balance());

		// blockchain status
		// _miner: 200, w1: 50, w2: 50, w3: 0

		try {
			Tx tx2 = w1.sendCoin(w2.getWalletAddress(), 200);
			printlnParagraph("Tx2: " + tx2);
		} catch (TxException txEx) {
			printlnParagraph("Error: " + txEx.getMessage());
		}

		try {
			Tx tx3 = w3.sendCoin(w1.getWalletAddress(), 100);
			printlnParagraph("Tx3: " + tx3);
		} catch (TxException txEx) {
			printlnParagraph("Error: " + txEx.getMessage());
		}

		printlnParagraph("Exit!");
	}
}
