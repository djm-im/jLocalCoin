package im.djm.blockchain.block;

import im.djm.blockchain.exception.BlockChainException;

public class NullBlockException extends BlockChainException {

	private static final long serialVersionUID = -7697007268158053867L;

	public NullBlockException(String message) {
		super(message);
	}

}
