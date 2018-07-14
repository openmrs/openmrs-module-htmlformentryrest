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

import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Controller
@RequestMapping(value = "/rest/**")
public class HFERBaseRestController extends MainResourceController {
	
	@Override
	public String getNamespace() {
		//return RestConstants.VERSION_1 + "/htmlformentryrest";
		return RestConstants.VERSION_1;
	}
	
}
