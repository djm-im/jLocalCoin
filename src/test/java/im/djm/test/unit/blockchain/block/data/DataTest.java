package im.djm.test.unit.blockchain.block.data;

import org.junit.Test;

import im.djm.blockchain.block.data.Data;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author djm.im
 */
public class DataTest {

	@Test
	public void dataInterfaceTest() {
		Data data = new Data() {
		};

		byte[] dataRaw = data.getRawData();
		assertThat(dataRaw).isNotNull().hasSize(32).contains(new byte[32]);
	}
}
