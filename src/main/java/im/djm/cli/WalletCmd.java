package im.djm.cli;

import static im.djm.cli.StdOutUtil.printMessages;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import im.djm.node.BlockChainNode;
import im.djm.tx.Tx;
import im.djm.wallet.Payment;
import im.djm.wallet.Trezor;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
class WalletCmd implements Cmd {

	public void sendMultiCoins(Trezor trezor, String[] cmdLine) {
		if (cmdLine.length < 4 || cmdLine.length % 2 != 0) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_MSEND));
			return;
		}

		String senderWalletName = cmdLine[1].trim();
		if (!trezor.containsKey(senderWalletName)) {
			printMessages("Wallet " + senderWalletName + " not exists in collection.");
			return;
		}

		List<Payment> payments = new ArrayList<>();
		for (int i = 2; i < cmdLine.length; i += 2) {
			String walletReceiverName = cmdLine[i].trim();
			if (!trezor.containsKey(walletReceiverName)) {
				// TODO
				// throw an exception
				printMessages("Wallet " + senderWalletName + " not exists in collection.");
				return;
			}

			long coinValue = Long.valueOf(cmdLine[i + 1]);
			Wallet walletReceiver = trezor.get(walletReceiverName);
			Payment payment = new Payment(walletReceiver.getWalletAddress(), coinValue);

			payments.add(payment);
		}

		Wallet walletSender = trezor.get(senderWalletName);
		@SuppressWarnings("unused")
		Tx tx = walletSender.send(payments);
	}

	public void sendCoin(Trezor trezor, String[] cmdLine) {
		if (cmdLine.length != 4) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_SEND));
			return;
		}

		String wallet1Name = cmdLine[1].trim();
		if (!trezor.containsKey(wallet1Name)) {
			printMessages("Wallet " + wallet1Name + " not exists in collection.");
			return;
		}

		String wallet2Name = cmdLine[2].trim();
		if (!trezor.containsKey(wallet2Name)) {
			printMessages("Wallet " + wallet2Name + " does not exist in collection of wallets.");
			return;
		}

		Wallet wallet1 = trezor.get(wallet1Name);
		Wallet wallet2 = trezor.get(wallet2Name);
		int coinValue = Integer.valueOf(cmdLine[3]);

		Payment payment = new Payment(wallet2.getWalletAddress(), coinValue);

		wallet1.send(Lists.newArrayList(payment));
	}

	public void deleteWallet(Trezor trezor, String[] cmdLine) {
		if (cmdLine.length != 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_WNEW));
			return;
		}

		// TODO
		// Add question to user confirm
		trezor.remove(cmdLine[1].trim());
	}

	public void walletStatus(Trezor trezor, String[] cmdLine) {
		if (cmdLine.length != 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_WSTAT));
			return;
		}

		String walletName = cmdLine[1].trim();
		if (!trezor.containsKey(walletName)) {
			printMessages("Wallet with name " + walletName + " doesn't exist in collection.");
			return;
		}

		Wallet wallet = trezor.get(walletName);

		printMessages(this.getWalletStatus(walletName, wallet));
	}

	public void listAllWallets(Trezor trezor) {
		trezor.allWallets().forEach((walletName, wallet) -> {
			printMessages(this.getWalletStatus(walletName, wallet));
		});
	}

	private String getWalletStatus(String walletName, Wallet wallet) {
		return "{ Wallet Name: " + walletName + " " + wallet + ", Balance: " + wallet.balance() + " }";
	}

	public void createMultiNewWallets(BlockChainNode blockChainNode, Trezor trezor, String[] cmdLine) {
		if (cmdLine.length < 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_MWNEW));
			return;
		}

		for (int i = 1; i < cmdLine.length; i++) {
			this.creatWalletWithName(blockChainNode, trezor, cmdLine[i].trim());
		}
	}

	public void createNewWallet(BlockChainNode blockChainNode, Trezor trezor, String[] cmdLine) {
		if (cmdLine.length != 2) {
			printMessages("Wrong command format.", HelpCommand.cmdHelpExample.get(CmdConstants.CMD_WNEW));
			return;
		}

		creatWalletWithName(blockChainNode, trezor, cmdLine[1].trim());
	}

	private Wallet creatWalletWithName(BlockChainNode blockChainNode, Trezor trezor, String walletName) {
		if (trezor.containsKey(walletName)) {
			printMessages("Wallet with name " + walletName + " already exists.");
		}

		Wallet newWallet = new Wallet(blockChainNode.getBlockchain());
		trezor.put(walletName, newWallet);

		return newWallet;
	}

}
