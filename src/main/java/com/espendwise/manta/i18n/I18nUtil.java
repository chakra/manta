package com.espendwise.manta.i18n;


import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.model.view.CountryView;

import java.util.Locale;



public class I18nUtil {


    public static String getMessage(Locale locale, String key, Object[] args) {
        return getMessage(key,
                args,
                false,
                locale
        );
    }

    public static String getMessage(Locale locale, String key) {
        return getMessage(key,
                null,
                false,
                locale
        );
    }

    public static  String getMessage(String key, Object[] args, boolean pReturnNullIfNotFound, Locale userLocale) {

         return I18nResource.getUserResource().getMessage(
                 key,
                 args,
                 pReturnNullIfNotFound,
                 userLocale
         );

    }

    public static String getMessage(AppUser appUser, String key, Object[] args, Locale preferedLocale, boolean returnNullIfNotFound){
        return I18nResource.getUserResource().getMessage(appUser,
                key,
                args,
                preferedLocale,
                returnNullIfNotFound
        );
    }

    public static String getDatePattern() {
	  String dateFormat = Constants.SYSTEM_DATE_PATTERN;
	  try{

          AppLocale userLocale = new AppLocale(Auth.getAppUser().getLocale());
          dateFormat = getDatePattern(userLocale);

	  } catch (Exception exc) {
		  exc.printStackTrace();
	  }
	  return dateFormat;

    }

    public static String getTimePattern() {
	  String timeFormat = Constants.SYSTEM_TIME_PATTERN;
	  try{
          AppLocale userLocale = new AppLocale(Auth.getAppUser().getLocale());
          timeFormat = getTimePattern(userLocale);
	  } catch (Exception exc) {
		  exc.printStackTrace();
	  }
	  return timeFormat;

    }

    public static String getDayWithMonthPattern() {
      String dayWithMonthFormat = Constants.SYSTEM_MONTH_WITH_DAY_PATTERN;
	  try{
          AppLocale userLocale = new AppLocale(Auth.getAppUser().getLocale());
          dayWithMonthFormat = getDayWithMonthPattern(userLocale);
	  } catch (Exception exc) {
		  exc.printStackTrace();
	  }
	  return dayWithMonthFormat;

    }

    public static String getDayWithMonthPattern(AppLocale locale) {
	  String monthWithDayFormat = Constants.SYSTEM_MONTH_WITH_DAY_PATTERN;
	  try{
	      String countryCd = locale.getLocale().getCountry();
		  DbConstantResource dbResource = AppResourceHolder.getAppResource().getDbConstantsResource();
          CountryView country = dbResource.getCountryByCd(countryCd);
		  if(Utility.isSet(country.getInputDayMonthFormat())){
			  monthWithDayFormat = country.getInputDayMonthFormat();
          }
	  } catch (Exception exc) {
		  exc.printStackTrace();
	  }
	  return monthWithDayFormat;

    }

    public static String getDatePickerPattern(AppLocale locale) {
        String pattern = getDatePattern(locale).toLowerCase();
        return pattern.replace("yyyy", "yy");
    }

    public static String getDatePickerPattern() {
      String dateFormat = Constants.SYSTEM_DATE_PATTERN;
	  try{
          AppLocale userLocale = new AppLocale(Auth.getAppUser().getLocale());
          dateFormat = getDatePickerPattern(userLocale);
	  } catch (Exception exc) {
		  exc.printStackTrace();
	  }
	  return dateFormat;
    }

    public static String getDatePatternPrompt() {
	  return getDatePattern().toLowerCase();
    }

    public static String getTimePatternPrompt() {
	  return getTimePattern().toLowerCase();
    }


  public static String getDatePattern(AppLocale appLocale) {
	  String dateFormat = Constants.SYSTEM_DATE_PATTERN;
	  try{
	      Locale locale = appLocale.getLocale();
		  String countryCd = locale.getCountry();

		  DbConstantResource dbResource = AppResourceHolder.getAppResource().getDbConstantsResource();
          CountryView country = dbResource.getCountryByCd(countryCd);
		  if(Utility.isSet(country.getInputDateFormat())){
			  dateFormat = country.getInputDateFormat();
          }

	  } catch (Exception exc) {
		  exc.printStackTrace();
	  }
	  return dateFormat;
    }



    public static String getDatePatternPrompt(AppLocale locale) {
        return getDatePattern(locale).toLowerCase();
    }

    public static String getTimePatternPrompt(AppLocale locale) {
        return getTimePattern(locale);
   }

    public static String getTimePattern(AppLocale locale) {
  	  String timeFormat = Constants.SYSTEM_TIME_PATTERN; 
	  try{
		  String countryCd = locale.getLocale().getCountry();

		  DbConstantResource dbResource = AppResourceHolder.getAppResource().getDbConstantsResource();
          CountryView country = dbResource.getCountryByCd(countryCd);
		  if(Utility.isSet(country.getInputTimeFormat())){
			  timeFormat = country.getInputTimeFormat();
          }

	  } catch (Exception exc) {
		  exc.printStackTrace();
	  }
	  return timeFormat;
    }

    public static String getDayWithMonthPatternPrompt(AppLocale locale) {
        return getDayWithMonthPattern(locale).toLowerCase();
    }

    public static String getDayWithMonthPatternPrompt() {
      String monthWithDayFormat = Constants.SYSTEM_MONTH_WITH_DAY_PATTERN;
	  try{
          AppLocale userLocale = new AppLocale(Auth.getAppUser().getLocale());
          monthWithDayFormat = getDayWithMonthPatternPrompt(userLocale);
	  } catch (Exception exc) {
		  exc.printStackTrace();
	  }
	  return monthWithDayFormat;

    }


}
