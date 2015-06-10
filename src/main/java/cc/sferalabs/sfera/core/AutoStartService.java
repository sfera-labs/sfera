package cc.sferalabs.sfera.core;

public interface AutoStartService {
	
	/**
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception;
	
	/**
	 * 
	 * @throws Exception
	 */
	public void quit() throws Exception;

}
