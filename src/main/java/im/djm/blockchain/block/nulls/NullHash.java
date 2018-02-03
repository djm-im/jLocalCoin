package im.djm.blockchain.block.nulls;

import im.djm.blockchain.hash.BlockHash;

/**
 * 
 * @author djm.im
 *
 */
public final class NullHash extends BlockHash {

	public NullHash() {
		super(new byte[32]);
	}

}
