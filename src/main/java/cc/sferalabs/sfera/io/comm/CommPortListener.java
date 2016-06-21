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
