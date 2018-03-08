package im.djm.coin.tx;

import java.util.ArrayList;
import java.util.List;

import im.djm.blockchain.BlockUtil;
import im.djm.blockchain.block.data.Data;

/**
 * 
 * @author djm.im
 *
 */
public class TxData implements Data {

	private List<Tx> txs;

	@Override
	public byte[] getRawData() {
		byte[] result = new byte[0];
		for (Tx tx : this.txs) {
			// TODO pack and return tx raw
			result = BlockUtil.concatenateArrays(result, tx.getRawDataForSignature());
		}
		return result;
	}

	public TxData() {
		this.txs = new ArrayList<>();
	}

	public void addCoinbaseTx(Tx coinbaseTx) {
		if (!coinbaseTx.isCoinbase()) {
			// TODO
			// throw an error if it is not a coinbase transaction
			return;
		}
		this.txs.add(0, coinbaseTx);
	}

	public boolean add(Tx tx) {
		return this.txs.add(tx);
	}

	public List<Tx> getTxs() {
		// TODO
		// return an array copy
		return this.txs;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (Tx tx : this.txs) {
			sb.append(tx).append("\n");
		}
		sb.append("}\n");

		return sb.toString();
	}

}
