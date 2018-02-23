package im.djm.wallet;

import java.security.interfaces.RSAPublicKey;

import im.djm.blockchain.hash.AddressHash;
import im.djm.blockchain.hash.ByteArrayUtil;

/**
 * 
 * @author djm.im
 *
 */
// TODO
// make this final and immutable
public final class WalletAddress {

	private final RSAPublicKey address;

	private AddressHash addressHash;

	public WalletAddress(RSAPublicKey publicKey) {
		this.address = publicKey;

		this.addressHash = ByteArrayUtil.calculateAddressHash(this.address.getEncoded());
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
