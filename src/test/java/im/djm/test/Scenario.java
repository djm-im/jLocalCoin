package im.djm.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import im.djm.node.BlockChainNode;
import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.txhash.TxHash;
import im.djm.utxo.Utxo;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class Scenario {

	private Wallet miner;
	private WalletAddress minerAddress;
	private BlockChainNode blockChainNode;

	@Before
	public void init() {
		this.miner = Wallet.createNewWallet();
		this.minerAddress = this.miner.address();

		this.blockChainNode = new BlockChainNode(this.minerAddress);

		this.miner.setBlockchainNode(this.blockChainNode);
	}

	@Test
	public void test() throws InterruptedException {
		// ---------------------------------------------------------------------------------------
		// Length: 0
		// Null Data Block

		assertThat(this.blockChainNode).isNotNull();

		// ---------------------------------------------------------------------------------------
		// Length: 1
		// Null Tx Block

		assertThat(this.blockChainNode.status()).isEqualTo("1");

		assertThat(this.blockChainNode.getBalance(this.minerAddress)).isEqualTo(100);

		List<Utxo> airAllUtxo = this.blockChainNode.getAllUtxo();
		assertThat(airAllUtxo.size()).isEqualTo(1);

		Utxo airUtxo = airAllUtxo.get(0);
		assertThat(airUtxo.getOutputIndexd()).isEqualTo(0);

		TxHash airTxId = airUtxo.getTxId();
		assertThat(airTxId.toString()).hasSize(66);

		Tx airCoinbaseTx = this.blockChainNode.getTxFromPool(airTxId);
		this.assertCoinbaseTx(airCoinbaseTx, this.minerAddress);

		// ---------------------------------------------------------------------------------------
		this.sleepOneSecond();

		// ---------------------------------------------------------------------------------------
		// Length: 2
		// Tx 0 Block
		Wallet djm = Wallet.createNewWallet().setBlockchainNode(this.blockChainNode);
		List<Payment> tx0Payments = ImmutableList.of(new Payment(djm.address(), 99));
		Tx tx0Send = this.miner.send(tx0Payments);

		assertThat(tx0Send).isNotNull();

		assertThat(this.blockChainNode.status()).isEqualTo("2");

		assertThat(this.blockChainNode.getBalance(this.minerAddress)).isEqualTo(101);
		assertThat(this.blockChainNode.getBalance(djm.address())).isEqualTo(99);

		List<Utxo> tx0allUtxo = this.blockChainNode.getAllUtxo();
		assertThat(tx0allUtxo.size()).isEqualTo(3);

		for (Utxo utxo : tx0allUtxo) {
			assertThat(utxo.equals(airUtxo)).isFalse();
		}

		Tx tx0Coinbase = null;
		for (Utxo utxo : tx0allUtxo) {
			Tx tx = this.blockChainNode.getTxFromPool(utxo.getTxId());
			if (tx.isCoinbase()) {
				tx0Coinbase = tx;
			}
		}
		this.assertCoinbaseTx(tx0Coinbase, this.minerAddress);

		// ---------------------------------------------------------------------------------------

		sleepOneSecond();

		// ---------------------------------------------------------------------------------------
		// Length: 3
		// Send to multiple addresses
		Wallet a = Wallet.createNewWallet();
		Wallet b = Wallet.createNewWallet();
		Wallet c = Wallet.createNewWallet();
		Wallet d = Wallet.createNewWallet();
		Payment pm0 = new Payment(a.address(), 1);
		Payment pm1 = new Payment(b.address(), 2);
		Payment pm2 = new Payment(c.address(), 3);
		Payment pm3 = new Payment(d.address(), 4);
		List<Payment> tx1Payments = ImmutableList.of(pm0, pm1, pm2, pm3);

		Tx tx1Send = djm.send(tx1Payments);

		assertThat(tx1Send).isNotNull();

		assertThat(this.blockChainNode.status()).isEqualTo("3");

		assertThat(this.miner.balance()).isEqualTo(201);
		assertThat(djm.balance()).isEqualTo(89);
		assertThat(this.blockChainNode.getBalance(a.address())).isEqualTo(1);
		assertThat(this.blockChainNode.getBalance(b.address())).isEqualTo(2);
		assertThat(this.blockChainNode.getBalance(c.address())).isEqualTo(3);
		assertThat(this.blockChainNode.getBalance(d.address())).isEqualTo(4);

	}

	private void assertCoinbaseTx(Tx coinbaseTx, WalletAddress minerAddress) {
		assertThat(coinbaseTx).isNotNull();
		assertThat(coinbaseTx.isCoinbase()).isTrue();
		assertThat(coinbaseTx.getInputs()).hasSize(0);
		assertThat(coinbaseTx.getOutputs()).hasSize(1);
		assertThat(coinbaseTx.getSignature()).isNull();

		Output airOutput = coinbaseTx.getOutput(0);
		assertThat(airOutput).isNotNull();
		assertThat(airOutput.getWalletAddres()).isEqualTo(minerAddress);
		assertThat(airOutput.getCoinValue()).isEqualTo(100);
	}

	// Sleep one second
	// The reason
	// If is time the same coinbase transaction has the same txId as the previous
	// transaction
	private int sleepOneSecond() throws InterruptedException {
		int oneSecond = 1000;
		Thread.sleep(oneSecond);

		return oneSecond;
	}

}
