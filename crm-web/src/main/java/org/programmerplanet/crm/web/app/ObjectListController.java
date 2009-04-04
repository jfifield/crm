package org.programmerplanet.crm.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.data.DataManager;
import org.programmerplanet.crm.data.ObjectData;
import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectListController extends ObjectController {

	private MetadataManager metadataManager;
	private DataManager dataManager;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);

		ObjectDefinition objectDefinition = metadataManager.getObjectDefinition(objectName);
		List fieldDefinitions = metadataManager.getFieldDefinitionsForObjectList(objectDefinition);
		List<ObjectData> objects = dataManager.getObjects(objectDefinition, fieldDefinitions);
		
		List<Map> data = new ArrayList<Map>();
		for (ObjectData objectData : objects) {
			data.add(objectData.getData());
		}

		Map model = new HashMap();
		model.put("objectDefinition", objectDefinition);
		model.put("fieldDefinitions", fieldDefinitions);
		model.put("data", data);

		return new ModelAndView("list", model);
	}

}
