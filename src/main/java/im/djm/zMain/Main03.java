package im.djm.zMain;

import java.security.NoSuchAlgorithmException;

import im.djm.node.NodeCli;

/**
 * 
 * @author djm.im
 *
 */
public class Main03 {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		Thread nodeThread = new Thread(new NodeCli());
		nodeThread.start();
	}

}
