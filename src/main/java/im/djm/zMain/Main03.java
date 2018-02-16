package im.djm.zMain;

import im.djm.node.NodeCliStdIn;

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
