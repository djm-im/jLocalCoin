package im.djm.blockchain.hash;

import im.djm.blockchain.exception.BlockChainException;

public class NullHashException extends BlockChainException {

	private static final long serialVersionUID = -619561739986705441L;

	public NullHashException() {
		super();
	}

	public NullHashException(String message) {
		super(message);
	}

}
