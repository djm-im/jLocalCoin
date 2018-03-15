package im.djm.blockchain;

/**
 * @author djm.im
 *
 *         Value object that contains data about current state of blockchain.
 */
public final class BlockChainStatus {

	private final long length;

	public BlockChainStatus(long length) {
		this.length = length;
	}

	public long getLength() {
		return this.length;
	}

	@Override
	public String toString() {
		return "{ BlockChain : { Length: " + this.length + " } }";
	}

}
