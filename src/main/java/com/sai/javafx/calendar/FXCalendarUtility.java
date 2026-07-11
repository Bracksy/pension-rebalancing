package com.sai.javafx.calendar;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 
 * @author Sai.Dandem
 * @see http://www.e-zest.net/blog/calendar-control-in-javafx-2-0/
 */
public class FXCalendarUtility {
	private final static String TIME_SEC_PATTERN = "HH:mm:ss"; //$NON-NLS-1$

	private final static String DATE_PATTERN = "dd.MM.yyyy"; //$NON-NLS-1$

	private final static String WEEKDAY_PATTERN = "EE"; //$NON-NLS-1$

	private final static String DATE_TIME_SEC_PATTERN = DATE_PATTERN + " " + TIME_SEC_PATTERN; //$NON-NLS-1$

	private String[] mShortestWeekDays; // {"","S","M","T","W","T","F","S"}
	private String[] mShortMonths; // {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",""}
	private String[] mMonths; // {"January","February","March","April","May","June","July","August","September","October","November","December",""}

	/**
	 * 
	 * Get the instance of the class Calendar
	 *
	 * @param <name>
	 *            <description>
	 * @return <description>
	 * @exception <name>
	 *                <description>
	 *
	 * @see <classname>#<method name>
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * Returns current calendar
	 */
	public static Calendar getCurrentDateCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c;
	}

	/**
	 * Returns week day string
	 * 
	 * @param pDate
	 *            Date to transform
	 * @return String string representation of the week day
	 */
	public static String getDayStr(final Date pDate, final Locale pLocale) {
		String dayStr = ""; //$NON-NLS-1$
		if (pDate != null) {
			String dayString = new SimpleDateFormat(WEEKDAY_PATTERN, pLocale).format(pDate);
			while (dayString.length() < 2) {
				dayString += "-"; //$NON-NLS-1$
			}
			if (dayString.length() > 2) {
				dayString = dayString.substring(0, 2);
			}
			dayStr = dayString.substring(0, 1).toUpperCase() + dayString.substring(1, 2);
		}
		return dayStr;
	}

	/**
	 * Returns week day str
	 * 
	 * @param dateStr
	 */
	public static String getDayStr(String dateStr, Locale locale) throws ParseException {
		String dayStr = ""; //$NON-NLS-1$
		if (dateStr != null && !dateStr.isEmpty()) {
			Date textDate = FXCalendarUtility.getDate(dateStr, locale);
			dayStr = FXCalendarUtility.getDayStr(textDate, locale); //$NON-NLS-1$
		}
		return dayStr;
	}

	/**
	 * Returns the Date transformation of an input string.
	 * 
	 * Valid input string formats: - dd.MM.yyyy hh:mm:ss - dd.MM.yyyy
	 * 
	 * @param pInputString
	 *            string to transform into Date
	 * @param pLocale
	 *            Locale to use
	 * 
	 * @return Date Date transformation
	 * 
	 * @throws ParseException
	 *             if transformation fails
	 */
	public static Date getDate(final String pInputString, final Locale pLocale) throws ParseException {
		if (pInputString != null && !pInputString.isEmpty()) {
			try {
				return new SimpleDateFormat(DATE_TIME_SEC_PATTERN, pLocale).parse(pInputString);
			} catch (ParseException lExc) {
				return new SimpleDateFormat(DATE_PATTERN, pLocale).parse(pInputString);
			}

		}
		return null;
	}

	/**
	 * Returns first day of month
	 * 
	 * Valid input string formats: - dd.MM.yyyy hh:mm:ss - dd.MM.yyyy
	 * 
	 * @param day
	 *            1..31
	 * @param month
	 *            1..12
	 * @param year
	 *            1900...
	 * @return Date first day of month as date
	 */
	public static Calendar getDate(int day, int month, int year) {
		Calendar lCal = new GregorianCalendar();

		// year
		if (year < 0) {
			year = lCal.get(Calendar.YEAR);
		}

		// month
		if (month > 12) {
			month = 12;
		}

		lCal.set(Calendar.MONTH, month);

		if (year < 0) {
			year = lCal.get(Calendar.YEAR);
		}

		// year
		lCal.set(Calendar.YEAR, year);

		// day
		lCal.set(Calendar.DATE, 1);

		return lCal;
	}

	/**
	 * Returns a valid date string (dd.MM.yyyy).
	 * 
	 * A given year smaller than 100 is modified: - A value of 2000 is added to
	 * a year smaller than 80. - A value of 1900 is added to a year in the range
	 * of 80..99.
	 *
	 * Day and month overflow: - If the given day is greater than the days of a
	 * month, the days will be added to next month. - If the given month is
	 * greater than the months of a year, the months will be added to next year.
	 * 
	 * @param pDay
	 *            day of month 1..31
	 * @param pMonth
	 *            month of year 1..12
	 * @param pYear
	 *            year
	 * @param pLocale
	 *            Locale to use
	 * @return String valid date string
	 */
	public static String getDateStr(final int pDay, final int pMonth, final int pYear, final Locale pLocale) {
		int lYear = pYear;

		if (pYear < 80) {
			lYear += 2000;
		} else if (pYear < 100) {
			lYear += 1900;
		}

		Date lDate = new GregorianCalendar(lYear, pMonth - 1, pDay).getTime();

		return new SimpleDateFormat(DATE_PATTERN, pLocale).format(lDate);
	}

	/**
	 * Resets the list of weekday short names
	 * 
	 * @param pLocale
	 *            Locale used for names
	 */
	public void resetShortestWeekDays(final Locale pLocale) {
		mShortestWeekDays = null;
		getShortestWeekDays(pLocale);
	}

	/**
	 * Returns list of weekday short names
	 * 
	 * @param pLocale
	 *            Locale used for names
	 * 
	 * @return String array of weekday short names
	 */
	public String[] getShortestWeekDays(final Locale pLocale) {
		if (mShortestWeekDays == null || mShortestWeekDays.length == 0) {
			mShortestWeekDays = getDayNames("xs", pLocale); //$NON-NLS-1$
			// If Monday is first day of week.
			if (Calendar.getInstance(pLocale).getFirstDayOfWeek() == 2) {
				String dum = mShortestWeekDays[1];
				for (int i = 1; i < 7; i++) {
					mShortestWeekDays[i] = mShortestWeekDays[i + 1];
				}
				mShortestWeekDays[7] = dum;
			}
		}
		return mShortestWeekDays;
	}

	/**
	 * Resets the list of month short names
	 * 
	 * @param pLocale
	 *            Locale used for names
	 */
	public void resetShortMonths(final Locale pLocale) {
		mShortMonths = null;
		getShortMonths(pLocale);
	}

	/**
	 * Returns list of month short names
	 * 
	 * @param pLocale
	 *            Locale used for names
	 * @return String array of month short names
	 */
	public String[] getShortMonths(final Locale pLocale) {
		if (mShortMonths == null || mShortMonths.length == 0) {
			mShortMonths = getMonthNames("s", pLocale); //$NON-NLS-1$
		}
		return mShortMonths;
	}

	/**
	 * Resets the list of month names
	 * 
	 * @param pLocale
	 *            Locale used for names
	 */
	public void resetMonths(final Locale pLocale) {
		mMonths = null;
		getMonths(pLocale);
	}

	/**
	 * Returns list of month names
	 * 
	 * @param pLocale
	 *            Locale used for names
	 * 
	 * @return String array of month names
	 */
	public String[] getMonths(Locale pLocale) {
		if (mMonths == null || mMonths.length == 0) {
			// Always use english names for month
			mMonths = getMonthNames(null, pLocale);
		}
		return mMonths;
	}

	/**
	 * Returns list of weekday names
	 * 
	 * @param pLocale
	 *            Locale used for names
	 * @param pType
	 *            s,xs for small, else full name
	 * 
	 * @return String array of weekday names
	 */
	private String[] getDayNames(String pType, Locale pLocale) {
		if (pType != null && pType.equalsIgnoreCase("xs")) { //$NON-NLS-1$
			String[] days = new DateFormatSymbols(pLocale).getShortWeekdays();
			String[] xsDays = new String[days.length];
			for (int i = 0; i < days.length; i++) {
				xsDays[i] = (String) ((days[i].equals("")) ? days[i] : days[i].charAt(0) + ""); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return xsDays;
		}
		if (pType != null && pType.equalsIgnoreCase("s")) { //$NON-NLS-1$
			return new DateFormatSymbols(pLocale).getShortWeekdays();
		} else {
			return new DateFormatSymbols(pLocale).getWeekdays();
		}
	}

	/**
	 * Returns list of month names
	 * 
	 * @param pType
	 *            s for short, else full names
	 * @param pLocale
	 *            Locale used for names
	 * 
	 * @return String array of weekday names
	 */
	private String[] getMonthNames(String pType, Locale pLocale) {
		if (pType != null && pType.equalsIgnoreCase("s")) { //$NON-NLS-1$
			return new DateFormatSymbols(pLocale).getShortMonths();
		} else {
			return new DateFormatSymbols(pLocale).getMonths();
		}
	}

	/**
	 * Sets color of a node
	 */
	public static void setBaseColorToNode(Node node, Color baseColor) {
		node.setStyle("-fx-base:" + rgbToHex(baseColor) + ";"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Translates rgb to hex
	 */
	public static String rgbToHex(Color color) {
		int i = (int) Math.round((double) color.getRed() * 255D);
		int j = (int) Math.round((double) color.getGreen() * 255D);
		int k = (int) Math.round((double) color.getBlue() * 255D);
		return "#" + toHex(i) + toHex(j) + toHex(k); //$NON-NLS-1$
	}

	/**
	 * Translates int value to hex
	 */
	private static String toHex(int code) {
		String str = "0123456789ABCDEF"; //$NON-NLS-1$
		return str.charAt(code / 16) + "" + str.charAt(code % 16); //$NON-NLS-1$
	}
}
