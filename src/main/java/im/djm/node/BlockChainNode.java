package im.djm.node;

import java.util.List;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.block.Block;
import im.djm.blockchain.hash.TxHash;
import im.djm.tx.Tx;
import im.djm.tx.TxData;
import im.djm.utxo.Utxo;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class BlockChainNode {

	private BlockChain blockChain;

	private TxUtxoPoolsNode txUtxoPool;

	private TxDataBlock txDataBlock;

	public BlockChainNode(WalletAddress minerWallet) {
		this.txUtxoPool = new TxUtxoPoolsNode();

		this.blockChain = new BlockChain(this.txUtxoPool, minerWallet);

		this.txDataBlock = this.blockChain.getTxDataBlock();
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
		Block block = this.createTxDataBlock(newTx);

		this.blockChain.add(block);
	}

	public Block createTxDataBlock(Tx tx) {
		TxData txData = new TxData();
		txData.add(tx);

		Block block = this.txDataBlock.generateNewTxBlock(txData);

		return block;
	}

	@Override
	public String toString() {
		return this.blockChain.toString();
	}

}
