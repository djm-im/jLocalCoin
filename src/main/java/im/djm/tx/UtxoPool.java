package im.djm.tx;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author djm.im
 *
 */
public class UtxoPool {

	private List<Utxo> utxoList = new ArrayList<>();

	public void add(Utxo utxo) {
		this.utxoList.add(utxo);
	}

	public List<Utxo> getAll() {
		return this.utxoList;
	}

	public void remove(Utxo utxo) {
		this.utxoList.remove(utxo);
	}

	@Override
	public String toString() {
		return this.utxoList.toString();
	}

}
