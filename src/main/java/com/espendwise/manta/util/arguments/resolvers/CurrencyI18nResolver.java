package com.espendwise.manta.util.arguments.resolvers;


import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.ArgumentResolver;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.CurrencyData;

import java.math.BigDecimal;
import java.util.List;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class CurrencyI18nResolver implements ArgumentResolver<BigDecimal> {

    @Override
    public String resolve(AppLocale locale, BigDecimal obj) {
      NumberFormat format = DecimalFormat.getNumberInstance(locale.getLocale());
      format.setMaximumFractionDigits(2);
      format.setMinimumFractionDigits(2);

      double valueDbl = obj.doubleValue();
      String valueS = format.format(valueDbl);

      return valueS;
    }

    @Override
    public String resolve(BigDecimal obj) {
      NumberFormat format = DecimalFormat.getNumberInstance();
      format.setMaximumFractionDigits(2);
      format.setMinimumFractionDigits(2);

      double valueDbl = obj.doubleValue();
      String valueS = format.format(valueDbl);

      return valueS;
    }

    

}
