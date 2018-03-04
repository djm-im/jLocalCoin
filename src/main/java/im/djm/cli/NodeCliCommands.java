package im.djm.cli;

import static im.djm.cli.StdOutUtil.printMessages;

import java.util.HashMap;
import java.util.Map;

import im.djm.node.BlockChainNode;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
class NodeCliCommands {

	private static final String MINER_WALLET_NAME = "_miner";

	private Map<String, Wallet> wallets = new HashMap<>();

	private BlockChainNode blockChainNode;

	public NodeCliCommands() {
		Wallet minerWallet = new Wallet(null);

		this.blockChainNode = new BlockChainNode(minerWallet.getWalletAddress());

		minerWallet.setBlockchain(this.blockChainNode.getBlockchain());

		this.wallets.put(MINER_WALLET_NAME, minerWallet);
	}

	public String status() {
		return this.blockChainNode.status();
	}

	BlockChainCmd bcCmd = new BlockChainCmd();
	WalletCmd wCmd = new WalletCmd();

	// TODO
	// Create class Command and use it instead string
	public boolean commandSwitch(String... cmdLine) {
		String cmdName = cmdLine[0].trim();

		switch (cmdName) {
		case CmdConstants.CMD_HELP:
			HelpCommand.printHelp();
			return true;

		case CmdConstants.CMD_EXIT:
			bcCmd.exit();
			return false;

		case CmdConstants.CMD_SEND:
			wCmd.sendCoin(wallets, cmdLine);
			return true;

		case CmdConstants.CMD_MSEND:
			wCmd.sendMultiCoins(wallets, cmdLine);
			return true;

		case CmdConstants.CMD_WNEW:
			wCmd.createNewWallet(blockChainNode, wallets, cmdLine);
			return true;

		case CmdConstants.CMD_MWNEW:
			wCmd.createMultiNewWallets(blockChainNode, wallets, cmdLine);
			return true;

		case CmdConstants.CMD_WSTAT:
			wCmd.walletStatus(wallets, cmdLine);
			return true;

		case CmdConstants.CMD_WDEL:
			wCmd.deleteWallet(wallets, cmdLine);
			return true;

		case CmdConstants.CMD_WLIST:
			wCmd.listAllWallets(wallets);
			return true;

		case CmdConstants.CMD_PRINT:
			bcCmd.printCmd(this.blockChainNode, cmdLine);
			return true;

		default:
			printMessages("Unknow command.", "Type help.");
			return true;
		}
	}

}
