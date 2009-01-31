package org.programmerplanet.crm.web.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.service.SearchCriteria;
import org.programmerplanet.crm.service.SearchService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class SearchController extends SimpleFormController {

	private SearchService searchService;

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		SearchCriteria searchCriteria = (SearchCriteria)command;
		Map model = new HashMap();
		model.put("searchCriteria", searchCriteria);
		if (StringUtils.isNotEmpty(searchCriteria.getQuery())) {
			if (searchCriteria.getQuery().equals("index")) {
				searchService.index();
			}
			else {
				List searchResults = searchService.search(searchCriteria);
				model.put("searchResults", searchResults);
			}
		}
		return new ModelAndView(getSuccessView(), model);
	}

}
