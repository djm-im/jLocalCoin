package im.djm.blockchain.hash;

/**
 * @author djm.im
 */
public class DataHash extends Hash {

	public DataHash(byte[] bytes) {
		super(bytes);
	}

	public static DataHash hash(byte[] rawData) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawData);

		return new DataHash(hashBytes);
	}

}
