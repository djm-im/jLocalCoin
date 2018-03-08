package im.djm.txhash;

import im.djm.blockchain.hash.Hash;

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
