package im.djm.blockchain.exception;

public class WrapperException extends RuntimeException {

	private static final long serialVersionUID = -9135764168706871611L;

	public WrapperException(String message, Exception cause) {
		super(message, cause);
	}

}
