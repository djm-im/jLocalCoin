package im.djm.test.blockchain.hash;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import im.djm.blockchain.hash.AddressHash;
import im.djm.exception.NullHashException;

/**
 * @author djm.im
 */
public class AddressHashTest {

	@Test
	public void test_nullHash() {
		assertThatThrownBy(() -> {
			new AddressHash(null);
		}).isInstanceOf(NullHashException.class);
	}
}
