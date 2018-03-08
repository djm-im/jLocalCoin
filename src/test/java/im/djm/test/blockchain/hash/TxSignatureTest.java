package im.djm.test.blockchain.hash;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import im.djm.coin.txhash.NullTxSignatureException;
import im.djm.coin.txhash.TxSignature;

/**
 * @author djm.im
 */
public class TxSignatureTest {

	@Test
	public void nullTxSignature() {
		assertThatThrownBy(() -> {
			new TxSignature(null);
		}).isInstanceOf(NullTxSignatureException.class).hasMessage("Signature cannot be null.");
	}

	@Test
	public void test() {
		byte[] bytes = new byte[32];
		TxSignature txSignature = new TxSignature(bytes);

		assertThat(txSignature);
	}

	@Test
	public void toStringTest() {
		byte[] bytes = new byte[32];
		TxSignature txSignature = new TxSignature(bytes);

		assertThat(txSignature.toString())
				.isEqualTo("SIG-0x0000000000000000000000000000000000000000000000000000000000000000");
	}
}
