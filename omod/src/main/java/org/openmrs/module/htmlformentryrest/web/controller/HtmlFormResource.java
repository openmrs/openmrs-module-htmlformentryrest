/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.htmlformentryrest.web.controller;

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
@Resource(name = RestConstants.VERSION_1 + "/htmlformentryrest/htmlform", supportedClass = DummyHtmlForm.class, supportedOpenmrsVersions = {
        "1.11.*", "1.12.*", "2.0.*", "2.1.*" })
public class HtmlFormResource extends DelegatingCrudResource<DummyHtmlForm> {
	
	@Override
	public DummyHtmlForm newDelegate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public DummyHtmlForm save(DummyHtmlForm delegate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public DummyHtmlForm getByUniqueId(String uniqueId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void delete(DummyHtmlForm delegate, String reason, RequestContext context) throws ResponseException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void purge(DummyHtmlForm delegate, RequestContext context) throws ResponseException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
