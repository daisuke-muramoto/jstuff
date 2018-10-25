package com.solanasystems.jstuff.om;

public class ItemMaster {

	private int id;

	private int companyId;

	private int categoryId;

	private String name;

	private String code;

	private String description;

	private String nameJA;

	private String descriptionJA;

	private String categoryName;

	private String categoryNameJA;
	
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryNameJA() {
		return categoryNameJA;
	}

	public void setCategoryNameJA(String categoryNameJA) {
		this.categoryNameJA = categoryNameJA;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

}
