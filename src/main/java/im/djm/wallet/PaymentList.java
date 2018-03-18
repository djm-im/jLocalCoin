package im.djm.wallet;

import java.util.List;

import im.djm.coin.tx.TxException;

/**
 * @author djm.im
 */
public final class PaymentList {

	private final List<Payment> payments;

	private final long total;

	public PaymentList(List<Payment> payments) {
		if (payments == null) {
			throw new NullPaymentListException("Payments cannot be null.");
		}
		this.payments = payments;

		this.total = this.totalSpent(payments);
	}

	private long totalSpent(List<Payment> payments) {
		return payments.stream().reduce(0L, (sum, payment) -> {
			this.validatePayment(payment);

			sum = sum + payment.getCoinValue();
			return sum;
		}, (x, y) -> x + y);
	}

	private void validatePayment(Payment payment) {
		if (payment == null) {
			throw new NullPaymentException("Payment cannot be null.");
		}

		long coinValue = payment.getCoinValue();
		if (coinValue <= 0) {
			throw new TxException("Cannot send zero or less value for coin. Tried to send " + coinValue + ".");
		}
	}

	public List<Payment> getPayments() {
		return this.payments;
	}

	public long total() {
		return this.total;
	}
}
