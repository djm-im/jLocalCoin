package im.djm.blockchain.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import im.djm.exception.WrapperException;

/**
 * @author djm.im
 */
public final class ByteArrayUtil {

	private ByteArrayUtil() {
		throw new IllegalStateException("It is not allowed to call private constructor.");
	}

	public static DataHash calculateDataHash(byte[] rawData) {
		byte[] hashBytes = ByteArrayUtil.calculateRawHash(rawData);

		return new DataHash(hashBytes);
	}

	public static BlockHash calculateBlockHash(byte[] rawBlock) {
		byte[] hashBytes = ByteArrayUtil.calculateRawHash(rawBlock);

		return new BlockHash(hashBytes);
	}

	public static AddressHash calculateAddressHash(byte[] rawAddress) {
		byte[] hashBytes = ByteArrayUtil.calculateRawHash(rawAddress);

		return new AddressHash(hashBytes);
	}

	public static TxHash calculateTxHash(byte[] rawTx) {
		byte[] hashBytes = ByteArrayUtil.calculateRawHash(rawTx);

		return new TxHash(hashBytes);
	}

	private static byte[] calculateRawHash(byte[] rawBytes) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(rawBytes);

			return md.digest();
		} catch (NoSuchAlgorithmException ex) {
			throw new WrapperException(".calculateRawHash", ex);
		}

	}

}
