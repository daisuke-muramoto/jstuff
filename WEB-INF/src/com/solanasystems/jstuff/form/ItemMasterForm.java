package com.solanasystems.jstuff.form;

import javax.servlet.http.HttpServletRequest;

public class ItemMasterForm extends CustomValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private int categoryId;
	
	private String name;

	private String code;

	private String description;
	
	private String nameJA;

	private String descriptionJA;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNameJA() {
		return nameJA;
	}

	public void setNameJA(String nameJA) {
		this.nameJA = nameJA;
	}

	public String getDescriptionJA() {
		return descriptionJA;
	}

	public void setDescriptionJA(String descriptionJA) {
		this.descriptionJA = descriptionJA;
	}

	protected void setActionErrors(final HttpServletRequest request) {
		if(isBlank(name) && isBlank(nameJA)) addError(ERRORS_REQUIRED_NAME);
	}

}
