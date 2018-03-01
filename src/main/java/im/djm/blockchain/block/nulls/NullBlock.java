package im.djm.blockchain.block.nulls;

import java.security.NoSuchAlgorithmException;

import im.djm.blockchain.block.Block;

/**
 * 
 * @author djm.im
 *
 */
public final class NullBlock extends Block {

	/**
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public NullBlock() {
		// TODO
		// 1510903985
		super(new NullData(), new NullBlockHash());
	}

}
