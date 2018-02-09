package im.djm.node;

import im.djm.wallet.WalletAddress;

public final class Payment {

	private WalletAddress walletAddress;

	private long coinValue;

	public Payment(WalletAddress walletAddress, long coinValue) {
		this.walletAddress = walletAddress;
		this.coinValue = coinValue;
	}

	public WalletAddress getWalletAddress() {
		return this.walletAddress;
	}

	public long getCoinValue() {
		return this.coinValue;
	}
}
