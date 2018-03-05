package im.djm.test.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import im.djm.blockchain.BlockChain;
import im.djm.exception.TxException;
import im.djm.tx.Tx;
import im.djm.wallet.NullPaymentException;
import im.djm.wallet.Payment;
import im.djm.wallet.Wallet;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class WalletTest {

	private Wallet wallet;

	private BlockChain blockchain;

	@Before
	public void initVariables() {
		this.wallet = Wallet.createNewWallet();

		this.blockchain = new BlockChain(this.wallet.address());
		this.wallet.setBlockchain(this.blockchain);
	}

	@Test
	public void sendNullPayment() {
		assertThatThrownBy(() -> {
			Payment payment = null;
			List<Payment> payments = Lists.newArrayList(payment);
			this.wallet.send(payments);
		}).isInstanceOf(NullPaymentException.class).hasMessage("Payment cannot be null.");
	}

	@Test
	public void sendMultipleNullPayments() {
		assertThatThrownBy(() -> {
			WalletAddress walletAddress = Wallet.createNewWallet().address();
			Payment pm0 = new Payment(walletAddress, 10);
			Payment pm1 = null;
			Payment pm2 = new Payment(walletAddress, 20);
			List<Payment> payments = Lists.newArrayList(pm0, pm1, pm2);

			this.wallet.send(payments);
		}).isInstanceOf(NullPaymentException.class).hasMessage("Payment cannot be null.");
	}

	@Test
	public void sendNegativePayment() {
		assertThatThrownBy(() -> {
			this.wallet.setBlockchain(this.blockchain);

			Payment payment = new Payment(Wallet.createNewWallet().address(), -1);
			List<Payment> payments = Lists.newArrayList(payment);
			this.wallet.send(payments);
		}).isInstanceOf(TxException.class).hasMessage("Cannot send zero or less value for coin. Tried to send -1.");
	}

	@Test
	public void sendImmutablePayments() {
		Payment payment = new Payment(Wallet.createNewWallet().address(), 10);
		List<Payment> payments = ImmutableList.of(payment);
		Tx txSent = this.wallet.send(payments);

		this.checkTx(txSent);
	}

	@Test
	public void checkSignature() {
		Payment payment = new Payment(Wallet.createNewWallet().address(), 20);
		List<Payment> payments = Lists.newArrayList(payment);
		Tx txSent = this.wallet.send(payments);

		this.checkTx(txSent);
	}

	private void checkTx(Tx txSent) {
		assertThat(txSent).isNotNull();
		assertThat(txSent.getInputs()).hasSize(1);

		assertThat(txSent.getSignature()).isNotNull();
		assertThat(txSent.getSignature().toString()).hasSize(134).startsWith("SIG-0x");
	}

}