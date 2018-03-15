package im.djm.test.blockchain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.BlockChainStatus;

/**
 * @author djm.im
 */
public class BlockChainStatusTest {

	@Test
	public void statusCheck() {
		BlockChainStatus status = new BlockChainStatus(1);

		assertThat(status.getLength()).isEqualTo(1);
	}

}
