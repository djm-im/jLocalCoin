package im.djm.test.coin.wallet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import im.djm.coin.wallet.Payment;
import im.djm.coin.wallet.Wallet;
import im.djm.coin.wallet.WalletAddress;
import im.djm.p2p.node.NullBlockChainNodeException;

/**
 * @author djm.im
 */
public class WalletNullTest {

	public Wallet wallet;

	@Before
	public void initVariables() {
		this.wallet = Wallet.createNewWallet();
	}

	@Test
	public void sendNotSetBlockchain() {
		assertThatThrownBy(() -> {
			WalletAddress walletAddress = Wallet.createNewWallet().address();
			Payment paymet = new Payment(walletAddress, 10);
			List<Payment> payments = Lists.newArrayList(paymet);

			this.wallet.send(payments);
		}).isInstanceOf(NullBlockChainNodeException.class).hasMessage("Cannot send coins: BlockChainNode is not set.");
	}

	@Test
	public void balanceBlockChainNotSet() {
		assertThatThrownBy(() -> {
			this.wallet.balance();
		}).isInstanceOf(NullBlockChainNodeException.class)
				.hasMessage("Cannot check balance: BlockChainNode is not set.");
	}
}
