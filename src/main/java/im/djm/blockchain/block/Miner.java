package im.djm.blockchain.block;

import java.util.List;
import java.util.function.Predicate;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.block.data.Data;
import im.djm.blockchain.block.data.Validator;
import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.ByteArrayUtil;
import im.djm.blockchain.hash.DataHash;

/**
 * @author djm.im
 */
public class Miner {

	static Head createHead(Block prevBlock, Data data) {
		BlockHash prevHash = prevBlock.getBlockHash();
		long length = prevBlock.getLength() + 1;

		byte[] rawData = data.getRawData();
		DataHash dataHash = ByteArrayUtil.calculateDataHash(rawData);

		return new Head(prevHash, length, dataHash);
	}

	public static Block createNewBlock(Block prevBlock, Data data) {
		Head head = createHead(prevBlock, data);

		BlockHash blockHash = findNonce(head);

		return Miner.createBlock(head, data, blockHash);
	}

	private static BlockHash findNonce(Head head) {
		Validator<BlockHash> hashValidator = BlockChain.blockHashValidator;
		List<Predicate<BlockHash>> hashValidationRules = BlockChain.blockHashValidationRules;

		BlockHash blockHash = Miner.calcBlockHashForHead(head);
		while (!hashValidator.isValid(blockHash, hashValidationRules)) {
			head.incNonce();
			blockHash = Miner.calcBlockHashForHead(head);
		}

		return blockHash;
	}

	public static BlockHash calcBlockHashForHead(Head head) {
		byte[] rawHead = head.getRawHead();
		BlockHash blockHash = ByteArrayUtil.calculateBlockHashForHead(rawHead);

		return blockHash;
	}

	// Factory method.
	private static Block createBlock(Head head, Data data, BlockHash hash) {
		return new Block(head, data, hash);
	}

}
