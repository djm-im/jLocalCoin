package im.djm.p2p.cli;

/**
 * @author djm.im
 */
public final class StdOutUtil {

	public static void printMessages(String... msgs) {
		for (String msg : msgs) {
			System.out.println(msg);
		}
	}

}
