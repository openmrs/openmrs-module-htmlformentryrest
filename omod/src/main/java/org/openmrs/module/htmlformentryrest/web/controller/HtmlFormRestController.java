package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.HtmlFormValidator;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.propertyeditor.EncounterTypeEditor;
import org.openmrs.web.WebConstants;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Authorized
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/htmlformentryrest/htmlform")
public class HtmlFormRestController extends HFERBaseRestController {
	
	/**
	 * Show a single HTML Form
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object showHtmlForm(HttpSession httpSession, @RequestParam(value = "id", required = false) Integer id)
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
		
		HashMap<Object, Object> response = new HashMap<Object, Object>();
		//decide what all to send, as form hf contains everything
		response.put("id", id);
		//response.put("form", hf);
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
	@ResponseBody
	public Object saveHtmlForm(/*@RequestBody HtmlForm htmlForm,*/HttpServletRequest request, HttpSession session)
	        throws Exception {
		
		Errors result = new Errors() {
			
			@Override
			public String getObjectName() {
				return null;
			}
			
			@Override
			public void setNestedPath(String s) {
				
			}
			
			@Override
			public String getNestedPath() {
				return null;
			}
			
			@Override
			public void pushNestedPath(String s) {
				
			}
			
			@Override
			public void popNestedPath() throws IllegalStateException {
				
			}
			
			@Override
			public void reject(String s) {
				
			}
			
			@Override
			public void reject(String s, String s1) {
				
			}
			
			@Override
			public void reject(String s, Object[] objects, String s1) {
				
			}
			
			@Override
			public void rejectValue(String s, String s1) {
				
			}
			
			@Override
			public void rejectValue(String s, String s1, String s2) {
				
			}
			
			@Override
			public void rejectValue(String s, String s1, Object[] objects, String s2) {
				
			}
			
			@Override
			public void addAllErrors(Errors errors) {
				
			}
			
			@Override
			public boolean hasErrors() {
				return false;
			}
			
			@Override
			public int getErrorCount() {
				return 0;
			}
			
			@Override
			public List<ObjectError> getAllErrors() {
				return null;
			}
			
			@Override
			public boolean hasGlobalErrors() {
				return false;
			}
			
			@Override
			public int getGlobalErrorCount() {
				return 0;
			}
			
			@Override
			public List<ObjectError> getGlobalErrors() {
				return null;
			}
			
			@Override
			public ObjectError getGlobalError() {
				return null;
			}
			
			@Override
			public boolean hasFieldErrors() {
				return false;
			}
			
			@Override
			public int getFieldErrorCount() {
				return 0;
			}
			
			@Override
			public List<FieldError> getFieldErrors() {
				return null;
			}
			
			@Override
			public FieldError getFieldError() {
				return null;
			}
			
			@Override
			public boolean hasFieldErrors(String s) {
				return false;
			}
			
			@Override
			public int getFieldErrorCount(String s) {
				return 0;
			}
			
			@Override
			public List<FieldError> getFieldErrors(String s) {
				return null;
			}
			
			@Override
			public FieldError getFieldError(String s) {
				return null;
			}
			
			@Override
			public Object getFieldValue(String s) {
				return null;
			}
			
			@Override
			public Class<?> getFieldType(String s) {
				return null;
			}
		};
		
		String payloadRequest = getBody(request);
		JSONObject json = new JSONObject(payloadRequest);
		
		HtmlForm htmlForm = new HtmlForm();
		Form form = htmlForm.getForm();
		if (form == null) {
			form = new Form();
			htmlForm.setForm(form);
		}
		
		JSONObject formJson = null;
		try {
			formJson = json.getJSONObject("form");
		}
		catch (Exception e) {
			log.error(e + "form json object not found in POST body!");
		}
		
		try {
			form.setName(formJson.getString("name"));
		}
		catch (Exception e) {
			log.error(e + "not able to set name or name key not found in json");
		}
		
		try {
			form.setDescription(formJson.getString("description"));
		}
		catch (Exception e) {
			log.error(e + "not able to set description or description key not found in json");
		}
		
		try {
			setEncounterType(formJson.getString("encounterType"), htmlForm);
		}
		catch (Exception e) {
			log.error(e + "not able to set encounterType or encounterType not defined in POST body");
		}
		
		try {
			form.setVersion(formJson.getString("version"));
		}
		catch (Exception e) {
			log.error(e + "version not found in POST body or not able to set version");
		}
		
		try {
			form.setPublished(formJson.getString("published").equals("checked"));
		}
		catch (Exception e) {
			log.error(e + "published key not found in POST body or not able to set it");
		}
		
		try {
			htmlForm.setXmlData(json.getString("xmlData"));
		}
		catch (Exception e) {
			log.error(e + "xmlData key not found in POST or not able to set xmlData");
		}
		
		//form.setCreator(new User()); // default user
		
		HtmlFormEntryService service = HtmlFormEntryUtil.getService();
		if (htmlForm.getId() == null && StringUtils.isBlank(htmlForm.getXmlData())) {
			htmlForm.setXmlData(service.getStartingFormXml(htmlForm));
		}
		HtmlFormValidator validator = new HtmlFormValidator();
		validator.validate(htmlForm, result);
		
		HashMap<Object, Object> response = new HashMap<Object, Object>();
		
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
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/*@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(java.util.Date.class,
		    new CustomDateEditor(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Context.getLocale()), true));
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
	
	public void setEncounterType(String text, HtmlForm htmlForm) throws IllegalArgumentException {
		EncounterService ps = Context.getEncounterService();
		if (org.springframework.util.StringUtils.hasText(text)) {
			try {
				htmlForm.getForm().setEncounterType(ps.getEncounterType(Integer.valueOf(text)));
			}
			catch (Exception ex) {
				EncounterType encounterType = ps.getEncounterTypeByUuid(text);
				htmlForm.getForm().setEncounterType(encounterType);
				if (encounterType == null) {
					log.error("Error setting text: " + text, ex);
					throw new IllegalArgumentException("Encounter Type not found: " + ex.getMessage());
				}
			}
		} else {
			htmlForm.getForm().setEncounterType(null);
		}
	}
	
	public static String getBody(HttpServletRequest request) throws IOException {
		
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		}
		catch (IOException ex) {
			throw ex;
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException ex) {
					throw ex;
				}
			}
		}
		
		body = stringBuilder.toString();
		return body;
	}
	
}
