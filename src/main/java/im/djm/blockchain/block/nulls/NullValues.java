package im.djm.blockchain.block.nulls;

import im.djm.blockchain.hash.DataHash;
import im.djm.blockchain.hash.HashUtil;

/**
 * @author djm.im
 */
public final class NullValues {

	public final static NullBlockHash NULL_BLOCK_HASH = new NullBlockHash();

	public final static NullData NULL_DATA = new NullData();

	public final static DataHash NULL_DATA_HASH = HashUtil.calculateDataHash(NULL_DATA.getRawData());

}
