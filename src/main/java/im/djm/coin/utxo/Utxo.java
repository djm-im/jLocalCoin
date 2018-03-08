package im.djm.coin.utxo;

import im.djm.coin.txhash.TxHash;

/**
 * 
 * @author djm.im
 *
 */
// TODO
// this class should be immutable
public class Utxo {

	private TxHash txId;

	private int index;

	private int hashCode;

	public Utxo(TxHash txHash, int index) {
		if (txHash == null) {
			// TODO
			// Throw a concrete type of exception
			throw null;
		}
		this.txId = txHash;
		this.index = index;

		this.hashCode = 31 * txHash.hashCode() + index;
	}

	public Utxo(Utxo utxo) {
		this.txId = utxo.txId;
		this.index = utxo.index;
	}

	public TxHash getTxId() {
		return this.txId;
	}

	public int getOutputIndexd() {
		return this.index;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		Utxo other = (Utxo) obj;

		return (this.index == other.index && this.txId.equals(other.txId));
	}

	@Override
	public String toString() {
		return "{ UTXO: { TxId: " + this.txId + ", OutputIndex: " + this.index + " }}";
	}

}
