/**
 * 
 */
package org.openmrs.module.htmlformentryrest.web.controller.rest;

import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Controller
@RequestMapping("/rest/" + RestConstants.VERSION_1 + "/htmlformentryrest")
public class HtmlformentryrestRestController extends MainResourceController {
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return RestConstants.VERSION_1 + "/htmlformentryrest";
	}
	
}
