package im.djm.test.blockchain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import im.djm.node.BlockChainNode;
import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.tx.TxException;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;

/**
 * @author djm.im
 */
public class BlockchainTest02 {

	Wallet miner = Wallet.createNewWallet();

	@Test
	public void testMain2() {
		BlockChainNode blockChainNode = new BlockChainNode(miner.address());
		miner.setBlockchainNode(blockChainNode);

		Wallet w1 = Wallet.createNewWallet().setBlockchainNode(blockChainNode);
		Wallet w2 = Wallet.createNewWallet().setBlockchainNode(blockChainNode);
		Wallet w3 = Wallet.createNewWallet().setBlockchainNode(blockChainNode);

		List<Payment> pm0 = Lists.newArrayList(new Payment(w1.address(), 100));
		Tx tx0 = miner.send(pm0);

		assertThat(tx0).isNotNull();
		assertThat(tx0.getInputSize()).isEqualTo(1);
		assertThat(tx0.getOutputSize()).isEqualTo(1);

		Output out0 = tx0.getOutput(0);
		assertThat(out0.getWalletAddres()).isEqualTo(w1.address());
		assertThat(out0.getCoinValue()).isEqualTo(100);

		assertThat(miner.balance()).isEqualTo(100);
		assertThat(w1.balance()).isEqualTo(100);
		assertThat(w2.balance()).isEqualTo(0);

		List<Payment> pm1 = Lists.newArrayList(new Payment(w2.address(), 49));
		Tx tx1 = w1.send(pm1);

		assertThat(tx1).isNotNull();
		assertThat(tx1.getInputSize()).isEqualTo(1);
		assertThat(tx1.getOutputSize()).isEqualTo(2);

		Output out1 = tx1.getOutput(0);
		assertThat(out1.getCoinValue()).isEqualTo(49);
		assertThat(out1.getWalletAddres()).isEqualTo(w2.address());

		Output out2 = tx1.getOutput(1);
		assertThat(out2.getCoinValue()).isEqualTo(51);
		assertThat(out2.getWalletAddres()).isEqualTo(w1.address());

		assertThat(miner.balance()).isEqualTo(200);
		assertThat(w1.balance()).isEqualTo(51);
		assertThat(w2.balance()).isEqualTo(49);
		assertThat(w3.balance()).isEqualTo(0);

		assertThatThrownBy(() -> {
			List<Payment> pm2 = Lists.newArrayList(new Payment(w2.address(), 200));
			w1.send(pm2);
		}).isInstanceOf(TxException.class).hasMessage("Not enough coins for tx. Tried to send 200. Utxo is 51.");

		assertThatThrownBy(() -> {
			List<Payment> pm3 = Lists.newArrayList(new Payment(w1.address(), 100));
			w3.send(pm3);
		}).isInstanceOf(TxException.class).hasMessage("Not enough coins for tx. Tried to send 100. Utxo is 0.");
	}

}
