package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.schema.HtmlFormSchema;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/htmlformentryrest/htmlformschema")
public class SchemaController extends HFERBaseRestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public JSONObject onGet(@RequestParam(value = "id", required = false) Integer id, HttpSession httpSession)
	        throws Exception {
		String message = "";
		String xml = null;
		
		if (id != null) {
			HtmlForm form = Context.getService(HtmlFormEntryService.class).getHtmlForm(id);
			xml = form.getXmlData();
		} else {
			message = "You must specify a form id to view a form schema";
		}
		
		JSONObject response = new JSONObject();
		response.put("schema", generateSchema(xml, httpSession));
		//discuss how the schema should look, refer the view htmlFormSchema.jsp
		//currently returns the raw object form of the schema
		response.put("message", message);
		
		return response;
	}
	
	private HtmlFormSchema generateSchema(String xml, HttpSession httpSession) throws Exception {
		Patient p = HtmlFormEntryUtil.getFakePerson();
		HtmlForm fakeForm = new HtmlForm();
		fakeForm.setXmlData(xml);
		FormEntrySession fes = new FormEntrySession(p, null, FormEntryContext.Mode.ENTER, fakeForm, httpSession);
		fes.getHtmlToDisplay();
		return fes.getContext().getSchema();
	}
	
}
