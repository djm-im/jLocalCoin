package im.djm.test.blockchain.hash;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import im.djm.blockchain.hash.DataHash;
import im.djm.exception.NullHashException;

/**
 * @author djm.im
 */
public class DataHashTest {

	@Test
	public void test_nullHash() {
		assertThatThrownBy(() -> {
			new DataHash(null);
		}).isInstanceOf(NullHashException.class).hasMessage("Hash cannot have null value.").hasNoCause();
	}

	@Test
	public void test_emptyArray() {
		byte[] bytes = new byte[0];
		new DataHash(bytes);
	}

}
