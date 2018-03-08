package im.djm.txhash;

import im.djm.blockchain.hash.Hash;
import im.djm.blockchain.hash.HashUtil;

/**
 * @author djm.im
 */
public class AddressHash extends Hash {

	public AddressHash(byte[] bytes) {
		super(bytes);
	}

	public static AddressHash hash(byte[] rawAddress) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawAddress);

		return new AddressHash(hashBytes);
	}

}
