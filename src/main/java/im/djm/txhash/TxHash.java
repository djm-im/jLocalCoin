package im.djm.txhash;

import im.djm.blockchain.hash.Hash;
import im.djm.blockchain.hash.HashUtil;

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

	public static TxHash hash(byte[] rawTx) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawTx);

		return new TxHash(hashBytes);
	}

}
