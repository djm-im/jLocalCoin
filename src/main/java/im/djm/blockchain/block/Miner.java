package im.djm.blockchain.block;

import java.util.List;
import java.util.function.Predicate;

import im.djm.blockchain.BlockUtil;
import im.djm.blockchain.block.data.Data;
import im.djm.blockchain.block.data.Validator;
import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.DataHash;
import im.djm.blockchain.hash.ByteArrayUtil;

/**
 * @author djm.im
 *
 */
public class Miner {

	// TODO
	// Replace Data with TxData
	public static Block createNewBlock(Block prevBlock, Data data, Validator<BlockHash> hashValidator,
			List<Predicate<BlockHash>> rules) {

		Head head = createHead(prevBlock, data);

		byte[] rawData = data.getRawData();

		byte[] rawBlock = BlockUtil.concatenateArrays(head.getRawHead(), rawData);

		BlockHash hash = ByteArrayUtil.calculateBlockHash(rawBlock);

		while (!hashValidator.isValid(hash, rules)) {
			head.incNonce();
			rawBlock = BlockUtil.concatenateArrays(head.getRawHead(), rawData);
			hash = ByteArrayUtil.calculateBlockHash(rawBlock);
		}

		return Block.createBlock(head, data, hash);
	}

	static Head createHead(Block prevBlock, Data data) {
		BlockHash prevHash = prevBlock.getBlockHash();

		long length = prevBlock.getLength() + 1;
		DataHash dataHash = ByteArrayUtil.calculateDataHash(data.getRawData());

		return new Head(prevHash, length, dataHash);
	}

}
