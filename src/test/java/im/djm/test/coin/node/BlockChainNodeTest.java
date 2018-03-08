package im.djm.test.coin.node;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import im.djm.coin.node.BlockChainNode;
import im.djm.coin.tx.Tx;
import im.djm.coin.txhash.TxHash;
import im.djm.coin.utxo.Utxo;
import im.djm.coin.wallet.Wallet;

/**
 * @author djm.im
 */
public class BlockChainNodeTest {

	@Test
	public void blockChainNode() {
		Wallet miner = Wallet.createNewWallet();
		BlockChainNode blockChainNode = new BlockChainNode(miner.address());

		assertThat(blockChainNode).isNotNull();

		assertThat(blockChainNode.status()).isEqualTo("1");

		List<Utxo> allUtxo = blockChainNode.getAllUtxo();
		assertThat(allUtxo.size()).isEqualTo(1);

		Utxo utxo = allUtxo.get(0);
		assertThat(utxo.getOutputIndexd()).isEqualTo(0);

		TxHash txId = utxo.getTxId();
		Tx tx = blockChainNode.getTxFromPool(txId);
		assertThat(tx.getOutputs()).hasSize(1);

		assertThat(tx.getOutput(0).getWalletAddres()).isEqualTo(miner.address());
	}

}
