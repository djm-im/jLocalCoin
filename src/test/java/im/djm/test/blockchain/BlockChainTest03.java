package im.djm.test.blockchain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import im.djm.coin.tx.Output;
import im.djm.coin.tx.Tx;
import im.djm.coin.tx.TxException;
import im.djm.coin.utxo.Utxo;
import im.djm.coin.wallet.Payment;
import im.djm.coin.wallet.Wallet;
import im.djm.p2p.node.BlockChainNode;

/**
 * @author djm.im
 */
public class BlockChainTest03 {

	@Test
	public void nullTx() {
		Wallet miner = Wallet.createNewWallet();
		BlockChainNode bcn = new BlockChainNode(miner.address());
		miner.setBlockchainNode(bcn);

		List<Utxo> allUtxo = bcn.getAllUtxo();
		assertThat(allUtxo).hasSize(1);

		Tx txNull = bcn.getTxFromPool(allUtxo.get(0).getTxId());
		assertThat(txNull.isCoinbase()).isTrue();
		assertThat(txNull.getInputSize()).isEqualTo(0);
		assertThat(txNull.getOutputSize()).isEqualTo(1);

		Output txOutput = txNull.getOutput(0);
		assertThat(txOutput.getWalletAddres()).isEqualTo(miner.address());
		assertThat(txOutput.getCoinValue()).isEqualTo(100);
	}

	@Test
	public void noEnoughCoins() {
		Wallet miner = Wallet.createNewWallet();
		BlockChainNode blockChainNode = new BlockChainNode(miner.address());
		miner.setBlockchainNode(blockChainNode);

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
		BlockChainNode blockChainNode = new BlockChainNode(miner.address());
		miner.setBlockchainNode(blockChainNode);

		Wallet w1 = Wallet.createNewWallet();
		Payment payment = new Payment(w1.address(), 100);
		miner.send(Lists.newArrayList(payment));

		assertThat(miner.balance()).isEqualTo(100);
		assertThat(miner.balance()).isEqualTo(100);
	}

	// TODO
	// Check UTXO and blockchain
}
