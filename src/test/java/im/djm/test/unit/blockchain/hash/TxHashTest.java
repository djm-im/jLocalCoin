package im.djm.test.unit.blockchain.hash;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import im.djm.blockchain.hash.TxHash;
import im.djm.exception.NullHashException;

/**
 * @author djm.im
 */
public class TxHashTest {

	@Test
	public void test_nullHash() {
		assertThatThrownBy(() -> {
			byte[] arr = null;
			new TxHash(arr);
		}).isInstanceOf(NullHashException.class).hasMessage("Hash cannot have null value.").hasNoCause();
	}

	@Test
	public void test_emptyArray() {
		byte[] bytes = new byte[0];
		new TxHash(bytes);
	}

}
