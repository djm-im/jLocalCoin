package im.djm.test.tx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import im.djm.blockchain.hash.TxHash;
import im.djm.blockchain.hash.TxSignature;
import im.djm.tx.Input;
import im.djm.tx.Output;
import im.djm.tx.Tx;
import im.djm.tx.Tx.TxBuilder;
import im.djm.tx.TxException;
import im.djm.wallet.Wallet;
import im.djm.wallet.WalletAddress;

public class TxBulderTest {

	private TxBuilder txBuilder;

	@Before
	public void init() {
		this.txBuilder = new Tx.TxBuilder();
	}

	@Test
	public void emptyTx() {
		assertThatThrownBy(() -> {
			this.txBuilder.build();
		}).isInstanceOf(TxException.class).hasMessage("Tx is not complited. Inputs and Outputs are null.");
	}

	// -------------------------------------------------------------------------------------------------------
	// Inputs

	@Test
	public void inputsNull() {
		assertThatThrownBy(() -> {
			this.txBuilder.addInputs(null);
		}).isInstanceOf(TxException.class).hasMessage("Error: Inputs cannot be null.");
	}

	@Test
	public void noInputs() {
		assertThatThrownBy(() -> {
			this.txBuilder.addOutputs(createOutputs());

			this.txBuilder.build();
		}).isInstanceOf(TxException.class).hasMessage("Tx in not completed. Inputs are null.");
	}

	@Test
	public void inputsAdded() {
		assertThatThrownBy(() -> {
			this.txBuilder.addInputs(createInputs());
			this.txBuilder.addInputs(createInputs());
		}).isInstanceOf(TxException.class).hasMessage("Error: Inputs are already added.");
	}

	// -------------------------------------------------------------------------------------------------------
	// Outputs

	@Test
	public void outputsNull() {
		assertThatThrownBy(() -> {
			this.txBuilder.addOutputs(null);
		}).isInstanceOf(TxException.class).hasMessage("Error: Outputs cannot be null.");
	}

	@Test
	public void noOutputs() {
		assertThatThrownBy(() -> {
			this.txBuilder.addInputs(createInputs());

			this.txBuilder.build();
		}).isInstanceOf(TxException.class).hasMessage("Tx in not completed. Outputs are null.");
	}

	@Test
	public void outputsAdded() {
		assertThatThrownBy(() -> {
			this.txBuilder.addOutputs(createOutputs());

			this.txBuilder.addOutputs(createOutputs());
		}).isInstanceOf(TxException.class).hasMessage("Error: Outputs are already added.");
	}

	// -------------------------------------------------------------------------------------------------------
	// Signature

	@Test
	public void nullSignature() {
		assertThatThrownBy(() -> {
			this.txBuilder.addSignature(null);
		}).isInstanceOf(TxException.class).hasMessage("Error: Signature cannot be null.");
	}

	@Test
	public void noSignature() {
		assertThatThrownBy(() -> {
			this.txBuilder.addInputs(createInputs());
			this.txBuilder.addOutputs(createOutputs());

			this.txBuilder.build();
		}).isInstanceOf(TxException.class).hasMessage("Tx in not completed. Signature is null.");
	}

	@Test
	public void signatureAdded() {
		assertThatThrownBy(() -> {
			this.txBuilder.addSignature(createSignature());

			this.txBuilder.addSignature(createSignature());
		}).isInstanceOf(TxException.class).hasMessage("Error: Signature is already added.");
	}

	// -------------------------------------------------------------------------------------------------------
	// Other tests

	@Test
	public void txNotNull() {
		this.txBuilder.addInputs(createInputs());
		this.txBuilder.addOutputs(createOutputs());
		this.txBuilder.addSignature(createSignature());

		Tx tx = this.txBuilder.build();

		assertThat(tx).isNotNull();
	}

	@Test
	public void telescope() {
		Tx tx = this.txBuilder.addInputs(createInputs()).addOutputs(createOutputs()).addSignature(createSignature())
				.build();

		assertThat(tx).isNotNull();
	}

	@Test
	public void regularCase() {
		// assertThat(false).isTrue();
	}
	// -------------------------------------------------------------------------------------------------------
	// Helper methods

	private List<Input> createInputs() {
		TxHash prevTxId = new TxHash(new byte[32]);
		List<Input> inputs = ImmutableList.of(new Input(prevTxId, 0), new Input(prevTxId, 1));

		return inputs;
	}

	private List<Output> createOutputs() {
		WalletAddress walletAddress = Wallet.createNewWallet().address();
		List<Output> outputs = ImmutableList.of(new Output(walletAddress, 10));

		return outputs;
	}

	private TxSignature createSignature() {
		byte[] bytes = new byte[32];
		TxSignature signature = new TxSignature(bytes);

		return signature;
	}

}
