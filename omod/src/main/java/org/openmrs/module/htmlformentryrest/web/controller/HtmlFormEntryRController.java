package org.openmrs.module.htmlformentryrest.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.ValidationException;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RequestMapping("/htmlFormEntry")
public class HtmlFormEntryRController extends BaseRestController {

	protected final Log log = LogFactory.getLog(getClass());

	@RequestMapping(method=RequestMethod.POST)
	//check how mapping will take place btw fes and the post body(study the form / command used in the hfe module) or create a FormEntrySession manually here in the method
	//check how fescontext is initialized
	public JSONObject handleSubmit(@RequestBody FormEntrySession session,
			Errors errors,
			HttpServletRequest request) throws Exception {
		try {
			List<FormSubmissionError> validationErrors = session.getSubmissionController().validateSubmission(session.getContext(), request);
			if (validationErrors != null && validationErrors.size() > 0) {
				errors.reject("Fix errors");
			}
		} catch (Exception ex) {
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

		if (session.getContext().getMode() == FormEntryContext.Mode.ENTER && session.hasPatientTag() && session.getPatient() == null
				&& (session.getSubmissionActions().getPersonsToCreate() == null || session.getSubmissionActions().getPersonsToCreate().size() == 0))
			throw new IllegalArgumentException("This form is not going to create an Patient");

		if (session.getContext().getMode() == FormEntryContext.Mode.ENTER && session.hasEncouterTag() && (session.getSubmissionActions().getEncountersToCreate() == null || session.getSubmissionActions().getEncountersToCreate().size() == 0))
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
		} catch (ValidationException ex) {
			log.error("Invalid input:", ex);
			response.put("errors", ex.getMessage());
			errors.reject(ex.getMessage());
		} catch (BadFormDesignException ex) {
			log.error("Bad Form Design:", ex);
			response.put("errors", ex.getMessage());
			errors.reject(ex.getMessage());
		} catch (Exception ex) {
			log.error("Exception trying to submit form", ex);
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			errors.reject("Exception! " + ex.getMessage() + "<br/>" + sw.toString());
			response.put("errors", "Exception! " + ex.getMessage() + "<br/>" + sw.toString());
		}

		// if we get here it's because we caught an error trying to submit/apply
		return response;
	}

}
