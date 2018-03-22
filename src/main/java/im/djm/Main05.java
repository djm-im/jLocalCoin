package im.djm;

/**
 * @author djm.im
 */
public class Main05 {

	public static void main(String[] args) {
		String param = "web";
		if (args.length > 0 && args[0].equals("cli")) {
			param = "cli";
		}
		switch (param) {
		case "cli":
			Main03.main(args);
			break;

		case "web":
			Main04.main(args);
			break;
		default:
			break;
		}
	}
}
