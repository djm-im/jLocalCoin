package im.djm.cli;

import static im.djm.cli.StdOutUtil.printMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import im.djm.node.BlockChainNode;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
class NodeCliCommands {

	private static final String MINER_WALLET_NAME = "_miner";

	public NodeCliCommands() {
		Wallet minerWallet = new Wallet(null);

		this.blockChainNode = new BlockChainNode(minerWallet.getWalletAddress());

		minerWallet.setBlockchain(this.blockChainNode.getBlockchain());

		this.wallets.put(MINER_WALLET_NAME, minerWallet);
	}

	public String status() {
		return this.blockChainNode.status();
	}

	// TODO
	// Create class Command and use it instead string
	public boolean commandSwitch(String... cmdLine) {
		String cmdName = cmdLine[0].trim();

		switch (cmdName) {
		case CmdConstants.CMD_HELP:
			HelpCommand.printHelp();
			return true;

		case CmdConstants.CMD_EXIT:
			exit();
			return false;

		case CmdConstants.CMD_SEND:
			sendCoin(cmdLine);
			return true;

		case CmdConstants.CMD_MSEND:
			sendMultiCoins(cmdLine);
			return true;

		case CmdConstants.CMD_WNEW:
			createNewWallet(cmdLine);
			return true;

		case CmdConstants.CMD_MWNEW:
			createMultiNewWallets(cmdLine);
			return true;

		case CmdConstants.CMD_WSTAT:
			walletStatus(cmdLine);
			return true;

		case CmdConstants.CMD_WDEL:
			deleteWallet(cmdLine);
			return true;

		case CmdConstants.CMD_WLIST:
			listAllWallets();
			return true;

		case CmdConstants.CMD_PRINT:
			printCmd(cmdLine);
			return true;

		default:
			printMessages("Unknow command.", "Type help.");
			return true;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------
	// Blockchain commands
	// ------------------------------------------------------------------------------------------------------------------------

	private BlockChainNode blockChainNode;

	private void printCmd(String[] cmdLine) {
		if (cmdLine.length == 1) {
			printMessages("BlockChain length: " + this.blockChainNode.status() + ".");
			return;
		}

		switch (cmdLine[1]) {
		case CmdConstants.CMD_PRINT_BC:
			printBlockchain();
			return;

		case CmdConstants.CMD_PRINT_UTXO:
			printUtxo();
			return;

		case CmdConstants.CMD_PRINT_BLOCK:
			printBlock(cmdLine);
			return;

		default:
			printMessages("Unknow command.", "Type help.");
			return;
		}
	}

	private void printBlock(String[] cmdLine) {
		if (cmdLine.length != 3) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_PRINT));
		}

		printMessages("This command is not implemented yet.");
	}

	private void printUtxo() {
		List<Utxo> allUtxo = this.blockChainNode.getAllUtxo();
		for (Utxo utxo : allUtxo) {
			printMessages(utxo.toString());
		}
	}

	private void printBlockchain() {
		printMessages(this.blockChainNode.printBlockChain());
	}

	private void exit() {
		// TODO
		// Save blockchain in file
		printMessages("", "Good by blockchain!");
	}

	// ------------------------------------------------------------------------------------------------------------------------
	// Wallet commands
	// ------------------------------------------------------------------------------------------------------------------------

	private Map<String, Wallet> wallets = new HashMap<>();

	private void sendMultiCoins(String[] cmdLine) {
		if (cmdLine.length < 4 || cmdLine.length % 2 != 0) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_MSEND));
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
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_SEND));
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
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_WNEW));
			return;
		}

		// TODO
		// Add question to user confirm
		this.wallets.remove(cmdLine[1].trim());
	}

	private void walletStatus(String[] cmdLine) {
		if (cmdLine.length != 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_WSTAT));
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
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_MWNEW));
			return;
		}

		for (int i = 1; i < cmdLine.length; i++) {
			this.creatWalletWithName(cmdLine[i].trim());
		}
	}

	private void createNewWallet(String[] cmdLine) {
		if (cmdLine.length != 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_WNEW));
			return;
		}

		creatWalletWithName(cmdLine[1].trim());
	}

	private Wallet creatWalletWithName(String walletName) {
		if (this.wallets.containsKey(walletName)) {
			printMessages("Wallet with name " + walletName + " already exists.");
		}

		Wallet newWallet = new Wallet(this.blockChainNode.getBlockchain());
		this.wallets.put(walletName, newWallet);

		return newWallet;
	}

	// ------------------------------------------------------------------------------------------------------------------------

}
