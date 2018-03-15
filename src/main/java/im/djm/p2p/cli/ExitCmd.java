package im.djm.p2p.cli;

import static im.djm.p2p.cli.StdOutUtil.printMessages;

import im.djm.p2p.cli.cmd.Cmd;

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