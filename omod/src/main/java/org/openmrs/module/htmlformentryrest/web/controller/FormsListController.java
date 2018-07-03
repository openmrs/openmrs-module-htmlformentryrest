package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/htmlformslist")
public class FormsListController extends BaseRestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	//returns the list of html forms containing the form id and the form name
	//returns the whole html form element for now, discuss if we want to limit what fields in each form we want to send.
	@RequestMapping(method = RequestMethod.GET)
	protected Object getAllHtmlForms() throws Exception {
		return HtmlFormEntryUtil.getService().getAllHtmlForms();
	}
}
