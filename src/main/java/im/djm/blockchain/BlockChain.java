package im.djm.blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import im.djm.blockchain.block.Block;
import im.djm.blockchain.block.data.Data;
import im.djm.blockchain.block.data.Validator;
import im.djm.blockchain.block.nulls.NullTxData;
import im.djm.blockchain.block.nulls.NullValues;
import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.TxHash;
import im.djm.exception.NullWalletAddressException;
import im.djm.node.TxDataBlock;
import im.djm.node.TxUtxoPoolsNode;
import im.djm.tx.Tx;
import im.djm.tx.TxData;
import im.djm.utxo.Utxo;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class BlockChain {

	public static final int DIFFICULTY = 16;

	private Map<BlockHash, BlockWrapper> blocks = new HashMap<>();

	private BlockWrapper topBlockWrapper;

	private static Validator<Data> dataValidator = new Validator<Data>() {
	};

	private TxUtxoPoolsNode txNode;

	private TxDataBlock txDataBlock;

	private static List<Predicate<Data>> dataValidationRules = new ArrayList<>();
	static {
		dataValidationRules.add(data -> data != null);
		dataValidationRules.add(data -> true);
	}

	private static Validator<Block> blockValidator = new Validator<Block>() {
	};

	private List<Predicate<Block>> blockValidationRules = new ArrayList<>();

	public static Validator<BlockHash> blockHashValidator = new Validator<BlockHash>() {
	};

	public static List<Predicate<BlockHash>> blockHashValidationRules = new ArrayList<>();
	static {
		blockHashValidationRules.add(blockHash -> blockHash != null);
		blockHashValidationRules.add(blockHash -> blockHash.getBinaryLeadingZeros() >= DIFFICULTY);
	}

	private void initBlockValidationRules() {
		this.blockValidationRules.add(block -> block != null);

		// Validate data
		this.blockValidationRules.add(block -> {
			return BlockChain.dataValidator.isValid(block.getData(), BlockChain.dataValidationRules);
		});

		// check if previous block exist in block chain
		this.blockValidationRules.add(block -> {
			BlockHash prevHashKey = block.getPrevBlockHash();
			BlockWrapper prevBlock = this.blocks.get(prevHashKey);
			if (prevBlock == null) {
				return false;
			}
			return true;
		});

		// check if `block` already exist in map
		this.blockValidationRules.add(block -> {
			BlockHash theBlockKey = block.getBlockHash();
			BlockWrapper theBlock = this.blocks.get(theBlockKey);
			if (theBlock != null) {
				return false;
			}
			return true;
		});

	}

	public BlockChain(WalletAddress walletAddress) {
		if (walletAddress == null) {
			throw new NullWalletAddressException("Wallet address cannot be null.");
		}

		this.initBlockValidationRules();

		this.txNode = new TxUtxoPoolsNode();

		this.txDataBlock = new TxDataBlock(this, walletAddress, this.txNode);

		this.initNullBlock();
		this.initNullTxBlock();
	}

	// the null block has to be the same for in all nodes
	private void initNullBlock() {
		this.wrapAndAddBlock(NullValues.NULL_BLOCK, null);
	}

	private void initNullTxBlock() {
		NullTxData nullTxData = new NullTxData();

		Block txBlock = this.txDataBlock.generateNewTxBlock(nullTxData);
		this.add(txBlock);
	}

	// End: constructor area
	// **************************************************************************************

	/**
	 * 
	 * @param block
	 * @return
	 * 
	 * 		Add block that already exists.
	 */
	public boolean add(Block block) {
		if (BlockChain.blockValidator.isValid(block, this.blockValidationRules)) {
			return addValidBlock(block);
		}

		return false;
	}

	private boolean addValidBlock(Block block) {
		BlockWrapper prevBlockWrapper = this.blocks.get(block.getPrevBlockHash());

		if (prevBlockWrapper == null) {
			// TODO
			// throw an exception
			return false;
		}

		return this.wrapAndAddBlock(block, prevBlockWrapper);
	}

	private boolean wrapAndAddBlock(Block block, BlockWrapper prevBlockWrapper) {
		BlockHash mapKey = block.getBlockHash();
		BlockWrapper blockWrapper = new BlockWrapper(block, prevBlockWrapper);

		this.blocks.put(mapKey, blockWrapper);
		this.topBlockWrapper = blockWrapper;

		return true;
	}

	public void add(Tx tx) {
		Block block = createTxDataBlock(tx);

		this.add(block);
	}

	private Block createTxDataBlock(Tx tx) {
		TxData txData = new TxData();
		txData.add(tx);

		Block block = this.txDataBlock.generateNewTxBlock(txData);

		return block;
	}

	public Block getTopBlock() {
		return this.topBlockWrapper.getBlock();
	}

	public List<Utxo> getAllUtxo() {
		return this.txNode.getAllUtxo();
	}

	public Tx getTxFromPool(TxHash txId) {
		return this.txNode.getTxFromPool(txId);
	}

	public List<Utxo> getUtxoFor(WalletAddress walletAddress) {
		return this.txNode.getUtxoFor(walletAddress);
	}

	public long getBalance(WalletAddress walletAddress) {
		return this.txNode.getBalance(walletAddress);
	}

	public String status() {
		return Long.toString(this.getTopBlock().getLength());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Print blockchain from top to bottom (genesis block).");
		sb.append(System.lineSeparator());

		BlockWrapper currentBlockWrapper = this.topBlockWrapper;
		do {
			sb.append(currentBlockWrapper.getBlock());

			currentBlockWrapper = currentBlockWrapper.getPrevBlockWrapper();
		} while (currentBlockWrapper != null);

		return sb.toString();
	}

}
