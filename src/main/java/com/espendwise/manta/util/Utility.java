package com.espendwise.manta.util;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.springframework.util.StringUtils;

import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.criteria.FilterValue;
import com.espendwise.manta.util.parser.Parse;

public class Utility {

    private static final Logger logger = Logger.getLogger(Utility.class);
    

    public static boolean isSet(String string) {
        return string != null && !string.trim().equals(Constants.EMPTY);
    }

    public static String trim(String storeName) {
        return Utility.strNN(storeName).trim();
    }

     public static String escape(String string) {

        if ((string == null) || (string.indexOf('\'') < 0)) {
            return string;
        }

        int n = string.length();

        StringBuilder sb = new StringBuilder(n);

         int i = 0;
         while (i < n) {

             char ch = string.charAt(i);

             if (ch == '\'') {
                 sb.append('\'');
             }

             sb.append(ch);
             i++;
         }

         return sb.toString();

    }

    public static BigDecimal addAmt(BigDecimal amt1, BigDecimal amt2) {


        if (null == amt1) {
            amt1 = new BigDecimal(0);
        }
        if (null == amt2) {
            amt2 = new BigDecimal(0);
        }
        return amt1.add(amt2);
    }
    public static BigDecimal addAmtNullable(BigDecimal amt1, BigDecimal amt2) {


        if (null == amt1 &&  amt2 == null) {
            return null;
        }
        if (null == amt1) {
            amt1 = new BigDecimal(0);
        }
        if (null == amt2) {
            amt2 = new BigDecimal(0);
        }

        return amt1.add(amt2);

    }

    public static BigDecimal subtractAmtNullable(BigDecimal amt1, BigDecimal amt2) {

        if (null == amt1 &&  amt2 == null) {
              return null;
        }

        if (null == amt1) {
            amt1 = new BigDecimal(0);
        }

        if (null == amt2) {
            amt2 = new BigDecimal(0);
        }

        return amt1.subtract(amt2);

    }

    public static boolean isTrue(String pValue) {
        return isTrue(pValue, false);
    }

    public static boolean isTrue(Boolean value) {
        return value != null && value;
    }

    public static String isTrueStrOf(String value) {
        return String.valueOf(isTrue(value, false));
    }
    public static String isTrueStrOf(Boolean value) {
        return String.valueOf(isTrue(value));
    }
    public static String isTrueOnStrOf(String value) {
        return isTrue(value, false) ? Constants.ON : Constants.OFF;
    }

    public static String isYStrOf(String value) {
        return isTrue(value, false) ? Constants.Y : Constants.N;
    }

    public static boolean isOn(String pValue, boolean defaultValue){
        if(pValue != null){pValue = pValue.trim();}
        if(pValue != null && pValue.length() > 0){
            return pValue.equalsIgnoreCase(Constants.ON);
        }
        return defaultValue;
    }

    public static boolean isOn(String pVal) {
        return isOn(pVal, false);
    }

    public static boolean isOff(String pValue, boolean defaultValue){
        if(pValue != null){pValue = pValue.trim();}
        if(pValue != null && pValue.length() > 0){
            return pValue.equalsIgnoreCase(Constants.OFF);
        }
        return defaultValue;
    }


    public static boolean isTrue(String pValue, boolean pDefaultValue) {

        if(pValue==null || pValue.equals(Constants.NULLS)){
            return pDefaultValue;
        }

        String value = pValue.trim();

        if (value.length() > 0) {
            try {
                return Integer.parseInt(value) > 0;
            } catch (Exception e) {
                String first = value.substring(0, 1);
                return (first.equalsIgnoreCase(Constants.CHARS.T)
                        || first.equalsIgnoreCase(Constants.CHARS.Y)
                        || first.equalsIgnoreCase(Constants.CHARS._1));
            }
        }

        return pDefaultValue;
    }

    public static Locale parseLocaleCode(String pLocaleCode) {
        // Split the locale into language and country.
        StringTokenizer st = new StringTokenizer(pLocaleCode, "_");
        String lang = "", country = "";
        for (int i = 0; st.hasMoreTokens() && i < 2; i++) {
            if (i == 0) lang = st.nextToken();
            if (i == 1) country = st.nextToken();
        }
        return new Locale(lang, country);
    }

    public static int intNN(Integer value) {
        return value == null ? 0 : value;
    }
    public static long longNN(Long value) {
        return value == null ? 0 : value;
    }

