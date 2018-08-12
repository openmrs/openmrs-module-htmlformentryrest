package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentryrest.HtmlFormShort;
import org.openmrs.module.htmlformentryrest.services.HtmlFormListServices;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@Authorized
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/htmlformentryrest/htmlformslist")
public class FormsListController extends HFERBaseRestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	HtmlFormListServices htmlFormListServices;
	
	//returns the list of html forms containing the form id and the form name
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object getAllHtmlForms() {
		return getResponse();
	}
	
	public List<HtmlFormShort> getResponse() {
		List<HtmlForm> hflist = htmlFormListServices.getHtmlFormList();
		ArrayList<HtmlFormShort> hfss = new ArrayList<HtmlFormShort>();
		for (int i = 0; i < hflist.size(); i++) {
			hfss.add(new HtmlFormShort(hflist.get(i).getName(), hflist.get(i).getId()));
		}
		return hfss;
	}
	
}
