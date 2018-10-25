package com.solanasystems.jstuff.form;

import javax.servlet.http.HttpServletRequest;

public class LocationForm extends CustomValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private String address;

	private String nameJA;

	private String addressJA;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNameJA() {
		return nameJA;
	}

	public void setNameJA(String nameJA) {
		this.nameJA = nameJA;
	}

	public String getAddressJA() {
		return addressJA;
	}

	public void setAddressJA(String addressJA) {
		this.addressJA = addressJA;
	}

	protected void setActionErrors(final HttpServletRequest request) {
		if(isBlank(name) && isBlank(nameJA)) addError(ERRORS_REQUIRED_NAME);
	}

}
