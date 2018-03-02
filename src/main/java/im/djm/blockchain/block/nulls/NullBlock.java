package im.djm.blockchain.block.nulls;

import im.djm.blockchain.block.Block;

/**
 * 
 * @author djm.im
 *
 */
public final class NullBlock extends Block {

	public NullBlock() {
		// TODO
		// 1510903985
		super(NullValues.NULL_DATA, NullValues.NULL_BLOCK_HASH);
	}

}
