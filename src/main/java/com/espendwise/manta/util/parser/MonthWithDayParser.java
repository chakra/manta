package com.espendwise.manta.util.parser;


import org.apache.log4j.Logger;

import com.espendwise.manta.util.Utility;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MonthWithDayParser extends DateParser {

    private static final Logger logger = Logger.getLogger(MonthWithDayParser.class);

    @Override
    public Date parse(String parserValue, Object... params) throws AppParserException {

        logger.debug("parse()=> BEGIN" +
                ", parse: " + parserValue +
                ", params: " + Arrays.asList(params));

        Integer year = (Integer) params[0];
        String dayWithMonthPattern = (String) params[1];

        int yearForParse = (year == 0 ? 2001 : year);

        Date date = super.parse(parserValue + ("/" + (yearForParse)), dayWithMonthPattern + "/yyyy");

        Calendar calendar = new GregorianCalendar();
        calendar.setLenient(false);
        calendar.setTime(date);

        Calendar mmDDCalendar = new GregorianCalendar();
        mmDDCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        mmDDCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        if (year > 0) {
            mmDDCalendar.set(Calendar.YEAR, year);
        }
        mmDDCalendar.setLenient(false);
        Date returnDate = Utility.setToMidnight(mmDDCalendar.getTime());
        logger.debug("parse()=> END, date: " + returnDate);
        return returnDate;
    }

}
