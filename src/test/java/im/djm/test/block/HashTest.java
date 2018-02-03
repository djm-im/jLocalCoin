/**
 * @author djm.im
 */
package im.djm.test.block;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.Hash;
import im.djm.blockchain.hash.HashUtil;

public class HashTest {

	@Test
	public void testTrue() {
		assertThat(true).isEqualTo(true);
	}

	@Test
	public void testToString() {
		byte[] rawArr = new byte[32];

		Hash h0 = new BlockHash(rawArr);

		assertThat(h0.toString()).isEqualTo("0x0000000000000000000000000000000000000000000000000000000000000000");
	}

	// ************************************************************************************************

	@Test
	public void isSame() throws NoSuchAlgorithmException {
		byte[] rawArr = new byte[32];

		Hash h1 = new BlockHash(rawArr);
		System.out.println(h1);

		Hash h2 = new BlockHash(HashUtil.calculateRawHash(rawArr));
		System.out.println(h2);

		Hash h3 = new BlockHash(HashUtil.calculateRawHash(h2.getRawHash()));
		System.out.println(h3);

		// ----------------------
		Hash a = new BlockHash(HashUtil.calculateRawHash("".getBytes()));
		System.out.println(a);

		System.out.println(new BlockHash(HashUtil.calculateRawHash("Djole".getBytes())));

		System.out.println(new BlockHash(HashUtil.calculateRawHash(":Djole".getBytes())));
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

}
