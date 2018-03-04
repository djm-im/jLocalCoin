package im.djm.test.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import im.djm.exception.NullBlockChainException;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class WalletTest {

	@Test
	public void notSetBlockchain() {
		Wallet wallet = Wallet.createNewWallet();

		assertThat(wallet).isNotNull();

		assertThatThrownBy(() -> {
			WalletAddress walletAddress = Wallet.createNewWallet().getWalletAddress();
			Payment paymet = new Payment(walletAddress, 10);
			List<Payment> payments = Lists.newArrayList(paymet);

			wallet.send(payments);
		}).isInstanceOf(NullBlockChainException.class).hasMessage("Cannot send coins: blockchain is not set.");

		assertThatThrownBy(() -> {
			wallet.balance();
		}).isInstanceOf(NullBlockChainException.class).hasMessage("Cannot check balance: blockchain is not set.");
	}

}
