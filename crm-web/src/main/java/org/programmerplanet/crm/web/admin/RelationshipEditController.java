package org.programmerplanet.crm.web.admin;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.Relationship;
import org.programmerplanet.crm.web.RequestUtil;
import org.programmerplanet.crm.web.SimpleMultiActionFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class RelationshipEditController extends SimpleMultiActionFormController {

	private MetadataManager metadataManager;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Relationship relationship = (Relationship)command;
		metadataManager.saveRelationship(relationship);
		return new ModelAndView(getSuccessView(), "id", relationship.getParentObjectId());
	}

	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Relationship relationship = (Relationship)command;
		metadataManager.deleteRelationship(relationship);
		return new ModelAndView(getSuccessView(), "id", relationship.getParentObjectId());
	}

	public ModelAndView move(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Relationship relationship = (Relationship)command;
		String direction = request.getParameter("__move");
		metadataManager.moveRelationshipViewIndex(relationship, direction);
		return new ModelAndView(getSuccessView(), "id", relationship.getParentObjectId());
	}

	protected boolean suppressBinding(HttpServletRequest request) {
		return true;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UUID id = RequestUtil.getRequestId(request);
		if (id != null) {
			Relationship relationship = metadataManager.getRelationship(id);
			return relationship;
		}
		else {
			Relationship relationship = new Relationship();
			UUID parentObjectId = RequestUtil.getRequestId(request, "parent_object_id");
			relationship.setParentObjectId(parentObjectId);
			UUID childObjectId = RequestUtil.getRequestId(request, "child_object_id");
			relationship.setChildObjectId(childObjectId);
			return relationship;
		}
	}

}
