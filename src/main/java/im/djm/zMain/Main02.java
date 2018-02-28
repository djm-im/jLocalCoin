package im.djm.zMain;

import static im.djm.zMain.StdoutUtil.printlnParagraph;

import java.util.ArrayList;
import java.util.List;

import im.djm.blockchain.BlockChain;
import im.djm.exception.TxException;
import im.djm.tx.Tx;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;

/**
 * 
 * @author djm.im
 *
 */
public class Main02 {

	public static void main(String[] args) {
		Wallet minerWallet = new Wallet(null);

		BlockChain blockChain = new BlockChain(minerWallet.getWalletAddress());
		minerWallet.setBlockchain(blockChain);

		Wallet w1 = new Wallet(blockChain);
		Wallet w2 = new Wallet(blockChain);
		Wallet w3 = new Wallet(blockChain);

		printlnParagraph("Wallets", "Miner " + minerWallet, "W1    " + w1, "W2    " + w2);

		List<Payment> pl0 = new ArrayList<>();
		pl0.add(new Payment(w1.getWalletAddress(), 100));
		Tx tx0 = minerWallet.send(pl0);
		printlnParagraph("New tx added to blockchain. Tx0: " + tx0);
		printlnParagraph("Blockchain after tx0", blockChain.toString());

		printlnParagraph("Miner coins: " + minerWallet.balance(), "W1 coins: " + w1.balance(),
				"W2 coins: " + w2.balance());

		List<Payment> pl1 = new ArrayList<>();
		pl1.add(new Payment(w2.getWalletAddress(), 50));
		Tx tx1 = w1.send(pl0);

		printlnParagraph("Tx1: " + tx1);
		printlnParagraph("Miner coins: " + minerWallet.balance(), "W1 coins: " + w1.balance(),
				"W2 coins: " + w2.balance(), "W3 coins: " + w3.balance());

		// blockchain status
		// _miner: 200, w1: 50, w2: 50, w3: 0

		try {
			List<Payment> pl2 = new ArrayList<>();
			pl2.add(new Payment(w2.getWalletAddress(), 200));
			Tx tx2 = w1.send(pl2);
			printlnParagraph("Tx2: " + tx2);
		} catch (TxException txEx) {
			printlnParagraph("Error: " + txEx.getMessage());
		}

		try {
			List<Payment> pl3 = new ArrayList<>();
			pl3.add(new Payment(w1.getWalletAddress(), 100));
			Tx tx3 = w3.send(pl3);
			printlnParagraph("Tx3: " + tx3);
		} catch (TxException txEx) {
			printlnParagraph("Error: " + txEx.getMessage());
		}

		printlnParagraph("Exit!");
	}
}
