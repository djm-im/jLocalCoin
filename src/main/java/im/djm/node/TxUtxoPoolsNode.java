package im.djm.node;

import java.util.ArrayList;
import java.util.List;

import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.tx.TxData;
import im.djm.tx.TxPool;
import im.djm.txhash.TxHash;
import im.djm.utxo.Utxo;
import im.djm.utxo.UtxoPool;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class TxUtxoPoolsNode {

	private UtxoPool utxoPool;

	private TxPool txPool;

	public TxUtxoPoolsNode() {
		this.txPool = new TxPool();

		this.utxoPool = new UtxoPool();
	}

	public void updateTxPoolAndUtxoPool(TxData txData) {
		txData.getTxs().forEach(tx -> {
			this.updateUtxoPoolByTx(tx);
		});
	}

	private void updateUtxoPoolByTx(Tx tx) {
		this.txPool.add(tx);

		this.utxoPoolAddUtxos(tx);

		this.utxoPoolRemoveUtxos(tx);
	}

	private void utxoPoolAddUtxos(Tx tx) {
		for (int index = 0; index < tx.getOutputSize(); index++) {
			Utxo utxo = new Utxo(tx.getTxId(), index);
			this.utxoPool.add(utxo);
		}
	}

	private void utxoPoolRemoveUtxos(Tx tx) {
		tx.getInputs().forEach(input -> {
			Utxo utxo = new Utxo(input.getPrevTxId(), input.getOutputIndex());
			this.utxoPool.remove(utxo);
		});
	}

	public List<Utxo> getUtxoFor(WalletAddress walletAddress) {
		List<Utxo> utxoList = new ArrayList<>();
		// TODO
		// use fileter method
		for (Utxo utxo : this.utxoPool.getAll()) {
			Tx tx = this.txPool.getTx(utxo.getTxId());
			Output output = tx.getOutput(utxo.getOutputIndexd());
			if (walletAddress.equals(output.getWalletAddres())) {
				utxoList.add(new Utxo(utxo));
			}
		}

		return utxoList;
	}

	public long getBalance(WalletAddress walletAddress) {
		long sum = 0;
		List<Utxo> utxos = this.getUtxoFor(walletAddress);
		for (Utxo utxo : utxos) {
			Tx tx = this.txPool.getTx(utxo.getTxId());
			Output output = tx.getOutput(utxo.getOutputIndexd());
			if (walletAddress.equals(output.getWalletAddres())) {
				sum += output.getCoinValue();
			}
		}

		return sum;
	}

	// TODO
	// Group getter methods
	public Tx getTxFromPool(TxHash txId) {
		return this.txPool.getTx(txId);
	}

	public List<Utxo> getAllUtxo() {
		return this.utxoPool.getAll();
	}
}