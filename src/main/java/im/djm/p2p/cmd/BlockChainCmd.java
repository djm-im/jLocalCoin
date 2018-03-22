package im.djm.p2p.cmd;

import java.util.List;

import im.djm.coin.utxo.Utxo;
import im.djm.p2p.cmd.Cmd;
import im.djm.p2p.cmd.CmdConstants;
import im.djm.p2p.cmd.HelpCmd;
import im.djm.p2p.node.BlockChainNode;

/**
 * @author djm.im
 */
public class BlockChainCmd implements Cmd {

	public String printCmd(BlockChainNode blockChainNode, String[] cmdLine) {
		StringBuilder response = new StringBuilder();
		if (cmdLine.length == 1) {
			response.append("BlockChain length: " + blockChainNode.status() + ".").append("\n");
			response.append("\n");

			return response.toString();
		}

		switch (cmdLine[1]) {
		case CmdConstants.CMD_PRINT_BC:
			return printBlockchain(blockChainNode);

		case CmdConstants.CMD_PRINT_UTXO:
			return printUtxo(blockChainNode);

		case CmdConstants.CMD_PRINT_BLOCK:
			return printBlock(cmdLine);

		default:
			return unknowCommand();
		}
	}

	private String unknowCommand() {
		return "Unknow command." + "\n" + "Type help." + "\n" + "\n";
	}

	private String printBlock(String[] cmdLine) {
		StringBuilder response = new StringBuilder();
		if (cmdLine.length != 3) {
			response.append("Wrong command format.").append("\n");
			response.append(HelpCmd.cmdHelpExample.get(CmdConstants.CMD_PRINT)).append("\n");

			response.append("\n");

			return response.toString();
		}

		response.append("This command is not implemented yet.").append("\n");
		response.append("\n");

		return response.toString();
	}

	private String printUtxo(BlockChainNode blockChainNode) {
		StringBuilder response = new StringBuilder();
		List<Utxo> allUtxo = blockChainNode.getAllUtxo();
		allUtxo.forEach(utxo -> {
			response.append(utxo.toString()).append("\n");
		});
		response.append("\n");

		return response.toString();
	}

	private String printBlockchain(BlockChainNode blockChainNode) {
		return blockChainNode.printBlockChain();
	}

}
