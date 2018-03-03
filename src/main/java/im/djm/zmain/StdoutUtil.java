package im.djm.zmain;

/**
 * @author djm.im
 */
public final class StdoutUtil {

	public static void printMessages(String... msgs) {
		for (String msg : msgs) {
			System.out.println(msg);
		}
	}

}
