package im.djm.blockchain.hash;

/**
 * 
 * @author djm.im
 *
 */
public class TxHash extends Hash {

	public TxHash(byte[] bytes) {
		super(bytes);
	}

	public TxHash(TxHash prevTxHash) {
		this(prevTxHash.getRawHash());
	}

}
