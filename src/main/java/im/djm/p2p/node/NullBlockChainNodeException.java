package im.djm.p2p.node;

import im.djm.blockchain.exception.BlockChainException;

/**
 * @author djm.im
 */
public class NullBlockChainNodeException extends BlockChainException {

	private static final long serialVersionUID = 3658219440244090523L;

	public NullBlockChainNodeException(String message) {
		super(message);
	}

}
