package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.ValidationException;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.openmrs.module.htmlformentry.compatibility.EncounterServiceCompatibility;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/htmlformentryrest")
public class FormEntryRestController extends HFERBaseRestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private static Map<User, Map<String, Object>> volatileUserData = new WeakHashMap<User, Map<String, Object>>();
	
	public final static String FORM_IN_PROGRESS_KEY = "HTML_FORM_IN_PROGRESS_KEY";
	
	@Autowired
	private EncounterServiceCompatibility encounterServiceCompatibility;
	
	@RequestMapping(value = "htmlformentry", method = RequestMethod.GET/*, produces = "application/json"*/)
	@ResponseBody
	public FormEntrySession onGet(HttpServletRequest request) throws Exception {
		return showForm(request);
	}
	
	@RequestMapping(value = "htmlformentry", method = RequestMethod.POST)
	@ResponseBody
	//check how fescontext is initialized
	//handling html form submit
	public JSONObject onPost(Errors errors, HttpServletRequest request) throws Exception {
		FormEntrySession session = showForm(request);
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
		JSONObject response = new JSONObject();
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
			/*String successView = session.getAfterSaveUrlTemplate();
			if (successView != null) {
				successView = successView.replaceAll("\\{\\{patient.id\\}\\}", session.getPatient().getId().toString());
				successView = successView.replaceAll("\\{\\{encounter.id\\}\\}", session.getEncounter().getId().toString());
				successView = request.getContextPath() + "/" + successView;
			} else {
				successView = session.getReturnUrlWithParameters();
			}
			if (successView == null)
				successView = request.getContextPath() + "/patientDashboard.form" + getQueryPrameters(request, session);
			if (StringUtils.hasText(request.getParameter("closeAfterSubmission"))) {
				return new ModelAndView(closeDialogView, "dialogToClose", request.getParameter("closeAfterSubmission"));
			} else {
				return new ModelAndView(new RedirectView(successView));
			}*/
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
	
	public FormEntrySession showForm(HttpServletRequest request) throws Exception {
		
		Integer patientId = null, formId = null, htmlFormId = null;
		Long formModifiedTimestamp = null, encounterModifiedTimestamp = null;
		String hasChangedInd = null;
		
		if (StringUtils.hasText(request.getParameter("patientId"))) {
			patientId = Integer.valueOf(request.getParameter("patientId"));
		}
		if (StringUtils.hasText(request.getParameter("formId"))) {
			formId = Integer.valueOf(request.getParameter("formId"));
		}
		if (StringUtils.hasText(request.getParameter("htmlFormId"))) {
			htmlFormId = Integer.valueOf(request.getParameter("htmlFormId"));
		}
		if (StringUtils.hasText(request.getParameter("formModifiedTimestamp"))) {
			formModifiedTimestamp = Long.valueOf(request.getParameter("formModifiedTimestamp"));
		}
		if (StringUtils.hasText(request.getParameter("encounterModifiedTimestamp"))) {
			encounterModifiedTimestamp = Long.valueOf(request.getParameter("encounterModifiedTimestamp"));
		}
		if (StringUtils.hasText(request.getParameter("hasChangedInd"))) {
			hasChangedInd = request.getParameter("hasChangedInd");
		}
		
		long ts = System.currentTimeMillis();
		
		FormEntryContext.Mode mode = FormEntryContext.Mode.VIEW;
		
		Integer personId = null;
		
		if (StringUtils.hasText(request.getParameter("personId"))) {
			personId = Integer.valueOf(request.getParameter("personId"));
		}
		
		String modeParam = request.getParameter("mode");
		if ("enter".equalsIgnoreCase(modeParam)) {
			mode = FormEntryContext.Mode.ENTER;
		} else if ("edit".equalsIgnoreCase(modeParam)) {
			mode = FormEntryContext.Mode.EDIT;
		}
		
		Patient patient = null;
		Encounter encounter = null;
		Form form = null;
		HtmlForm htmlForm = null;
		
		if (StringUtils.hasText(request.getParameter("encounterId"))) {
			
			String encounterId = request.getParameter("encounterId");
			try {
				encounter = Context.getEncounterService().getEncounter(Integer.valueOf(encounterId));
			}
			catch (NumberFormatException ex) {
				encounter = Context.getEncounterService().getEncounterByUuid(encounterId);
			}
			
			if (encounter == null)
				throw new IllegalArgumentException("No encounter with id=" + encounterId);
			patient = encounter.getPatient();
			patientId = patient.getPatientId();
			personId = patient.getPersonId();
			
			if (formId != null) { // I think formId is allowed to differ from encounter.form.id because of HtmlFormFlowsheet
				form = Context.getFormService().getForm(formId);
				htmlForm = HtmlFormEntryUtil.getService().getHtmlFormByForm(form);
				if (htmlForm == null)
					throw new IllegalArgumentException("No HtmlForm associated with formId " + formId);
			} else {
				form = encounter.getForm();
				htmlForm = HtmlFormEntryUtil.getService().getHtmlFormByForm(encounter.getForm());
				if (htmlForm == null)
					throw new IllegalArgumentException("The form for the specified encounter (" + encounter.getForm()
					        + ") does not have an HtmlForm associated with it");
			}
			
		} else { // no encounter specified
		
			// get person from patientId/personId (register module uses patientId, htmlformentry uses personId)
			if (patientId != null) {
				personId = patientId;
			}
			if (personId != null) {
				patient = Context.getPatientService().getPatient(personId);
			}
			
			// determine form
			if (htmlFormId != null) {
				htmlForm = HtmlFormEntryUtil.getService().getHtmlForm(htmlFormId);
			} else if (formId != null) {
				form = Context.getFormService().getForm(formId);
				htmlForm = HtmlFormEntryUtil.getService().getHtmlFormByForm(form);
			}
			if (htmlForm == null) {
				throw new IllegalArgumentException("You must specify either an htmlFormId or a formId for a valid html form");
			}
			
			String which = request.getParameter("which");
			if (StringUtils.hasText(which)) {
				if (patient == null)
					throw new IllegalArgumentException("Cannot specify 'which' without specifying a person/patient");
				List<Encounter> encs = encounterServiceCompatibility.getEncounters(patient, null, null, null,
				    Collections.singleton(form), null, null, null, null, false);
				if (which.equals("first")) {
					encounter = encs.get(0);
				} else if (which.equals("last")) {
					encounter = encs.get(encs.size() - 1);
				} else {
					throw new IllegalArgumentException("which must be 'first' or 'last'");
				}
			}
		}
		
		if (mode != FormEntryContext.Mode.ENTER && patient == null)
			throw new IllegalArgumentException("No patient with id of personId=" + personId + " or patientId=" + patientId);
		
		FormEntrySession session = null;
		if (mode == FormEntryContext.Mode.ENTER && patient == null) {
			patient = new Patient();
		}
		if (encounter != null) {
			session = new FormEntrySession(patient, encounter, mode, htmlForm, request.getSession());
		} else {
			session = new FormEntrySession(patient, htmlForm, request.getSession());
		}
		
		// Since we're not using a sessionForm, we need to check for the case where the underlying form was modified while a user was filling a form out
		if (formModifiedTimestamp != null) {
			if (!OpenmrsUtil.nullSafeEquals(formModifiedTimestamp, session.getFormModifiedTimestamp())) {
				throw new RuntimeException(Context.getMessageSourceService().getMessage(
				    "htmlformentry.error.formModifiedBeforeSubmission"));
			}
		}
		
		// Since we're not using a sessionForm, we need to make sure this encounter hasn't been modified since the user opened it
		if (encounter != null) {
			if (encounterModifiedTimestamp != null
			        && !OpenmrsUtil.nullSafeEquals(encounterModifiedTimestamp, session.getEncounterModifiedTimestamp())) {
				throw new RuntimeException(Context.getMessageSourceService().getMessage(
				    "htmlformentry.error.encounterModifiedBeforeSubmission"));
			}
		}
		
		if (hasChangedInd != null)
			session.setHasChangedInd(hasChangedInd);
		
		// ensure we've generated the form's HTML (and thus set up the submission actions, etc) before we do anything
		session.getHtmlToDisplay();
		
		setVolatileUserData(FORM_IN_PROGRESS_KEY, session);
		
		log.info("Took " + (System.currentTimeMillis() - ts) + " ms");
		
		return session;
	}
}
