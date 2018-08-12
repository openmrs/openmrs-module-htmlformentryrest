package org.openmrs.module.htmlformentryrest.services;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.compatibility.EncounterServiceCompatibility;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class HtmlFormEntryService {
	
	private EncounterServiceCompatibility encounterServiceCompatibility = new EncounterServiceCompatibility() {
		
		@Override
		public List<Encounter> getEncounters(Patient who, Location loc, Date fromDate, Date toDate,
		        Collection<Form> enteredViaForms, Collection<EncounterType> encounterTypes, Collection<Provider> providers,
		        Collection<VisitType> visitTypes, Collection<Visit> visits, boolean includeVoided) {
			
			return Context.getEncounterService().getEncounters(who, loc, fromDate, toDate, enteredViaForms, encounterTypes,
			    providers, visitTypes, visits, includeVoided);
		}
	};
	
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
		
		return session;
	}
}
