package im.djm.test.wallet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import im.djm.blockchain.BlockChain;
import im.djm.exception.NullBlockChainException;
import im.djm.exception.TxException;
import im.djm.wallet.NullPaymentException;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class WalletTest {

	private Wallet wallet;

	// TODO
	// Split into two class
	private BlockChain blockchain;

	@Before
	public void initVariables() {
		this.wallet = Wallet.createNewWallet();

		this.blockchain = new BlockChain(this.wallet.getWalletAddress());
	}

	@Test
	public void sendNotSetBlockchain() {
		assertThatThrownBy(() -> {
			WalletAddress walletAddress = Wallet.createNewWallet().getWalletAddress();
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

	@Test
	public void sendNullPayment() {
		assertThatThrownBy(() -> {
			this.wallet.setBlockchain(this.blockchain);

			Payment payment = null;
			List<Payment> payments = Lists.newArrayList(payment);
			this.wallet.send(payments);
		}).isInstanceOf(NullPaymentException.class).hasMessage("Payment cannot be null.");
	}

	@Test
	public void sendMultipleNullPayments() {
		assertThatThrownBy(() -> {
			this.wallet.setBlockchain(this.blockchain);

			WalletAddress walletAddress = Wallet.createNewWallet().getWalletAddress();
			Payment pm0 = new Payment(walletAddress, 10);
			Payment pm1 = null;
			Payment pm2 = new Payment(walletAddress, 20);
			List<Payment> payments = Lists.newArrayList(pm0, pm1, pm2);

			this.wallet.send(payments);
		}).isInstanceOf(NullPaymentException.class).hasMessage("Payment cannot be null.");
	}

	@Test
	public void sendNegativePayment() {
		this.wallet.setBlockchain(this.blockchain);

		assertThatThrownBy(() -> {
			this.wallet.setBlockchain(this.blockchain);

			Payment payment = new Payment(Wallet.createNewWallet().getWalletAddress(), -1);
			List<Payment> payments = Lists.newArrayList(payment);
			this.wallet.send(payments);
		}).isInstanceOf(TxException.class).hasMessage("Cannot send zero or less value for coin. Tried to send -1.");
	}

}
