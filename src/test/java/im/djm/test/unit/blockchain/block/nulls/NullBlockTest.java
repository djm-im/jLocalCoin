package im.djm.test.unit.blockchain.block.nulls;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.block.nulls.NullBlock;
import im.djm.blockchain.block.nulls.NullValues;

/**
 * @author djm.im
 */
public class NullBlockTest {

	@Test
	public void nullBlockTest() {
		NullBlock nullBlock = NullValues.NULL_BLOCK;

		assertThat(nullBlock).isNotNull();

		assertThat(nullBlock.getLength()).isEqualTo(0);

		// This compares memory location in memory - default .equals method
		// TODO
		// find better way to check
		assertThat(nullBlock.getData()).isEqualTo(NullValues.NULL_DATA);

		assertThat(nullBlock.getPrevBlockHash()).isEqualTo(NullValues.NULL_BLOCK_HASH);

		assertThat(nullBlock.getBlockHash().toString())
				.isEqualTo("0x0B749F45D37C8FC00CC2B1E180314BA5AD9B7C435D4C6E0B5415DF37FD811558");
	}
}
