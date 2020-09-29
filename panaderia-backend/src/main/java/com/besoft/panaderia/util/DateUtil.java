package com.besoft.panaderia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String formatSlashDDMMYYYY(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(ConstanteUtil.slash_DDMMYYYY);
		if (date != null) {
			return sdf.format(date);
		} else {
			return null;
		}
	}

	public static Date parseSlashDDMMYYYY(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(ConstanteUtil.slash_DDMMYYYY);
		if (date != null && date.toUpperCase() != "NULL") {
			return sdf.parse(date);
		} else {
			return null;
		}
	}
	
	public static Date parseGuionyyyyMMddHHmmss(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(ConstanteUtil.guion_yyyyMMddHHmmss);
		if (date != null && date.toUpperCase() != "NULL") {
			return sdf.parse(date);
		} else {
			return null;
		}
	}

	public static String formatGuionDDMMYYYY(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(ConstanteUtil.guion_DDMMYYYY);
		if (date != null) {
			return sdf.format(date);
		} else {
			return null;
		}
	}

	public static String formatGuionYYYYMMDD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(ConstanteUtil.guion_YYYYMMDD);
		if (date != null) {
			return sdf.format(date);
		} else {
			return null;
		}
	}
}
