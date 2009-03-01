package org.programmerplanet.crm.web.admin;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.Relationship;
import org.programmerplanet.crm.service.AdministrationService;
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

	private AdministrationService administrationService;

	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}

	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Relationship relationship = (Relationship)command;
		administrationService.insertRelationship(relationship);
		return new ModelAndView(getSuccessView(), "id", relationship.getParentObjectId());
	}

	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Relationship relationship = (Relationship)command;
		administrationService.deleteRelationship(relationship);
		return new ModelAndView(getSuccessView(), "id", relationship.getParentObjectId());
	}

	public ModelAndView move(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Relationship relationship = (Relationship)command;
		String direction = request.getParameter("__move");
		administrationService.moveRelationshipViewIndex(relationship, direction);
		return new ModelAndView(getSuccessView(), "id", relationship.getParentObjectId());
	}

	protected boolean suppressBinding(HttpServletRequest request) {
		return true;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UUID id = RequestUtil.getRequestId(request);
		if (id != null) {
			Relationship relationship = administrationService.getRelationship(id);
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
