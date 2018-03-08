package im.djm.node;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.block.Block;
import im.djm.blockchain.block.Miner;
import im.djm.tx.Tx;
import im.djm.tx.TxData;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class TxDataBlock {

	private static final int REWARD = 100;

	private BlockChain blockChain;

	private WalletAddress minerAddress;

	private TxUtxoPoolsNode txUtxoPool;

	public TxDataBlock(BlockChain blockChain, WalletAddress minerAddress, TxUtxoPoolsNode txUtxoPool) {
		this.blockChain = blockChain;
		this.minerAddress = minerAddress;

		this.txUtxoPool = txUtxoPool;
	}

	public Block generateNewTxBlock(final TxData txData) {
		TxData txDataLocal = this.addCoinbaseTx(txData);

		this.txUtxoPool.updateTxPoolAndUtxoPool(txDataLocal);

		Block prevBlock = blockChain.getTopBlock();

		return Miner.createNewBlock(prevBlock, txDataLocal);
	}

	private TxData addCoinbaseTx(TxData txData) {
		Tx coinbaseTx = new Tx(this.minerAddress, TxDataBlock.REWARD);
		txData.addCoinbaseTx(coinbaseTx);

		return txData;
	}

}
