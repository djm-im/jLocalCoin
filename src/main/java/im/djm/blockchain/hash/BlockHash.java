package im.djm.blockchain.hash;

/**
 * @author djm.im
 */
public class BlockHash extends Hash {

	private int binaryLeadingZeros;

	public BlockHash(byte[] bytes) {
		super(bytes);
		this.binaryLeadingZeros = countBinaryLeadingZeros();
	}

	private int countBinaryLeadingZeros() {
		int counter = 0;

		int index;
		for (index = 0; index < super.content.length; index++) {
			byte val = super.content[index];
			if (val != 0) {
				break;
			}
			counter += 8;
		}
		if (index < super.content.length) {
			counter += countInByte(super.content[index]);
		}

		return counter;
	}

	private int countInByte(byte aByte) {
		int count = 0;

		byte mask = (byte) 0B1000_0000;

		while ((aByte & mask) == 0 && mask != 1) {
			count++;
			mask >>= 1;
		}

		return count;
	}

	public int getBinaryLeadingZeros() {
		return this.binaryLeadingZeros;
	}

}
