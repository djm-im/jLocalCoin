package im.djm.p2p;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.stop;

import java.util.List;

import com.google.common.collect.ImmutableList;

import im.djm.coin.tx.Tx;
import im.djm.p2p.cli.NodeCliStdIn;
import im.djm.p2p.node.BlockChainNode;
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

		get("/miner-address", (req, res) -> {
			return this.trezor.get(NodeCliStdIn.MINER_WALLET_NAME) + "\n\n";
		});

		get("/help", (req, res) -> {
			// TODO
			// HelpCmd.getHelpMessage();
			//

			return "TODO: Help...\n\n";
		});

		get("/exit", (req, res) -> {
			stop();
			return "Spark Node Stoped.\n\n";
		});

		// wnew WALLET-NAME
		get("/wnew", (req, res) -> {
			String walletName = req.queryParams("name");

			Wallet newWallet = Wallet.createNewWallet().setBlockchainNode(this.blockChainNode);
			this.trezor.put(walletName, newWallet);

			return "Created new wallet: " + walletName + ".\n\n";
		});

		get("/mwnew", (req, res) -> {
			// TODO
			// Use post method
			// mwnew WALLET-NAME-1 WALLET-NAME-2 ... WALLET-NAME-N
			//

			return "TODO: MWNew...\n\n";
		});

		get("/wstat", (req, res) -> {
			String walletName = req.queryParams("name");
			Wallet wallet = this.trezor.get(walletName);

			String walletStat = wallet + "\n\n";

			return walletStat;
		});

		get("/wdel- Delete a wallet wdel WALLET- NAME", (req, res) -> {
			// TODO
			//

			return "TODO: wdel...\n\n";
		});

		get("/wlist", (req, res) -> {
			StringBuilder sb = new StringBuilder();
			this.trezor.allWallets().forEach((walletName, wallet) -> {
				sb.append("{ Wallet Name: " + walletName + " " + wallet + ", Balance: " + wallet.balance() + " }");
			});

			String retString = sb.append("\n\n").toString();

			return retString;
		});

		// Send coins from one to another wallet.
		// send WALLET-NAME-1 WLLET-NAME-2 VALUE"
		get("/send/:w1/:w2/:amount", (req, res) -> {
			// TODO
			String wallet1Name = req.params(":w1");
			String wallet2Name = req.params(":w2");
			String amount = req.params(":amount");
			System.out.println("Req: " + req);
			System.out.println("Parameters: " + req.queryParams());
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

		get("/msend. msend WALLET-SENDER WALLET-NAME-1 VALUE-1...WALLET-NAME-N VALUE-N", (req, res) -> {
			// TODO
			//

			return "TODO: MSend...";
		});

		get("/print", (req, res) -> {
			String retString = this.blockChainNode.status() + "\n\n";

			return retString;
		});

		get("/print-bc", (req, res) -> {
			String bcString = this.blockChainNode.printBlockChain();

			return bcString;
		});

		get("/print-utxo", (req, res) -> {
			StringBuilder sb = new StringBuilder();

			this.blockChainNode.getAllUtxo().forEach(utxo -> {
				sb.append(utxo.toString());
			});

			String retString = sb.append("\n\n").toString();

			return retString;
		});

		get("/print-block[NUM]", (req, res) -> {
			// TODO
			//

			return "TODO: Pring Block Num...";
		});
	}

}
