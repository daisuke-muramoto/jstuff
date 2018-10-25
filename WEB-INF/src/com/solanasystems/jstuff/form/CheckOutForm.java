package com.solanasystems.jstuff.form;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class CheckOutForm extends CustomValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private int roleId;

	private int itemId;

	private int clientId;
	
	private int supplierId;

	private int locationId;
	
	private int quantity;

	private String comment;
	
	private String checkOutDate;
	
	private String today;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getToday() {
		final Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.today = sdf.format(date);
		return today;
	}

	protected void setActionErrors(final HttpServletRequest request) {
		if (quantity == 0 || quantity < 0)
			addError(ERRORS_REQUIRED_QUANTITY);
	}

}
