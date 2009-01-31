package org.programmerplanet.crm.web.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.service.ApplicationService;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectListController extends ObjectController {

	private ApplicationService applicationService;

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);

		ObjectDefinition objectDefinition = applicationService.getObjectDefinition(objectName);
		List fieldDefinitions = applicationService.getFieldDefinitionsForObjectList(objectDefinition);
		List data = applicationService.getCrmObjects(objectDefinition, fieldDefinitions);

		Map model = new HashMap();
		model.put("objectDefinition", objectDefinition);
		model.put("fieldDefinitions", fieldDefinitions);
		model.put("data", data);

		return new ModelAndView("list", model);
	}

}
