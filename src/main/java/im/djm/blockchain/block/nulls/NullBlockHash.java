package im.djm.blockchain.block.nulls;

import im.djm.blockchain.hash.BlockHash;

/**
 * @author djm.im
 */
public final class NullBlockHash extends BlockHash {

	public NullBlockHash() {
		super(new byte[32]);
	}

}
