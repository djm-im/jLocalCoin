package im.djm.blockchain.hash;

import java.util.Arrays;

/**
 * @author djm.im
 */
public class TxSignature extends ByteArray {

	public TxSignature(byte[] bytes) {
		if (bytes == null) {
			throw new NullTxSignatureException("Signature cannot be null.");
		}

		this.content = Arrays.copyOf(bytes, bytes.length);
	}

	public byte[] getBytes() {
		return this.content;
	}

	@Override
	public String toString() {
		return "SIG-" + super.toString();
	}

}
