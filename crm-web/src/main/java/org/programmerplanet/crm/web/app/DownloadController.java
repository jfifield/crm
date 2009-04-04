package org.programmerplanet.crm.web.app;

import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.data.DataManager;
import org.programmerplanet.crm.data.FileInfo;
import org.programmerplanet.crm.web.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class DownloadController implements Controller {

	private DataManager dataManager;

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UUID id = RequestUtil.getRequestId(request);

		FileInfo fileInfo = dataManager.getFileInfo(id);

		response.setHeader("Content-Type", fileInfo.getMimeType());
		response.setHeader("Content-Length", Long.toString(fileInfo.getFileSize()));
		response.setHeader("Content-Disposition", "attachment; filename=" + fileInfo.getFileName());

		ServletOutputStream outputStream = response.getOutputStream();
		dataManager.getFile(id, outputStream);
		response.flushBuffer();

		return null;
	}

}
