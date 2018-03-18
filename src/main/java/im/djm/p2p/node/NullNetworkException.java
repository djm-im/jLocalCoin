package im.djm.p2p.node;

import im.djm.blockchain.exception.BlockChainException;

/**
 * @author djm.im
 */
public class NullNetworkException extends BlockChainException {

	private static final long serialVersionUID = 8923623811746253577L;

	public NullNetworkException(String message) {
		super(message);
	}

}
