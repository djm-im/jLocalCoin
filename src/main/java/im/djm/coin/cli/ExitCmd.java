package im.djm.coin.cli;

import static im.djm.coin.cli.StdOutUtil.printMessages;

import im.djm.coin.cli.cmd.Cmd;

/**
 * @author djm.im
 */
class ExitCmd implements Cmd {

	public static void exit() {
		// TODO
		// Save blockchain in file
		printMessages("", "Good by blockchain!");
	}

}