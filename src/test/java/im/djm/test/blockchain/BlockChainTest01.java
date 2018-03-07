package im.djm.test.blockchain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.hash.TxHash;
import im.djm.exception.NullWalletAddressException;
import im.djm.node.BlockChainNode;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
public class BlockChainTest01 {

	@Test
	public void blockChainNullWallet() {
		assertThatThrownBy(() -> {
			new BlockChain(null);
		}).isInstanceOf(NullWalletAddressException.class).hasMessage("Wallet address cannot be null.");
	}

	@Test
	public void oneBlockTest() {
		Wallet miner = Wallet.createNewWallet();
		BlockChainNode blockChainNode = new BlockChainNode(miner.address());
		miner.setBlockchainNode(blockChainNode);

		assertThat(blockChainNode).isNotNull();
		assertThat(blockChainNode.status()).isEqualTo("1");

		List<Utxo> allUtxo = blockChainNode.getAllUtxo();
		assertThat(allUtxo).hasSize(1);

		TxHash txId = allUtxo.get(0).getTxId();

		Tx txFromPool = blockChainNode.getBlockchain().getTxFromPool(txId);
		assertThat(txFromPool).isNotNull();
	}

}
