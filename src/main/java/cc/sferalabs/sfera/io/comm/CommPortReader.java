package cc.sferalabs.sfera.io.comm;

public interface CommPortReader {

	/**
	 * 
	 * @param bytes
	 */
	void onRead(byte[] bytes);

	/**
	 * 
	 * @param t
	 */
	void onError(Throwable t);

}
