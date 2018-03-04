package im.djm.test.blockchain.block.nulls;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.block.nulls.NullHead;
import im.djm.blockchain.block.nulls.NullValues;

/**
 * @author djm.im
 */
public class NullHeadTest {

	@Test
	public void nullHead() {
		NullHead nullHead = NullValues.NULL_HEAD;

		assertThat(nullHead).isNotNull();

		assertThat(nullHead.getLength()).isEqualTo(0);

		assertThat(nullHead.getPrevHash()).isEqualTo(NullValues.NULL_BLOCK_HASH);

		assertThat(nullHead.getDifficulty()).isEqualTo(0);

		assertThat(nullHead.getTimestamp()).isEqualTo(1510903985);
	}
}
