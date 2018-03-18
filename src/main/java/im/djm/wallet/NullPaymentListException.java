package im.djm.wallet;

import im.djm.blockchain.exception.BlockChainException;

/**
 * @author djm.im
 */
public class NullPaymentListException extends BlockChainException {

	private static final long serialVersionUID = 4840415427528834671L;

	public NullPaymentListException(String message) {
		super(message);
	}

}