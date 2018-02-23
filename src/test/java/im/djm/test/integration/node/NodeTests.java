package im.djm.test.integration.node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import im.djm.blockchain.BlockChain;
import im.djm.exception.TxException;
import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;

public class NodeTests {

	@Test
	public void test_NullTx() {
		Wallet miner = new Wallet(null);
		BlockChain bc = new BlockChain(miner.getWalletAddress());
		miner.setBlockchain(bc);

		List<Utxo> allUtxo = bc.getAllUtxo();
		assertThat(allUtxo).hasSize(1);

		System.out.println(allUtxo);

		Tx txNull = bc.getTxFromPool(allUtxo.get(0).getTxId());
		assertThat(txNull.isCoinbase()).isTrue();
		assertThat(txNull.getInputSize()).isEqualTo(0);
		assertThat(txNull.getOutputSize()).isEqualTo(1);

		Output txOutput = txNull.getOutput(0);
		assertThat(txOutput.getWalletAddres()).isEqualTo(miner.getWalletAddress());
		assertThat(txOutput.getCoinValue()).isEqualTo(100);

		System.out.println(txNull);
	}

	@Test
	public void test_NoEnoughCoins() {
		Wallet miner = new Wallet(null);
		BlockChain blockChain = new BlockChain(miner.getWalletAddress());
		miner.setBlockchain(blockChain);

		Wallet w1 = new Wallet(blockChain);
		assertThatThrownBy(() -> {
			Payment payment = new Payment(w1.getWalletAddress(), 101);
			miner.send(Lists.newArrayList(payment));
		}).isInstanceOf(TxException.class).hasMessage("Not enough coins for tx. Tried to send 101. Utxo is 100.")
				.hasNoCause();

	}

	@Test
	public void test_txMore() {
		Wallet miner = new Wallet(null);
		BlockChain blockChain = new BlockChain(miner.getWalletAddress());
		miner.setBlockchain(blockChain);

		Wallet w1 = new Wallet(blockChain);
		Payment payment = new Payment(w1.getWalletAddress(), 100);
		Tx tx = miner.send(Lists.newArrayList(payment));

		assertThat(miner.balance()).isEqualTo(100);
		assertThat(miner.balance()).isEqualTo(100);
		System.out.println(tx);
	}

	// TODO
	// Check UTXO and blockchain
}
