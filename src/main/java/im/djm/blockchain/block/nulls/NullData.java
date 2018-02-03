package im.djm.blockchain.block.nulls;

import im.djm.blockchain.block.data.Data;

/**
 * 
 * @author djm.im
 *
 */
public final class NullData implements Data {

	private static final String NULL_DATA = "Null Data";

	@Override
	public byte[] getRawData() {
		return NULL_DATA.getBytes();
	}

	@Override
	public String toString() {
		return NULL_DATA;
	}

}
