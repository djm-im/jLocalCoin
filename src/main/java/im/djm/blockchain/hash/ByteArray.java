package im.djm.blockchain.hash;

/**
 * @author djm.im
 */
// TODO
// This should be interface for Hash and Signature
public abstract class ByteArray {

	protected byte[] content;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("0x");
		for (byte aByte : this.content) {
			sb.append(String.format("%02X", aByte));
		}

		return sb.toString();
	}

}
