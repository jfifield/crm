package org.programmerplanet.crm.web.app.renderer;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class PagedList {

	private List list;
	private int pageSize;
	private int page = 1;

	public void setList(List list) {
		this.list = list;
	}

	public List getList() {
		return list;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPage(int page) {
		// make sure current page does not exceed total pages
		this.page = Math.min(page, this.getTotalPages());
	}

	public int getPage() {
		return page;
	}

	public int getTotalPages() {
		return Math.max((int)Math.ceil((double)list.size() / (double)pageSize), 1);
	}

	public int getTotalItemCount() {
		return list.size();
	}

	public boolean isFirst() {
		return page == 1;
	}

	public boolean isLast() {
		return page == this.getTotalPages();
	}

	public int getFirstItemNumber() {
		int totalItems = this.getTotalItemCount();
		int result = Math.min((page - 1) * pageSize + 1, totalItems);
		return result;
	}

	public int getLastItemNumber() {
		int totalItems = this.getTotalItemCount();
		int result = Math.min(page * pageSize, totalItems);
		return result;
	}

	public List getPageList() {
		if (!list.isEmpty()) {
			return list.subList(getFirstItemNumber() - 1, getLastItemNumber());
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}

}