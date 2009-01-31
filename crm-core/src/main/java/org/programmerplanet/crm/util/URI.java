package org.programmerplanet.crm.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

/**
 * Represents an abstraction of a URI and the ability to dynamically add parameters.
 * 
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class URI {

	private String baseUri;
	private Map parameters = new LinkedHashMap();

	public URI(String uri) {
		int index = uri.indexOf('?');
		if (index > -1) {
			baseUri = uri.substring(0, index);
			String query = uri.substring(index + 1);
			String[] params = query.split("&");
			for (int i = 0; i < params.length; i++) {
				String[] param = params[i].split("=");
				String key = param[0];
				String value = param[1];
				addParameter(key, value);
			}
		}
		else {
			baseUri = uri;
		}
	}

	public String getBaseUri() {
		return baseUri;
	}

	public Map getParameters() {
		Map result = new LinkedHashMap();
		result.putAll(parameters);
		return result;
	}

	public void addParameter(String key, Object value) {
		this.parameters.put(key, value);
	}

	public void addParameters(Map parameters) {
		this.parameters.putAll(parameters);
	}

	public String toString() {
		StringBuffer result = new StringBuffer(baseUri);
		for (Iterator i = parameters.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry)i.next();

			String key = ObjectUtils.toString(entry.getKey());
			Object value = entry.getValue();

			if (value.getClass().isArray()) {
				Object[] values = (Object[])value;
				for (int j = 0; j < values.length; j++) {
					if (result.indexOf("?") > -1) {
						result.append('&');
					}
					else {
						result.append('?');
					}
					result.append(key).append('=').append(ObjectUtils.toString(values[j]));
				}
			}
			else {
				if (result.indexOf("?") > -1) {
					result.append('&');
				}
				else {
					result.append('?');
				}
				result.append(key).append('=').append(ObjectUtils.toString(value));
			}
		}
		return result.toString();
	}

}
