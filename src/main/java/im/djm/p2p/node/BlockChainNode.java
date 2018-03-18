package im.djm.p2p.node;

import java.util.ArrayList;
import java.util.List;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.BlockChainStatus;
import im.djm.blockchain.block.Block;
import im.djm.blockchain.block.Miner;
import im.djm.coin.NullTxData;
import im.djm.coin.tx.Tx;
import im.djm.coin.tx.TxData;
import im.djm.coin.txhash.TxHash;
import im.djm.coin.utxo.Utxo;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class BlockChainNode {

	static final int REWARD = 100;

	private BlockChain blockChain;

	private TxUtxoPoolsNode txUtxoPool;

	private WalletAddress minerAddress;

	private List<BlockChainNode> network = new ArrayList<>();

	public BlockChainNode() {
		this.initBlockChainAndPools();
	}

	public BlockChainNode(WalletAddress minerAddress) {
		this.initMinerAddress(minerAddress);

		this.initBlockChainAndPools();

		this.initNullTxBlock();
	}

	private void initMinerAddress(WalletAddress minerAddress) {
		if (minerAddress == null) {
			throw new NullWalletAddressException("Wallet address cannot be null.");
		}

		this.minerAddress = minerAddress;
	}

	private void initBlockChainAndPools() {
		this.txUtxoPool = new TxUtxoPoolsNode();
		this.blockChain = new BlockChain();
	}

	private void initNullTxBlock() {
		Block nullTxBlock = this.createNullTxBlock();
		this.blockChain.add(nullTxBlock);
	}

	private Block createNullTxBlock() {
		NullTxData nullTxData = new NullTxData();

		Block txBlock = this.generateNewTxBlock(nullTxData);

		return txBlock;
	}

	public BlockChainStatus status() {
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

		this.announceNewBlockToNetwork(block);
	}

	private void announceNewBlockToNetwork(Block block) {
		this.network.forEach(node -> node.annonceNewBlockCreated(block));
	}

	private void annonceNewBlockCreated(Block block) {
		this.update(block);
	}

	private Block createTxDataBlock(Tx tx) {
		TxData txData = new TxData();
		txData.add(tx);

		Block block = this.generateNewTxBlock(txData);

		return block;
	}

	private Block generateNewTxBlock(final TxData txData) {
		TxData txDataLocal = this.addCoinbaseTx(txData, BlockChainNode.REWARD);

		Block prevBlock = blockChain.getTopBlock();

		Block newBlock = Miner.createNewBlock(prevBlock, txDataLocal);
		this.updatePools(newBlock);

		return newBlock;
	}

	private TxData addCoinbaseTx(TxData txData, long reward) {
		if (this.minerAddress == null) {
			throw new NullBlockChainNodeException("Miner wallet is not set.");
		}

		Tx coinbaseTx = new Tx(this.minerAddress, reward);
		txData.addCoinbaseTx(coinbaseTx);

		return txData;
	}

	public void sync() {
		if (this.network == null || this.network.isEmpty()) {
			throw new NullNetworkException("No netwrok: The node didn't discover network.");
		}

		// TODO
		// Improve algorithm for sync
		// for now just sync with the first node

		BlockChainNode bcn = this.network.get(0);
		if (!this.status().equals(bcn.status())) {
			long start = this.status().getLength();

			List<Block> blocksFrom = bcn.getBlocksFrom(start);

			this.updateBlockChain(blocksFrom);
		}
	}

	private void updateBlockChain(List<Block> newBlocks) {
		newBlocks.forEach(block -> this.update(block));
	}

	private void update(Block block) {
		this.blockChain.add(block);

		this.updatePools(block);
	}

	private void updatePools(Block block) {
		TxData txData = (TxData) block.getData();

		this.txUtxoPool.updateTxPoolAndUtxoPool(txData);
	}

	private List<Block> getBlocksFrom(long start) {
		return this.blockChain.getBlocksFrom(start);
	}

	public void addNode(BlockChainNode bcn) {
		this.network.add(bcn);
	}

	public void setMinerAddress(WalletAddress minerAddress) {
		this.minerAddress = minerAddress;
	}

	@Override
	public String toString() {
		return this.blockChain.toString();
	}

}
