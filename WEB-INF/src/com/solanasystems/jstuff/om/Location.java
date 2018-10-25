package com.solanasystems.jstuff.om;

public class Location {

	private int id;

	private int companyId;

	private String name;

	private String address;

	private String nameJA;

	private String addressJA;

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

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

}
