package im.djm.test.blockchain.hash;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import im.djm.exception.NullHashException;
import im.djm.txhash.AddressHash;

/**
 * @author djm.im
 */
public class AddressHashTest {

	@Test
	public void nullHash() {
		assertThatThrownBy(() -> {
			new AddressHash(null);
		}).isInstanceOf(NullHashException.class);
	}
}
