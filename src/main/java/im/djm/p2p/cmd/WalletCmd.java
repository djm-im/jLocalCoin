package im.djm.p2p.cmd;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import im.djm.coin.tx.Tx;
import im.djm.p2p.node.BlockChainNode;
import im.djm.wallet.Payment;
import im.djm.wallet.Trezor;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
public class WalletCmd implements Cmd {

	public String sendMultiCoins(Trezor trezor, String[] cmdLine) {
		StringBuilder response = new StringBuilder();
		if (cmdLine.length < 4 || cmdLine.length % 2 != 0) {
			response.append("Wrong command format.").append("\n");
			response.append(HelpCmd.cmdHelpExample.get(CmdConstants.CMD_MSEND)).append("\n");
			response.append("\n");

			return response.toString();
		}

		String senderWalletName = cmdLine[1].trim();
		if (!trezor.containsKey(senderWalletName)) {
			response.append("Wallet " + senderWalletName + " not exists in collection.");
			response.append("\n");

			return response.toString();
		}

		List<Payment> payments = new ArrayList<>();
		for (int i = 2; i < cmdLine.length; i += 2) {
			String walletReceiverName = cmdLine[i].trim();
			if (!trezor.containsKey(walletReceiverName)) {
				// TODO
				// throw an exception

				response.append("Wallet " + senderWalletName + " not exists in collection.");
				response.append("\n");

				return response.toString();
			}

			long coinValue = Long.valueOf(cmdLine[i + 1]);
			Wallet walletReceiver = trezor.get(walletReceiverName);
			Payment payment = new Payment(walletReceiver.address(), coinValue);

			payments.add(payment);
		}

		Wallet walletSender = trezor.get(senderWalletName);
		Tx tx = walletSender.send(payments);

		response.append(tx);
		response.append("\n");

		return response.toString();
	}

	public String sendCoin(Trezor trezor, String[] cmdLine) {
		StringBuilder response = new StringBuilder();
		if (cmdLine.length != 4) {
			response.append("Wrong command format.").append("\n");
			response.append(HelpCmd.cmdHelpExample.get(CmdConstants.CMD_SEND)).append("\n");
			response.append("\n");

			return response.toString();
		}

		String wallet1Name = cmdLine[1].trim();
		if (!trezor.containsKey(wallet1Name)) {
			response.append("Wallet " + wallet1Name + " not exists in collection.").append("\n");
			response.append("\n");

			return response.toString();
		}

		String wallet2Name = cmdLine[2].trim();
		if (!trezor.containsKey(wallet2Name)) {
			response.append("Wallet " + wallet2Name + " does not exist in collection of wallets.").append("\n");
			response.append("\n");

			return response.toString();
		}

		Wallet wallet1 = trezor.get(wallet1Name);
		Wallet wallet2 = trezor.get(wallet2Name);
		int coinValue = Integer.valueOf(cmdLine[3]);

		Payment payment = new Payment(wallet2.address(), coinValue);

		Tx newTx = wallet1.send(Lists.newArrayList(payment));
		response.append(newTx).append("\n");
		response.append("\n");

		return response.toString();
	}

	public String deleteWallet(Trezor trezor, String[] cmdLine) {
		StringBuilder response = new StringBuilder();
		if (cmdLine.length != 2) {
			response.append("Wrong command format.").append("\n");
			response.append(HelpCmd.cmdHelpExample.get(CmdConstants.CMD_WNEW)).append("\n");
			response.append("\n");

			return response.toString();
		}

		String walletName = cmdLine[1].trim();
		trezor.remove(walletName);

		response.append("Removed: " + walletName);
		response.append("\n");

		return response.toString();
	}

	public String walletStatus(Trezor trezor, String[] cmdLine) {
		StringBuilder response = new StringBuilder();
		if (cmdLine.length != 2) {
			response.append("Wrong command format.").append("\n");
			response.append(HelpCmd.cmdHelpExample.get(CmdConstants.CMD_WSTAT)).append("\n");
			response.append("\n");

			return response.toString();
		}

		String walletName = cmdLine[1].trim();
		if (!trezor.containsKey(walletName)) {
			response.append("Wallet with name " + walletName + " doesn't exist in collection.").append("\n");
			response.append("\n");

			return response.toString();
		}

		Wallet wallet = trezor.get(walletName);

		String walletStatus = this.getWalletStatus(walletName, wallet);

		response.append(walletStatus).append("\n");
		response.append("\n");

		return response.toString();
	}

	public String listAllWallets(Trezor trezor) {
		StringBuilder response = new StringBuilder();

		trezor.allWallets().forEach((walletName, wallet) -> {
			response.append(this.getWalletStatus(walletName, wallet)).append("\n");
		});
		response.append("\n");

		return response.toString();
	}

	private String getWalletStatus(String walletName, Wallet wallet) {
		return "{ Wallet Name: " + walletName + " " + wallet + ", Balance: " + wallet.balance() + " }";
	}

	public String createMultiNewWallets(BlockChainNode blockChainNode, Trezor trezor, String[] cmdLine) {
		StringBuilder response = new StringBuilder();
		if (cmdLine.length < 2) {
			response.append("Wrong command format.").append("\n");
			response.append(HelpCmd.cmdHelpExample.get(CmdConstants.CMD_MWNEW)).append("\n");
			response.append("\n");

			return response.toString();
		}

		for (int i = 1; i < cmdLine.length; i++) {
			Wallet creatWalletWithName = this.creatWalletWithName(blockChainNode, trezor, cmdLine[i].trim());
			response.append("Creatd: " + creatWalletWithName).append("\n");
		}
		response.append("\n");

		return response.toString();
	}

	public String createNewWallet(BlockChainNode blockChainNode, Trezor trezor, String[] cmdLine) {
		StringBuilder response = new StringBuilder();
		if (cmdLine.length != 2) {
			response.append("Wrong command format.").append("\n");
			response.append(HelpCmd.cmdHelpExample.get(CmdConstants.CMD_WNEW)).append("\n");
			response.append("\n");

			return response.toString();
		}

		String walletName = cmdLine[1].trim();
		if (trezor.containsKey(walletName)) {
			// TODO
			// throw exception
			response.append("Wallet with name " + walletName + " already exists.").append("\n");
			response.append("\n");
		}

		response.append(this.creatWalletWithName(blockChainNode, trezor, walletName)).append("\n");
		response.append("\n");

		return response.toString();
	}

	private Wallet creatWalletWithName(BlockChainNode blockChainNode, Trezor trezor, String walletName) {
		Wallet newWallet = Wallet.createNewWallet().setBlockchainNode(blockChainNode);
		trezor.put(walletName, newWallet);

		return newWallet;
	}

}
