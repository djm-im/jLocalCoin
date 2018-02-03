package im.djm.blockchain;

/**
 * 
 * @author djm.im
 *
 */
public final class BlockUtil {

	private BlockUtil() {
		throw new IllegalStateException("It is not allowed to call private constructor.");
	}

	public static byte[] concatenateArrays(byte[] arr1, byte[] arr2) {
		byte[] result = new byte[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, result, 0, arr1.length);
		System.arraycopy(arr2, 0, result, arr1.length, arr2.length);

		return result;
	}

	public static byte[] concatenateArrays(byte[]... arrays) {
		byte[] result = new byte[0];
		for (byte[] array : arrays) {
			result = BlockUtil.concatenateArrays(result, array);
		}

		return result;
	}

}
