package org.openmrs.module.htmlformentryrest.web.controller;

import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.annotation.AllowDirectAccess;

import java.util.Date;
import java.util.Set;

public class EncounterWrapper {

	public static final long serialVersionUID = 2L;
	private Integer encounterId;
	private Date encounterDatetime;
	private Patient patient;
	private Integer patientId;
	private Location location;
	private Form form;
	private EncounterType encounterType;
	private Set<Order> orders;
	@AllowDirectAccess
	private Set<Obs> obs;
	private Visit visit;

}
