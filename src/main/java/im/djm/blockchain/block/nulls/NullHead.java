package im.djm.blockchain.block.nulls;

import im.djm.blockchain.block.Head;

public class NullHead extends Head {

	public NullHead() {
		super(NullValues.NULL_BLOCK_HASH, NullValues.NULL_LENGTH, NullValues.NULL_TIMESTAMP, NullValues.NULL_DATA_HASH);
	}

}
