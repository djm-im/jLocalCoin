package im.djm.coin.wallet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author djm.im
 */
public class Trezor {

	private Map<String, Wallet> wallets = new HashMap<>();

	public boolean containsKey(String walletName) {
		return this.wallets.containsKey(walletName);
	}

	public Wallet get(String walletName) {
		return this.wallets.get(walletName);
	}

	public void addMinerWallet(String minerWalletName, Wallet minerWallet) {
		this.wallets.put(minerWalletName, minerWallet);
	}

	public void put(String walletName, Wallet wallet) {
		this.wallets.put(walletName, wallet);
	}

	public void remove(String walletName) {
		this.wallets.remove(walletName);
	}

	public Map<String, Wallet> allWallets() {
		return this.wallets;
	}

}
