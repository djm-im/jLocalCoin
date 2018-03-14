package im.djm.blockchain.exception;

/**
 * @author djm.im
 */
public abstract class BlockChainException extends RuntimeException {

	private static final long serialVersionUID = 3219644652662286852L;

	public BlockChainException(String message) {
		super(message);
	}

	public BlockChainException() {
		super();
	}

}