    public static <T> Map<Long, T> toMap(Collection<T> list) {

        HashMap<Long, T> map = new HashMap<Long, T>();
        if (list == null) {
            return map;
        }

        for (T obj : list) {
            if (obj != null) {
                map.put( getId(obj), obj);
            }
        }

        return map;
    }

    public static <T, K> Map<K, T> toMap(K key, T value) {
        HashMap<K, T> map = new HashMap<K, T>();
        map.put(key, value);
        return map;
    }

    public static boolean isNew(Object obj) {
        Long id = getId(obj);
        return id == null || !(id > 0);
    }

    public static Long getId(Object obj) {

        if (obj instanceof BusEntityData) {
            return ((BusEntityData) obj).getBusEntityId();
        } else if (obj instanceof EmailTemplateListView) {
            return ((EmailTemplateListView) obj).getTemplateId();
        } else if (obj instanceof FiscalCalendarIdentView) {
            return ((FiscalCalendarIdentView) obj).getFiscalCalendarData().getFiscalCalenderId();
        } else if (obj instanceof AccountListView) {
            return ((AccountListView) obj).getAccountId();
        } else if (obj instanceof GroupData) {
            return ((GroupData) obj).getGroupId();
        } else if (obj instanceof SiteListView) {
            return ((SiteListView) obj).getSiteId();
        } else if (obj instanceof UserData) {
            return ((UserData) obj).getUserId();
        } else if (obj instanceof CostCenterData) {
            return ((CostCenterData) obj).getCostCenterId();
        } else if (obj instanceof BudgetData) {
            return ((BudgetData) obj).getBudgetId();
        } else if (obj instanceof BudgetDetailData) {
            return ((BudgetDetailData) obj).getBudgetDetailId();
        } else if (obj instanceof BudgetView) {
            return ((BudgetView) obj).getBudgetData().getBudgetId();
        } else if (obj instanceof WorkflowAssocData) {
            return ((WorkflowAssocData) obj).getWorkflowRuleId();
        }  else if (obj instanceof WorkflowRuleData) {
            return ((WorkflowRuleData) obj).getWorkflowRuleId();
        } else if (obj instanceof ProductListView) {
            return ((ProductListView) obj).getItemId();
        } else if (obj instanceof ShoppingControlItemView) {
            return ((ShoppingControlItemView) obj).getShoppingControlId();
        } else if (obj instanceof GroupReportListView) {
            return ((GroupReportListView) obj).getReportId();
        } else if (obj instanceof GroupConfigListView) {
            return ((GroupConfigListView) obj).getId();
        } else if (obj instanceof ItemListView) {
            return ((ItemListView)obj).getItemId();
        } else if (obj instanceof CorporateScheduleView) {
            return ((CorporateScheduleView)obj).getScheduleId();
        } else if (obj instanceof CatalogStructureData) {
            return ((CatalogStructureData)obj).getCatalogId();
        } else if (obj instanceof ContractData) {
            return ((ContractData)obj).getContractId();
        } else if (obj instanceof UploadSkuData) {
            return ((UploadSkuData)obj).getItemId();
        } else if (obj instanceof UploadSkuView) {
            return ((UploadSkuView)obj).getUploadSkuData().getItemId();
        } else if (obj instanceof OrderGuideItemView) {
            return ((OrderGuideItemView)obj).getOrderGuideStructureId();
        } else if (obj instanceof OrderScheduleData) {
            return ((OrderScheduleData)obj).getOrderScheduleId();
        } else if (obj instanceof OrderGuideData) {
            return ((OrderGuideData)obj).getOrderGuideId();
        } else if (obj instanceof ItemLoaderView) {
            return ((ItemLoaderView)obj).getItemId();
        }


        throw SystemError.methodDoesNotSupportObjectOfType(obj.getClass().getName());
    }

    public static String getShortDesc(Object obj) {

        if (obj instanceof BusEntityData) {
            return ((BusEntityData) obj).getShortDesc();
        } else if (obj instanceof EmailTemplateListView) {
            return ((EmailTemplateListView) obj).getName();
        } else if (obj instanceof FiscalCalendarIdentView) {
            return ((FiscalCalendarIdentView) obj).getFiscalCalendarData().getShortDesc();
        } else if (obj instanceof AccountListView) {
            return ((AccountListView) obj).getAccountName();
        } else if (obj instanceof GroupData) {
            return ((GroupData) obj).getShortDesc();
        }

        throw SystemError.methodDoesNotSupportObjectOfType(obj.getClass().getName());
    }


