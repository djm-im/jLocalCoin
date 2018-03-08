package im.djm.coin.cli;

/**
 * @author djm.im
 */
final class StdOutUtil {

	public static void printMessages(String... msgs) {
		for (String msg : msgs) {
			System.out.println(msg);
		}
	}

}
