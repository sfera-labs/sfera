package cc.sferalabs.sfera.io.comm;

/**
 * Interface to be implemented by classes processing data read from a
 * {@link CommPort}.
 * <p>
 * Instances of classes implementing this interface can register to a
 * {@link CommPort} using the {@link CommPort#setListener(CommPortListener)}
 * method.
 * </p>
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface CommPortListener {

	/**
	 * This method is called by the {@link CommPort} this listener is registered
	 * to when data is read from the associated serial port.
	 * 
	 * @param bytes
	 *            the data read
	 */
	void onRead(byte[] bytes);

	/**
	 * This method is called if an error occurs while reading from the
	 * {@link CommPort} this listener is registered to.
	 * 
	 * @param t
	 *            the {@code Throwable} that caused the error
	 */
	void onError(Throwable t);

}
