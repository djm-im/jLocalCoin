package im.djm.test.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import im.djm.wallet.NullPaymentListException;
import im.djm.wallet.Payment;
import im.djm.wallet.PaymentList;
import im.djm.wallet.Wallet;
import im.djm.wallet.WalletAddress;

public class PaymentListTest {

	@Test
	public void paymentsNull() {
		assertThatThrownBy(() -> {
			List<Payment> payments = null;
			new PaymentList(payments);
		}).isInstanceOf(NullPaymentListException.class).hasMessage("Payments cannot be null.");
	}

	@Test
	public void emptyPaymentList() {
		List<Payment> payments = ImmutableList.of();
		PaymentList paymentList = new PaymentList(payments);

		assertThat(paymentList).isNotNull();
		assertThat(paymentList.total()).isEqualTo(0L);
	}

	@Test
	public void totalOnePayment() {
		Payment payment = new Payment(Wallet.createNewWallet().address(), 10);
		List<Payment> payments = ImmutableList.of(payment);
		PaymentList paymentList = new PaymentList(payments);

		assertThat(paymentList.total()).isEqualTo(10);
	}

	public void totalMultiplePayments() {
		WalletAddress walletAddress = Wallet.createNewWallet().address();
		Payment pm0 = new Payment(walletAddress, 10);
		Payment pm1 = new Payment(walletAddress, 20);
		Payment pm2 = new Payment(walletAddress, 30);
		List<Payment> payments = ImmutableList.of(pm0, pm1, pm2);
		PaymentList paytmentList = new PaymentList(payments);

		assertThat(paytmentList.total()).isEqualTo(50);
	}

	@Test
	public void paymentsInPaymentList() {
		// TODO
		Payment payment = new Payment(Wallet.createNewWallet().address(), 10);
		List<Payment> payments = ImmutableList.of(payment);
		PaymentList paymentList = new PaymentList(payments);

		assertThat(paymentList.getPayments()).hasSize(1);
	}

}
