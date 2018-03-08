package im.djm.test.blockchain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import im.djm.blockchain.block.Block;
import im.djm.blockchain.block.Head;
import im.djm.blockchain.block.Miner;
import im.djm.blockchain.hash.BlockHash;
import im.djm.blockchain.hash.DataHash;
import im.djm.tx.TxData;

/**
 * @author djm.im
 */
public class MinerTest {

	@Test
	public void createHead() {
		Block prevBlock = mock(Block.class);
		byte[] prevBlockHashByte = new byte[32];

		prevBlockHashByte[31] = 1;
		BlockHash prevBlockHash = new BlockHash(prevBlockHashByte);
		when(prevBlock.getBlockHash()).thenReturn(prevBlockHash);
		when(prevBlock.getLength()).thenReturn(1L);

		TxData txData = mock(TxData.class);
		when(txData.getRawData()).thenReturn(prevBlockHashByte);

		DataHash dataHash = DataHash.hash(txData.getRawData());
		Head aHead = Miner.createHead(prevBlock, dataHash);

		assertThat(aHead.getDifficulty()).isEqualTo(16);
		assertThat(aHead.getLength()).isEqualTo(2);
		assertThat(aHead.getPrevHash()).isEqualTo(prevBlockHash);
	}

}
