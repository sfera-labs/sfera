/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.core;

import java.security.Security;

import cc.sferalabs.sfera.logging.LoggerUtils;

/**
 * Entry point class containing the main method
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Sfera {

	/** Package prefix used by Sfera */
	public static final String BASE_PACKAGE = "cc.sferalabs.sfera";

	static {

	}

	/**
	 * The main method
	 * 
	 * @param args
	 *            program arguments
	 */
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		Security.setProperty("networkaddress.cache.ttl", "30");
		Security.setProperty("networkaddress.cache.negative.ttl", "10");
		LoggerUtils.init();
		Thread.setDefaultUncaughtExceptionHandler(new SystemExceptionHandler());
		SystemNode.getInstance().start();
	}

}
