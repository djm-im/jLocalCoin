package im.djm.blockchain.hash;

import java.util.Arrays;

/**
 * 
 * @author djm.djm
 *
 */
public class TxSignature extends ByteArray {

	public TxSignature(byte[] bytes) {
		this.content = Arrays.copyOf(bytes, bytes.length);
	}

	@Override
	public String toString() {
		return "SIG-" + super.toString();
	}
}
