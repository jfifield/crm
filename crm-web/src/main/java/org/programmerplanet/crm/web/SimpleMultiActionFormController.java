package org.programmerplanet.crm.web;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.util.UUIDPropertyEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class SimpleMultiActionFormController extends SimpleFormController {

	private MethodNameResolver methodNameResolver;

	public void setMethodNameResolver(MethodNameResolver methodNameResolver) {
		this.methodNameResolver = methodNameResolver;
	}

	public MethodNameResolver getMethodNameResolver() {
		return methodNameResolver;
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(UUID.class, new UUIDPropertyEditor());
	}

	protected final ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		String methodName = methodNameResolver.getHandlerMethodName(request);

		Class[] parameterTypes = new Class[] { HttpServletRequest.class, HttpServletResponse.class, Object.class, BindException.class };
		Method method = this.getClass().getMethod(methodName, parameterTypes);
		Object[] parameters = new Object[] { request, response, command, errors };
		Object object = method.invoke(this, parameters);
		ModelAndView modelAndView = (ModelAndView)object;
		return modelAndView;
	}
	
}
