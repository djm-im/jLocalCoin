package im.djm.p2p.cmd;

import java.util.LinkedHashMap;
import java.util.Map;

import im.djm.p2p.cli.GlobalConstants;

/**
 * @author djm.im
 */
public class HelpCmd implements Cmd {

	private static Map<String, String> cmdHelp = new LinkedHashMap<>();

	public static Map<String, String> cmdHelpExample = new LinkedHashMap<>();

	static {
		cmdHelp.put(CmdConstants.CMD_HELP, "Help.");
		cmdHelp.put(CmdConstants.CMD_EXIT, "Stop and exit from the program.");
		cmdHelp.put(CmdConstants.CMD_WNEW, "Create a new wallet.");
		cmdHelp.put(CmdConstants.CMD_MWNEW,
				"Create multiple new wallets. If any wallet with name alread exist it will be skiped.");
		cmdHelp.put(CmdConstants.CMD_WSTAT, "Display status for wallet.");
		cmdHelp.put(CmdConstants.CMD_WDEL, "Delete a wallet");
		cmdHelp.put(CmdConstants.CMD_WLIST, "List walletes and 'balances' for each wallet.");
		cmdHelp.put(CmdConstants.CMD_SEND, "Send coins from one to another wallet.");
		cmdHelp.put(CmdConstants.CMD_MSEND, "Send to multiple wallet addresses.");
		cmdHelp.put(CmdConstants.CMD_PRINT, "Display data about blockchain, utxo pool, or a block.");
	}

	static {
		cmdHelpExample.put(CmdConstants.CMD_WNEW, CmdConstants.CMD_WNEW + " WALLET-NAME");
		cmdHelpExample.put(CmdConstants.CMD_MWNEW,
				CmdConstants.CMD_MWNEW + " WALLET-NAME-1 WALLET-NAME-2 ... WALLET-NAME-N");
		cmdHelpExample.put(CmdConstants.CMD_WSTAT, CmdConstants.CMD_WSTAT + " WALLET-NAME");
		cmdHelpExample.put(CmdConstants.CMD_WDEL, CmdConstants.CMD_WDEL + " WALLET-NAME");
		cmdHelpExample.put(CmdConstants.CMD_SEND, CmdConstants.CMD_SEND + " WALLET-NAME-1 WLLET-NAME-2 VALUE");
		cmdHelpExample.put(CmdConstants.CMD_PRINT, CmdConstants.CMD_PRINT + "\n" + CmdConstants.CMD_PRINT + " bc\n"
				+ CmdConstants.CMD_PRINT + " utxo\n" + CmdConstants.CMD_PRINT + " block [NUM]");
		cmdHelpExample.put(CmdConstants.CMD_MSEND,
				CmdConstants.CMD_MSEND + " WALLET-SENDER WALLET-NAME-1 VALUE-1 ... WALLET-NAME-N VALUE-N");
	}

	public static String helpMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("jLocalCoin - BlockChain implementation in Java.").append(GlobalConstants.LINE_SEPARATOR);

		cmdHelp.forEach((cmdName, helpDesc) -> {
			sb.append(cmdName + GlobalConstants.TAB_SIGN + " - " + helpDesc).append(GlobalConstants.LINE_SEPARATOR);
			if (cmdHelpExample.containsKey(cmdName)) {
				sb.append(cmdHelpExample.get(cmdName)).append(GlobalConstants.LINE_SEPARATOR);
			}
			sb.append(GlobalConstants.LINE_SEPARATOR);
		});

		return sb.toString();
	}

}
