package com.espendwise.manta.util.format;


import com.espendwise.manta.util.arguments.ArgumentType;

import java.math.BigDecimal;
import java.util.Date;

public abstract class AbstractAppFormatter implements AppFormatter {

    public String format(ArgumentType type , Object obj){
        switch (type) {
            case DATE :  return formatDate((Date)obj);
            case TIME :  return formatTime((Date)obj);
            case NUMBER:  return formatNumber((Number)obj);
            case CURRENCY:  return formatCurrency((BigDecimal)obj);
            case I18N_STRING:  return formatString((String)obj);
            case STRING:  return formatString((String)obj);
            case MONTH_WITH_DAY:  return formatMonthDay((Date) obj);
            case OBJECT:  return formatObject(obj);
            default:  return formatObject(obj);
        }
    }

    @Override
    public abstract String formatMonthDay(Date obj);

    @Override
    public abstract String formatDate(Date date);

    @Override
    public abstract String formatTime(Date date);

    @Override
    public abstract String formatCurrency(BigDecimal currency);

    @Override
    public abstract String formatNumber(Number number);

    @Override
    public abstract String formatString(String string);

    @Override
    public abstract String formatObject(Object object);
}
