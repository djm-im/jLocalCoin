package im.djm.tx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.primitives.Longs;

import im.djm.blockchain.BlockUtil;
import im.djm.blockchain.hash.HashUtil;
import im.djm.blockchain.hash.TxHash;
import im.djm.wallet.WalletAddress;

/**
 * 
 * @author djm.im
 *
 */
public class Tx {

	private TxHash txId;

	private List<Input> inputs;

	private List<Output> outputs;

	private boolean coinbase;

	private long timestamp;

	@SuppressWarnings("unused")
	private byte[] txSignature;

	public Tx() {
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
		this.coinbase = false;
		this.timestamp = System.currentTimeMillis() / 1000;
		// this.txSignature = null;
	}

	public Tx(WalletAddress minerAddress, long coinValue) {
		this.coinbase = true;

		// TOOD
		// Create NullArrayList
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
		this.timestamp = System.currentTimeMillis() / 1000;
		this.addOutput(minerAddress, coinValue);
	}

	public void addOutput(WalletAddress address, long coinAmount) {
		Output output = new Output(address, coinAmount);
		this.outputs.add(output);

		// TODO ???
		// wrap this call in method
		byte[] txRawHash = HashUtil.calculateRawHash(this.getRawDataForSignature());
		this.txId = new TxHash(txRawHash);
	}

	public boolean isCoinbase() {
		return this.coinbase;
	}

	public void addInput(TxHash prevTxHash, int outputIndexd) {
		Input input = new Input(prevTxHash, outputIndexd);
		this.inputs.add(input);
	}

	public byte[] getRawDataForSignature() {
		byte[] inputRawByte = getInputRawBytes();
		byte[] outputRawByte = getOutputRawBytes();
		byte[] timestampRawWyte = Longs.toByteArray(this.timestamp);

		// TODO
		// Add tx signature - also part of tx

		return BlockUtil.concatenateArrays(inputRawByte, outputRawByte, timestampRawWyte);
	}

	private byte[] getInputRawBytes() {
		byte[] txInputRaw = new byte[0];
		// TODO
		// replace with reduce method
		for (Input input : this.inputs) {
			txInputRaw = BlockUtil.concatenateArrays(txInputRaw, input.getRawData());
		}

		return txInputRaw;
	}

	private byte[] getOutputRawBytes() {
		byte[] txOutputRaw = new byte[0];
		for (Output output : this.outputs) {
			txOutputRaw = BlockUtil.concatenateArrays(txOutputRaw, output.getRawData());
		}

		return txOutputRaw;
	}

	public void addSignature(byte[] signature) {
		this.txSignature = Arrays.copyOf(signature, signature.length);
	}

	public TxHash getTxId() {
		return this.txId;
	}

	public List<Input> getInputs() {
		return this.inputs;
	}

	public Input getInput(int index) {
		if (0 > index || index > this.inputs.size()) {
			// TODO
			// throw exception
			return null;
		}

		// TODO
		// return a copy of element
		return this.inputs.get(index);
	}

	public int getInputSize() {
		return this.inputs.size();
	}

	public List<Output> getOutputs() {
		// TODO
		// return (immutable) array copy
		return this.outputs;
	}

	public Output getOutput(int index) {
		if (index < 0 || index > this.outputs.size()) {
			// TODO
			// throw an exception
			return null;
		}

		// TODO
		// return a copy of element
		return this.outputs.get(index);
	}

	public int getOutputSize() {
		return this.outputs.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t{\n");
		sb.append("\t\tTxId    : ").append(this.txId).append("\n");
		sb.append("\t\tTime    : ").append(this.timestamp).append("\n");
		sb.append("\t\tCoinbase: ").append(this.coinbase).append("\n");
		sb.append("\t\tInputs  : ").append(this.inputs).append("\n");
		sb.append("\t\tOutputs : ").append(this.outputs).append("\n");
		sb.append("\t\tSignarur: ").append(this.txSignature).append("\n");

		return sb.toString();
	}

}
