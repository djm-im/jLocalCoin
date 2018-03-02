package im.djm.blockchain.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import im.djm.exception.WrapperException;

/**
 * @author djm.im
 */
public final class HashUtil {

	private HashUtil() {
		throw new IllegalStateException("It is not allowed to call private constructor.");
	}

	public static DataHash calculateDataHash(byte[] rawData) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawData);

		return new DataHash(hashBytes);
	}

	public static BlockHash calculateBlockHashForHead(byte[] rawHead) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawHead);

		return new BlockHash(hashBytes);
	}

	public static AddressHash calculateAddressHash(byte[] rawAddress) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawAddress);

		return new AddressHash(hashBytes);
	}

	public static TxHash calculateTxHash(byte[] rawTx) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawTx);

		return new TxHash(hashBytes);
	}

	private static byte[] calculateRawHash(byte[] rawBytes) {
		try {
			String hashAlgorithm = "SHA-256";

			return hash(hashAlgorithm, rawBytes);
		} catch (NoSuchAlgorithmException ex) {
			throw new WrapperException(".calculateRawHash", ex);
		}

	}

	private static byte[] hash(String hashAlgorithm, byte[] rawBytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
		md.update(rawBytes);
		byte[] digest = md.digest();

		return digest;
	}

}
