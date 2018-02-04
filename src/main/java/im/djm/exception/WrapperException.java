package im.djm.exception;

public class WrapperException extends RuntimeException {

	public WrapperException(String message, Exception cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = -9135764168706871611L;

}
