package im.djm.test.unit.blockchain.block.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import im.djm.blockchain.BlockChain;
import im.djm.blockchain.block.data.Data;

class TestData implements Data {
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

/**
 * @author djm.im
 */
public class TestDataTest {

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
	@Test
	public void dataInterfaceImplementation() {
		BlockChain blockChain = new BlockChain();

		for (Data aData : TestDataTest.data) {
			blockChain.add(aData);
		}

		System.err.println();
		System.out.println("TestDataTest:");
		System.out.println();
		System.out.println(blockChain);
	}
}
