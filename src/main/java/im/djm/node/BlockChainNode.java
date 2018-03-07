package im.djm.node;

import java.util.List;

import im.djm.blockchain.BlockChain;
import im.djm.tx.Tx;
import im.djm.utxo.Utxo;
import im.djm.wallet.WalletAddress;

/**
 * @author djm.im
 */
public class BlockChainNode {

	private BlockChain blockChain;

	public BlockChainNode(WalletAddress minerWallet) {
		this.blockChain = new BlockChain(minerWallet);
	}

	public BlockChain getBlockchain() {
		return this.blockChain;
	}

	public String status() {
		return this.blockChain.status();
	}

	public List<Utxo> getAllUtxo() {
		return this.blockChain.getAllUtxo();
	}

	public String printBlockChain() {
		return this.blockChain.toString();
	}

	public long getBalance(WalletAddress walletAddress) {
		return this.blockChain.getBalance(walletAddress);
	}

	@Override
	public String toString() {
		return this.blockChain.toString();
	}

	public void sendCoin(Tx newTx) {
		this.blockChain.add(newTx);
	}
}
