package im.djm.blockchain.block;

import java.util.List;
import java.util.function.Predicate;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.BlockUtil;
import im.djm.blockchain.block.data.Data;
import im.djm.blockchain.block.data.Validator;
import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.ByteArrayUtil;
import im.djm.blockchain.hash.DataHash;

/**
 * @author djm.im
 */
public class Miner {

	public static Block createNewBlock(Block prevBlock, Data data) {
		Head head = createHead(prevBlock, data);

		BlockHash blockHash = findNonce(data, head);

		// head - side effect
		return Miner.createBlock(head, data, blockHash);
	}

	private static BlockHash findNonce(Data data, Head head) {
		Validator<BlockHash> hashValidator = BlockChain.hashValidator;
		List<Predicate<BlockHash>> hashValidationRules = BlockChain.hashValidationRules;

		byte[] rawData = data.getRawData();
		BlockHash blockHash = calcBlockHash(head, rawData);
		while (!hashValidator.isValid(blockHash, hashValidationRules)) {
			blockHash = nextNonce(head, rawData);
		}

		return blockHash;
	}

	private static BlockHash nextNonce(Head head, byte[] rawData) {
		head.incNonce();

		// TODO ???
		// Should rawData of a data block be get each time
		// a header contains hash of data
		return calcBlockHash(head, rawData);
	}

	private static BlockHash calcBlockHash(Head head, byte[] rawData) {
		byte[] rawBlock = BlockUtil.concatenateArrays(head.getRawHead(), rawData);

		return ByteArrayUtil.calculateBlockHash(rawBlock);
	}

	// Factory method.
	private static Block createBlock(Head head, Data data, BlockHash hash) {
		return new Block(head, data, hash);
	}

	static Head createHead(Block prevBlock, Data data) {
		BlockHash prevHash = prevBlock.getBlockHash();

		long length = prevBlock.getLength() + 1;
		DataHash dataHash = ByteArrayUtil.calculateDataHash(data.getRawData());

		return new Head(prevHash, length, dataHash);
	}

}
