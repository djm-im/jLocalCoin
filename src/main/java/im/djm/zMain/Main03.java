package im.djm.zMain;

import im.djm.node.NodeCli;

/**
 * 
 * @author djm.im
 *
 */
public class Main03 {

	public static void main(String[] args) {
		Thread nodeThread = new Thread(new NodeCli());
		nodeThread.start();
	}

}
