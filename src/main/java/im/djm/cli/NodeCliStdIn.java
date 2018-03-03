package im.djm.cli;

import static im.djm.cli.StdOutUtil.printMessages;

import java.util.Scanner;

import im.djm.blockchain.GlobalConstants;
import im.djm.exception.TxException;

/**
 * @author djm.im
 */
public class NodeCliStdIn implements Runnable {

	private NodeCliCommands cliNode = new NodeCliCommands();

	@Override
	public void run() {
		boolean isRunning = true;
		Scanner stdin = new Scanner(System.in);

		while (isRunning) {
			try {
				printMessages("[BC length: " + cliNode.status() + "].$ ");
				isRunning = this.menuReadStdin(stdin);
			} catch (TxException txEx) {
				printMessages("Error: " + txEx.getMessage());
			}
			printMessages(GlobalConstants.LINE_SEPARATOR);
		}
		stdin.close();
	}

	private boolean menuReadStdin(Scanner input) {
		String inLine = input.nextLine();
		if (inLine.trim().length() < 4) {
			printMessages("Invalid input");

			// Continue with execution of the thread
			return true;
		}

		String[] cmdLine = inLine.split("\\s+");
		boolean run = this.cliNode.commandSwitch(cmdLine);

		return run;
	}

}
