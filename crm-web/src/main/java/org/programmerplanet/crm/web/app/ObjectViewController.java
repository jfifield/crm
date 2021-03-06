package org.programmerplanet.crm.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.data.DataManager;
import org.programmerplanet.crm.data.ObjectData;
import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.Relationship;
import org.programmerplanet.crm.web.RequestUtil;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectViewController extends ObjectController {

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
		UUID id = RequestUtil.getRequestId(request);

		ObjectDefinition objectDefinition = metadataManager.getObjectDefinition(objectName);
		List fieldDefinitions = metadataManager.getFieldDefinitionsForObjectView(objectDefinition);
		ObjectData objectData = dataManager.getObject(objectDefinition, fieldDefinitions, id);
		Map data = objectData.getData();

		Map model = new HashMap();
		model.put("objectDefinition", objectDefinition);
		model.put("fieldDefinitions", fieldDefinitions);
		model.put("data", data);

		List relationships = getRelationshipModel(objectDefinition, id);
		model.put("relationships", relationships);

		return new ModelAndView("view", model);
	}

	private List getRelationshipModel(ObjectDefinition parentObjectDefinition, UUID id) {
		List relationshipModel = new ArrayList();

		UUID parentObjectId = parentObjectDefinition.getId();

		List relationships = metadataManager.getRelationshipsForObject(parentObjectDefinition);
		
		for (Iterator i = relationships.iterator(); i.hasNext();) {
			Relationship relationship = (Relationship)i.next();

			UUID objectId = relationship.getChildObjectId();
			ObjectDefinition objectDefinition = metadataManager.getObjectDefinition(objectId);
			List fieldDefinitions = metadataManager.getFieldDefinitionsForObjectList(objectDefinition);

			List<ObjectData> objects = dataManager.getRelatedObjects(objectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);
			
			List<Map> data = new ArrayList<Map>();
			for (ObjectData objectData : objects) {
				data.add(objectData.getData());
			}

			Map model = new HashMap();
			model.put("objectDefinition", objectDefinition);
			model.put("fieldDefinitions", fieldDefinitions);
			model.put("data", data);

			relationshipModel.add(model);
		}

		return relationshipModel;
	}

}
