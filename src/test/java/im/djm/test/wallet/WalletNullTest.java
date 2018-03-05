package im.djm.test.wallet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import im.djm.exception.NullBlockChainException;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;
import im.djm.wallet.WalletAddress;

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
		}).isInstanceOf(NullBlockChainException.class).hasMessage("Cannot send coins: blockchain is not set.");
	}

	@Test
	public void balanceBlockChainNotSet() {
		assertThatThrownBy(() -> {
			this.wallet.balance();
		}).isInstanceOf(NullBlockChainException.class).hasMessage("Cannot check balance: blockchain is not set.");
	}
}
