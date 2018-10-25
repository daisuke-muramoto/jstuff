package com.solanasystems.jstuff.om;

public class Category {

	private int id;

	private int companyId;

	private String name;

	private String description;

	private String nameJA;

	private String descriptionJA;

	private boolean canDelete;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

}
