package im.djm.test.p2p.node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import im.djm.coin.wallet.Payment;
import im.djm.coin.wallet.Wallet;
import im.djm.p2p.node.BlockChainNode;
import im.djm.p2p.node.NullBlockChainNodeException;
import im.djm.p2p.node.NullNetworkException;

public class BlockChainNodeNetworkTest {

	@Test
	public void noNullTx() {
		BlockChainNode node = new BlockChainNode();

		assertThat(node.status().getLength()).isEqualTo(0);
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
	public void syncOneTx() {
		Wallet miner00 = Wallet.createNewWallet();
		BlockChainNode node00 = new BlockChainNode(miner00.address());
		miner00.setBlockchainNode(node00);

		Wallet djm = Wallet.createNewWallet();
		Payment pm00 = new Payment(djm.address(), 99);
		List<Payment> payments = ImmutableList.of(pm00);
		miner00.send(payments);

		Wallet miner01 = Wallet.createNewWallet();
		BlockChainNode node01 = new BlockChainNode();

		node01.addNode(node00);
		node01.sync();

		assertThat(node00.getBalance(miner00.address())).isEqualTo(101);
		assertThat(node00.getBalance(djm.address())).isEqualTo(99);
		assertThat(node00.getBalance(miner01.address())).isEqualTo(0);

		assertThat(node01.getBalance(miner00.address())).isEqualTo(101);
		assertThat(node01.getBalance(djm.address())).isEqualTo(99);
		assertThat(node01.getBalance(miner01.address())).isEqualTo(0);
	}

	@Test
	public void annonceNewBlock() {
		assertThatThrownBy(() -> {
			Wallet miner00 = Wallet.createNewWallet();
			BlockChainNode node00 = new BlockChainNode(miner00.address());
			miner00.setBlockchainNode(node00);

			Wallet djm = Wallet.createNewWallet();
			Payment pm00 = new Payment(djm.address(), 99);
			List<Payment> pms00 = ImmutableList.of(pm00);
			miner00.send(pms00);

			Wallet miner01 = Wallet.createNewWallet();
			BlockChainNode node01 = new BlockChainNode();

			node01.addNode(node00);
			node01.sync();

			djm.setBlockchainNode(node01);
			Payment pm01 = new Payment(miner01.address(), 50);
			List<Payment> pms01 = ImmutableList.of(pm01);
			djm.send(pms01);
		}).isInstanceOf(NullBlockChainNodeException.class).hasMessage("Miner wallet is not set.");
	}

	@Test
	public void tx2NotSync() {
		Wallet miner00 = Wallet.createNewWallet();
		BlockChainNode node00 = new BlockChainNode(miner00.address());
		miner00.setBlockchainNode(node00);

		Wallet djm = Wallet.createNewWallet();
		Payment pm00 = new Payment(djm.address(), 99);
		List<Payment> pms00 = ImmutableList.of(pm00);
		miner00.send(pms00);

		Wallet miner01 = Wallet.createNewWallet();
		BlockChainNode node01 = new BlockChainNode();
		node01.setMinerAddress(miner01.address());

		node01.addNode(node00);
		node01.sync();

		djm.setBlockchainNode(node01);
		Payment pm01 = new Payment(miner01.address(), 50);
		List<Payment> pms01 = ImmutableList.of(pm01);
		djm.send(pms01);

		assertThat(node00.getBalance(miner00.address())).isEqualTo(101);
		assertThat(node00.getBalance(djm.address())).isEqualTo(99);
		assertThat(node00.getBalance(miner01.address())).isEqualTo(0);

		assertThat(node01.getBalance(miner00.address())).isEqualTo(101);
		assertThat(node01.getBalance(djm.address())).isEqualTo(49);
		assertThat(node01.getBalance(miner01.address())).isEqualTo(150);

	}

	@Test
	public void tx2Synced() {
		Wallet miner00 = Wallet.createNewWallet();
		BlockChainNode node00 = new BlockChainNode(miner00.address());
		miner00.setBlockchainNode(node00);

		Wallet djm = Wallet.createNewWallet();
		Payment pm00 = new Payment(djm.address(), 99);
		List<Payment> pms00 = ImmutableList.of(pm00);
		miner00.send(pms00);

		Wallet miner01 = Wallet.createNewWallet();
		BlockChainNode node01 = new BlockChainNode();
		node01.setMinerAddress(miner01.address());

		node01.addNode(node00);
		node01.sync();

		djm.setBlockchainNode(node01);
		Payment pm01 = new Payment(miner01.address(), 50);
		List<Payment> pms01 = ImmutableList.of(pm01);
		djm.send(pms01);

		node00.addNode(node01);
		node00.sync();

		assertThat(node00.getBalance(miner00.address())).isEqualTo(101);
		assertThat(node00.getBalance(djm.address())).isEqualTo(49);
		assertThat(node00.getBalance(miner01.address())).isEqualTo(150);

		assertThat(node01.getBalance(miner00.address())).isEqualTo(101);
		assertThat(node01.getBalance(djm.address())).isEqualTo(49);
		assertThat(node01.getBalance(miner01.address())).isEqualTo(150);
	}

}
