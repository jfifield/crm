package org.programmerplanet.crm.web.app.renderer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class PagedListTest extends TestCase {

	public void testGetTotalPages() {
		PagedList pagedList = new PagedList();
		List list = new ArrayList();
		pagedList.setList(list);
		pagedList.setPageSize(3);

		assertEquals(1, pagedList.getTotalPages());
		list.add("Item 1");
		assertEquals(1, pagedList.getTotalPages());
		list.add("Item 2");
		assertEquals(1, pagedList.getTotalPages());
		list.add("Item 3");
		assertEquals(1, pagedList.getTotalPages());
		list.add("Item 4");
		assertEquals(2, pagedList.getTotalPages());
	}

	public void testSetPage() {
		PagedList pagedList = new PagedList();
		List list = new ArrayList();
		pagedList.setList(list);
		pagedList.setPageSize(3);

		pagedList.setPage(2);
		assertEquals(1, pagedList.getPage());
		list.add("Item 1");
		pagedList.setPage(3);
		assertEquals(1, pagedList.getPage());
		list.add("Item 2");
		pagedList.setPage(4);
		assertEquals(1, pagedList.getPage());
		list.add("Item 3");
		pagedList.setPage(5);
		assertEquals(1, pagedList.getPage());
		list.add("Item 4");
		pagedList.setPage(6);
		assertEquals(2, pagedList.getPage());
	}

	public void testGetTotalItemCount() {
		PagedList pagedList = new PagedList();
		List list = new ArrayList();
		pagedList.setList(list);
		pagedList.setPageSize(3);

		assertEquals(0, pagedList.getTotalItemCount());
		list.add("Item 1");
		assertEquals(1, pagedList.getTotalItemCount());
		list.add("Item 2");
		assertEquals(2, pagedList.getTotalItemCount());
		list.add("Item 3");
		assertEquals(3, pagedList.getTotalItemCount());
		list.add("Item 4");
		assertEquals(4, pagedList.getTotalItemCount());
	}

	public void testIsFirst() {
		PagedList pagedList = new PagedList();
		List list = new ArrayList();
		list.add("Item 1");
		list.add("Item 2");
		list.add("Item 3");
		list.add("Item 4");
		pagedList.setList(list);
		pagedList.setPageSize(3);

		pagedList.setPage(1);
		assertTrue(pagedList.isFirst());
		pagedList.setPage(2);
		assertFalse(pagedList.isFirst());
	}

	public void testIsLast() {
		PagedList pagedList = new PagedList();
		List list = new ArrayList();
		list.add("Item 1");
		list.add("Item 2");
		list.add("Item 3");
		list.add("Item 4");
		pagedList.setList(list);
		pagedList.setPageSize(3);

		pagedList.setPage(1);
		assertFalse(pagedList.isLast());
		pagedList.setPage(2);
		assertTrue(pagedList.isLast());
	}

	public void testGetFirstItemNumber() {
		PagedList pagedList = new PagedList();
		List list = new ArrayList();
		list.add("Item 1");
		list.add("Item 2");
		list.add("Item 3");
		list.add("Item 4");
		pagedList.setList(list);
		pagedList.setPageSize(3);

		pagedList.setPage(1);
		assertEquals(1, pagedList.getFirstItemNumber());
		pagedList.setPage(2);
		assertEquals(4, pagedList.getFirstItemNumber());
	}

	public void testGetLastItemNumber() {
		PagedList pagedList = new PagedList();
		List list = new ArrayList();
		list.add("Item 1");
		list.add("Item 2");
		list.add("Item 3");
		list.add("Item 4");
		pagedList.setList(list);
		pagedList.setPageSize(3);

		pagedList.setPage(1);
		assertEquals(3, pagedList.getLastItemNumber());
		pagedList.setPage(2);
		assertEquals(4, pagedList.getLastItemNumber());
	}

	public void testGetPageList() {
		PagedList pagedList = new PagedList();
		List list = new ArrayList();
		list.add("Item 1");
		list.add("Item 2");
		list.add("Item 3");
		list.add("Item 4");
		pagedList.setList(list);
		pagedList.setPageSize(3);

		pagedList.setPage(1);
		List page1 = pagedList.getPageList();
		assertEquals(3, page1.size());
		assertEquals("Item 1", page1.get(0));
		assertEquals("Item 2", page1.get(1));
		assertEquals("Item 3", page1.get(2));

		pagedList.setPage(2);
		List page2 = pagedList.getPageList();
		assertEquals(1, page2.size());
		assertEquals("Item 4", page2.get(0));
	}

}
