package org.programmerplanet.crm.util;

import java.util.Comparator;
import java.util.Map;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class MapEntryComparator implements Comparator {

	private Object key;

	public MapEntryComparator(Object key) {
		this.key = key;
	}

	public int compare(Object o1, Object o2) {
		Map map1 = (Map)o1;
		Map map2 = (Map)o2;

		Object v1 = map1.get(key);
		Object v2 = map2.get(key);

		int result;

		if (v1 != null) {
			result = (v2 != null ? ((Comparable)v1).compareTo(v2) : -1);
		}
		else {
			result = (v2 != null ? 1 : 0);
		}

		return result;
	}

}
