package im.djm.node;

import static im.djm.zmain.StdoutUtil.printMessages;

import java.util.Scanner;

import im.djm.exception.TxException;

/**
 * @author djm.im
 */
public class NodeCliStdIn implements Runnable {

	private BlockChainNode bcn = new BlockChainNode();

	@Override
	public void run() {
		boolean isRunning = true;
		Scanner stdin = new Scanner(System.in);

		while (isRunning) {
			try {
				printMessages("[BC length: " + bcn.status() + "].$ ");
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
		boolean run = this.bcn.commandSwitch(cmdLine);

		return run;
	}

}
