package org.openmrs.module.htmlformentryrest.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + "/htmlformentryrest/htmlform", supportedClass = HtmlForm.class, supportedOpenmrsVersions = {
        "1.11.*", "1.12.*", "2.0.*", "2.1.*" })
public class HtmlFormResource extends DelegatingCrudResource<HtmlForm> {
	
	@Override
	public HtmlForm newDelegate() {
		return new HtmlForm();
	}
	
	@Override
	public HtmlForm save(HtmlForm delegate) {
		return Context.getService(HtmlFormEntryService.class).saveHtmlForm(delegate);
	}
	
	@Override
	public HtmlForm getByUniqueId(String uniqueId) {
		return (HtmlForm) Context.getService(HtmlFormEntryService.class).getItemByUuid(HtmlForm.class, uniqueId);
	}
	
	@Override
	protected void delete(HtmlForm delegate, String reason, RequestContext context) throws ResponseException {
		// TODO
	}
	
	@Override
	public void purge(HtmlForm delegate, RequestContext context) throws ResponseException {
		Context.getService(HtmlFormEntryService.class).purgeHtmlForm(delegate);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = null;
		
		if (rep instanceof RefRepresentation) {
			description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addSelfLink();
			return description;
		} else if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("name");
			description.addProperty("xmlData");
			description.addProperty("description");
			description.addSelfLink();
			if (rep instanceof DefaultRepresentation) {
				description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			}
		}
		return description;
	}
}
