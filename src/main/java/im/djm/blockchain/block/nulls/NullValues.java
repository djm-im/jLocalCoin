package im.djm.blockchain.block.nulls;

import im.djm.blockchain.hash.DataHash;
import im.djm.blockchain.hash.HashUtil;

/**
 * @author djm.im
 */
public final class NullValues {

	public static final NullBlockHash NULL_BLOCK_HASH = new NullBlockHash();

	public static final NullData NULL_DATA = new NullData();

	public static final DataHash NULL_DATA_HASH = HashUtil.calculateDataHash(NULL_DATA.getRawData());

	public static final long NULL_TIMESTAMP = 1510903985;

	public static final long NULL_LENGTH = 0;

	public static final NullHead NULL_HEAD = new NullHead();

	public static final NullBlock NULL_BLOCK = new NullBlock();

}
