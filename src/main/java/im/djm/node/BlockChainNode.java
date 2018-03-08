package im.djm.node;

import java.util.List;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.hash.TxHash;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class BlockChainNode {

	private BlockChain blockChain;

	private TxUtxoPoolsNode txUtxoPool;

	public BlockChainNode(WalletAddress minerWallet) {
		this.txUtxoPool = new TxUtxoPoolsNode();

		this.blockChain = new BlockChain(this.txUtxoPool, minerWallet);
	}

	// TODO remove
	public BlockChain getBlockchain() {
		return this.blockChain;
	}

	public String status() {
		return this.blockChain.status();
	}

	public List<Utxo> getAllUtxo() {
		return this.txUtxoPool.getAllUtxo();
	}

	public Tx getTxFromPool(TxHash txId) {
		return this.txUtxoPool.getTxFromPool(txId);
	}

	public String printBlockChain() {
		return this.blockChain.toString();
	}

	public long getBalance(WalletAddress walletAddress) {
		return this.txUtxoPool.getBalance(walletAddress);
	}

	public List<Utxo> getUtxoFor(WalletAddress walletAddress) {
		return this.txUtxoPool.getUtxoFor(walletAddress);
	}

	public void sendCoin(Tx newTx) {
		this.blockChain.add(newTx);
	}

	@Override
	public String toString() {
		return this.blockChain.toString();
	}

}
