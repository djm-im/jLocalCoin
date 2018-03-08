package im.djm.blockchain.block;

import java.util.List;
import java.util.function.Predicate;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.block.data.Data;
import im.djm.blockchain.block.data.Validator;
import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.DataHash;
import im.djm.blockchain.hash.HashUtil;

/**
 * @author djm.im
 */
public class Miner {

	public static Head createHead(Block prevBlock, DataHash dataHash) {
		BlockHash prevHash = prevBlock.getBlockHash();
		long length = prevBlock.getLength() + 1;

		return new Head(prevHash, length, dataHash, BlockChain.DIFFICULTY);
	}

	public static Block createNewBlock(Block prevBlock, Data data) {
		DataHash dataHash = HashUtil.dataHash(data.getRawData());
		Head head = createHead(prevBlock, dataHash);

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
		BlockHash blockHash = HashUtil.blockHash(rawHead);

		return blockHash;
	}

	// Factory method.
	private static Block createBlock(Head head, Data data, BlockHash blockHash) {
		return new Block(head, data, blockHash);
	}

}
