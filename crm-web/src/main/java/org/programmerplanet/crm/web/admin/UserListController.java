package org.programmerplanet.crm.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.service.AdministrationService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class UserListController implements Controller {

	private AdministrationService administrationService;

	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List users = administrationService.getAllUsers();

		Map model = new HashMap();
		model.put("users", users);

		return new ModelAndView("userList", model);
	}

}