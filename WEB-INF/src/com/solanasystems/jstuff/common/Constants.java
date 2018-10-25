package com.solanasystems.jstuff.common;

import static com.solanasystems.utils.common.Constants.CHAR_CO;
import static com.solanasystems.utils.common.Constants.CHAR_CO_JA;
import static com.solanasystems.utils.common.Constants.CHAR_SP;
import static com.solanasystems.utils.common.Constants.CHAR_SP_JA;

public interface Constants {

	// actions
	public static final int EXPIRED = -1;
	public static final int CANCEL = 0;
	public static final int READ = 1;
	public static final int SAVE = 2;
	public static final int DELETE = 3;

	public static final String ACTION_READ = "read";
	public static final String ACTION_SAVE = "save";
	public static final String ACTION_DELETE = "delete";

	// action forwards
	public static final String FORWARD_ROLE_ERROR = "roleError";
	public static final String FORWARD_EDIT = "edit";

	// request params
	public static final String REQ_USER_ID = "userId";
	public static final String REQ_USER_NAME = "userName";
	public static final String REQ_ID = "id";
	public static final String REQ_COMPANY_ID = "companyId";

	// attributes
	public static final String SES_LOGIN_USER = "loginUser";
	public static final String SES_IS_SET_USER_COMPANY_LIST = "isSetUserCompanyList";
	public static final String SES_USER_COMPANY_LIST = "userCompanyList";

	public static final String DATA_IS_LOGGED_IN = "isLoggedIn";
	public static final String DATA_USER_LIST = "userList";
	public static final String DATA_USER_ID = REQ_USER_ID;
	public static final String DATA_USER_FORM = "userForm";
	public static final String DATA_ROLE_LIST = "roleList";

	public static final String DATA_COMPANY_LIST = "companyList";
	public static final String DATA_COMPANY_FORM = "companyForm";
	public static final String DATA_CHECKOUT_LIST = "checkOutList";
	public static final String DATA_CHECKOUT_FORM = "checkOutForm";
	public static final String DATA_LOCATION_LIST = "locationList";
	public static final String DATA_LOCATION_FORM = "locationForm";
	public static final String DATA_LOG_LIST = "logList";
	public static final String DATA_CATEGORY_LIST = "categoryList";
	public static final String DATA_CATEGORY_FORM = "categoryForm";
	public static final String DATA_CHECKIN_LIST = "checkInList";
	public static final String DATA_CHECKIN_FORM = "checkInForm";
	public static final String DATA_ITEM_MASTER_LIST = "itemMaster";
	public static final String DATA_ITEM_MASTER_FORM = "itemMasterForm";
	public static final String DATA_SUPPLIER_LIST = "supplierList";
	public static final String DATA_SUPPLIER_FORM = "supplierForm";
	public static final String DATA_CLIENT_LIST = "clientList";
	public static final String DATA_CLIENT_FORM = "clientForm";

	// separators
	public static final String SEPARATORS = new String(new char[] { CHAR_SP, CHAR_CO, CHAR_SP_JA, CHAR_CO_JA });

	// password
	public static final int PASSWORD_MIN_LENGTH = 9;
	public static final String PASSWORD_PATTERN = "^(?=.*[\\d])(?=.*[a-zA-Z_-]).{" + PASSWORD_MIN_LENGTH + ",}$";

	// message key
	public static final String MES_USER = "mes.user";
	public static final String MES_CATEGORY = "mes.category";
	public static final String MES_CLIENT = "mes.client";

	public static final String SAVE_MES_COMPANY = "sav.company";
	public static final String SAVE_MES_CHECKOUT = "sav.checkOut";
	public static final String SAVE_MES_LOCATION = "sav.location";
	public static final String SAVE_MES_CHECKIN = "sav.checkIn";
	public static final String SAVE_MES_ITEM_MASTER = "sav.itemMaster";
	public static final String SAVE_MES_SUPPLIER = "sav.supplier";

	public static final String DEL_MES_COMPANY = "del.company";
	public static final String DEL_MES_CHECKOUT = "del.checkOut";
	public static final String DEL_MES_LOCATION = "del.location";
	public static final String DEL_MES_CHECKIN = "del.checkIn";
	public static final String DEL_MES_ITEM_MASTER = "del.itemMaster";
	public static final String DEL_MES_SUPPLIER = "del.supplier";
}
