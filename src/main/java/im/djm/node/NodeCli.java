package im.djm.node;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import im.djm.blockchain.BlockChain;
import im.djm.exception.TxException;
import im.djm.tx.Utxo;
import im.djm.wallet.Wallet;

/**
 * 
 * @author djm.im
 *
 */
public class NodeCli implements Runnable {

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
				UtilString.printMessages("[BC length: " + blockchain.status() + "].$ ");
				isRunning = menuReadStdin(stdin);
			} catch (TxException txEx) {
				UtilString.printMessages("Error: " + txEx.getMessage());
			}
			UtilString.printMessages(NodeCli.LINE_SEPARATOR);
		}
		stdin.close();
	}

	// ------------------------------------------------------------------------

	private boolean menuReadStdin(Scanner input) {
		String inLine = input.nextLine();
		if (inLine.trim().length() < 4) {
			UtilString.printMessages("Invalid input");

			// Continue with execution of the thread
			return true;
		}
		String[] cmdLine = inLine.split("\\s+");

		return menuSwitch(cmdLine);
	}

	private boolean menuSwitch(String[] cmdLine) {
		String cmdName = cmdLine[0].trim();

		switch (cmdName) {
		case CMD_HELP:
			HelpCommand.printHelp();
			return true;

		case CMD_EXIT:
			exit();
			return false;

		case CMD_SEND:
			sendCoins(cmdLine);
			return true;

		case CMD_WNEW:
			createNewWallet(cmdLine);
			return true;

		case CMD_MWNEW:
			createMultiNewWallets(cmdLine);
			return true;

		case CMD_WSTAT:
			walletStatus(cmdLine);
			return true;

		case CMD_WDEL:
			deleteWallet(cmdLine);
			return true;

		case CMD_WLIST:
			listAllWallets();
			return true;

		case CMD_PRINT:
			printCmd(cmdLine);
			return true;

		default:
			UtilString.printMessages("Unknow command.", "Type help.");
			return true;
		}
	}

	private void printCmd(String[] cmdLine) {
		if (cmdLine.length == 1) {
			UtilString.printMessages("BlockChain status: " + blockchain.status() + ".");
			return;
		}

		switch (cmdLine[1]) {
		case CMD_PRINT_BC:
			printBlockchain();
			return;

		case CMD_PRINT_UTXO:
			printUtxo();
			return;

		case CMD_PRINT_BLOCK:
			printBlock(cmdLine);
			return;

		default:
			UtilString.printMessages("Unknow command.", "Type help.");
			return;
		}
	}

	private void printBlock(String[] cmdLine) {
		if (cmdLine.length != 3) {
			UtilString.printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_PRINT));
		}

		UtilString.printMessages("This command is not implemented yet.");
	}

	private void printUtxo() {
		List<Utxo> allUtxo = this.blockchain.getAllUtxo();
		for (Utxo utxo : allUtxo) {
			UtilString.printMessages(utxo.toString());
		}
	}

	private void printBlockchain() {
		UtilString.printMessages(this.blockchain.toString());
	}

	private void sendCoins(String[] cmdLine) {
		if (cmdLine.length != 4) {
			UtilString.printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_SEND));
			return;
		}

		String wallet1Name = cmdLine[1].trim();
		if (!this.wallets.containsKey(wallet1Name)) {
			UtilString.printMessages("Wallet " + wallet1Name + " not exists in collection.");
			return;
		}

		String wallet2Name = cmdLine[2].trim();
		if (!this.wallets.containsKey(wallet2Name)) {
			UtilString.printMessages("Wallet " + wallet2Name + " does not exist in collection of wallets.");
			return;
		}

		Wallet wallet1 = this.wallets.get(wallet1Name);
		Wallet wallet2 = this.wallets.get(wallet2Name);
		int value = Integer.valueOf(cmdLine[3]);

		wallet1.sendCoin(wallet2.getWalletAddress(), value);
	}

	private void deleteWallet(String[] cmdLine) {
		if (cmdLine.length != 2) {
			UtilString.printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_WNEW));
			return;
		}

		// TODO
		// Add question to user confirm
		this.wallets.remove(cmdLine[1].trim());
	}

	private void walletStatus(String[] cmdLine) {
		if (cmdLine.length != 2) {
			UtilString.printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_WSTAT));
			return;
		}

		String walletName = cmdLine[1].trim();
		if (!this.wallets.containsKey(walletName)) {
			UtilString.printMessages("Wallet with name " + walletName + " doesn't exist in collection.");
			return;
		}

		Wallet wallet = this.wallets.get(walletName);

		UtilString.printMessages(this.getWalletStatus(walletName, wallet));
	}

	private void listAllWallets() {
		this.wallets.forEach((walletName, wallet) -> {
			UtilString.printMessages(this.getWalletStatus(walletName, wallet));
		});
	}

	private String getWalletStatus(String walletName, Wallet wallet) {
		return "{ Wallet Name: " + walletName + " " + wallet + ", Balance: " + wallet.balance() + " }";
	}

	private void createMultiNewWallets(String[] cmdLine) {
		if (cmdLine.length < 2) {
			UtilString.printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_MWNEW));
			return;
		}

		for (int i = 1; i < cmdLine.length; i++) {
			this.creatWalletWithName(cmdLine[i].trim());
		}
	}

	private void createNewWallet(String[] cmdLine) {
		if (cmdLine.length != 2) {
			UtilString.printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_WNEW));
			return;
		}

		creatWalletWithName(cmdLine[1].trim());
	}

	private Wallet creatWalletWithName(String walletName) {
		if (this.wallets.containsKey(walletName)) {
			UtilString.printMessages("Wallet with name " + walletName + " already exists.");
		}

		Wallet newWallet = new Wallet(this.blockchain);
		this.wallets.put(walletName, newWallet);

		return newWallet;
	}

	private void exit() {
		// TODO
		// Save blockchain in file
		UtilString.printMessages("", "Good by blockchain!");
	}

	private static class UtilString {

		public static void printMessages(String... msgs) {
			for (String msg : msgs) {
				System.out.println(msg);
			}
		}

	}

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final String TAB_SIGN = "\t";

	private static final String MINER_WALLET_NAME = "_miner";

	private static final String CMD_HELP = "help";

	private static final String CMD_EXIT = "exit";

	private static final String CMD_WNEW = "wnew";

	private static final String CMD_MWNEW = "mwnew";

	private static final String CMD_WSTAT = "wstat";

	private static final String CMD_WDEL = "wdel";

	private static final String CMD_WLIST = "wlist";

	private static final String CMD_SEND = "send";

	private static final String CMD_PRINT = "print";

	private static final String CMD_PRINT_BLOCK = "block";

	private static final String CMD_PRINT_UTXO = "utxo";

	private static final String CMD_PRINT_BC = "bc";

	private static class HelpCommand {

		private static Map<String, String> cmdHelp = new LinkedHashMap<>();

		private static Map<String, String> cmdHelpExample = new LinkedHashMap<>();

		static {
			cmdHelp.put(CMD_HELP, "Help.");
			cmdHelp.put(CMD_EXIT, "Stop and exit from the program.");
			cmdHelp.put(CMD_WNEW, "Create a new wallet.");
			cmdHelp.put(CMD_MWNEW,
					"Create multiple new wallets. If any wallet with name alread exist it will be skiped.");
			cmdHelp.put(CMD_WSTAT, "Display status for wallet.");
			cmdHelp.put(CMD_WDEL, "Delete a wallet");
			cmdHelp.put(CMD_WLIST, "List walletes and 'balances' for each wallet.");
			cmdHelp.put(CMD_SEND, "Send coins from one to another wallet.");
			cmdHelp.put(CMD_PRINT, "Display data about blockchain, utxo pool, or a block.");
		}

		static {
			cmdHelpExample.put(CMD_WNEW, CMD_WNEW + " WALLET-NAME");
			cmdHelpExample.put(CMD_MWNEW, CMD_MWNEW + " WALLET-NAME-1 WALLET-NAME-2 ... WALLET-NAME-N");
			cmdHelpExample.put(CMD_WSTAT, CMD_WSTAT + " WALLET-NAME");
			cmdHelpExample.put(CMD_WDEL, CMD_WDEL + " WALLET-NAME");
			cmdHelpExample.put(CMD_SEND, CMD_SEND + " WALLET-NAME-1 WLLET-NAME-2 VALUE");
			cmdHelpExample.put(CMD_PRINT,
					CMD_PRINT + "\n" + CMD_PRINT + " bc\n" + CMD_PRINT + " utxo\n" + CMD_PRINT + " block [NUM]");
		}

		private static void printHelp() {
			UtilString.printMessages("jLocalCooin - blockchain implementation in Java.");

			cmdHelp.forEach((cmdName, helpDesc) -> {
				UtilString.printMessages(cmdName + TAB_SIGN + " - " + helpDesc);
				if (cmdHelpExample.containsKey(cmdName)) {
					UtilString.printMessages(cmdHelpExample.get(cmdName));
				}
				UtilString.printMessages(LINE_SEPARATOR);
			});
		}
	}

}
