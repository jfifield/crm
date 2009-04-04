package org.programmerplanet.crm.web.util;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class URITest extends TestCase {

	public void testURI() {
		URI uri = new URI("/test/file?param1=value1");
		assertEquals("/test/file", uri.getBaseUri());
		assertEquals("value1", uri.getParameters().get("param1"));
	}

	public void testAddParameter() {
		URI uri = new URI("/test/file");
		assertNull(uri.getParameters().get("param1"));
		uri.addParameter("param1", "value1");
		assertEquals("value1", uri.getParameters().get("param1"));
	}

	public void testToString() {
		URI uri = new URI("/test/file");
		assertEquals("/test/file", uri.toString());
		uri.addParameter("param1", "value1");
		assertEquals("/test/file?param1=value1", uri.toString());
		uri.addParameter("param2", "value2");
		assertEquals("/test/file?param1=value1&param2=value2", uri.toString());
	}

}
