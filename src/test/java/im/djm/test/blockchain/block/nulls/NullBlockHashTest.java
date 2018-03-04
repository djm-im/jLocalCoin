package im.djm.test.blockchain.block.nulls;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.block.nulls.NullBlockHash;
import im.djm.blockchain.block.nulls.NullValues;

/**
 * @author djm.im
 */
public class NullBlockHashTest {

	@Test
	public void nullBlockHash() {
		NullBlockHash nullBlockHash = NullValues.NULL_BLOCK_HASH;

		assertThat(nullBlockHash).isNotNull();

		assertThat(nullBlockHash.getRawHash()).hasSize(32).contains(0);

		assertThat(nullBlockHash.getBinaryLeadingZeros()).isEqualTo(256);
	}

}
