package im.djm.test.unit.blockchain.block;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import im.djm.blockchain.block.Block;
import im.djm.blockchain.block.data.Data;
import im.djm.blockchain.block.nulls.NullValues;
import im.djm.blockchain.hash.BlockHash;
import im.djm.exception.NullBlockException;
import im.djm.exception.NullDataException;

/**
 * @author djm.im
 */
public class BlockTest {

	@Test
	public void nullBlockTest() {
		assertThatThrownBy(() -> {
			Data data = NullValues.NULL_DATA;
			new Block(null, data);
		}).isInstanceOf(NullBlockException.class).hasMessage("Previous block cannot have null value.");
	}

	@Test
	public void nullDataTest() {
		assertThatThrownBy(() -> {
			Block prevBlock = NullValues.NULL_BLOCK;
			new Block(prevBlock, null);
		}).isInstanceOf(NullDataException.class).hasMessage("Data cannot be null.");
	}

	@Test
	public void firstBlock() {
		Block prevBlock = NullValues.NULL_BLOCK;
		Data data = NullValues.NULL_DATA;
		Block block = new Block(prevBlock, data);

		assertThat(block).isNotNull();
		assertThat(block.getLength()).isEqualTo(1);

		BlockHash blockHash = block.getBlockHash();
		assertThat(blockHash).isNotNull();
		assertThat(blockHash.getBinaryLeadingZeros()).isGreaterThanOrEqualTo(0);
		assertThat(blockHash.getRawHash()).hasSize(32);

		System.out.println("Block " + block);
	}

}
