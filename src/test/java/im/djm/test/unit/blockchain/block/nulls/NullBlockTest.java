package im.djm.test.unit.blockchain.block.nulls;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.block.nulls.NullBlock;

/**
 * @author djm
 */
public class NullBlockTest {

	@Test
	public void nullBlockTest() {
		NullBlock nullBlock = new NullBlock();

		assertThat(nullBlock).isNotNull();

	}
}
