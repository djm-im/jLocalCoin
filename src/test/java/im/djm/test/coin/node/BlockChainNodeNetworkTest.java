package im.djm.test.coin.node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import im.djm.coin.node.BlockChainNode;
import im.djm.coin.node.NullNetworkException;
import im.djm.coin.wallet.Payment;
import im.djm.coin.wallet.Wallet;

public class BlockChainNodeNetworkTest {

	@Test
	public void noNullTx() {
		BlockChainNode bcn = new BlockChainNode();

		assertThat(bcn.status().getLength()).isEqualTo(0);
	}

	@Test
	public void noNetwork() {
		assertThatThrownBy(() -> {
			BlockChainNode bcn = new BlockChainNode();
			bcn.sync();
		}).isInstanceOf(NullNetworkException.class).hasMessage("No netwrok: The node didn't discover network.");
	}

	@Test
	public void syncNullTxBlock() {
		Wallet miner00 = Wallet.createNewWallet();
		BlockChainNode bcn0 = new BlockChainNode(miner00.address());
		miner00.setBlockchainNode(bcn0);

		BlockChainNode bcn1 = new BlockChainNode();

		bcn1.addNode(bcn0);
		bcn1.sync();

		assertThat(bcn0.getBalance(miner00.address())).isEqualTo(100);

		assertThat(bcn1.getBalance(miner00.address())).isEqualTo(100);
	}

	@Test
	public void test() {
		Wallet miner00 = Wallet.createNewWallet();
		BlockChainNode bcn0 = new BlockChainNode(miner00.address());
		miner00.setBlockchainNode(bcn0);

		Wallet djm = Wallet.createNewWallet();
		Payment pm00 = new Payment(djm.address(), 99);
		List<Payment> payments = ImmutableList.of(pm00);
		miner00.send(payments);

		Wallet miner01 = Wallet.createNewWallet();
		BlockChainNode bcn1 = new BlockChainNode();

		bcn1.addNode(bcn0);
		bcn1.sync();

		assertThat(bcn0.getBalance(miner00.address())).isEqualTo(101);
		assertThat(bcn0.getBalance(djm.address())).isEqualTo(99);
		assertThat(bcn0.getBalance(miner01.address())).isEqualTo(0);

		assertThat(bcn1.getBalance(miner00.address())).isEqualTo(101);
		assertThat(bcn1.getBalance(djm.address())).isEqualTo(99);
		assertThat(bcn1.getBalance(miner01.address())).isEqualTo(0);
	}

}
