package im.djm.test.blockchain.block.nulls;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.block.nulls.NullData;
import im.djm.blockchain.block.nulls.NullValues;

/**
 * @author djm.im
 */
public class NullDataTest {

	@Test
	public void nullDataTest() {
		NullData nullData = NullValues.NULL_DATA;

		assertThat(nullData).isNotNull();

		assertThat(nullData.getRawData()).hasSize(9);
		assertThat(nullData.toString()).isEqualTo("Null Data");
	}

}
