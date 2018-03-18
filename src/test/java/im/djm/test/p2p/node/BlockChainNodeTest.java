package im.djm.test.p2p.node;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import im.djm.coin.tx.Tx;
import im.djm.coin.txhash.TxHash;
import im.djm.coin.utxo.Utxo;
import im.djm.p2p.node.BlockChainNode;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
public class BlockChainNodeTest {

	@Test
	public void constructorNoWallet() {
		BlockChainNode bcn = new BlockChainNode();

		assertThat(bcn.status().getLength()).isEqualTo(0);
	}

	@Test
	public void constructorWallet() {
		Wallet miner = Wallet.createNewWallet();
		BlockChainNode blockChainNode = new BlockChainNode(miner.address());

		assertThat(blockChainNode).isNotNull();

		assertThat(blockChainNode.status().getLength()).isEqualTo(1);

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
