package im.djm.wallet;

import im.djm.exception.BlockChainException;

public class NullPaymentException extends BlockChainException {

	private static final long serialVersionUID = -8520960453785021275L;

	public NullPaymentException(String message) {
		super(message);
	}

}