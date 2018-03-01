package im.djm.test.unit.blockchain.block.nulls;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.block.nulls.NullData;

/**
 * @author djm
 */
public class NullDataTest {

	@Test
	public void nullDataTest() {
		NullData nullData = new NullData();

		assertThat(nullData).isNotNull();

		assertThat(nullData.getRawData()).hasSize(9);
		assertThat(nullData.toString()).isEqualTo("Null Data");
	}

}
