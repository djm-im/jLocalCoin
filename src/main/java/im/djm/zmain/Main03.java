package im.djm.zmain;

import im.djm.cli.NodeCliStdIn;

/**
 * 
 * @author djm.im
 *
 */
public class Main03 {

	public static void main(String[] args) {
		Thread nodeThread = new Thread(new NodeCliStdIn());
		nodeThread.start();
	}

}
