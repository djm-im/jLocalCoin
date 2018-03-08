package im.djm.wallet;

import java.security.interfaces.RSAPublicKey;

import im.djm.txhash.AddressHash;

/**
 * @author djm.im
 */
public final class WalletAddress {

	private final RSAPublicKey address;

	private final AddressHash addressHash;

	public WalletAddress(RSAPublicKey publicKey) {
		this.address = publicKey;

		this.addressHash = AddressHash.hash(this.address.getEncoded());
	}

	public RSAPublicKey getAddress() {
		return this.address;
	}

	public byte[] getRawData() {
		return this.address.getEncoded();
	}

	@Override
	public String toString() {
		return "{ WalletAddress: " + this.addressHash + " }";
	}

}
