package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.beanutils.PropertyUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
//import org.openmrs.module.webservices.rest.SimpleObject;
//import org.openmrs.module.webservices.rest.web.RestConstants;
//import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
//import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceControllerTest;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.HandlerExecutionChain;
//import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class FormListRestControllerTest extends BaseModuleWebContextSensitiveTest {
	
	public String getURI() {
		return "/htmlformentryrest/htmlformslist";
	}
	
	@Before
	public void setUp() throws Exception {
		executeDataSet("org,openmrs.module.htmlformentryrest/include/hfee'.xml");
	}
	
	@Test()
	public void shouldReturnFormList() throws Exception {
		Encounter encounter = null;
		encounter = Context.getEncounterService().getEncounter(3);
		logger.error(encounter);
		assertEquals(false, encounter == null);
		//SimpleObject autoGenerationOption = new SimpleObject();
		//		autoGenerationOption.add("identifierType", IDENTIFIER_TYPE_UUID);
		//		autoGenerationOption.add("location", LOCATION);
		//		autoGenerationOption.add("source", SOURCE);
		//		autoGenerationOption.add("automaticGenerationEnabled", true);
		//		autoGenerationOption.add("manualEntryEnabled", true);
		//		String json = new ObjectMapper().writeValueAsString(autoGenerationOption);
		///MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		//		req.setContent(json.getBytes());
		///SimpleObject response = deserialize(handle(req));
		//		assertEquals(originalCount + 1, getAllCount());
		//		Object autogenerationOptionUuid = PropertyUtils.getProperty(response, "uuid");
		//		AutoGenerationOption newAutoGenerationOption = identifierSourceService
		//				.getAutoGenerationOption(Integer.parseInt(autogenerationOptionUuid.toString()));
		//assertEquals(SOURCE, newAutoGenerationOption.getSource().getUuid());
		//assertEquals(LOCATION, newAutoGenerationOption.getLocation().getUuid());
		//assertEquals(IDENTIFIER_TYPE_UUID, newAutoGenerationOption.getIdentifierType().getUuid());
		//assertEquals(true, newAutoGenerationOption.isAutomaticGenerationEnabled());
		///assertEquals(false, response.isEmpty());
	}
	
	//	public MockHttpServletRequest request(RequestMethod method, String requestURI) {
	//		MockHttpServletRequest request = new MockHttpServletRequest(method.toString(), "/rest/" + this.getNamespace() + "/" + requestURI);
	//		request.addHeader("content-type", "application/json");
	//		return request;
	//	}
	//
	//	public SimpleObject deserialize(MockHttpServletResponse response) throws Exception {
	//		return (SimpleObject)(new ObjectMapper()).readValue(response.getContentAsString(), SimpleObject.class);
	//	}
}