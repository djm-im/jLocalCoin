package im.djm.test.blockchain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import im.djm.coin.tx.Tx;
import im.djm.coin.txhash.TxHash;
import im.djm.coin.utxo.Utxo;
import im.djm.coin.wallet.Wallet;
import im.djm.p2p.node.BlockChainNode;

/**
 * @author djm.im
 */
public class BlockChainTest01 {

	@Test
	public void oneBlockTest() {
		Wallet miner = Wallet.createNewWallet();
		BlockChainNode blockChainNode = new BlockChainNode(miner.address());
		miner.setBlockchainNode(blockChainNode);

		assertThat(blockChainNode).isNotNull();
		assertThat(blockChainNode.status().getLength()).isEqualTo(1);

		List<Utxo> allUtxo = blockChainNode.getAllUtxo();
		assertThat(allUtxo).hasSize(1);

		TxHash txId = allUtxo.get(0).getTxId();

		Tx txFromPool = blockChainNode.getTxFromPool(txId);
		assertThat(txFromPool).isNotNull();
	}

}
