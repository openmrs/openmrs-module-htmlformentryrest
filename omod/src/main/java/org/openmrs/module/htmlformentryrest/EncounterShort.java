package org.openmrs.module.htmlformentryrest;

import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Visit;

import java.util.Date;
import java.util.Set;

public class EncounterShort {
	
	private Integer encounterId;
	
	private Date encounterDatetime;
	
	//private Patient patient;
	
	private Integer patientId;
	
	private Location location;
	
	private Form form;
	
	private EncounterType encounterType;
	
	private Set<Order> orders;
	
	private Set<Obs> obs;
	
	private Visit visit;
	
	public EncounterShort(Integer encounterId, Date encounterDatetime, Patient patient, Integer patientId,
	    Location location, Form form, EncounterType encounterType, Set<Order> orders, Set<Obs> obs, Visit visit) {
		this.encounterId = encounterId;
		this.encounterDatetime = encounterDatetime;
		//	this.patient = patient;
		this.patientId = patientId;
		this.location = location;
		this.form = form;
		this.encounterType = encounterType;
		this.orders = orders;
		this.obs = obs;
		this.visit = visit;
	}
	
	public Date getEncounterDatetime() {
		return encounterDatetime;
	}
	
	public EncounterType getEncounterType() {
		return encounterType;
	}
	
	public Form getForm() {
		return form;
	}
	
	public Integer getEncounterId() {
		return encounterId;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public Location getLocation() {
		return location;
	}
	
	//public Patient getPatient() {
	//	return patient;
	//}
	
	public Set<Obs> getObs() {
		return obs;
	}
	
	public Set<Order> getOrders() {
		return orders;
	}
	
	public Visit getVisit() {
		return visit;
	}
	
	public void setEncounterDatetime(Date encounterDatetime) {
		this.encounterDatetime = encounterDatetime;
	}
	
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	
	public void setEncounterType(EncounterType encounterType) {
		this.encounterType = encounterType;
	}
	
	public void setForm(Form form) {
		this.form = form;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void setObs(Set<Obs> obs) {
		this.obs = obs;
	}
	
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	public void setPatient(Patient patient) {
		//	this.patient = patient;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	
}
