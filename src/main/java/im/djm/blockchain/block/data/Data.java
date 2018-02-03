package im.djm.blockchain.block.data;

/**
 * @author djm.im
 */
public interface Data {

	/**
	 * @return
	 */
	public default byte[] getRawData() {
		// Implicit fill array with zeros - Java automatically fills with zeros array
		return new byte[32];
	}

}
