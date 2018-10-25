package com.solanasystems.jstuff.form;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.solanasystems.jstuff.common.ErrorConstants;
import com.solanasystems.utils.form.CommonValidatorForm;
import com.solanasystems.utils.util.NumUtils;

public class CustomValidatorForm extends CommonValidatorForm implements ErrorConstants {

	public static int getInt(final String s) {
		return StringUtils.isNumeric(s) ? Integer.parseInt(s) : (int) NumUtils.convertToAbsLong(s);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected BigDecimal getBigDecimal(final String s) {
		return NumUtils.getBigDecimal(s);
	}

	protected String getValue(final String s) {
		return NumUtils.getValue(s);
	}

	protected String getString(final BigDecimal bd) {
		return NumUtils.getString(bd);
	}

	protected void setActionErrors(final HttpServletRequest request) {
	}

}
