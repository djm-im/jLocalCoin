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

	public static DataHash calculateDataHash(byte[] rawBlock) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawBlock);
		return new DataHash(hashBytes);
	}

	public static BlockHash calculateBlockHash(byte[] rawBlock) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawBlock);
		return new BlockHash(hashBytes);
	}

	public static AddressHash calculateAddressHash(byte[] rawBlock) {
		byte[] hashBytes = HashUtil.calculateRawHash(rawBlock);
		return new AddressHash(hashBytes);
	}

	// TODO ?
	// change this method to be private
	public static byte[] calculateRawHash(byte[] rawBlock) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(rawBlock);
			byte[] digest = md.digest();

			return digest;
		} catch (NoSuchAlgorithmException ex) {
			throw new WrapperException(".calculateRawHash", ex);
		}

	}

}
