package im.djm.test.unit.node;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import im.djm.blockchain.hash.TxHash;
import im.djm.node.BlockChainNode;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
public class BlockChainNodeTest {

	@Test
	public void blockChainNode() {
		Wallet miner = new Wallet(null);
		BlockChainNode blockChainNode = new BlockChainNode(miner.getWalletAddress());

		assertThat(blockChainNode).isNotNull();

		assertThat(blockChainNode.status()).isEqualTo("1");

		List<Utxo> allUtxo = blockChainNode.getAllUtxo();
		assertThat(allUtxo.size()).isEqualTo(1);

		Utxo utxo = allUtxo.get(0);
		assertThat(utxo.getOutputIndexd()).isEqualTo(0);

		TxHash txId = utxo.getTxId();
		Tx tx = blockChainNode.getBlockchain().getTxFromPool(txId);
		assertThat(tx.getOutputs()).hasSize(1);

		assertThat(tx.getOutput(0).getWalletAddres()).isEqualTo(miner.getWalletAddress());
	}

}
