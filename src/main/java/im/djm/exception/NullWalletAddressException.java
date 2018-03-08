package im.djm.exception;

import im.djm.blockchain.exception.BlockChainException;

public class NullWalletAddressException extends BlockChainException {

	private static final long serialVersionUID = -5123753550132077977L;

	public NullWalletAddressException(String message) {
		super(message);
	}

}
