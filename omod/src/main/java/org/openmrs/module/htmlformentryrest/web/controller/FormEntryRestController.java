package org.openmrs.module.htmlformentryrest.web.controller;

import net.sf.saxon.Err;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.ValidationException;
import org.openmrs.module.htmlformentryrest.services.HtmlFormEntryService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.openmrs.module.htmlformentry.compatibility.EncounterServiceCompatibility;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/htmlformentryrest/htmlformentry")
@Controller
public class FormEntryRestController extends HFERBaseRestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private static Map<User, Map<String, Object>> volatileUserData = new WeakHashMap<User, Map<String, Object>>();
	
	public final static String FORM_IN_PROGRESS_KEY = "HTML_FORM_IN_PROGRESS_KEY";
	
	@Autowired
	HtmlFormEntryService htmlFormEntryService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object getformentry(HttpServletRequest request) throws Exception {
		Context.authenticate(request.getParameter("username"), request.getParameter("password"));
		return getformentryhelper(request);
	}
	
	public HashMap<Object, Object> getformentryhelper(HttpServletRequest request) throws Exception {
		FormEntrySession fes = htmlFormEntryService.showForm(request);
		setVolatileUserData(FORM_IN_PROGRESS_KEY, fes);
		HashMap<Object, Object> response = new HashMap<Object, Object>();
		response.put("patientPersonName", fes.getPatientPersonName());
		response.put("formName", fes.getFormName());
		response.put("encounterTypeName", fes.getFormEncounterTypeName());
		response.put("encounterFormName", fes.getEncounterFormName());
		response.put("encounterTypeName", fes.getEncounterEncounterTypeName());
		response.put("encounterDatetime", fes.getEncounter().getEncounterDatetime());
		response.put("encounterLocationName", fes.getEncounterLocationName());
		response.put("context.mode", fes.getContext().getMode());
		response.put("patient.personId", fes.getPatient().getPersonId());
		response.put("htmlFormId", fes.getHtmlFormId());
		response.put("formModifiedTimeStamp", fes.getFormModifiedTimestamp());
		response.put("encounterModifiedTimeStamp", fes.getEncounterModifiedTimestamp());
		response.put("encounterId", fes.getEncounter().getEncounterId());
		response.put("hasChangedId", fes.getHasChangedInd());
		response.put("GuessingInd", fes.getContext().getGuessingInd());
		response.put("htmlToDisplay", fes.getHtmlToDisplay());
		response.put("fieldAccessorJavascript", fes.getFieldAccessorJavascript());
		//log.error(response.toString());
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	//http://localhost:8080/openmrs/module/htmlformentry/htmlFormEntry.form?encounterId=393&mode=EDIT
	public Object onPost(HttpServletRequest request) throws Exception {
		
		Errors errors = new Errors() {
			
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
		
		Context.authenticate(request.getParameter("username"), request.getParameter("password"));
		
		FormEntrySession session = htmlFormEntryService.showForm(request);
		try {
			List<FormSubmissionError> validationErrors = session.getSubmissionController().validateSubmission(
			    session.getContext(), request);
			// .getContext()??
			if (validationErrors != null && validationErrors.size() > 0) {
				errors.reject("Fix errors");
			}
		}
		catch (Exception ex) {
			log.error("Exception during form validation", ex);
			errors.reject("Exception during form validation, see log for more details: " + ex);
		}
		HashMap<Object, Object> response = new HashMap<Object, Object>();
		if (errors.hasErrors()) {
			response.put("errors", errors);
			return response;
		}
		
		// no form validation errors, proceed with submission
		session.prepareForSubmit();
		
		if (session.getContext().getMode() == FormEntryContext.Mode.ENTER
		        && session.hasPatientTag()
		        && session.getPatient() == null
		        && (session.getSubmissionActions().getPersonsToCreate() == null || session.getSubmissionActions()
		                .getPersonsToCreate().size() == 0))
			throw new IllegalArgumentException("This form is not going to create an Patient");
		
		if (session.getContext().getMode() == FormEntryContext.Mode.ENTER
		        && session.hasEncouterTag()
		        && (session.getSubmissionActions().getEncountersToCreate() == null || session.getSubmissionActions()
		                .getEncountersToCreate().size() == 0))
			throw new IllegalArgumentException("This form is not going to create an encounter");
		
		try {
			session.getSubmissionController().handleFormSubmission(session, request);
			HtmlFormEntryUtil.getService().applyActions(session);
			response.put("message", "success");
		}
		catch (ValidationException ex) {
			log.error("Invalid input:", ex);
			response.put("errors", ex.getMessage());
			errors.reject(ex.getMessage());
		}
		catch (BadFormDesignException ex) {
			log.error("Bad Form Design:", ex);
			response.put("errors", ex.getMessage());
			errors.reject(ex.getMessage());
		}
		catch (Exception ex) {
			log.error("Exception trying to submit form", ex);
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			errors.reject("Exception! " + ex.getMessage() + "<br/>" + sw.toString());
			response.put("errors", "Exception! " + ex.getMessage() + "<br/>" + sw.toString());
		}
		
		return response;
	}
	
	public static void setVolatileUserData(String key, Object value) {
		User u = Context.getAuthenticatedUser();
		if (u == null) {
			throw new APIAuthenticationException();
		}
		Map<String, Object> myData = volatileUserData.get(u);
		if (myData == null) {
			myData = new HashMap<String, Object>();
			volatileUserData.put(u, myData);
		}
		myData.put(key, value);
	}
	
}
