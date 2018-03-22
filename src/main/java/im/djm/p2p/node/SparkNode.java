package im.djm.p2p.node;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import java.util.List;

import com.google.common.collect.ImmutableList;

import im.djm.coin.tx.Tx;
import im.djm.p2p.cli.HelpCmd;
import im.djm.p2p.cli.NodeCliStdIn;
import im.djm.wallet.Payment;
import im.djm.wallet.Trezor;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
public class SparkNode {

	private BlockChainNode blockChainNode;

	private Trezor trezor;

	public SparkNode() {
		Wallet minerWallet = Wallet.createNewWallet();

		this.blockChainNode = new BlockChainNode(minerWallet.address());
		minerWallet.setBlockchainNode(this.blockChainNode);

		this.trezor = new Trezor();
		this.trezor.addMinerWallet(NodeCliStdIn.MINER_WALLET_NAME, minerWallet);
	}

	public void mainMethod(int port) {
		port(port);

		post("/help", (request, response) -> {
			response.type("application/json");

			return HelpCmd.getHelpText();
		});

		post("/exit", (request, response) -> {
			stop();

			return "{ Message: Spark Node Stoped. }\n\n";
		});

		post("/maddr", (request, response) -> {
			String minerAddress = "{ Miner: " + this.trezor.get(NodeCliStdIn.MINER_WALLET_NAME) + "}" + "\n\n";

			return minerAddress;
		});

		// TODO
		// improve - use JSON or YAML format
		// Body: 'name: WALLET-NAME'
		post("/wnew", "application/json", (request, response) -> {
			String walletName = request.body().trim();

			Wallet newWallet = Wallet.createNewWallet().setBlockchainNode(this.blockChainNode);
			this.trezor.put(walletName, newWallet);

			String retString = "{ NewWallet: " + newWallet + " }\n\n";
			return retString;
		});

		// TODO
		// improve
		post("/mwnew", "application/json", (request, response) -> {
			String body = request.body();

			String[] walletNames = body.split(" ");

			StringBuilder report = new StringBuilder();

			for (String walletName : walletNames) {
				if (trezor.containsKey(walletName)) {
					report.append("Wallet with name " + walletName + " already exists.").append("\n");
				}

				Wallet newWallet = Wallet.createNewWallet().setBlockchainNode(blockChainNode);
				trezor.put(walletName, newWallet);

				report.append("Created: " + newWallet).append("\n");
			}

			return report.toString();
		});

		post("/wstat", "application/json", (request, response) -> {
			String walletName = request.body().trim();

			Wallet wallet = this.trezor.get(walletName);

			String walletStat = "{ " + wallet + " }" + "\n\n";

			return walletStat;
		});

		// wdel NAME
		post("/wdel", "application/json", (request, response) -> {
			// TODO

			return "TODO: wdel...\n\n";
		});

		post("/wlist", "application/json", (request, response) -> {
			StringBuilder sb = new StringBuilder();
			this.trezor.allWallets().forEach((walletName, wallet) -> {
				sb.append("{ Wallet Name: " + walletName + " " + wallet + ", Balance: " + wallet.balance() + " }");
			});

			String retString = sb.append("\n\n").toString();

			return retString;
		});

		// Send coins from one to another wallet.
		// send WALLET-NAME-1 WLLET-NAME-2 VALUE"
		post("/send", "application/json", (request, response) -> {
			String[] cmd = request.body().split(" ");
			String wallet1Name = cmd[0].trim();
			String wallet2Name = cmd[1].trim();
			String amount = cmd[2].trim();

			System.out.println("w1: " + wallet1Name);
			System.out.println("w2: " + wallet2Name);
			System.out.println("Amount: " + amount);

			Wallet wallet1 = this.trezor.get(wallet1Name);
			Wallet wallet2 = this.trezor.get(wallet2Name);
			int coinValue = Integer.valueOf(amount);

			Payment payment = new Payment(wallet2.address(), coinValue);

			List<Payment> payments = ImmutableList.of(payment);
			Tx sendTx = wallet1.send(payments);

			String retString = sendTx + "\n\n";

			return retString;
		});

		// msend WALLET-SENDER WALLET-NAME-1 VALUE-1...WALLET-NAME-N VALUE-N"
		post("/msend", "application/json", (request, response) -> {
			// TODO
			//

			return "TODO: MSend..." + "\n\n";
		});

		post("/print", "application/json", (request, response) -> {
			String retString = this.blockChainNode.status() + "\n\n";

			return retString;
		});

		post("/print-bc", "application/json", (request, response) -> {
			String bcString = this.blockChainNode.printBlockChain();

			return bcString;
		});

		post("/print-utxo", "application/json", (request, response) -> {
			StringBuilder sb = new StringBuilder();

			this.blockChainNode.getAllUtxo().forEach(utxo -> {
				sb.append(utxo.toString());
			});

			String retString = sb.append("\n\n").toString();

			return retString;
		});

		post("/print-block[NUM]", "application/json", (request, response) -> {
			// TODO
			//

			return "TODO: Pring Block Num..." + "\n\n";
		});
	}

}
