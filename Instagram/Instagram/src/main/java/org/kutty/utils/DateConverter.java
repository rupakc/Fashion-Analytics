package org.kutty.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

/** 
 * Utility class containing functions for conversion of one date format into another which is often necessary
 * @author Rupak Chakraborty
 * @for Kutty
 * @since 18 July, 2015
 *
 */ 

public class DateConverter {

	/** 
	 * Converts a given String date into a java date object
	 * @param date String containing the date which is to be converted
	 * @return java.util.Date object containing the date
	 */ 

	public static Date getJavaDate(String date) { 

		DateTime d = new DateTime(date.trim());

		return d.toDate();
	}

	/** 
	 * Returns the Julian day from a given String date
	 * @param date String containing the date which is to be converted
	 * @return Floating point number containing the date in Juilan Day format
	 */ 

	public static double getJulianDate(String date) { 

		DateTime d = new DateTime(date.trim());

		return DateTimeUtils.fromJulianDay(DateTimeUtils.toJulianDay(d.getMillis()))/1000;
	}

	/** 
	 * Converts a java date into a Julian Day 
	 * @param date java.util.Date object which is to be converted
	 * @return double containing the julian day number
	 */ 

	public static double getJulianDate(Date date) { 

		DateTime joda_date = new DateTime(date);

		return DateTimeUtils.fromJulianDay(DateTimeUtils.toJulianDay(joda_date.getMillis()))/1000.0;
	}

	/** 
	 * Converts a string date in the given format "MMMM d, yyyy hh:mm a" to a java date
	 * @param date String date which is to be converted for example April 06, 2015 07:23 PM
	 * @return java.util.Date object corresponding to the String date
	 * @throws ParseException
	 */ 

	public static Date getMarketingDate(String date) throws ParseException { 

		DateFormat format;
		Date java_date;

		format = new SimpleDateFormat("MMMM d, yyyy hh:mm a");
		java_date = format.parse(date);

		return java_date;
	}
	
	/** 
	 * Converts a julian day number into its corresponding java date
	 * @param julian date to convert
	 * @return Date object but not in standard java format
	 */ 
	
	public static Date getJavaDateFromJulian(double julian) { 

		Date java_date = new Date((long) (julian*1000));
		return java_date;
	}
}
