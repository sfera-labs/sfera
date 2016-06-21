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
package cc.sferalabs.sfera.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;

import cc.sferalabs.sfera.console.ConsoleCommandHandler;
import cc.sferalabs.sfera.console.ConsoleSession;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.EventsUtil;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class SystemConsoleCommandHandler implements ConsoleCommandHandler {

	static final SystemConsoleCommandHandler INSTANCE = new SystemConsoleCommandHandler();

	/**
	 * 
	 */
	private SystemConsoleCommandHandler() {
	}

	@Override
	public String getKey() {
		return "sys";
	}

	@Override
	public String accept(String cmd, ConsoleSession session) {
		if (cmd.startsWith("state")) {
			return getState(cmd.substring(5));
		} else if (cmd.equals("quit")) {
			SystemNode.getInstance().quit();
			return "Quitting...";
		} else if (cmd.equals("kill")) {
			System.exit(1);
		} else {
			return "Unkown command";
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 */
	private String getState(String id) {
		final String finalId = id.trim();
		Comparator<Event> comparator = new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				return o1.getId().compareTo(o2.getId());
			}
		};

		Predicate<Event> predicate = EventsUtil.getEventIdSpecMatchingPredicate(finalId);

		Map<String, Event> state = Bus.getCurrentState();
		Collection<Event> events = state.values();

		StringBuilder sb = new StringBuilder();
		events.stream().filter(predicate).sorted(comparator).forEach(e -> {
			sb.append(e.getId()).append(" = ").append(e.getValue()).append("\n");
		});
		return sb.toString();
	}

}
