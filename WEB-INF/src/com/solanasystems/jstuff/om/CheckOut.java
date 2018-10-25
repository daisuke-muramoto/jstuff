package com.solanasystems.jstuff.om;

import java.util.Date;

public class CheckOut {

	private int id;

	private int companyId;

	private int roleId;

	private int itemId;

	private int clientId;

	private int supplierId;

	private int categoryId;

	private int locationId;
	
	private int quantity;

	private String comment;

	private String itemName;

	private String itemNameJA;

	private String clientName;

	private String clientNameJA;

	private String supplierName;

	private String supplierNameJA;

	private String categoryName;

	private String categoryNameJA;

	private String locationName;

	private String locationNameJA;
	
	private Date date;
	
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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemNameJA() {
		return itemNameJA;
	}

	public void setItemNameJA(String itemNameJA) {
		this.itemNameJA = itemNameJA;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientNameJA() {
		return clientNameJA;
	}

	public void setClientNameJA(String clientNameJA) {
		this.clientNameJA = clientNameJA;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierNameJA() {
		return supplierNameJA;
	}

	public void setSupplierNameJA(String supplierNameJA) {
		this.supplierNameJA = supplierNameJA;
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

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationNameJA() {
		return locationNameJA;
	}

	public void setLocationNameJA(String locationNameJA) {
		this.locationNameJA = locationNameJA;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	
}
