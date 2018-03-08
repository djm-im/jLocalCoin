package im.djm.exception;

import im.djm.blockchain.exception.BlockChainException;

public class TxInputException extends BlockChainException {

	private static final long serialVersionUID = -7445875445653750488L;

	public TxInputException() {
		super();
	}

	public TxInputException(String message) {
		super(message);
	}

}
