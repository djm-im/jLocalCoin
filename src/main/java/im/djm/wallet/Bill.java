package im.djm.wallet;

import java.util.List;

import im.djm.coin.utxo.Utxo;

/**
 * @author djm.im
 */
class Bill {

	private final long senderBalance;

	private final List<Utxo> spentOutputs;

	public Bill(List<Utxo> spentOutputs, long senderBalance) {
		this.spentOutputs = spentOutputs;

		this.senderBalance = senderBalance;
	}

	public long getSenderBalance() {
		return this.senderBalance;
	}

	public List<Utxo> getSpentOutputs() {
		return this.spentOutputs;
	}

}
