package com.espendwise.manta.util.parser;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AmountParser implements AppParser<BigDecimal> {

    private static final Logger logger = Logger.getLogger(MonthWithDayParser.class);

    @Override
    public BigDecimal parse(String parserValue, Object... params) throws AppParserException {

        logger.info("parse()=> BEGIN" +
                ", parse: " + parserValue +
                ", params: " + Arrays.asList(params));
        BigDecimal parsedValue;

        try {
            String pattern =  "[^0-9,.]" ;
            Pattern p = Pattern.compile(pattern);

            Matcher matcher = p.matcher(parserValue);
            if (matcher.find()) {
                throw new NumberFormatException();
            }
            parsedValue = new BigDecimal(Double.parseDouble(parserValue));

        } catch (NumberFormatException e) {
            throw  new AppParserException(e.getMessage(), parserValue);
        }

        logger.info("parse()=> END, " + "parsedValue: " + parsedValue);

        return parsedValue;
    }


}