    public static String strNN(String value) {
        return value == null ? Constants.EMPTY : value;
    }

    public static String strNN(BigDecimal value) {
        return value == null ? Constants.EMPTY : value.toString();
    }
    
    public static String strNN(Long value) {
        return value == null ? Constants.EMPTY : value.toString();
    }

    public static String strNN(String value,String  defaultValue) {
        return value == null ? defaultValue : value;
    }
    
    public static boolean isSet(Object obj) {
        return obj != null;
    }

    public static boolean isSet(FilterValue obj) {
        return obj != null && isSet(obj.getFilterValue());
    }

    public static boolean isSet(Collection obj) {
        return obj != null && !obj.isEmpty();
    }

    public static Date setToMidnight(Date date) {
        Date resultDate = null;
        if (date != null) {
            Calendar dateC = Calendar.getInstance();
            dateC.setTime(date);

            // Set time fields to zero
            dateC.set(Calendar.HOUR_OF_DAY, 0);
            dateC.set(Calendar.MINUTE, 0);
            dateC.set(Calendar.SECOND, 0);
            dateC.set(Calendar.MILLISECOND, 0);

            // Put it back in the Date object
            resultDate = dateC.getTime();
        }
        return resultDate;
    }
    
    public static Integer getYearForDate(Date date) {
        Integer year = null;
        if (date != null) {
            Calendar dateC = Calendar.getInstance();
            dateC.setTime(date);

            year = dateC.get(Calendar.YEAR);
        }
        return year;
    }

    public static TypedArgument[] joins(TypedArgument[]... argsArray) {
        return Args.joins(argsArray);
    }


    public static TypedArgument[] typed(Object... args){
        return Args.typed(args);
    }


    public static <E> List<E> emptyList(Class<E> listElementType) {
       return new ArrayList<E>();
    }

    public static <T> List<T> toList(T obbj) {
        List<T> list = new ArrayList<T>();
        list.add(obbj);
        return list;
    }

    public static <T> List<T> toList(T... onjs) {
        List<T> list = new ArrayList<T>();
        if (onjs != null) {
            Collections.addAll(list, onjs);
        }
        return list;
    }
    public static <T> List<T> toListOrNull(T... objs) {
        List<T> list = null;
        if (objs != null && objs.length > 0) {
            list = new ArrayList<T>();
            Collections.addAll(list, objs);
        }
        return list;
    }

