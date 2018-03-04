package im.djm.test.unit.wallet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import im.djm.wallet.Trezor;

public class TrezorTest {

	@Test
	public void emptyTrezor() {
		Trezor trezor = new Trezor();

		assertThat(trezor.allWallets()).hasSize(0);

		String walletName = "NoWallet";
		assertThat(trezor.get(walletName)).isNull();
		assertThat(trezor.containsKey(walletName)).isFalse();
	}

}
