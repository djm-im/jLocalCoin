package im.djm.test.blockchain.hash;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import im.djm.blockchain.hash.NullHashException;
import im.djm.txhash.TxHash;

/**
 * @author djm.im
 */
public class TxHashTest {

	@Test
	public void nullHash() {
		assertThatThrownBy(() -> {
			byte[] arr = null;
			new TxHash(arr);
		}).isInstanceOf(NullHashException.class).hasMessage("Hash cannot have null value.").hasNoCause();
	}

	@Test
	public void emptyArray() {
		byte[] bytes = new byte[0];
		new TxHash(bytes);
	}

}
