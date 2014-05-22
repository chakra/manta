package com.espendwise.manta.util.parser;


import org.apache.log4j.Logger;

import java.util.Arrays;

public class PercentParserInt implements AppParser<Integer> {

    private static final Logger logger = Logger.getLogger(PercentParserInt.class);

    @Override
    public Integer parse(String parserValue, Object... params) throws AppParserException {

        logger.debug("parse()=> BEGIN" +
                ", parse: " + parserValue +
                ", params: " + Arrays.asList(params));

        Integer result;

        if (parserValue.endsWith("%")) {
            result = parserValue.startsWith("+") ?
                    Parse.parseInt(parserValue.substring(1, parserValue.length() - 1)) :
                    Parse.parseInt(parserValue.substring(0, parserValue.length() - 1));
        } else {
            result = parserValue.startsWith("+") ?
                    Parse.parseInt(parserValue.substring(1)) :
                    Parse.parseInt(parserValue);
        }

        if (result < -100 || result > 100) {
            throw new AppParserException("wrong percent,interval", parserValue);
        }


        logger.debug("parse()=> END, result: " + result);

        return result;
    }

}
