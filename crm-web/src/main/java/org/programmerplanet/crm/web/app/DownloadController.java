package org.programmerplanet.crm.web.app;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.FileInfo;
import org.programmerplanet.crm.service.ApplicationService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class DownloadController implements Controller {

	private ApplicationService applicationService;

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = new Long(request.getParameter("id"));

		FileInfo fileInfo = applicationService.getFileInfo(id);

		response.setHeader("Content-Type", fileInfo.getMimeType());
		response.setHeader("Content-Length", Long.toString(fileInfo.getFileSize()));
		response.setHeader("Content-Disposition", "attachment; filename=" + fileInfo.getFileName());

		ServletOutputStream outputStream = response.getOutputStream();
		applicationService.getFile(id, outputStream);
		response.flushBuffer();

		return null;
	}

}
