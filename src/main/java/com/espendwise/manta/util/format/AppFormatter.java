package com.espendwise.manta.util.format;


import com.espendwise.manta.util.arguments.ArgumentType;

import java.math.BigDecimal;
import java.util.Date;

public interface AppFormatter {

    public String format(ArgumentType type , Object obj);

    public String formatDate(Date date);

    public String formatTime(Date date);

    public String formatCurrency(BigDecimal currency);

    public String formatNumber(Number number);

    public String formatString(String string);

    public String formatObject(Object object);

    public abstract String formatMonthDay(Date obj);


}
