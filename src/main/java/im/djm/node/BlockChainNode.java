package im.djm.node;

import static im.djm.zmain.StdoutUtil.printMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import im.djm.blockchain.BlockChain;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;

/**
 * @author djm
 */
class BlockChainNode {

	private BlockChain blockchain;

	private Wallet minerWallet;

	private Map<String, Wallet> wallets = new HashMap<>();

	private static final String MINER_WALLET_NAME = "_miner";

	public BlockChainNode() {
		this.minerWallet = new Wallet(null);
		this.blockchain = new BlockChain(minerWallet.getWalletAddress());
		this.minerWallet.setBlockchain(this.blockchain);

		this.wallets.put(MINER_WALLET_NAME, this.minerWallet);
	}

	public String status() {
		return this.blockchain.status();
	}

	// TODO
	// Create class Command and use it instead string
	boolean commandSwitch(String... cmdLine) {
		String cmdName = cmdLine[0].trim();

		switch (cmdName) {
		case CMD_HELP:
			HelpCommand.printHelp();
			return true;

		case CMD_EXIT:
			exit();
			return false;

		case CMD_SEND:
			sendCoin(cmdLine);
			return true;

		case CMD_MSEND:
			sendMultiCoins(cmdLine);
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
			printMessages("Unknow command.", "Type help.");
			return true;
		}
	}

	private void printCmd(String[] cmdLine) {
		if (cmdLine.length == 1) {
			printMessages("BlockChain length: " + blockchain.status() + ".");
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
			printMessages("Unknow command.", "Type help.");
			return;
		}
	}

	private void printBlock(String[] cmdLine) {
		if (cmdLine.length != 3) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_PRINT));
		}

		printMessages("This command is not implemented yet.");
	}

	private void printUtxo() {
		List<Utxo> allUtxo = this.blockchain.getAllUtxo();
		for (Utxo utxo : allUtxo) {
			printMessages(utxo.toString());
		}
	}

	private void printBlockchain() {
		printMessages(this.blockchain.toString());
	}

	private void sendMultiCoins(String[] cmdLine) {
		if (cmdLine.length < 4 || cmdLine.length % 2 != 0) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_MSEND));
			return;
		}

		String senderWalletName = cmdLine[1].trim();
		if (!this.wallets.containsKey(senderWalletName)) {
			printMessages("Wallet " + senderWalletName + " not exists in collection.");
			return;
		}

		List<Payment> payments = new ArrayList<>();
		for (int i = 2; i < cmdLine.length; i += 2) {
			String walletReceiverName = cmdLine[i].trim();
			if (!this.wallets.containsKey(walletReceiverName)) {
				// TODO
				// throw an exception
				printMessages("Wallet " + senderWalletName + " not exists in collection.");
				return;
			}

			long coinValue = Long.valueOf(cmdLine[i + 1]);
			Wallet walletReceiver = this.wallets.get(walletReceiverName);
			Payment payment = new Payment(walletReceiver.getWalletAddress(), coinValue);

			payments.add(payment);
		}

		Wallet walletSender = this.wallets.get(senderWalletName);
		@SuppressWarnings("unused")
		Tx tx = walletSender.send(payments);
	}

	private void sendCoin(String[] cmdLine) {
		if (cmdLine.length != 4) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_SEND));
			return;
		}

		String wallet1Name = cmdLine[1].trim();
		if (!this.wallets.containsKey(wallet1Name)) {
			printMessages("Wallet " + wallet1Name + " not exists in collection.");
			return;
		}

		String wallet2Name = cmdLine[2].trim();
		if (!this.wallets.containsKey(wallet2Name)) {
			printMessages("Wallet " + wallet2Name + " does not exist in collection of wallets.");
			return;
		}

		Wallet wallet1 = this.wallets.get(wallet1Name);
		Wallet wallet2 = this.wallets.get(wallet2Name);
		int coinValue = Integer.valueOf(cmdLine[3]);

		Payment payment = new Payment(wallet2.getWalletAddress(), coinValue);

		wallet1.send(Lists.newArrayList(payment));
	}

	private void deleteWallet(String[] cmdLine) {
		if (cmdLine.length != 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_WNEW));
			return;
		}

		// TODO
		// Add question to user confirm
		this.wallets.remove(cmdLine[1].trim());
	}

	private void walletStatus(String[] cmdLine) {
		if (cmdLine.length != 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_WSTAT));
			return;
		}

		String walletName = cmdLine[1].trim();
		if (!this.wallets.containsKey(walletName)) {
			printMessages("Wallet with name " + walletName + " doesn't exist in collection.");
			return;
		}

		Wallet wallet = this.wallets.get(walletName);

		printMessages(this.getWalletStatus(walletName, wallet));
	}

	private void listAllWallets() {
		this.wallets.forEach((walletName, wallet) -> {
			printMessages(this.getWalletStatus(walletName, wallet));
		});
	}

	private String getWalletStatus(String walletName, Wallet wallet) {
		return "{ Wallet Name: " + walletName + " " + wallet + ", Balance: " + wallet.balance() + " }";
	}

	private void createMultiNewWallets(String[] cmdLine) {
		if (cmdLine.length < 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_MWNEW));
			return;
		}

		for (int i = 1; i < cmdLine.length; i++) {
			this.creatWalletWithName(cmdLine[i].trim());
		}
	}

	private void createNewWallet(String[] cmdLine) {
		if (cmdLine.length != 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CMD_WNEW));
			return;
		}

		creatWalletWithName(cmdLine[1].trim());
	}

	private Wallet creatWalletWithName(String walletName) {
		if (this.wallets.containsKey(walletName)) {
			printMessages("Wallet with name " + walletName + " already exists.");
		}

		Wallet newWallet = new Wallet(this.blockchain);
		this.wallets.put(walletName, newWallet);

		return newWallet;
	}

	private void exit() {
		// TODO
		// Save blockchain in file
		printMessages("", "Good by blockchain!");
	}

	private static final String CMD_HELP = "help";

	private static final String CMD_EXIT = "exit";

	private static final String CMD_WNEW = "wnew";

	private static final String CMD_MWNEW = "mwnew";

	private static final String CMD_WSTAT = "wstat";

	private static final String CMD_WDEL = "wdel";

	private static final String CMD_WLIST = "wlist";

	private static final String CMD_SEND = "send";

	private static final String CMD_MSEND = "msend";

	private static final String CMD_PRINT = "print";

	private static final String CMD_PRINT_BLOCK = "block";

	private static final String CMD_PRINT_UTXO = "utxo";

	private static final String CMD_PRINT_BC = "bc";

	// TODO
	// split in another class
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
			cmdHelp.put(CMD_MSEND, "Send to multiple wallet addresses.");
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
			cmdHelpExample.put(CMD_MSEND, CMD_MSEND + " WALLET-SENDER WALLET-NAME-1 VALUE-1 ... WALLET-NAME-N VALUE-N");
		}

		private static void printHelp() {
			printMessages("jLocalCooin - blockchain implementation in Java.");

			cmdHelp.forEach((cmdName, helpDesc) -> {
				printMessages(cmdName + GlobalConstants.TAB_SIGN + " - " + helpDesc);
				if (cmdHelpExample.containsKey(cmdName)) {
					printMessages(cmdHelpExample.get(cmdName));
				}
				printMessages(GlobalConstants.LINE_SEPARATOR);
			});
		}
	}

}
