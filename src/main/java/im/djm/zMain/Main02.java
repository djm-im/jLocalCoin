package im.djm.zMain;

import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import im.djm.blockchain.BlockChain;
import im.djm.tx.Tx;
import im.djm.wallet.Wallet;

import static im.djm.zMain.SoutUtil.printlnParagraph;

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

		printlnParagraph("Wallets", "Miner " + minerWallet, "W1    " + w1, "W2    " + w2);

		Tx tx0 = minerWallet.sendCoin(w1.getWalletAddress(), 100);
		printlnParagraph("New tx added to blockchain. Tx0: " + tx0);
		printlnParagraph("Blockchain after tx0", blockChain.toString());

		printlnParagraph("Miner coins: " + minerWallet.balance(), "W1 coins: " + w1.balance(),
				"W2 coins: " + w2.balance());

		Tx tx1 = w1.sendCoin(w2.getWalletAddress(), 50);

		printlnParagraph("Tx1: " + tx1);
		printlnParagraph("Miner coins: " + minerWallet.balance(), "W1 coins: " + w1.balance(),
				"W2 coins: " + w2.balance());

		printlnParagraph("Wallets", "Miner " + minerWallet, "W1    " + w1, "W2    " + w2);
	}
}
