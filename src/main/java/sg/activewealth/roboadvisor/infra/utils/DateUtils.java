package sg.activewealth.roboadvisor.infra.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class DateUtils {
	
	private Logger logger = Logger.getLogger(DateUtils.class);
		
	private static DateUtils me;

	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a");

	public static DateUtils getInstance() {
		if (me == null) me = new DateUtils();

		return me;
	}

	// ************************************** DATE METHODS ****************************************
	public String buildStringFromCalendarDDMONYYYY(Calendar date) {
		StringBuffer sb = new StringBuffer();

		if (date != null) {
			sb.append(date.get(Calendar.DAY_OF_MONTH));
			sb.append(" ");
			sb.append(getMon(date.get(Calendar.MONTH)));
			sb.append(" ");
			sb.append(date.get(Calendar.YEAR));
		}

		return sb.toString();
	}

	public String getMon(int month) {
		switch (month) {
			case 0:
				return "Jan";
			case 1:
				return "Feb";
			case 2:
				return "Mar";
			case 3:
				return "Apr";
			case 4:
				return "May";
			case 5:
				return "Jun";
			case 6:
				return "Jul";
			case 7:
				return "Aug";
			case 8:
				return "Sep";
			case 9:
				return "Oct";
			case 10:
				return "Nov";
			case 11:
				return "Dec";
		}

		return "";
	}

	public String[] buildYearRange(String year) {
		String[] ret = new String[2];

		int yearInt = Integer.parseInt(year);

		// build start
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, yearInt);

		ret[0] = this.convertDDMMYYtoYYMMDD(this.buildStringFromCalendar(cal));

		// roll 1 year. and minus 1 day
		cal.roll(Calendar.YEAR, true);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		ret[1] = this.convertDDMMYYtoYYMMDD(this.buildStringFromCalendar(cal));

		return ret;
	}

	public String[] buildMonthRange(String month, String year) {
		String[] ret = new String[2];

		int monthInt = Integer.parseInt(month), yearInt = Integer.parseInt(year);

		// build start
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, monthInt - 1);
		cal.set(Calendar.YEAR, yearInt);

		ret[0] = this.convertDDMMYYtoYYMMDD(this.buildStringFromCalendar(cal));

		// get last day of month
		if (monthInt == 1 || monthInt == 3 || monthInt == 5 || monthInt == 7 || monthInt == 8 || monthInt == 10 || monthInt == 12) cal.set(Calendar.DAY_OF_MONTH, 31);
		if (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11) cal.set(Calendar.DAY_OF_MONTH, 30);
		// if feb, add month by 1, then roll back 1 day.
		if (monthInt == 2) {
			cal.roll(Calendar.MONTH, true);
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		ret[1] = this.convertDDMMYYtoYYMMDD(this.buildStringFromCalendar(cal));

		return ret;
	}

	public String convertDDMMYYtoYYMMDDDelimDash(String DDMMYY) {
		String ret = "";

		if (DDMMYY != null) {
			StringTokenizer st = new StringTokenizer(DDMMYY, "/");

			while (st.hasMoreTokens()) {
				String dd = st.nextToken();
				String mm = st.nextToken();
				String yy = st.nextToken();
				ret = yy + "-" + mm + "-" + dd;
			}
		}

		return ret;
	}

	public String convertDDMMYYtoYYMMDD(String DDMMYY) {
		String ret = "";

		if (DDMMYY != null) {
			StringTokenizer st = new StringTokenizer(DDMMYY, "/");

			while (st.hasMoreTokens()) {
				String dd = st.nextToken();
				String mm = st.nextToken();
				String yy = st.nextToken();
				ret = yy + "/" + mm + "/" + dd;
			}
		}

		return ret;
	}

	public String convertDDMMYYtoMMDDYY(String DDMMYY) {
		String ret = "";

		if (DDMMYY != null) {
			StringTokenizer st = new StringTokenizer(DDMMYY, "/");

			while (st.hasMoreTokens()) {
				String dd = st.nextToken();
				String mm = st.nextToken();
				String yy = st.nextToken();
				ret = mm + "/" + dd + "/" + yy;
			}
		}

		return ret;
	}

	public Calendar buildCalendarFromStringYYYYMMDD(String yyyyMMdd) {
		Calendar ret = null;

		StringTokenizer st = new StringTokenizer(yyyyMMdd, "/");
		while (st.hasMoreTokens()) {
			ret = new GregorianCalendar();

			ret.set(Calendar.YEAR, Integer.parseInt(st.nextToken()));
			ret.set(Calendar.MONTH, Integer.parseInt(st.nextToken()) - 1);
			ret.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st.nextToken()));
		}

		return ret;
	}

	public Calendar buildCalendarFromString(String ddMMyyyy) {
		Calendar ret = null;
		try {
			StringTokenizer st = new StringTokenizer(ddMMyyyy, "/");
			while (st.hasMoreTokens()) {
				ret = new GregorianCalendar();
				// nullify timings
				ret.set(Calendar.HOUR_OF_DAY, 0);
				ret.set(Calendar.MINUTE, 0);
				ret.set(Calendar.SECOND, 0);

				ret.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st.nextToken()));
				ret.set(Calendar.MONTH, Integer.parseInt(st.nextToken()) - 1);
				ret.set(Calendar.YEAR, Integer.parseInt(st.nextToken()));
			}
		}
		catch (NumberFormatException e) {
			// do nothing. just return null.
		}

		return ret;
	}

	public String buildStringFromCalendarYYYYMMDD(Calendar date) {
		if (date != null)
			return date.get(Calendar.YEAR) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH);
		else return "";
	}

	public String buildStringFromCalendarDateTime(Calendar cal) {
		return buildStringFromCalendar(cal) + ", " + NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.HOUR_OF_DAY), 2) + ":" + NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.MINUTE), 2) + ":"
				+ NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.SECOND), 2) + "hrs";
	}


	public Date buildDateFromString(String date) {
		try {
			return DATE_FORMAT.parse(date);
		}
		catch (ParseException e) {
			return null;
		}
	}
	public String buildStringFromDate(Date date) {
		return DATE_FORMAT.format(date);
	}

	public String buildStringFromCalendarYYYY(Calendar date) {
		if (date != null)
			return NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.DAY_OF_MONTH), 2) + "/" + NumberUtils.getInstance().buildDigitNumber((date.get(Calendar.MONTH) + 1), 2) + "/"
					+ NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.YEAR), 4);
		else return "";
	}
	
	public String buildStringFromCalendar(Calendar date) {
		if (date != null)
			return NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.DAY_OF_MONTH), 2) + "/" + NumberUtils.getInstance().buildDigitNumber((date.get(Calendar.MONTH) + 1), 2) + "/"
					+ NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.YEAR), 2);
		else return "";
	}
	
	public String buildStringFromCalendarDelimDashYYYYMMDD(Calendar date) {
		if (date != null)
			return date.get(Calendar.YEAR) + "-" + NumberUtils.getInstance().buildDigitNumber((date.get(Calendar.MONTH) + 1), 2) + "-" + NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.DAY_OF_MONTH), 2);
		else return "";
	}
	
	public String buildStringFromCalendarDateTimeDelimDashYYYYMMDD(Calendar cal) {
		return buildStringFromCalendarDelimDashYYYYMMDD(cal) + " " + NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.HOUR_OF_DAY), 2) + ":" + NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.MINUTE), 2) + ":"
				+ NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.SECOND), 2);
	}
	
	public Date buildTimeFromString(String time) {
		try {
			return TIME_FORMAT.parse(time);
		}
		catch (ParseException e) {
			return null;
		}
	}
	
	public String buildStringFromTime(Time time){
		Date date = new Date(time.getTime());
		return TIME_FORMAT.format(date);
	}
	
	public Date buildNullableDateFromString(String date) {
		try {
			if(null == date) {
				return null;
			}
			return DATE_FORMAT.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	
}
