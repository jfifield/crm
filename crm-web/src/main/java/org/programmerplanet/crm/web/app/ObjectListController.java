package org.programmerplanet.crm.web.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.service.ApplicationService;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectListController extends ObjectController {

	private MetadataManager metadataManager;
	private ApplicationService applicationService;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);

		ObjectDefinition objectDefinition = metadataManager.getObjectDefinition(objectName);
		List fieldDefinitions = metadataManager.getFieldDefinitionsForObjectList(objectDefinition);
		List data = applicationService.getCrmObjects(objectDefinition, fieldDefinitions);

		Map model = new HashMap();
		model.put("objectDefinition", objectDefinition);
		model.put("fieldDefinitions", fieldDefinitions);
		model.put("data", data);

		return new ModelAndView("list", model);
	}

}
