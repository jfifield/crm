package org.programmerplanet.crm.util;

import java.util.List;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public final class ListUtil {

	/**
	 * Moves an element up or down one position within a list.
	 */
	public static void moveElement(List list, Object element, String direction) {
		if (!("up".equalsIgnoreCase(direction) || "down".equalsIgnoreCase(direction))) {
			throw new IllegalArgumentException("direction argument must be 'up' or 'down': " + direction);
		}

		// get the position of the element in the list
		int position = list.indexOf(element);

		if (position == -1) {
			return;
		}

		// move the element up or down one position
		Object object = list.remove(position);
		if ("up".equalsIgnoreCase(direction)) {
			position--;
		}
		else if ("down".equalsIgnoreCase(direction)) {
			position++;
		}
		if (position < 0) {
			position = 0;
		}
		else if (position > list.size()) {
			position = list.size();
		}
		list.add(position, object);
	}

}
