package im.djm.test.node;

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
	public void nullTx() {
		Wallet miner = Wallet.createNewWallet();
		BlockChain bc = new BlockChain(miner.address());
		miner.setBlockchain(bc);

		List<Utxo> allUtxo = bc.getAllUtxo();
		assertThat(allUtxo).hasSize(1);

		System.out.println(allUtxo);

		Tx txNull = bc.getTxFromPool(allUtxo.get(0).getTxId());
		assertThat(txNull.isCoinbase()).isTrue();
		assertThat(txNull.getInputSize()).isEqualTo(0);
		assertThat(txNull.getOutputSize()).isEqualTo(1);

		Output txOutput = txNull.getOutput(0);
		assertThat(txOutput.getWalletAddres()).isEqualTo(miner.address());
		assertThat(txOutput.getCoinValue()).isEqualTo(100);

		System.out.println(txNull);
	}

	@Test
	public void noEnoughCoins() {
		Wallet miner = Wallet.createNewWallet();
		BlockChain blockChain = new BlockChain(miner.address());
		miner.setBlockchain(blockChain);

		Wallet w1 = Wallet.createNewWallet();
		assertThatThrownBy(() -> {
			Payment payment = new Payment(w1.address(), 101);
			miner.send(Lists.newArrayList(payment));
		}).isInstanceOf(TxException.class).hasMessage("Not enough coins for tx. Tried to send 101. Utxo is 100.")
				.hasNoCause();

	}

	@Test
	public void txMore() {
		Wallet miner = Wallet.createNewWallet();
		BlockChain blockChain = new BlockChain(miner.address());
		miner.setBlockchain(blockChain);

		Wallet w1 = Wallet.createNewWallet();
		Payment payment = new Payment(w1.address(), 100);
		Tx tx = miner.send(Lists.newArrayList(payment));

		assertThat(miner.balance()).isEqualTo(100);
		assertThat(miner.balance()).isEqualTo(100);
		System.out.println(tx);
	}

	// TODO
	// Check UTXO and blockchain
}
