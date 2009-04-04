package org.programmerplanet.crm.web.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class MapEntryComparatorTest extends TestCase {

	public void testCompare() {
		MapEntryComparator comparator = new MapEntryComparator("key1");

		Map map1 = new HashMap();
		Map map2 = new HashMap();

		assertEquals(0, comparator.compare(map1, map2));

		map1.put("key1", "A");
		map2.put("key1", "A");
		assertEquals(0, comparator.compare(map1, map2));

		map1.put("key1", "A");
		map2.put("key1", "B");
		assertEquals(-1, comparator.compare(map1, map2));
		assertEquals(1, comparator.compare(map2, map1));
	}

}
