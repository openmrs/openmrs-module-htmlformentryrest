package org.openmrs.module.htmlformentryrest.web.controller;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentryrest.HtmlFormShort;
import org.openmrs.module.htmlformentryrest.services.HtmlFormListServices;
import org.openmrs.test.BaseContextMockTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class FormsListControllerTest extends BaseContextMockTest {
	
	@Mock
	private HtmlFormListServices htmlFormListServices;
	
	@InjectMocks
	FormsListController formsListController;
	
	@Test
	public void getAListOfHtmlForms() {
		MockitoAnnotations.initMocks(this);
		List<HtmlForm> hfl = new ArrayList<HtmlForm>();
		HtmlForm hf = new HtmlForm();
		hf.setName("html form 1");
		hf.setId(1);
		hfl.add(hf);
		hf = new HtmlForm();
		hf.setName("html form 2");
		hf.setId(2);
		Mockito.when(htmlFormListServices.getHtmlFormList()).thenReturn(hfl);
		List<HtmlFormShort> mockResp = formsListController.getResponse();
		Assert.assertNotEquals(mockResp.size(), 2);
	}
}
