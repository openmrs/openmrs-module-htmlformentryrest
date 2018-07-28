package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentryrest.HtmlFormShort;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@Authorized
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/htmlformentryrest/htmlformslist")
public class FormsListController extends HFERBaseRestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	//returns the list of html forms containing the form id and the form name
	//returns the whole html form element for now, discuss if we want to limit what fields in each form we want to send.
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	protected Object getAllHtmlForms() throws Exception {
		List<HtmlForm> hflist = HtmlFormEntryUtil.getService().getAllHtmlForms();
		ArrayList<HtmlFormShort> response = new ArrayList<HtmlFormShort>();
		for (int i = 0; i < hflist.size(); i++) {
			response.add(new HtmlFormShort(hflist.get(i).getName(), hflist.get(i).getId()));
		}
		return response;
		//ObjectMapper mapperObj = new ObjectMapper();
		//return mapperObj.writeValueAsString(response);
	}
}