    public static <T> List<T> toListNN(T... onjs) {
        List<T> list = new ArrayList<T>();
        if (onjs != null) {
            for (T obj : onjs) {
                if (obj != null) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    public static <T> List<Long> toIds(Collection list) {

        List<Long> ids = emptyList(Long.class);

        if (list == null) {
            return ids;
        }

        for (Object obj : list) {
            if (obj != null) {
                Long id = getId(obj);
                ids.add(id);
            }
        }

        return ids;

    }

    public static <T> List<Pair<Long, String>> toPairs(List<T> list) {
      
        ArrayList<Pair<Long, String>> pairs = new ArrayList<Pair<Long, String>>();

        if (list== null) {
            return pairs;
        }

        for (Object obj : list) {
            if (obj != null) {
                pairs.add(new Pair<Long, String>(getId(obj), getShortDesc(obj)));
            }
        }

        return pairs;
    }

    public static <T> List<IdNamePair> toIdNamePairs(List<T> list) {

        ArrayList<IdNamePair> pairs = new ArrayList<IdNamePair>();

        if (list== null) {
            return pairs;
        }

        for (Object obj : list) {
            if (obj != null) {
                pairs.add(new IdNamePair(getId(obj), getShortDesc(obj)));
            }
        }

        return pairs;
    }

    public static String getJavaGetterName(String javaBeanProperty)   {
        char firstChar = Character.toUpperCase(javaBeanProperty.charAt(0));
        String theRest = javaBeanProperty.substring(1);
        return  Constants.BEAN_GETTER_PREFFIX + firstChar + theRest;
    }

    public static String arrayExp(String field, int i) {
       return  field+"["+i+"]";
    }


    public static String javaBeanPath(String... fields) {

        if (fields == null) {
            return null;
        }


        String exp = Constants.EMPTY;

        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
            String f = fields[i];
            if (isSet(f)) {
                if (isSet(exp)) {
                    exp += ".";
                }
                exp += f;
            }
        }

        return exp;
    }

    public static String[] split(String pathExp, String s) {

        StringTokenizer st = new StringTokenizer(pathExp,  s);

        String[] tokens = new String[st.countTokens()];

        int i =0;

        while(st.hasMoreTokens()){
            tokens[i++] = st.nextToken();
        }

        return tokens;
    }

    public static <T extends Annotation> Class<T> getAnnonClass(Class objClass, Class<T> ann) {
        List<Class<T>> x = new ArrayList<Class<T>>();
        extractAnnonClass(x, objClass, ann, true);
        return x.isEmpty() ? null : x.get(0);
    }

    public static <T extends Annotation> void extractAnnonClass(List x, Class objClass, Class<T> ann, boolean onlyFirst) {

        if (objClass.isAnnotationPresent(ann)) {
            x.add(objClass);
            if (onlyFirst) {
                return;
            }
        }

        Class superClass = objClass.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            extractAnnonClass(x,
                    objClass.getSuperclass(),
                    ann,
                    onlyFirst
            );
        }

    }

    public static List<String> typeCodes(PropertyTypeCode[] values) {

        if (values == null) {
            return null;
        }

        List<String> l = new ArrayList<String>();

        for (PropertyTypeCode v : values) {
            l.add(v.getTypeCode());
        }

        return l;
    }

    public static List<String> names(NamedPropertyTypeCode[] values) {

        if (values == null) {
            return null;
        }

        List<String> l = new ArrayList<String>();

        for (NamedPropertyTypeCode v : values) {
            l.add(v.name());
        }

        return l;
    }

    public static List<String> statusCodes(PropertyStatusCode[] values) {

        if (values == null) {
            return null;
        }

        List<String> l = new ArrayList<String>();

        for (PropertyStatusCode v : values) {
            l.add(v.getStatusCode());
        }

        return l;
    }

    public static List<String> nameExtraCodes(PropertyExtraCode[] values) {

        if (values == null) {
            return null;
        }

        List<String> l = new ArrayList<String>();

        for (PropertyExtraCode v : values) {
            l.add(v.getNameExtraCode());
        }

        return l;
    }


    public static List<ClassField> findAllGetterFieldsNames(Class<?> targetClass) {
        return ReflectionUtils.findAllGetterFieldsNames(targetClass);
    }


    public static String toCommaString(List list) {
        return StringUtils.collectionToCommaDelimitedString(list);
    }

    public static String toCommaString(List list, String s) {
        return StringUtils.collectionToDelimitedString(list, s);
    }

    public static String toCommaString(String[] list) {
        return StringUtils.collectionToCommaDelimitedString(Arrays.asList(list));
    }

    public static String toCommaString(Object... list) {
        return StringUtils.collectionToCommaDelimitedString(Arrays.asList(list));
    }

    public static List<Long> splitLong(String str) {
        if (Utility.isSet(str)) {
            List<Long> ids = new ArrayList<Long>();
            for (String s : StringUtils.commaDelimitedListToSet(str)) {
                ids.add(Parse.parseLong(s.trim()));
            }
            return ids;
        } else {
            return null;
        }
    }

    public static boolean isSetIgnorePattern(String s, String pattern) {
        if (s == null) {
            return false;
        } else {
            s = s.trim();
            return !s.equals("") && !s.equals(pattern);
        }
    }

    public static String ignorePattern(String value, String pattern) {
        return pattern != null && value != null && value.equals(pattern)
                ? Constants.EMPTY
                : value;

    }


    public static <T, R extends Pair<T, ?>> List<T> obj1Only(List<R> list) {
        List<T> x = new ArrayList<T>();
        for (R r : list) {
            x.add(r.getObject1());
        }
        return x;
    }

    public static boolean isEqual(String str1, String str2) {
        return !isSet(str1) ? !isSet(str2) : str1.equals(str2);
    }

    public static boolean isChanged(String str1, String str2) {
        return !isSet(str1) ? isSet(str2) : !str1.equals(str2);
    }

    public static boolean isChanged(Integer int1, Integer int2) {
        return int1 == null ? int2 != null : !int1.equals(int2);
    }

    public static int compareBigDecimal(BigDecimal a, BigDecimal b) {
        return a.subtract(b).abs().compareTo(new BigDecimal(.00001)) >= 0 ? 1 : 0;
    }
    public static int compareToIgnoreCase(String str1, String str2) {
        if (str1 == null)
            str1 = "";
        if (str2 == null)
            str2 = "";

        return str1.compareToIgnoreCase(str2);
    }

    public static <T> List<T> listNN(List<T> list) {
        return list == null ? new ArrayList<T>() : list;
    }


    public static List<List<Long>> createPackages(List<Long> ids, int pPackageSize) {
        List<List<Long>> packages = new ArrayList<List<Long>>();
        if (ids != null && !ids.isEmpty()) {
            if (ids.size() <= pPackageSize) {
                packages.add(ids);
            } else {
                for (int i = 0; i <= (ids.size() / pPackageSize); i++) {
                    List<Long> pack = new ArrayList<Long>();
                    for (int j = 0; j < (i == (ids.size() / pPackageSize) ? (ids.size() % pPackageSize) : pPackageSize); j++){
                        pack.add(ids.get((i * pPackageSize) + j));
                    }
                    packages.add(pack);
                }
            }
        }
        return packages;
    }

    public static <T, R> List<T> toObject1(List<Pair<T, R>> pairs) {
        if (pairs != null) {
            List<T> l = new ArrayList<T>();
            for (Pair<T, ?> p : pairs) {
                l.add(p.getObject1());
            }
            return l;
        } else {
            return null;
        }
    }

    public static <T, R> List<R> toObject2(List<Pair<T, R>> pairs) {
        if (pairs != null) {
            List<R> l = new ArrayList<R>();
            for (Pair<T, R> p : pairs) {
                l.add(p.getObject2());
            }
            return l;
        } else {
            return null;
        }
    }
    
    public static Set<String> anyDelimitedListToSet(String str){
    	Set<String> res = new HashSet<String>();
    	if (Utility.isSet(str)) {
    		Set<String> set = StringUtils.commaDelimitedListToSet(str.replaceAll("[\\W\\s*\\_]", ","));
    		for (String el : set){
    			if (Utility.isSet(el)){
    				res.add(el.trim());
    			}
    		}
    	}
    	return res;
    }
    
    public static String capitalizeFirstLetters(String s) {
    	String lowerCaseString = s.toLowerCase();
    	Pattern spaces=Pattern.compile("\\s+[a-z]");       
    	Matcher m=spaces.matcher(lowerCaseString);    
    	StringBuilder capitalWordBuilder = new StringBuilder(lowerCaseString.substring(0,1).toUpperCase());
    	int prevStart = 1;
    	while (m.find()) {
    		capitalWordBuilder.append(lowerCaseString.substring(prevStart, m.end() - 1));
    	    capitalWordBuilder.append(lowerCaseString.substring(m.end() -1 , m.end()).toUpperCase());
    	    prevStart = m.end();
    	}   
    	capitalWordBuilder.append(lowerCaseString.substring(prevStart, lowerCaseString.length()));    
    	return capitalWordBuilder.toString();
    }
    
    // trim left 0 of string. E.g. if string = '020', return '20', if string = '000' return 0, 
    // if string = '0', return 0
    public static String getIntegerStringValue(String string){
    	if (isSet(string) && string.length() > 1 && string.charAt(0) == '0'){
    		Integer intValue = Integer.parseInt(string);
    		return intValue.toString();    		
    	}
    	return string;
    }
    
    /**
     *Finds the setter method of the passed in object based off the javaBean property.
     *Thus myProp would find the method setMyProp of the passed in object.
     */
    public static Method getJavaBeanSetterMethod(Object pObj, String javaBeanProperty){
        //convert the javaBeanProperty to as set method
        char firstChar = Character.toUpperCase(javaBeanProperty.charAt(0));
        String theRest = javaBeanProperty.substring(1);
        String methodName = "set" + firstChar + theRest;
        Method[] methods = pObj.getClass().getMethods();
        for(int i=0;i<methods.length;i++){
            Method method = methods[i];
            if(method.getName().equals(methodName)){
                return method;
            }
        }
        return null;
    }    
        
    public static File createTempFileAttachment(){
        File tmp = null;

        try {
          tmp = File.createTempFile("Attachment", ".txt");
        }
        catch (IOException e) {
          e.printStackTrace();
        }
        return tmp;
    }
    
    public static File createTempFileAttachment(String fileName){
        File tmp = null;
        int ind = fileName.lastIndexOf(".");
        String prefix = (ind >0 ) ? fileName.substring(0,ind) : "Attachment" ;
        String suffix = (ind >=0 && fileName.length()>1 ) ? fileName.substring(ind) : ".txt";
        try {
          tmp = File.createTempFile(prefix, suffix);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
        return tmp;
    }
    
    static public boolean isSetForDisplay(String string) {
        if (isSet(string)) {    
            string = string.trim().toUpperCase();
            
            if (!("NA".equals(string)) && !("N/A".equals(string))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     *will populate the method object with the propertyValue
     *property from this object.
     *@param pObj the object of the method we are invoking
     *@param pMeth the method we are invoking
     *@param pArgument a @see String representation of the argument we will be using
     *@param pDateFormatPattern the date format to use to convert the String argument
     *  to a @see java.util.Data object
     */
    public static void populateJavaBeanSetterMethod(Object pObj,Method pMeth, Object pArgument,String pDateFormatPattern) throws
    IllegalAccessException,IllegalArgumentException,InvocationTargetException{
      populateJavaBeanSetterMethod(pObj,pMeth,pArgument,pDateFormatPattern,false);
    }
    
    /**
     *will populate the method object with the propertyValue
     *property from this object.
     *@param pObj the object of the method we are invoking
     *@param pMeth the method we are invoking
     *@param pArgument a @see String representation of the argument we will be using
     *@param pDateFormatPattern the date format to use to convert the String argument
     *  to a @see java.util.Data object
     *@param pExcelDataSource wheather data source is excel so we can parse the date correctly
     */
    public static void populateJavaBeanSetterMethod(Object pObj,Method pMeth, Object pArgument,String pDateFormatPattern, boolean pExcelDataSource) throws
    IllegalAccessException,IllegalArgumentException,InvocationTargetException{
        Class lParams = pMeth.getParameterTypes()[0];
        Object [] params = new Object [1];
        if (lParams.getName().equals("java.lang.String")){
            String s;
            if(pArgument!=null){
                s = pArgument.toString();
                s = s.trim();
            }else{
                s = "";
            }
            params[0] = s;
            pMeth.invoke(pObj,params);
        } else if((lParams.getName().equals("int")) || (lParams.getName().equals("java.lang.Integer"))){
            if(pArgument instanceof Integer){
                params[0] = pArgument;
            }else{
              BigDecimal b = new BigDecimal(pArgument.toString());
              b.setScale(0, BigDecimal.ROUND_UNNECESSARY);
                params[0] = new Integer(b.intValue());
            }
            pMeth.invoke(pObj,params);
        } else if((lParams.getName().equals("long")) || (lParams.getName().equals("java.lang.Long"))){
            if(pArgument instanceof Long){
                params[0] = pArgument;
            }else{
              BigDecimal b = new BigDecimal(pArgument.toString());
              b.setScale(0, BigDecimal.ROUND_UNNECESSARY);
                params[0] = new Long(b.longValue());
            }
            pMeth.invoke(pObj,params);
        } else if(lParams.getName().equals("java.math.BigDecimal")){
            if(pArgument instanceof BigDecimal){
                params[0] = pArgument;
            }else{
                String argS = pArgument.toString();
                argS = argS.trim();
                argS = replaceString(argS, ",", ""); //remove any commas
                argS = trimLeft(argS,"0"); //remove any left 0s  i.e. 00012 = 12
                if(argS.equals("-")){
                    params[0] = new BigDecimal(0);
                }else{
                    if(argS != null && argS.length() != 0){
                        params[0] = new java.math.BigDecimal(argS);
                    }else{
                        params[0] = new BigDecimal(0);
                    }
                }
            }
            pMeth.invoke(pObj,params);
        } else if (lParams.getName().equals("java.util.Date")){
            if(pArgument instanceof java.util.Date){
                params[0] = pArgument;
            }else{
              if(pExcelDataSource && pArgument instanceof Double){
                params[0] = HSSFDateUtil.getJavaDate(((Double)pArgument).doubleValue());
              }else{
                  SimpleDateFormat f = new SimpleDateFormat(pDateFormatPattern);
                  try{
                      params[0] = f.parse(pArgument.toString());
                  }catch(ParseException e){
                      e.printStackTrace();
                      throw new IllegalArgumentException("could not parse date: " + pArgument);
                  }
              }
            }
            pMeth.invoke(pObj,params);
        } else if(lParams.getName().equals("boolean") || lParams.getName().equals("java.lang.Boolean")){
          if(pArgument instanceof Boolean){
                params[0] = pArgument;
            }else{
                params[0] = new Boolean(isTrue(pArgument.toString()));
            }
            pMeth.invoke(pObj,params);
        } else if(lParams.getName().equals("java.util.List")){
        	logger.info("populateJavaBeanSetter() == > LIST begin" );
            if(pArgument instanceof List){
                  params[0] = pArgument;
              }else if (pArgument instanceof String){
            	  String[] els = ((String)pArgument).split(",");
            	  logger.info("populateJavaBeanSetter() == > LIST :" + els );
            	  List array = new ArrayList();
            	  for (int i=0; i<els.length; i++){
            		 array.add(els[i]); 
            	  }
                  params[0] = array;
              } else if (pArgument instanceof Object){
            	  List array = new ArrayList();
            	  array.add(pArgument);
            	  params[0] = array;
              } else {
            	  throw new IllegalArgumentException("illegal arg for type List: " + pArgument);
              }
              pMeth.invoke(pObj,params);
        } else {
            throw new IllegalArgumentException("cannot populate method "+pMeth.getName()+" with arg of type: " + lParams.getName());
        }
    }
    
    /**
     *Finds the getter method of the passed in object based off the javaBean property.
     *Thus myProp would find the method getMyProp of the passed in object.
     */
    public static Method getJavaBeanGetterMethod(Object pObj, String javaBeanProperty){
        //convert the javaBeanProperty to as set method
        char firstChar = Character.toUpperCase(javaBeanProperty.charAt(0));
        String theRest = javaBeanProperty.substring(1);
        String methodName = "get" + firstChar + theRest;
        Method[] methods = pObj.getClass().getMethods();
        for(int i=0;i<methods.length;i++){
            Method method = methods[i];
            if(method.getName().equals(methodName)){
                return method;
            }
        }
        methodName = "is" + firstChar + theRest;
        for(int i=0;i<methods.length;i++){
            Method method = methods[i];
            if(method.getName().equals(methodName)){
                return method;
            }
        }
        return null;
    }
    
    /**
     *@see replaceString(StringBuffer, String, String)
     */
      public static String replaceString(String pSource,String pSubString, String pReplaceString){
          StringBuffer buf = new StringBuffer(pSource);
          replaceString(buf, pSubString, pReplaceString);
          return buf.toString();
      }
      
      /**
       *Replaces all occurrences of a subString in a given stringBuffer with a specified replace value.
       *@PARAM pBuf - the StringBuffer to operate on
       *@PARAM pSubString - the String to replace
       *@PARAM pReplaceString - the String to substitute for pSubString
       *@RETURN the modified String that was passed in, returns an unmodified version of the
       *        string if no occurrences of pSubString were found
       */
      public static void replaceString(StringBuffer pBuf,
                                       String pSubString, String pReplaceString){
          int lSubStrLen=pSubString.length();
          if ( pSubString.equals(pReplaceString) ||
               pBuf.length() <= 0 )
          {
              return;
          }

          int origLen = pBuf.length();
          for(int i= origLen - 1; i >= 0; i--){
              if ( i+lSubStrLen > origLen ) continue;
              if ( i+lSubStrLen > pBuf.length() ) continue;
              String lSubStr = pBuf.substring(i,i+lSubStrLen);
              if (lSubStr.equals(pSubString)){
                  pBuf.delete(i,i+lSubStrLen);
                  pBuf.insert(i,pReplaceString);
              }
          }
      }
      
    //trims all of the left most occurences of a given string
      //trimRight("aaabcdefg", "aa") will return abcdefg
      static public String trimLeft(String pVal, String pTrimPattern){
          if(pVal == null || pTrimPattern == null){
            return pVal;
          }
          int trimLen = pTrimPattern.length();
          while(pVal.startsWith(pTrimPattern)){
              pVal = pVal.substring(trimLen);
          }
          return pVal;
      }

      //trims all of the right most occurences of a given string
      //trimRight("abcdefggg", "gg") will return abcdefg
      static public String trimRight(String pVal, String pTrimPattern){
          if(pVal == null || pTrimPattern == null){
            return pVal;
          }
          int trimLen = pTrimPattern.length();
          while(pVal.endsWith(pTrimPattern)){
              pVal = pVal.substring(0,pVal.length() - trimLen);
          }
          return pVal;
      }
}
