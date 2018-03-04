package im.djm.test.blockchain.hash;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.Hash;
import im.djm.exception.NullHashException;

/**
 * @author djm.im
 */
public class BlockHashTest {

	@Test
	public void test_nullHash() {
		assertThatThrownBy(() -> {
			new BlockHash(null);
		}).isInstanceOf(NullHashException.class).hasMessage("Hash cannot have null value.").hasNoCause();
	}

	@Test
	public void test_isSame() throws NoSuchAlgorithmException {
		byte[] rawArr = new byte[32];

		BlockHash h1 = new BlockHash(rawArr);
		assertThat(h1.equals(h1)).isTrue();
	}

	@Test
	public void testHashEquals() {
		byte[] zeros = new byte[32];
		Hash h0 = new BlockHash(zeros);
		Hash h1 = new BlockHash(zeros);

		assertThat(h0.equals(h0)).isTrue();
		assertThat(h0.equals(h1)).isTrue();

		assertThat(h1.equals(h1)).isTrue();
		assertThat(h1.equals(h0)).isTrue();

		assertThat(h0.hashCode() == h1.hashCode()).isTrue();
	}

	@Test
	public void testToString() {
		byte[] rawArr = new byte[32];

		Hash h0 = new BlockHash(rawArr);

		assertThat(h0.toString()).isEqualTo("0x0000000000000000000000000000000000000000000000000000000000000000");
	}
}
