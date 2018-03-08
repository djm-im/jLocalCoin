package im.djm.tx;

import im.djm.blockchain.exception.BlockChainException;

/**
 * @author djm.im
 */
public class TxException extends BlockChainException {

	private static final long serialVersionUID = 3125940497525781916L;

	public TxException(String message) {
		super(message);
	}

}
