/**
 * 
 */
package org.openmrs.module.htmlformentryrest.web.controller;

import org.openmrs.BaseOpenmrsData;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class DummyHtmlForm extends BaseOpenmrsData {
	
	private Integer formId;
	
	/* (non-Javadoc)
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	@Override
	public Integer getId() {
		return formId;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer formId) {
		this.formId = formId;
	}
	
}
