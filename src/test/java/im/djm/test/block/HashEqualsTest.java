package im.djm.test.block;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.Hash;

public class HashEqualsTest {

	@Test
	public void test_true() {
		assertThat(true).isEqualTo(true);
	}

	@SuppressWarnings("null")
	@Test
	public void test_null_hash() {
		Hash h = null;
		assertThatThrownBy(() -> h.toString()).isInstanceOf(NullPointerException.class).hasNoCause();
	}

	@Test
	public void test_reflexive() {
		// 1) It is reflexive: for any non-null reference value x, x.equals(x) should
		// return true.

		Hash h0 = new BlockHash(new byte[32]);
		assertThat(h0.equals(h0)).isTrue();
	}

	@Test
	public void test_symmetric() {
		// 2) It is symmetric: for any non-null reference values x and y, x.equals(y)
		// should return true if and only if y.equals(x) returns true.

		byte[] bytesX = new byte[32];
		bytesX[0] = 1;
		Hash x = new BlockHash(bytesX);

		byte[] bytesY = new byte[32];
		bytesY[0] = 1;
		Hash y = new BlockHash(bytesY);

		assertThat(x.equals(y)).isTrue();
		assertThat(y.equals(x)).isTrue();
	}

	@Test
	public void test_transitive() {
		// 3) It is transitive: for any non-null reference values x, y, and z, if
		// x.equals(y) returns true and y.equals(z) returns true, then x.equals(z)
		// should return true.

		byte[] bytesX = new byte[32];
		bytesX[0] = 1;
		Hash x = new BlockHash(bytesX);

		byte[] bytesY = new byte[32];
		bytesY[0] = 1;
		Hash y = new BlockHash(bytesY);

		byte[] bytesZ = new byte[32];
		bytesZ[0] = 1;
		Hash z = new BlockHash(bytesZ);

		assertThat(x.equals(y)).isTrue();
		assertThat(y.equals(z)).isTrue();
		assertThat(x.equals(z)).isTrue();
	}

	@Test
	public void test_consistent() {
		// 4) It is consistent:for any non-null reference values x and y,multiple
		// invocations of x.equals(y)consistently return true or consistently return
		// false, provided no information used in equals comparisons on the objects is
		// modified.

	}

	@Test
	public void test_null() {
		// 5) For any non-null reference value x,x.equals(null)should return false.

		byte[] bytesX = new byte[32];
		bytesX[0] = 1;
		Hash x = new BlockHash(bytesX);

		assertThat(x.equals(null)).isFalse();
	}

}
