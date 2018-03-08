package im.djm.node;

import java.util.List;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.block.Block;
import im.djm.blockchain.block.Miner;
import im.djm.coin.NullTxData;
import im.djm.exception.NullWalletAddressException;
import im.djm.tx.Tx;
import im.djm.tx.TxData;
import im.djm.txhash.TxHash;
import im.djm.utxo.Utxo;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class BlockChainNode {

	static final int REWARD = 100;

	private BlockChain blockChain;

	private TxUtxoPoolsNode txUtxoPool;

	private WalletAddress minerAddress;

	public BlockChainNode(WalletAddress minerAddress) {
		if (minerAddress == null) {
			throw new NullWalletAddressException("Wallet address cannot be null.");
		}
		this.minerAddress = minerAddress;
		this.txUtxoPool = new TxUtxoPoolsNode();

		this.blockChain = new BlockChain();

		Block nullTxBlock = this.createNullTxBlock();
		this.blockChain.add(nullTxBlock);
	}

	private Block createNullTxBlock() {
		NullTxData nullTxData = new NullTxData();

		Block txBlock = this.generateNewTxBlock(nullTxData);

		return txBlock;
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

		Block block = this.generateNewTxBlock(txData);

		return block;
	}

	public Block generateNewTxBlock(final TxData txData) {
		TxData txDataLocal = this.addCoinbaseTx(txData);

		this.txUtxoPool.updateTxPoolAndUtxoPool(txDataLocal);

		Block prevBlock = blockChain.getTopBlock();

		return Miner.createNewBlock(prevBlock, txDataLocal);
	}

	private TxData addCoinbaseTx(TxData txData) {
		Tx coinbaseTx = new Tx(this.minerAddress, BlockChainNode.REWARD);
		txData.addCoinbaseTx(coinbaseTx);

		return txData;
	}

	@Override
	public String toString() {
		return this.blockChain.toString();
	}

}
