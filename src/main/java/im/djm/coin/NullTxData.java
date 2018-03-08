package im.djm.coin;

import im.djm.coin.tx.TxData;

/**
 * @author djm.im
 */
public class NullTxData extends TxData {

	private static final String NULL_TX = "Null Tx";

	@Override
	public byte[] getRawData() {
		return NULL_TX.getBytes();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
