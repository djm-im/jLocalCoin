package im.djm.coin.tx;

import im.djm.blockchain.exception.BlockChainException;

/**
 * @author djm.im
 */
public class TxInputException extends BlockChainException {

	private static final long serialVersionUID = -7445875445653750488L;

	public TxInputException() {
		super();
	}

	public TxInputException(String message) {
		super(message);
	}

}
