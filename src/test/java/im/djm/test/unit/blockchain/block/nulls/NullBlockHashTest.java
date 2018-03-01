package im.djm.test.unit.blockchain.block.nulls;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.block.nulls.NullBlockHash;

/**
 * @author djm
 */
public class NullBlockHashTest {

	@Test
	public void nullBlockHash() {
		NullBlockHash nullBlockHash = new NullBlockHash();

		assertThat(nullBlockHash).isNotNull();

		assertThat(nullBlockHash.getRawHash()).hasSize(32).contains(0);

		assertThat(nullBlockHash.getBinaryLeadingZeros()).isEqualTo(256);
	}

}
