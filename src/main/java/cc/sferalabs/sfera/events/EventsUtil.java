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

/**
 * 
 */
package cc.sferalabs.sfera.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class EventsUtil {

	private static class EventIdSpecMatchingPredicatesList implements Predicate<Event> {

		private final List<Predicate<Event>> predicatesList;

		/**
		 * 
		 * @param specx
		 * @throws IllegalArgumentException
		 */
		private EventIdSpecMatchingPredicatesList(String specx) throws IllegalArgumentException {
			String[] specs = specx.split(";");
			this.predicatesList = new ArrayList<>(specs.length);
			for (String s : specs) {
				Predicate<Event> predicate;
				if (s.isEmpty()) {
					predicate = new Predicate<Event>() {
						@Override
						public boolean test(Event e) {
							return true;
						}
					};
				} else if (s.contains("*")) {
					String[] parts = s.split("\\*");
					if (parts.length > 2) {
						throw new IllegalArgumentException("Invalid syntax");
					}
					if (parts.length == 0) { // case "*"
						predicate = new Predicate<Event>() {
							@Override
							public boolean test(Event t) {
								return true;
							}
						};
					} else {
						predicate = new Predicate<Event>() {
							@Override
							public boolean test(Event e) {
								if (!e.getId().startsWith(parts[0])) {
									return false;
								}
								if (parts.length == 2 && !e.getId().endsWith(parts[1])) {
									return false;
								}
								return true;
							}
						};
					}
				} else {
					predicate = new Predicate<Event>() {
						@Override
						public boolean test(Event e) {
							return e.getId().equals(s);
						}
					};
				}
				predicatesList.add(predicate);
			}

		}

		@Override
		public boolean test(Event t) {
			for (Predicate<Event> p : predicatesList) {
				if (p.test(t)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Returns a {@link Predicate} that can be used to test if an {@link Event}
	 * has an ID matching the the given specification.
	 * 
	 * @param spec
	 *            the event ID specification
	 * @return a {@link Predicate} that can be used to test if an {@link Event}
	 *         has an ID matching the the given specification
	 * @throws IllegalArgumentException
	 *             if the given specification has an invalid syntax
	 */
	public static Predicate<Event> getEventIdSpecMatchingPredicate(String spec)
			throws IllegalArgumentException {
		return new EventIdSpecMatchingPredicatesList(spec);
	}

}
