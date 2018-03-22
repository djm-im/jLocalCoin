package im.djm;

import im.djm.p2p.node.SparkNode;

/**
 * @author djm.im
 */
public class Main04 {

	public static void main(String[] args) {
		SparkNode sNode = new SparkNode();
		int port = 2020;

		sNode.mainMethod(port);
	}

}
