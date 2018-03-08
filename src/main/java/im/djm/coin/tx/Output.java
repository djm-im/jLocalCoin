package im.djm.coin.tx;

import com.google.common.primitives.Longs;

import im.djm.blockchain.BlockUtil;
import im.djm.coin.wallet.WalletAddress;

/**
 * 
 * @author djm.im
 *
 */
// TODO ???
// Package-private class
public class Output {

	private WalletAddress walletAddress;

	private long coinValue;

	public Output(WalletAddress address, long coinValue) {
		this.walletAddress = address;
		this.coinValue = coinValue;
	}

	public long getCoinValue() {
		return this.coinValue;
	}

	public Object getWalletAddres() {
		return this.walletAddress;
	}

	public byte[] getRawData() {
		byte[] rawAddressBytes = this.walletAddress.getRawData();
		byte[] rawCoinValue = Longs.toByteArray(this.coinValue);
		byte[] rawOutputBytes = BlockUtil.concatenateArrays(rawAddressBytes, rawCoinValue);

		return rawOutputBytes;
	}

	@Override
	public String toString() {
		return "{ Output: { " + this.walletAddress + " received " + this.coinValue + " } }";
	}

}
