package im.djm.cli;

import static im.djm.cli.StdOutUtil.printMessages;

import im.djm.cli.cmd.Cmd;

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