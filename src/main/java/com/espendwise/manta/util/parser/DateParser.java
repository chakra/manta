package com.espendwise.manta.util.parser;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser implements AppParser<Date> {

	
	
    public Date parse(String value, Object... params) throws AppParserException {

        String datePattern = (String) params[0];

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        sdf.setLenient(false);

        ParsePosition parsePosition = new ParsePosition(0);
        Date parseResult = sdf.parse(value, parsePosition);
        //System.out.println("DateParser.parse() ==> parsePosition.getIndex()=" + parsePosition.getIndex() + ", value.length()=" + value.length() + ", sub =" + value.substring(parsePosition.getIndex()).trim());
        if (parsePosition.getIndex() == 0) {
            throw new AppParserException("Unparseable date: \"" + value + "\"", value);
        } else if (parsePosition.getIndex() < value.length() && value.substring(parsePosition.getIndex()).trim().length() > 0) {
            throw new AppParserException("Unparseable date: \"" + value + "\"", value);
        }

        return parseResult;

    }

}

