package im.djm.p2p;

import im.djm.p2p.cli.NodeCliStdIn;

/**
 * @author djm.im
 */
public class Main03 {

	public static void main(String[] args) {
		Thread nodeThread = new Thread(new NodeCliStdIn());
		nodeThread.start();
	}

}
