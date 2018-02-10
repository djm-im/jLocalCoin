package im.djm.exception;

public class TxOutputException extends BlockChainException {

	private static final long serialVersionUID = 2898183211477185983L;

	public TxOutputException() {
		super();
	}

	public TxOutputException(String message) {
		super(message);
	}
}
