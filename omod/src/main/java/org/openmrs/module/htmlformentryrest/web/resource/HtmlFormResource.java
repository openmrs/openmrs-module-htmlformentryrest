/**
 * 
 */
package org.openmrs.module.htmlformentryrest.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Resource(name = RestConstants.VERSION_1 + "/htmlformentryrest/htmlform", supportedClass = HtmlForm.class, supportedOpenmrsVersions = {
        "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*" })
public class HtmlFormResource extends DelegatingCrudResource<HtmlForm> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		return null;
	}
	
	@Override
	public HtmlForm newDelegate() {
		return new HtmlForm();
	}
	
	@Override
	public HtmlForm save(HtmlForm htmlForm) {
		return Context.getService(HtmlFormEntryService.class).saveHtmlForm(htmlForm);
	}
	
	@Override
	public HtmlForm getByUniqueId(String uuid) {
		return Context.getService(HtmlFormEntryService.class).getHtmlFormByUuid(uuid);
	}
	
	@Override
	protected void delete(HtmlForm htmlForm, String reason, RequestContext context) throws ResponseException {
		// TODO
	}
	
	@Override
	public void purge(HtmlForm htmlForm, RequestContext context) throws ResponseException {
		Context.getService(HtmlFormEntryService.class).purgeHtmlForm(htmlForm);
	}
	
}
