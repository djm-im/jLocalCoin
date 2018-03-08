package im.djm.blockchain.block;

import im.djm.blockchain.exception.BlockChainException;

public class NullDataException extends BlockChainException {

	private static final long serialVersionUID = -1031046769280836501L;

	public NullDataException(String message) {
		super(message);
	}

}
