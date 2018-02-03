package im.djm.blockchain;

import im.djm.blockchain.block.Block;

/**
 * @author djm.im
 */
public class BlockWrapper {

	private Block block;

	private BlockWrapper prevBlockWrapper;

	public BlockWrapper(Block block, BlockWrapper prevBlockWrapper) {
		this.block = block;
		this.prevBlockWrapper = prevBlockWrapper;
	}

	public Block getBlock() {
		return this.block;
	}

	public BlockWrapper getPrevBlockWrapper() {
		return this.prevBlockWrapper;
	}

}
