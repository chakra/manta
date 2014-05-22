package com.espendwise.manta.util;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PhysicalInventoryPeriod implements Serializable{

    public static final String DATE_FORMAT_PATTERN = Constants.SYSTEM_DATE_PATTERN;

    private Date startDate;
    private Date endDate;
    private Date absoluteFinishDate;

    public PhysicalInventoryPeriod(Date startDate, Date endDate, Date absoluteFinishDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.absoluteFinishDate = absoluteFinishDate;
    }

    public PhysicalInventoryPeriod() {
        this(null, null, null);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getAbsoluteFinishDate() {
        return absoluteFinishDate;
    }

    public void setAbsoluteFinishDate(Date absoluteFinishDate) {
        this.absoluteFinishDate = absoluteFinishDate;
    }

    public String getStartDateAsString() {
        return getStartDateAsString(DATE_FORMAT_PATTERN);
    }

    public String getStartDateAsString(String dateFormatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        return dateFormat.format(startDate);
    }

    public String getEndDateAsString() {
        return getEndDateAsString(DATE_FORMAT_PATTERN);
    }

    public String getEndDateAsString(String dateFormatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        return dateFormat.format(endDate);
    }

    public String getAbsoluteFinishDateAsString() {
        return getAbsoluteFinishDateAsString(DATE_FORMAT_PATTERN);
    }

    public String getAbsoluteFinishDateAsString(String dateFormatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        return dateFormat.format(absoluteFinishDate);
    }

    public String toString() {

        StringBuilder buffer = new StringBuilder();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        if (startDate == null) {
            buffer.append(getDateStringForNull());
        } else {
            buffer.append(dateFormat.format(startDate));
        }

        buffer.append(",");

        if (endDate == null) {
            buffer.append(getDateStringForNull());
        } else {
            buffer.append(dateFormat.format(endDate));
        }

        buffer.append(",");

        if (absoluteFinishDate == null) {
            buffer.append(getDateStringForNull());
        } else {
            buffer.append(dateFormat.format(absoluteFinishDate));
        }

        return buffer.toString();
    }

    public static boolean checkData(PhysicalInventoryPeriod dates) {

        if (dates == null) {
            return false;
        }

        if (dates.getStartDate() == null || dates.getEndDate() == null || dates.getAbsoluteFinishDate() == null) {
            return false;
        }

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        calendar1.setTime(dates.getStartDate());
        calendar2.setTime(dates.getEndDate());
        calendar3.setTime(dates.getAbsoluteFinishDate());

        if (calendar1.compareTo(calendar2) > 0) {
            return false;
        }

        if (calendar1.compareTo(calendar3) > 0) {
            return false;
        }

        if (calendar2.compareTo(calendar3) > 0) {
            return false;
        }

        return true;
    }

    private static void parseBlankSpaces(String text, ParsePosition position) {

        if (text == null || position == null) {
            return;
        }

        while (position.getIndex() < text.length() &&
                (text.charAt(position.getIndex()) == ' ' ||
                        text.charAt(position.getIndex()) == '\r' ||
                        text.charAt(position.getIndex()) == '\n')) {
            position.setIndex(position.getIndex() + 1);
        }
    }
    
    public boolean loadFrom(String text, ParsePosition position) {
    	return loadFrom(text, position, DATE_FORMAT_PATTERN);
    }

    public boolean loadFrom(String text, ParsePosition position, String dateFormatPattern) {

        if (text == null) {
            return false;
        }

        if (position == null) {
            return false;
        }

        if (position.getIndex() >= text.length()) {
            return false;
        }

        boolean isParsedSuccessfully = true;

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);

        Date startDate = null;
        Date endDate = null;
        Date absoluteFinishDate = null;

        do
        {
            parseBlankSpaces(text, position);

            if (position.getIndex() >= text.length()) {
                isParsedSuccessfully = false;
                break;
            }

            if (text.charAt(position.getIndex()) == ',') {
                position.setIndex(position.getIndex() + 1);
            }

            parseBlankSpaces(text, position);

            if (position.getIndex() >= text.length()) {
                isParsedSuccessfully = false;
                break;
            }

            try {
                startDate = dateFormat.parse(text, position);
            } catch (Exception ex) {
                isParsedSuccessfully = false;
                break;
            }

            parseBlankSpaces(text, position);

            if (position.getIndex() >= text.length()) {
                isParsedSuccessfully = false;
                break;
            }

            if (text.charAt(position.getIndex()) == ',') {
                position.setIndex(position.getIndex() + 1);
            }

            parseBlankSpaces(text, position);

            if (position.getIndex() >= text.length()) {
                isParsedSuccessfully = false;
                break;
            }

            try {
                endDate = dateFormat.parse(text, position);
            } catch (Exception ex) {
                isParsedSuccessfully = false;
                break;
            }

            parseBlankSpaces(text, position);

            if (position.getIndex() >= text.length()) {
                isParsedSuccessfully = false;
                break;
            }

            if (text.charAt(position.getIndex()) == ',') {
                position.setIndex(position.getIndex() + 1);
            }

            parseBlankSpaces(text, position);
            if (position.getIndex() >= text.length()) {
                isParsedSuccessfully = false;
                break;
            }

            try {
                absoluteFinishDate = dateFormat.parse(text, position);
            } catch (Exception ex) {
                isParsedSuccessfully = false;
                break;
            }

            if (startDate == null || endDate == null || absoluteFinishDate == null) {
                isParsedSuccessfully = false;
                break;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            if (calendar.get(Calendar.YEAR) < 1000) {
                isParsedSuccessfully = false;
                break;
            }

            calendar.setTime(endDate);
            if (calendar.get(Calendar.YEAR) < 1000) {
                isParsedSuccessfully = false;
                break;
            }

            calendar.setTime(absoluteFinishDate);
            if (calendar.get(Calendar.YEAR) < 1000) {
                isParsedSuccessfully = false;
                break;
            }

            this.setStartDate(startDate);
            this.setEndDate(endDate);
            this.setAbsoluteFinishDate(absoluteFinishDate);

        } while (false);

        return isParsedSuccessfully;
    }

    public boolean loadFrom(String text) {
        return loadFrom(text, new ParsePosition(0));
    }

    public static boolean isEquals(PhysicalInventoryPeriod o1, PhysicalInventoryPeriod o2) {

        if (o1 == null && o2 == null) {
            return true;
        }

        if ((o1 != null && o2 == null) || (o1 == null)) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(o1.getStartDate());
        calendar2.setTime(o2.getStartDate());

        if (calendar1.compareTo(calendar2) != 0) {
            return false;
        }
        calendar1.setTime(o1.getEndDate());
        calendar2.setTime(o2.getEndDate());

        if (calendar1.compareTo(calendar2) != 0) {
            return false;
        }

        calendar1.setTime(o1.getAbsoluteFinishDate());
        calendar2.setTime(o2.getAbsoluteFinishDate());

        return calendar1.compareTo(calendar2) == 0;
    }

    public static PhysicalInventoryPeriod parse(String text, ParsePosition position, String dateFormatPattern) {

        if (text == null || position == null) {
            return null;
        }

        PhysicalInventoryPeriod period = new PhysicalInventoryPeriod();
        boolean success = false;
        if (Utility.isSet(dateFormatPattern)) {
        	success = period.loadFrom(text, position, dateFormatPattern);
        }
        else {
        	success = period.loadFrom(text, position);
        }
        if (success) {
            return period;
        }

        return null;
    }

    public static PhysicalInventoryPeriod parse(String text, ParsePosition position) {
    	return parse(text, position, null);
    }

    public static PhysicalInventoryPeriod parse(String text) {
        return parse(text, new ParsePosition(0));
    }

    public static Date parseDate(String text) {
        if (text == null) {
            return null;
        }
        if (text.trim().length() == 0) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        Date date = null;
        try {
            date = dateFormat.parse(text);
        } catch (Exception ex) {  // ignore
        }
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.YEAR) < 1000) {
            date = null;
        }

        return date;
    }

    private String getDateStringForNull() {
        return "00/00/0000";
    }

}


