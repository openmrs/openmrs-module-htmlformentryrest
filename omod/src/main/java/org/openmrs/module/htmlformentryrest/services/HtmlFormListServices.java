package org.openmrs.module.htmlformentryrest.services;

import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HtmlFormListServices {
	
	public List<HtmlForm> getHtmlFormList() {
		return HtmlFormEntryUtil.getService().getAllHtmlForms();
	}
	
}
