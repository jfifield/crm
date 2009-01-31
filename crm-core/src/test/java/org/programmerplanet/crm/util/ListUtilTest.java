package org.programmerplanet.crm.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ListUtilTest extends TestCase {

	public void testMoveElement() {
		List list = new ArrayList();
		list.add(new TestElement(1));
		list.add(new TestElement(2));
		list.add(new TestElement(3));

		ListUtil.moveElement(list, new TestElement(1), "up");
		assertEquals(list.get(0), new TestElement(1));
		assertEquals(list.get(1), new TestElement(2));
		assertEquals(list.get(2), new TestElement(3));

		ListUtil.moveElement(list, new TestElement(2), "up");
		assertEquals(list.get(0), new TestElement(2));
		assertEquals(list.get(1), new TestElement(1));
		assertEquals(list.get(2), new TestElement(3));

		ListUtil.moveElement(list, new TestElement(3), "down");
		assertEquals(list.get(0), new TestElement(2));
		assertEquals(list.get(1), new TestElement(1));
		assertEquals(list.get(2), new TestElement(3));

		ListUtil.moveElement(list, new TestElement(2), "down");
		assertEquals(list.get(0), new TestElement(1));
		assertEquals(list.get(1), new TestElement(2));
		assertEquals(list.get(2), new TestElement(3));
	}

	private static class TestElement {

		private Long id;

		public TestElement(long id) {
			this.id = new Long(id);
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getId() {
			return id;
		}

		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}

		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

	}

}
