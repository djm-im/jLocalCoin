package im.djm.blockchain.hash;

import im.djm.exception.BlockChainException;

/**
 * @author djm.im
 */
public class NullTxSignatureException extends BlockChainException {

	private static final long serialVersionUID = -3009989191571131285L;

	public NullTxSignatureException(String message) {
		super(message);
	}

}
