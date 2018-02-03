package im.djm.tx;

import java.util.HashMap;
import java.util.Map;

import im.djm.blockchain.hash.TxHash;

/**
 * 
 * @author djm.im
 *
 */
public class TxPool {

	private Map<TxHash, Tx> txPoolMap;

	public TxPool() {
		this.txPoolMap = new HashMap<>();
	}

	public void add(Tx tx) {
		this.txPoolMap.put(tx.getTxId(), tx);
	}

	public Tx getTx(TxHash txId) {
		return this.txPoolMap.get(txId);
	}

	@Override
	public String toString() {
		return this.txPoolMap.toString();
	}

}
