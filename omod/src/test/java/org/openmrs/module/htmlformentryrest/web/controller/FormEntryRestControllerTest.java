package org.openmrs.module.htmlformentryrest.web.controller;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentryrest.services.HtmlFormEntryService;
import org.openmrs.module.htmlformentryrest.services.HtmlFormListServices;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FormEntryRestControllerTest {
	
	@Mock
	private HtmlFormEntryService htmlFormEntryService;
	
	@InjectMocks
	FormEntryRestController formEntryRestController;
	
	@Test
	public void shouldReturnValidFormData() throws Exception {
		MockitoAnnotations.initMocks(this);
		//FormEntrySession fes = new FormEntrySession(new Patient(), new HtmlForm(), FormEntryContext.Mode.VIEW,
		//        TestUtils.getDummySession());
		//Mockito.when(htmlFormEntryService.showForm(TestUtils.createDummyRequest())).thenReturn(fes);
		//HashMap<Object, Object> response = formEntryRestController.getformentryhelper(TestUtils.createDummyRequest());
		//Assert.assertEquals(response.keySet().size(), 17);
	}
}
