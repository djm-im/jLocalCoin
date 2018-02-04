package im.djm.zMain;

import java.util.ArrayList;
import java.util.List;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.block.data.Data;

/**
 * @author djm.im
 *
 */
public class Main01 {
	private static class TestData implements Data {
		private String txtData;

		public TestData(String txtData) {
			this.txtData = txtData;
		}

		@Override
		public String toString() {
			return this.txtData;
		}

		@Override
		public byte[] getRawData() {
			return txtData.getBytes();
		}

	}

	private static List<Data> data = new ArrayList<>();
	static {
		data.add(new TestData("Data 1"));
		data.add(new TestData("Data 2"));
		data.add(new TestData("Data 3"));
		data.add(new TestData("DATA"));
		data.add(new TestData("DATA"));
		data.add(new TestData("DATA"));
		data.add(new TestData("DATA"));
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		// create block chain
		BlockChain blockChain = new BlockChain();

		// add a few blocks
		for (Data aData : data) {
			blockChain.add(aData);
		}

		// print content
		System.out.println(blockChain);

	}
}
