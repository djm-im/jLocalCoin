package im.djm.p2p.node;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import im.djm.p2p.cli.BlockChainCmd;
import im.djm.p2p.cli.HelpCmd;
import im.djm.p2p.cli.NodeCliStdIn;
import im.djm.p2p.cli.WalletCmd;
import im.djm.wallet.Trezor;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
public class SparkNode {

	private BlockChainNode blockChainNode;

	private Trezor trezor;

	private BlockChainCmd bcCmd = new BlockChainCmd();

	private WalletCmd wCmd = new WalletCmd();

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

		// Send coins from one to another wallet.
		// send WALLET-NAME-1 WLLET-NAME-2 VALUE"
		post("/send", "application/json", (request, response) -> {
			return wCmd.sendCoin(this.trezor, request.body().split(" "));
		});

		// msend WALLET-SENDER WALLET-NAME-1 VALUE-1...WALLET-NAME-N VALUE-N"
		post("/msend", "application/json", (request, response) -> {
			return wCmd.sendMultiCoins(this.trezor, request.body().split(" "));
		});

		// TODO
		// improve - use JSON or YAML format
		// Body: 'name: WALLET-NAME'
		post("/wnew", "application/json", (request, response) -> {
			return wCmd.createNewWallet(this.blockChainNode, this.trezor, request.body().split(" "));
		});

		post("/mwnew", "application/json", (request, response) -> {
			return wCmd.createMultiNewWallets(this.blockChainNode, this.trezor, request.body().split(" "));
		});

		post("/wstat", "application/json", (request, response) -> {
			return wCmd.walletStatus(this.trezor, request.body().split(" "));
		});

		post("/wdel", "application/json", (request, response) -> {
			return wCmd.deleteWallet(this.trezor, request.body().split(" "));
		});

		post("/wlist", "application/json", (request, response) -> {
			return wCmd.listAllWallets(this.trezor);
		});

		post("/print", "application/json", (request, response) -> {
			return bcCmd.printCmd(this.blockChainNode, request.body().split(" "));
		});

		post("/print-bc", "application/json", (request, response) -> {
			return this.blockChainNode.printBlockChain();
		});

		// --------------------------------------------------------------------------------------------------------

		post("/maddr", (request, response) -> {
			return "{ Miner: " + this.trezor.get(NodeCliStdIn.MINER_WALLET_NAME) + "}" + "\n\n";
		});
	}

}
