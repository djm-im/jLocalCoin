package im.djm.test.unit.blockchain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.hash.TxHash;
import im.djm.exception.NullWalletAddressException;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;
import im.djm.wallet.Wallet;

public class BlockChainTest {

	@Test
	public void blockChainNullWallet() {
		assertThatThrownBy(() -> {
			new BlockChain(null);
		}).isInstanceOf(NullWalletAddressException.class).hasMessage("Wallet address cannot be null.");
	}

	@Test
	public void oneBlockTest() {
		Wallet miner = new Wallet(null);
		BlockChain blockChain = new BlockChain(miner.getWalletAddress());
		miner.setBlockchain(blockChain);

		assertThat(blockChain).isNotNull();
		assertThat(blockChain.status()).isEqualTo("1");

		List<Utxo> allUtxo = blockChain.getAllUtxo();
		assertThat(allUtxo).hasSize(1);

		TxHash txId = allUtxo.get(0).getTxId();

		Tx txFromPool = blockChain.getTxFromPool(txId);
		assertThat(txFromPool).isNotNull();

		System.out.println();
		System.out.println(blockChain);
		System.out.println();
		System.out.println(allUtxo);
		System.out.println();
	}

}
