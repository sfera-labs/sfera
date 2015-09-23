package cc.sferalabs.sfera.core.services;

/**
 * Base interface for Sfera services
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface Service {

	/**
	 * This method is called when the service is requested to stop
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void quit() throws Exception;

}
