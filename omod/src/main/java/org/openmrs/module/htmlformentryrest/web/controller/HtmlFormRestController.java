package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.HtmlFormValidator;
import org.openmrs.propertyeditor.EncounterTypeEditor;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/htmlForm")
public class HtmlFormRestController extends BaseRestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/*@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(EncounterType.class, new EncounterTypeEditor());
		binder.registerCustomEditor(java.util.Date.class,
				new CustomDateEditor(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Context.getLocale()), true));
	}*/
	
	/*@ModelAttribute("encounterTypes")
	List<EncounterType> getEncounterTypes() {
		return Context.getEncounterService().getAllEncounterTypes();
	}*/
	
	HtmlForm formBackingObject(Integer id) {
		if (id != null) {
			HtmlForm hf = HtmlFormEntryUtil.getService().getHtmlForm(id);
			// avoid LazyInitializationException
			hf.getForm().getFormFields().size();
			return hf;
		} else {
			HtmlForm hf = new HtmlForm();
			hf.setForm(new Form());
			return hf;
		}
	}
	
	/**
	 * Show a single HTML Form
	 */
	@RequestMapping(method = RequestMethod.GET)
	public JSONObject showHtmlForm(HttpSession httpSession, @RequestParam(value = "id", required = false) Integer id)
	        throws Exception {
		
		String previewHtml = null;
		HtmlForm hf = formBackingObject(id);
		if (hf.getId() == null) {
			previewHtml = "";
		} else {
			try {
				Patient demo = HtmlFormEntryUtil.getFakePerson();
				FormEntrySession fes = new FormEntrySession(demo, hf.getXmlData(), httpSession);
				String html = fes.getHtmlToDisplay();
				if (fes.getFieldAccessorJavascript() != null) {
					html += "<script>" + fes.getFieldAccessorJavascript() + "</script>";
				}
				previewHtml = html;
			}
			catch (Exception ex) {
				log.warn("Error rendering html form", ex);
				previewHtml = "Error! " + ex;
			}
		}
		
		JSONObject response = new JSONObject();
		//decide what all to send, as form hf contains everything
		response.put("id", id);
		response.put("form", hf);
		response.put("previewHtml", previewHtml);
		return response;
	}
	
	/**
	 * Save changes to an HTML Form The Request body needs to be as follows: { "form":{ "name":"",
	 * "description":"", "version":"", "encounterType":"", "creator":{ "personName":"" },
	 * "changedBy":{ "personName":"" }, "published":"", //"checked" OR "" "xmlData":"", // the html
	 * representing the form } }
	 */
	//TODO: add validation for encounter type and few other fields, set the dateCreated and dateChanged fields manually
	@RequestMapping(method = RequestMethod.POST)
	public JSONObject saveHtmlForm(@RequestBody HtmlForm htmlForm, BindingResult result, WebRequest request)
	        throws Exception {
		HtmlFormEntryService service = HtmlFormEntryUtil.getService();
		if (htmlForm.getId() == null && StringUtils.isBlank(htmlForm.getXmlData())) {
			htmlForm.setXmlData(service.getStartingFormXml(htmlForm));
		}
		HtmlFormValidator validator = new HtmlFormValidator();
		validator.validate(htmlForm, result);
		
		JSONObject response = new JSONObject();
		
		if (validator.getHtmlFormWarnings().size() > 0) {
			response.put("tagWarnings", validator.getHtmlFormWarnings());
		}
		
		if (result.hasErrors()) {
			response.put("error-message", "binding has some errors, plz check logs");
		} else {
			htmlForm = service.saveHtmlForm(htmlForm);
			response.put("htmlForm", htmlForm.getId());
			response.put(WebConstants.OPENMRS_MSG_ATTR, "Saved " + htmlForm.getForm().getName() + " "
			        + htmlForm.getForm().getVersion());
		}
		return response;
	}
	
}
