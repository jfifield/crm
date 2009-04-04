package org.programmerplanet.crm.search;

import java.util.List;


/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public interface SearchService {

	void index();
	
	List search(SearchCriteria searchCriteria);
	
}
