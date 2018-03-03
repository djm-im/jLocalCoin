package im.djm.blockchain.block;

import im.djm.blockchain.block.data.Data;
import im.djm.blockchain.block.nulls.NullBlockHash;
import im.djm.blockchain.block.nulls.NullData;
import im.djm.blockchain.block.nulls.NullHead;
import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.DataHash;
import im.djm.blockchain.hash.HashUtil;
import im.djm.exception.NullBlockException;
import im.djm.exception.NullDataException;

/**
 * @author djm.im
 */
public class Block {

	private final Head head;

	private final Data data;

	private final BlockHash blockHash;

	public Block(final Block prevBlock, final Data data) {
		this.validate(prevBlock, data);

		this.data = data;

		byte[] rawData = data.getRawData();
		DataHash dataHash = HashUtil.dataHash(rawData);
		this.head = Miner.createHead(prevBlock, dataHash);

		this.blockHash = Miner.calcBlockHashForHead(this.head);
	}

	private void validate(Block prevBlock, Data data) {
		if (prevBlock == null) {
			throw new NullBlockException("Previous block cannot have null value.");
		}

		if (data == null) {
			throw new NullDataException("Data cannot be null.");
		}
	}

	/**
	 * Special constructor to create the first block - genesis (null) block.
	 */
	protected Block(NullHead nullHead, NullData nullData, NullBlockHash prevHash) {
		this.data = nullData;
		this.head = nullHead;
		this.blockHash = Miner.calcBlockHashForHead(this.head);
	}

	// package private constructor - Miner class call it
	Block(Head head, Data data, BlockHash hash) {
		this.head = head;
		this.data = data;
		this.blockHash = hash;
	}

	/**
	 * @return
	 * 
	 * 		Method returns hash for the block.
	 */
	public BlockHash getBlockHash() {
		return this.blockHash;
	}

	/**
	 * @return Method returns hash of previous block.
	 */
	public BlockHash getPrevBlockHash() {
		return this.head.getPrevHash();
	}

	public long getLength() {
		return this.head.getLength();
	}

	public Data getData() {
		return this.data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(System.lineSeparator());
		String dashes = "--------------------------------------------------------------------------------------------------------------------------------";
		sb.append("[[" + dashes);
		sb.append(System.lineSeparator());

		sb.append("Head: ");
		sb.append(this.head.toString());
		sb.append(System.lineSeparator());

		sb.append("Data: ");
		sb.append(this.data.toString());
		sb.append(System.lineSeparator());

		sb.append("Hash: ");
		sb.append(this.blockHash.toString());
		sb.append(System.lineSeparator());

		sb.append(dashes + "]]");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		return sb.toString();
	}

}
