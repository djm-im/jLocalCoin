package im.djm.blockchain.block.data;

import java.util.List;
import java.util.function.Predicate;

/**
 * 
 * @author djm.im
 * @param <T>
 *
 */
public interface Validator<T> {

	/**
	 * 
	 * @param data
	 * @return
	 */
	public default boolean isValid(T tt, List<Predicate<T>> rules) {
		for (Predicate<T> rule : rules) {
			if (!rule.test(tt)) {
				return false;
			}
		}
		return true;
	}

}
