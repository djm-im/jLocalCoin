package im.djm.node;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import im.djm.blockchain.BlockChain;
import im.djm.exception.TxException;
import im.djm.wallet.Wallet;

/**
 * 
 * @author djm.im
 *
 */
public class NodeCli implements Runnable {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String TAB_SIGN = "\t";

	private static final String MINER_WALLET_NAME = "_miner";

	private static final String CMD_HELP = "help";

	private static final String CMD_EXIT = "exit";

	private static final String CMD_WNEW = "wnew";

	private static final String CMD_WDEL = "wdel";

	private static final String CMD_WLIST = "wlist";

	private static final String CMD_SEND = "send";

	private static final String CMD_BCPRINT = "bcprint";

	private static final String CMD_UTXOPRINT = "utxoprint";

	private BlockChain blockchain;

	private Wallet minerWallet;

	private Map<String, Wallet> wallets = new HashMap<>();

	public NodeCli() {
		this.minerWallet = new Wallet(null);
		this.blockchain = new BlockChain(minerWallet.getWalletAddress());
		this.minerWallet.setBlockchain(this.blockchain);

		this.wallets.put(MINER_WALLET_NAME, this.minerWallet);
	}

	@Override
	public void run() {
		boolean isRunning = true;
		Scanner stdin = new Scanner(System.in);

		while (isRunning) {
			try {
				System.out.println("[BC length: " + blockchain.status() + "].$ ");
				isRunning = menuReadStdin(stdin);
			} catch (TxException txEx) {
				System.out.println("Error: " + txEx.getMessage());
			}
			System.out.print(NodeCli.LINE_SEPARATOR);
		}
		stdin.close();
	}

	private boolean menuReadStdin(Scanner input) {
		String inLine = input.nextLine();
		String[] cmdLine = inLine.split("\\s+");

		return menuSwitch(cmdLine);
	}

	private boolean menuSwitch(String[] cmdLine) {
		String cmdName = cmdLine[0].trim();

		switch (cmdName) {
		case CMD_HELP:
			HelpMenu.printHelp();
			return true;

		case CMD_EXIT:
			System.out.println("Good by blockchain");
			return false;

		case CMD_SEND:
			sendCoins(cmdLine);
			return true;

		case CMD_WNEW:
			createNewWallet(cmdLine);
			return true;

		case CMD_WDEL:
			deleteWallet(cmdLine);
			return true;

		case CMD_WLIST:
			listAllWallets();
			return true;

		case CMD_BCPRINT:
			printBlockchain();
			return true;

		case CMD_UTXOPRINT:
			printUtxo();
			return true;

		default:
			System.out.println("Unknow command.");
			System.out.println("Type help.");
			return true;
		}
	}

	private void printUtxo() {
		System.out.println(this.blockchain.getAllUtxo());
	}

	private void printBlockchain() {
		System.out.println(this.blockchain);
	}

	private void sendCoins(String[] cmdLine) {
		if (cmdLine.length != 4) {
			System.out.println("Wrong command format.");
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
			System.out.println("Wrong command format.");
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

	private void createNewWallet(String[] cmdLine) {
		if (cmdLine.length != 2) {
			System.out.println("Wrong command format.");
			System.out.println("wnew WALLET_NAME");
			return;
		}

		String walletName = cmdLine[1].trim();
		if (this.wallets.containsKey(walletName)) {
			System.out.println("Wallet with name " + walletName + " already exists.");
		}

		this.wallets.put(walletName, new Wallet(this.blockchain));
	}

	private static class HelpMenu {

		private static Map<String, String> cmdHelp = new LinkedHashMap<>();

		private static Map<String, String> cmdHelpExample = new LinkedHashMap<>();

		static {
			cmdHelp.put(CMD_HELP, "Help.");
			cmdHelp.put(CMD_EXIT, "Stop and exit from the program.");
			cmdHelp.put(CMD_WNEW, "Create a new wallet.");
			cmdHelp.put(CMD_WDEL, "Delete a wallet");
			cmdHelp.put(CMD_WLIST, "List walletes and 'balances' for each wallet.");
			cmdHelp.put(CMD_SEND, "Send coins from one to another wallet.");
			cmdHelp.put(CMD_BCPRINT, "Print all blocks in blockchain.");
			cmdHelp.put(CMD_UTXOPRINT, "Print all unpent tx outpusts.");
		}

		static {
			cmdHelpExample.put(CMD_WNEW, "wnew WALLET-NAME");
			cmdHelpExample.put(CMD_WDEL, "wdel WALLET-NAME");
			cmdHelpExample.put(CMD_SEND, "send WALLET-NAME-1 WLLET-NAME-2 VALUE");
		}

		private static void printHelp() {
			System.out.println("jLocalCooin - blockchain implementation in Java.");
			cmdHelp.forEach((cmdName, helpDesc) -> {
				System.out.println(cmdName + TAB_SIGN + " - " + helpDesc);

				if (cmdHelpExample.containsKey(cmdName)) {
					System.out.println(TAB_SIGN + cmdHelpExample.get(cmdName));
				}

				System.out.println(LINE_SEPARATOR);
			});
		}
	}

}
