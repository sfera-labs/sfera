package cc.sferalabs.sfera.core.services;

/**
 * Interfaces for services to be started at the beginning of Sfera life cycle
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface AutoStartService extends Service {

	/**
	 * Initializes the service
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void init() throws Exception;

}
