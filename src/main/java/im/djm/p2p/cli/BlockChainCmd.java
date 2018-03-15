package im.djm.p2p.cli;

import static im.djm.p2p.cli.StdOutUtil.printMessages;

import java.util.List;

import im.djm.coin.node.BlockChainNode;
import im.djm.coin.utxo.Utxo;
import im.djm.p2p.cli.cmd.Cmd;

/**
 * @author djm.im
 */
class BlockChainCmd implements Cmd {

	public void printCmd(BlockChainNode blockChainNode, String[] cmdLine) {
		if (cmdLine.length == 1) {
			printMessages("BlockChain length: " + blockChainNode.status() + ".");
			return;
		}

		switch (cmdLine[1]) {
		case CmdConstants.CMD_PRINT_BC:
			printBlockchain(blockChainNode);
			return;

		case CmdConstants.CMD_PRINT_UTXO:
			printUtxo(blockChainNode);
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
			printMessages("Wrong command format.", HelpCmd.cmdHelpExample.get(CmdConstants.CMD_PRINT));
		}

		printMessages("This command is not implemented yet.");
	}

	private void printUtxo(BlockChainNode blockChainNode) {
		List<Utxo> allUtxo = blockChainNode.getAllUtxo();
		for (Utxo utxo : allUtxo) {
			printMessages(utxo.toString());
		}
	}

	private void printBlockchain(BlockChainNode blockChainNode) {
		printMessages(blockChainNode.printBlockChain());
	}

}
