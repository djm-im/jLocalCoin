package im.djm.zMain;

/**
 * 
 * @author djm.im
 *
 */
public final class SoutUtil {

	public static void printlnParagraph(String... strs) {
		System.out.println("#########################################################################################");
		for (String str : strs) {
			System.out.println(str);
		}
		System.out.println("#########################################################################################");

		printNewLine(2);
	}

	private static void printNewLine(int times) {
		for (int i = 0; i < times; i++) {
			System.out.println(System.lineSeparator());
		}
	}
}
