package im.djm.node;

import im.djm.exception.BlockChainException;

/**
 * @author djm.im
 */
public class NullBlockChainNodeException extends BlockChainException {

	private static final long serialVersionUID = 3658219440244090523L;

	public NullBlockChainNodeException(String message) {
		super(message);
	}

}