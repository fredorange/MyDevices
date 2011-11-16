package com.orange.evabricks.continuaconnector.hl7process.impl.translation.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.datatype.DTM;

/**
 * used for converting type from HL7 to "pivot" type
 *
 */
public class HapiTypeAdapter {

  private static final Logger logger = Logger.getLogger(HapiTypeAdapter.class);

  /**
   * transform a DTM date into a java.util.Date
   *
   * @param date DTM specified in HL7 (Health Level seven)
   * @return the corresponding date in the format XMLGregorianCalendar
   * @throws DataTypeException
   */
  public static Date getDate(DTM date)
          throws DataTypeException {

//		logger.info("DTMdate : " + date.toString());
//		logger.info("DTMdate getYear : " + date.getYear());
//		logger.info("DTMdate getMonth : " + date.getMonth());
//		logger.info("DTMdate getDay : " + date.getDay());
//		logger.info("DTMdate getHour : " + date.getHour());
//		logger.info("DTMdate getMinute : " + date.getMinute());
//		logger.info("DTMdate getSecond : " + date.getSecond());
//		logger.info("DTMdate getFractSecond : " + date.getFractSecond());
//		logger.info("DTMdate getGMTOffset : " + date.getGMTOffset());
//		logger.info("DTMdate getValue : " + date.getValue());
//		logger.info("DTMdate getName : " + date.getName());
//		logger.info("DTMdate getVersion : " + date.getVersion());
//		logger.info("DTMdate getExtraComponents : " + date.getExtraComponents());
//		logger.info("DTMdate getMessage : " + date.getMessage());

    int year, month, day, hour, minute, second, gMTOffset;


    year = date.getYear();
    month = date.getMonth();
    day = date.getDay();
    hour = date.getHour();
    minute = date.getMinute();
    second = date.getSecond();
    gMTOffset = date.getGMTOffset(); 							// ex : 130 for +0130 (means 1 hour and 30 minutes)

    int minutsOffset = gMTOffset % 100; 						// in the example : 1
    int hoursOffset = (gMTOffset - minutsOffset) / 100; 		// in the example : 30
    int offsetInSeconds = minutsOffset * 60 + hoursOffset * 3600; 	// calculate the offset in seconds
    logger.debug("minutsOffset : " + minutsOffset);
    logger.debug("hoursOffset : " + hoursOffset);
    logger.debug("offsetInSeconds : " + offsetInSeconds);

    if (month == 0 || day == 0) {
      return null;
    }

    // we will set the date into GMT format
    Calendar calGmt = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    calGmt.set(Calendar.YEAR, year);
    calGmt.set(Calendar.MONTH, month - 1);						// 0=january, 1=february, etc
    calGmt.set(Calendar.DAY_OF_MONTH, day);
    calGmt.set(Calendar.HOUR_OF_DAY, hour);
    calGmt.set(Calendar.MINUTE, minute);
    calGmt.set(Calendar.SECOND, second);
    calGmt.add(Calendar.SECOND, -offsetInSeconds);				// shift the date in order to be in GMT. Example : if the date was in GMT+1 ==> to be in GMT, we have to remove 1 hour

    logger.debug("DTMdate=" + date + " converted to javaDate=" + calGmt.getTime());

    return calGmt.getTime();
  }
}
