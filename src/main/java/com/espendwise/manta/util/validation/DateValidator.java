package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.DateArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.parser.AppParser;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.AppParserFactory;
import com.espendwise.manta.util.validation.resolvers.ExceptionResolver;
import com.espendwise.manta.util.validation.resolvers.ValidationCodeResolver;
import org.apache.log4j.Logger;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Date;

public class DateValidator implements CustomValidator<String, ValidationCodeResolver> {

    private static final Logger logger = Logger.getLogger(DateValidator.class);

    private String datePattern;

    private Date parsedDate;
    private Date leftBound;
    private Date rightBound;
    
    
    
    public static enum DateFormatCodes  {
        FORMAT1("dd/MM/yyyy"),
        FORMAT2("dd.MM.yyyy"),
        FORMAT3("dd-MM-yyyy"),
        FORMAT4("MM/dd/yyyy"),
        FORMAT5("MM.dd.yyyy"),
        FORMAT6("MM-dd-yyyy"),
        FORMAT7("yyyy-MM-dd"),
        FORMAT8("yyyy/MM/dd"),
        FORMAT9("yyyy.MM.dd"),
        FORMAT10("HH:mm");
        
        private final String value;

        DateFormatCodes(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static DateFormatCodes getCode(String v) {
            for (DateFormatCodes c: DateFormatCodes.values()) {
                if (c.value.equalsIgnoreCase(v)) {
                    return c;
                }
            }
            return DateFormatCodes.FORMAT1;
        }
      
        
    }
    
    public static enum DateFormatExpressions  {
        FORMAT1("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)"),
        FORMAT2("(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)"),
        FORMAT3("(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)"),
        FORMAT4("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)"),
        FORMAT5("(0?[1-9]|1[012]).(0?[1-9]|[12][0-9]|3[01]).((19|20)\\d\\d)"),
        FORMAT6("(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])-((19|20)\\d\\d)"),
        FORMAT7("((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])"),
        FORMAT8("((19|20)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])"),
        FORMAT9("((19|20)\\d\\d).(0?[1-9]|1[012]).(0?[1-9]|[12][0-9]|3[01])"),
        FORMAT10("([01]?[0-9]|2[0-3]):[0-5][0-9]");
        private final String value;

        DateFormatExpressions(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static String getExpression(DateFormatCodes code) {
        	switch (code) {
        	case FORMAT1 :
        		return FORMAT1.value();
        	case FORMAT2 :
        		return FORMAT2.value();
        	case FORMAT3 :
        		return FORMAT3.value();
        	case FORMAT4 :
        		return FORMAT4.value();
        	case FORMAT5 :
        		return FORMAT5.value();
        	case FORMAT6 :
        		return FORMAT6.value();
        	case FORMAT7 :
        		return FORMAT7.value();
        	case FORMAT8 :
        		return FORMAT8.value();
        	case FORMAT9 :
        		return FORMAT9.value();
        	case FORMAT10 :
        		return FORMAT10.value();
        	}
            return DateFormatExpressions.FORMAT1.value();
        }

    }    
    
   


    public DateValidator(String pattern) {
        this.datePattern = pattern;
    }

    public DateValidator(String datePattern, Date leftBound, Date rightBound) {
        this.datePattern = datePattern;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    private boolean isSet(String value) {
        return value != null && value.trim().length() > 0;
    }

    public Date parse(String value) throws AppParserException {
        AppParser<Date> parser = AppParserFactory.getParser(Date.class);
        seParsedDate(parser.parse(value, getPattern()));
        return  getParsedDate();
    }

    private boolean isRangeOut(Date value) {

        if (Utility.isSet(leftBound) && Utility.isSet(rightBound)) {
            if(value.before(getLeftBound()) || value.after(getRightBound())){
                return true;
            }
        } else if (Utility.isSet(leftBound) && !Utility.isSet(rightBound)) {
            if(value.before(getLeftBound()) ){
                return true;
            }
        }  else if (!Utility.isSet(leftBound) && Utility.isSet(rightBound)) {
            if(value.after(getRightBound()) ){
                return true;
            }
        }

        return false;
    }
    
    public void validateFormat(String pattern, String obj) throws AppParserException {
     	DateFormatCodes code = DateFormatCodes.getCode(pattern);
     	//logger.info("validateFormat()============> code = "+ code.value());
     	
     	String exp = DateFormatExpressions.getExpression(code);
     	//logger.info("validateFormat()============> exp = "+ exp);
     	Pattern datePatternExp = Pattern.compile(exp,Pattern.CASE_INSENSITIVE); 	
     	if (!datePatternExp.matcher(obj).matches()) {
     		throw new AppParserException("Unparseable date: \"" + obj + "\"", obj);
     	}
    }

    public ValidationResult validate(String obj, ValidationCodeResolver resolver) throws ValidationException {

        reset();

        if (!isSet(obj)) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.VALUE_IS_NOT_SET, new StringArgument(obj))
            );
        }

        try {
        	validateFormat(getPattern(), obj);
            parse(obj);
        } catch (AppParserException e) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(
                            ValidationReason.WRONG_DATE_FORMAT,
                            new StringArgument(obj),
                            new StringArgument(getPattern())
                    )
            );
        }


        if (isRangeOut(getParsedDate())) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(
                            ValidationReason.RANGE_OUT,
                            new StringArgument(obj),
                            new StringArgument(getPattern()),
                            new DateArgument(getParsedDate()),
                            new DateArgument(getLeftBound()),
                            new DateArgument(getRightBound())
                    )
            );
        }


        return null;
    }

    public ValidationResult validateDates(String obj1, String obj2, ValidationCodeResolver resolver) throws ValidationException {

        reset();

        if (!isSet(obj1) || !isSet(obj2)) {
            return null;
        }

        AppParser<Date> parser = AppParserFactory.getParser(Date.class);
        Date date1 = parser.parse(obj1, getPattern());
        Date date2 = parser.parse(obj2, getPattern());

        if (date1 == null || date2 == null) {
            return null;
        }

        if (date2.compareTo(date1) < 0) {
            return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(
                        ValidationReason.DATES_RANGE_OUT, 
                        new StringArgument(obj1),
                        new StringArgument(obj2),
                        new DateArgument(date1),
                        new DateArgument(date2))
            );
        }

        return null;
    }

    public ValidationResult checkRanges(ValidationCodeResolver resolver) throws ValidationException {

        if (getLeftBound() != null && getRightBound() != null) {
            
            if (getRightBound().before(getLeftBound())) {
                return new CodeValidationResult(
                    resolver,
                    new ValidationCodeImpl(ValidationReason.WRONG_BOUNDS_RIGHT_BEFORE_LEFT)
                );
            }
        }

        return null;
    }

    public ValidationResult validate(String obj) throws ValidationException {
        return validate(obj, new ExceptionResolver());
    }

    private void reset() {
        parsedDate = null;
    }

    public String getPattern() {
        return datePattern;
    }

    private void seParsedDate(Date date) {
        this.parsedDate = date;
    }

    public Date getParsedDate() {
        return parsedDate;
    }

    public Date getLeftBound() {
        return leftBound;
    }

    public Date getRightBound() {
        return rightBound;
    }
}
