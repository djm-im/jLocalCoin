package im.djm.node;

import java.util.List;

import im.djm.blockchain.BlockChain;
import im.djm.utxo.Utxo;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
class BlockchainNode {

	private BlockChain blockchain;

	public BlockchainNode(WalletAddress minerWallet) {
		this.blockchain = new BlockChain(minerWallet);
	}

	public BlockChain getBlockchain() {
		return this.blockchain;
	}

	public String status() {
		return this.blockchain.status();
	}

	public List<Utxo> getAllUtxo() {
		return this.blockchain.getAllUtxo();
	}

	public String printBlockChain() {
		return this.blockchain.toString();
	}

	@Override
	public String toString() {
		return this.blockchain.toString();
	}

}
