package im.djm.node;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import im.djm.blockchain.BlockChain;
import im.djm.wallet.Wallet;

/**
 * 
 * @author djm.im
 *
 */
public class NodeCli implements Runnable {

	private static final String CMD_WDEL = "wdel";

	private static final String CMD_WLIST = "wlist";

	private static final String CMD_WNEW = "wnew";

	private static final String CMD_SEND = "send";

	private static final String CMD_EXIT = "exit";

	private static final String CMD_HELP = "help";

	private BlockChain blockchain;

	private Wallet minerWallet;

	private Map<String, Wallet> wallets = new HashMap<>();

	public NodeCli() throws NoSuchAlgorithmException {
		this.minerWallet = new Wallet(null);
		this.blockchain = new BlockChain(minerWallet.getWalletAddress());
		this.minerWallet.setBlockchain(this.blockchain);

		this.wallets.put("_miner", this.minerWallet);
	}

	@Override
	public void run() {
		boolean isRunning = true;
		Scanner input = new Scanner(System.in);
		loop: while (isRunning) {
			System.out.println("[BC length: " + blockchain.status() + "].$ ");
			if (input.hasNext()) {

			}
			String inLine = input.nextLine();
			String[] cmdLine = inLine.split("\\s+");

			String cmdName = cmdLine[0].trim();
			// TODO
			// extract constants for command names
			switch (cmdName) {
			case CMD_HELP:
				printHelp();
				break;
			case CMD_EXIT:
				isRunning = false;
				// input.close();
				break loop;
			case CMD_SEND:
				try {
					sendCoins(cmdLine);
				} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e1) {
					// TODO
					// Use exception wrapper
					e1.printStackTrace();
				}
				break;
			case CMD_WNEW:
				try {
					createNewWallet(cmdLine);
				} catch (NoSuchAlgorithmException e) {
					// TODO
					// Use exception wrapper
					e.printStackTrace();
				}
				break;
			case CMD_WLIST:
				listAllWallets();
				break;
			case CMD_WDEL:
				deleteWallet(cmdLine);
				break;
			default:
				System.out.println("Unknow command.");
				System.out.println("Type help.");
				break;
			}
			System.out.println();
			System.out.println();
		}

		System.out.println("Good by blockchain");
	}

	private void sendCoins(String[] cmdLine) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		if (cmdLine.length != 4) {
			System.out.println("Commnad forma");
			System.out.println("send WALLET-NAME-1 WALLET-NAME-2 VALUE");
			return;
		}

		String wallet1Name = cmdLine[1].trim();
		if (!this.wallets.containsKey(wallet1Name)) {
			System.out.println("Wallet " + wallet1Name + " not exists in collection.");
			return;
		}

		String wallet2Name = cmdLine[2].trim();
		if (!this.wallets.containsKey(wallet2Name)) {
			System.out.println("Wallet " + wallet2Name + " does not exist in collection of wallets.");
			return;
		}

		Wallet wallet1 = this.wallets.get(wallet1Name);
		Wallet wallet2 = this.wallets.get(wallet2Name);
		int value = Integer.valueOf(cmdLine[3]);

		wallet1.sendCoin(wallet2.getWalletAddress(), value);
	}

	private void deleteWallet(String[] cmdLine) {
		if (cmdLine.length != 2) {
			System.out.println("Commnad forma");
			System.out.println("wnew WALLET_NAME");
			return;
		}

		// TODO
		// Add question to user confirm
		this.wallets.remove(cmdLine[1].trim());
	}

	private void listAllWallets() {
		this.wallets.forEach((walletName, wallet) -> {
			System.out.println("{ Wallet Name: " + walletName + " " + wallet + ", Balance: " + wallet.balance() + " }");
		});
	}

	private void createNewWallet(String[] cmdLine) throws NoSuchAlgorithmException {
		if (cmdLine.length != 2) {
			System.out.println("Commnad forma");
			System.out.println("wnew WALLET_NAME");
			System.out.println();
			return;
		}

		String walletName = cmdLine[1].trim();
		if (this.wallets.containsKey(walletName)) {
			System.out.println("Wallet with name " + walletName + " already exists.");
		}

		this.wallets.put(walletName, new Wallet(this.blockchain));
	}

	private static void printHelp() {
		// TODO
		// create help map command name -> Description

		System.out.println("Java Blockchain implementation");
		System.out.println("Help commands");
		System.out.println(CMD_HELP + "\t - Help");
		System.out.println();

		System.out.println("Blockchain commands");
		System.out.println(CMD_EXIT + "\t - Stop and exit from the program.");
		System.out.println();

		System.out.println(CMD_SEND + "\t - Send coins from one to another wallet.");
		System.out.println("\t send WALLET-NAME-1 WLLET-NAME-2 VALUE ");
		System.out.println();

		System.out.println("Wallet commands");
		System.out.println(CMD_WNEW + "\t - Create a new wallet");
		System.out.println("\t wnew WALLET-NAME");
		System.out.println();

		System.out.println(CMD_WLIST + "\t - List walletes and balances for each wallet");
		System.out.println();

		System.out.println(CMD_WDEL + "\t - Delete a wallet");
		System.out.println("\t wdel WALLET-NAME");
		System.out.println();
	}

}
