package im.djm.p2p.cli;

import static im.djm.p2p.cli.StdOutUtil.printMessages;

import java.util.Scanner;

import im.djm.blockchain.BlockChainStatus;
import im.djm.coin.tx.TxException;
import im.djm.coin.wallet.Trezor;
import im.djm.coin.wallet.Wallet;
import im.djm.p2p.node.BlockChainNode;

/**
 * @author djm.im
 */
public class NodeCliStdIn implements Runnable {

	private static final String MINER_WALLET_NAME = "_miner";

	private BlockChainNode blockChainNode;

	private Trezor trezor;

	private BlockChainCmd bcCmd = new BlockChainCmd();

	private WalletCmd wCmd = new WalletCmd();

	public NodeCliStdIn() {
		Wallet minerWallet = Wallet.createNewWallet();

		this.blockChainNode = new BlockChainNode(minerWallet.address());
		minerWallet.setBlockchainNode(this.blockChainNode);

		this.trezor = new Trezor();
		this.trezor.addMinerWallet(MINER_WALLET_NAME, minerWallet);
	}

	@Override
	public void run() {
		boolean isRunning = true;
		Scanner stdin = new Scanner(System.in);

		while (isRunning) {
			try {
				printMessages("[BC length: " + this.status() + "].$ ");
				isRunning = this.menuReadStdin(stdin);
			} catch (TxException txEx) {
				printMessages("Error: " + txEx.getMessage());
			}
			printMessages(GlobalConstants.LINE_SEPARATOR);
		}
		stdin.close();
	}

	public BlockChainStatus status() {
		return this.blockChainNode.status();
	}

	private boolean menuReadStdin(Scanner input) {
		String inLine = input.nextLine();
		if (inLine.trim().length() < 4) {
			printMessages("Invalid input");

			// Continue with execution of the thread
			return true;
		}

		String[] cmdLine = inLine.split("\\s+");
		boolean run = this.commandSwitch(cmdLine);

		return run;
	}

	// TODO
	// Create class Command and use it instead string
	private boolean commandSwitch(String... cmdLine) {
		String cmdName = cmdLine[0].trim();

		switch (cmdName) {
		case CmdConstants.CMD_HELP:
			HelpCmd.printHelp();
			return true;

		case CmdConstants.CMD_EXIT:
			ExitCmd.exit();
			return false;

		case CmdConstants.CMD_SEND:
			wCmd.sendCoin(this.trezor, cmdLine);
			return true;

		case CmdConstants.CMD_MSEND:
			wCmd.sendMultiCoins(this.trezor, cmdLine);
			return true;

		case CmdConstants.CMD_WNEW:
			wCmd.createNewWallet(this.blockChainNode, this.trezor, cmdLine);
			return true;

		case CmdConstants.CMD_MWNEW:
			wCmd.createMultiNewWallets(blockChainNode, this.trezor, cmdLine);
			return true;

		case CmdConstants.CMD_WSTAT:
			wCmd.walletStatus(this.trezor, cmdLine);
			return true;

		case CmdConstants.CMD_WDEL:
			wCmd.deleteWallet(this.trezor, cmdLine);
			return true;

		case CmdConstants.CMD_WLIST:
			wCmd.listAllWallets(this.trezor);
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
