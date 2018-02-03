package im.djm.blockchain.hash;

/**
 * 
 * @author djm.im
 *
 */
// TODO: update to Hash be interface
public class TxHash extends Hash {

	public TxHash(byte[] bytes) {
		super(bytes);
	}

	public TxHash(TxHash prevTxHash) {
		this(prevTxHash.getRawHash());
	}

}
