package im.djm.coin.node;

import im.djm.blockchain.exception.BlockChainException;

public class NullNetworkException extends BlockChainException {

	private static final long serialVersionUID = 8923623811746253577L;

	public NullNetworkException(String message) {
		super(message);
	}

}
