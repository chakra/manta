/*
 * BaseLoader.java
 *
 * Created on March 7, 2005, 9:38 AM
 */

package com.espendwise.manta.loader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import org.apache.poi.hssf.record.UnicodeString;

import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.DelimitedParser;
import com.espendwise.manta.util.ExcelReader;
import com.espendwise.manta.util.ResponseErrorException;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRuleResult;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.ocean.common.webaccess.ResponseError;

/**
 * Will parse a flat file based off the values defined in the trading partner screen.
 * May be overidden if additional processing is needed, but if it is a simple file to object
 * conversion no addition programming is needed.
 * @author bstevens
 */

public abstract class BaseLoader implements IInputStreamParser{
	protected Logger log = Logger.getLogger(this.getClass());
	/** Holds value of property sepertorChar. */
	private char sepertorChar = ',';

	/** Holds value of property quoteChar. */
	private char quoteChar = '\"';
	protected Long storeId = null;
	protected EntityManager entityManager;
	protected Locale locale;
	protected InputStream inputStream = null;
	protected String streamType = null;

	protected HashMap<Integer, String> columnNumberMap = new HashMap<Integer, String>(); // index start with 0
	private List<String> headerColumns = null;
	protected int currentLineNumber = 0; // index start with 1
	protected boolean useHeaderLine = true;
	protected boolean hasHeaderLine = true;
	protected final static String actionAdd = "A";
	protected final static String actionChange = "C";
	protected final static String actionDelete = "D";
	protected int addedCount=0;
	protected int modifiedCount=0;
	protected int deletedCount=0;
	protected Date runDate = new Date();
	
	private String dateFormat = "MM/dd/yyyy";
	private List<ResponseError> errors = new ArrayList<ResponseError>();

	public BaseLoader(EntityManager entityManager, Locale locale, Long currStoreId, InputStream inputStream,String streamType) {
		this.entityManager = entityManager;
		this.locale = locale;
		this.storeId = currStoreId;
		this.inputStream = inputStream;
		this.streamType = streamType;
	}
	/**
	 *Translates a stream of varying types.  This implementation supports xls and flat text.
	 *InputStream the input stream to operate on
	 *String the stream type (such as xls, xlsx, txt, etc.)
	 */
	public void translate() {
		try{
			currentLineNumber = 1;
			if(streamType.equalsIgnoreCase("xls") || streamType.equalsIgnoreCase("xlsx")){
				ExcelReader reader = new ExcelReader(inputStream);
				ExcelReader xlsRdr = (ExcelReader) reader;
				xlsRdr.processFile(this);
			}else{
				DelimitedParser delimitedParser = new DelimitedParser(inputStream);
				delimitedParser.processFile(this, sepertorChar, quoteChar);
			}
			doPostProcessing();
		}catch(Exception e){
			e.printStackTrace();
			if (e instanceof ResponseErrorException)
				appendErrors(((ResponseErrorException)e).getError());
			else{
				if (e.getMessage()==null){
					appendErrors(e.toString());
				}else{
					appendErrors(e.getMessage());
				}
			}
		}
	}
	
	/**
	 *passed in the parsed line will preform the necessary logic of populating the object
	 */
	public void parseLine(List pParsedLine) throws Exception{
		if(hasHeaderLine && (currentLineNumber == 1)){
			if(useHeaderLine){
				parseHeaderLine(pParsedLine);
			}else{
				List<String> headers = getHeaderColumns();
				for (int i = 0; i < headers.size(); i++)
					columnNumberMap.put(new Integer(i), headers.get(i));
			}
		}else{
			parseDetailLine(pParsedLine);
		}
		currentLineNumber++;
	}
	
	/**
	 *Parses the header lines into column number associations for ex:
	 *HEADER_A, HEADER_B
	 *where the definition is:
	 *HEADER_A maps to property headerA
	 *HEADER_B maps to property headerB
	 *becomes the following key pair map:
	 *0, headerA
	 *1, headerB
	 *etc
	 * @throws Exception
	 */
	protected void parseHeaderLine(List pParsedLine) throws Exception{
		log.info("=== BaseLoader -> parseHeaderLine ===");
		int ct = 0;
		Iterator it = pParsedLine.iterator();
		while(it.hasNext()){
			String s = (String) it.next();
			if(s != null){
				s = s.trim();
			}
			if (!Utility.isSet(s) && ct < getHeaderColumns().size()){
				s = getHeaderColumns().get(ct);
			}			
			columnNumberMap.put(new Integer(ct++), s);
		}		
		
		while (ct < getHeaderColumns().size()){
			columnNumberMap.put(new Integer(ct), getHeaderColumns().get(ct));
			ct++;
		}
	}
	
	abstract protected void parseDetailLine(List pParsedLine) throws Exception;	

	public boolean isEmpty(List pParsedLine){
		if(pParsedLine == null || pParsedLine.isEmpty()){
			return true;
		}
		Iterator it =pParsedLine.iterator();
		while(it.hasNext()){
			Object aRecord = it.next();
			if(aRecord != null){
				if(aRecord instanceof String){
					if(Utility.isSet((String) aRecord)){
						return false;
					}
				}else if(aRecord instanceof UnicodeString){
					if(Utility.isSet(((UnicodeString) aRecord).getString())){
						return false;
					}
				}else{
					if(Utility.isSet(aRecord.toString())){
						return false;
					}
				}
			}
		}
		return true;
	}
	

	/**
	 *Returns the current line number that we are parsing
	 */
	protected int getCurrentLineNumber(){
		return currentLineNumber;
	}

	protected HashMap getColumMap(){
		return columnNumberMap;
	}

	

	/**
	 * This method is called when the file is done processing.  Implementing methods should
	 * check the failed flag if post processing should only be done for successfully processed
	 * files.  The default implementation is to do nothing.
	 * @throws Exception
	 */
	abstract public void doPostProcessing() throws Exception;


	/** Getter for property sepertorChar (by default this is set to a comma.
	 * @return Value of property sepertorChar.
	 *
	 */
	public char getSepertorChar() {
		return this.sepertorChar;
	}

	/** Setter for property sepertorChar.
	 * @param sepertorChar New value of property sepertorChar.
	 *
	 */
	public void setSepertorChar(char sepertorChar) {
		this.sepertorChar = sepertorChar;
	}

	/** Getter for property quoteChar.
	 * @return Value of property quoteChar.
	 *
	 */
	public char getQuoteChar() {
		return this.quoteChar;
	}

	/** Setter for property quoteChar.
	 * @param quoteChar New value of property quoteChar.
	 *
	 */
	public void setQuoteChar(char quoteChar) {
		this.quoteChar = quoteChar;
	}
	
	public void setStoreId(Long currStoreId) {
		this.storeId = currStoreId;
	}

	public Long getStoreId() {
		return storeId;
	}	
	
	// add error message
	protected void appendErrors(String messageKey, Object... args) {
		ResponseError error = getResponseError(messageKey, args);
		getErrors().add(getResponseError(messageKey, args));
	}
	
	protected void appendErrorsOnLine(String messageKey, Object... args) {
		ResponseError error = getResponseError(messageKey, args);
		ResponseError existError = getErrorOnLine(error);
		if (existError == null){
			error = getResponseError("validation.loader.error.errorOnLine", currentLineNumber, error);
			getErrors().add(error);
		}else{
			existError.getArgs()[0] = existError.getArgs()[0] + ", " + currentLineNumber;
		}		
	}
	
	protected void appendErrorsOnLines(List<Integer> lines, ResponseError error) {
		String lineNums = null;
		for (int line:lines){
			if (lineNums==null)
				lineNums = line+"";
			else
				lineNums += ", " + line;
		}
		appendErrors("validation.loader.error.errorOnLine", lineNums, error);
	}
	
	// return same error with same key and args
	protected ResponseError getErrorOnLine(ResponseError error){
		for (ResponseError respError : getErrors()){
			if (Utility.isEqual(respError.getKey(), "validation.loader.error.errorOnLine")){
				ResponseError inError = (ResponseError) respError.getArgs()[1];
				if (Utility.isEqual(inError.getKey(), error.getKey())){
					if (inError.getArgs().length == error.getArgs().length){
						boolean found = true;
						for (int i = 0; i < inError.getArgs().length && found; i++){
							if (!Utility.isEqual(inError.getArgs()[i].toString(), error.getArgs()[i].toString())){
								found = false;
							}
						}
						if (found)
							return respError;
					}
				}
			}
		}
		return null;		
	}
	
	protected void appendErrors(ResponseError error) {
		getErrors().add(error);		
	}
	
	protected void appendErrors(String message) {
		ResponseError error = new ResponseError(message);
		getErrors().add(error);	
	}
	
	
	public ResponseError getResponseError(String messageKey, Object... args) {
		return new ResponseError(null, null, null, messageKey, args);
	}
	
	public ResponseError getErrorOnLineResponseError(String messageKey, Object... args) {
		ResponseError error = getResponseError(messageKey, args);
		return getResponseError("validation.loader.error.errorOnLine", currentLineNumber, error);
	}

	public void setErrors(List<ResponseError> errors) {
		this.errors = errors;
	}

	public List<ResponseError> getErrors() {
		return errors;
	}

	public void setHeaderColumns(List<String> headerColumns) {
		this.headerColumns = headerColumns;
	}

	public List<String> getHeaderColumns() {
		if (headerColumns == null)
			headerColumns = getHeaderColumnsAbs();
		return headerColumns;
	}
	
	// need to be implemented by extended class
	public abstract List<String> getHeaderColumnsAbs();
	
	/**
	 * Validate the data for required, minimum and maximum length. Set value to beanObj.
	 * @param columnName
	 * @param beanProp
	 * @param value
	 * @param beanObj
	 * @param required
	 * @param lineNum
	 * @throws Exception
	 */
	protected boolean validateAndPopulateProperty(int columnIndex, String beanProp, List parsedLine, Object beanObj, 
			boolean required) throws Exception{
		return validateAndPopulateProperty(columnIndex, beanProp, parsedLine, beanObj, required, null, -1, -1);
	}
	protected boolean validateAndPopulateProperty(int columnIndex, String beanProp, List parsedLine, Object beanObj, 
			boolean required, String defaultValue, int minLength, int maxLength) throws Exception{
		String columnName = columnNumberMap.get(columnIndex);
		Object value = null;
		if (columnIndex < parsedLine.size())			
			value = parsedLine.get(columnIndex);
		boolean valueSet = true;
		if (value != null && value instanceof String)
			value = Utility.trim((String)value);
		if (value == null || !Utility.isSet(value.toString()))
			valueSet = false;
		
		if (!valueSet && defaultValue!=null)
			value = defaultValue;
		if (!valueSet && required){
			appendErrorsOnLine("validation.loader.error.emptyValue", columnName);
			return false;
		}
					
		if (!valueSet)
			return true;
		
		if (maxLength==-1)
			maxLength=255;
		if (minLength > 0  || maxLength > 0){
			int length = value.toString().length();
			if (minLength > 0 && length < minLength){
				appendErrorsOnLine("validation.loader.error.stringRangeLess", columnName, minLength);
				return false;
			}
			if (maxLength > 0 && length > maxLength){
				appendErrorsOnLine("validation.loader.error.stringRangeOut", columnName, maxLength);
				return false;
			}
		}	
		
		Method meth = Utility.getJavaBeanSetterMethod(beanObj,beanProp);
		if(meth == null){
			throw new RuntimeException("No setter found for property: "+beanProp);
		}
		try{
			Utility.populateJavaBeanSetterMethod(beanObj, meth,value,dateFormat);
		}catch(Exception e){
			if (e instanceof NumberFormatException){
				appendErrorsOnLine("validation.loader.error.columnMustBeNumber", columnName);
				return false;
			}else{
				Class lParams = meth.getParameterTypes()[0];
				if (lParams.getName().equals("java.util.Date")){
					appendErrorsOnLine("validation.loader.error.columnMustBeDate", columnName);
					return false;
				}else{
					//validation.loader.error.errorParsingData=Error parsing column '{0}': {1}
					appendErrorsOnLine("validation.loader.error.errorParsingData", columnName, value);
					return false;
				}
			}			
		}
		
			
		return true;
	}
	
	public static String getString(Object[] objects){
		String temp = "[";
		for (int i = 0; i < objects.length; i++){
			temp += objects[i].toString();
			if (i != objects.length-1)
				temp += ",";			
		}
		temp += "]";
		return temp;
	}
	
	public void setWebErrors(WebErrors webErrors, Locale locale) {
		List<ResponseError> errors = getErrors();
    	for (ResponseError error : errors){
    		Object[] args = error.getArgs();
    		if (args != null){
	    		for (int i = 0; i < args.length; i++){
	    			if (args[i] instanceof ResponseError){
	    				ResponseError error1 = (ResponseError)args[i];
	    				args[i] = I18nUtil.getMessage(locale, (String)error1.getKey(), error1.getArgs());;
	    			}
	    		}
    		}
    		if (error.getArgs() != null)
    			webErrors.putError(new WebError(error.getKey(), Args.typed(error.getArgs())));
    		else{
    			webErrors.putError(new WebError("dummyMessageKey", null, error.getMessage()));
    		}	    			
    	}
	}
		
	protected void processErrorsOnline(Map<String, String> map, String errorKey){
		if (map.size() > 0){
			for (Iterator<String> it = map.keySet().iterator(); it.hasNext();){
				String columnName = it.next();
				String columnIndexList = map.get(columnName);
				ResponseError error = getResponseError(errorKey, new Object[]{columnName});
				appendErrors("validation.loader.error.errorOnLine", new Object[]{columnIndexList, error});
			}
		}
	}

	public ValidationRuleResult getValidationResult() {
		ValidationRuleResult result = new ValidationRuleResult();
    	
		for (ResponseError error : getErrors()){
    		Object[] args = error.getArgs();
    		if (args != null){
	    		for (int i = 0; i < args.length; i++){
	    			if (args[i] instanceof ResponseError){
	    				ResponseError error1 = (ResponseError)args[i];
	    				args[i] = I18nUtil.getMessage(locale, (String)error1.getKey(), error1.getArgs());
	    			}
	    		}
    		}
    		if (error.getArgs() != null){ 		
    			String message = I18nUtil.getMessage(locale, (String)error.getKey(), error.getArgs());
    			result.failed(ExceptionReason.LoaderReason.GENERAL_ERROR, Args.typed(message));
    		}else{
    			result.failed(ExceptionReason.LoaderReason.GENERAL_ERROR, Args.typed(error.getMessage()));
    		}	    			
    	}
		if (!result.isFailed()) {
			result.success();
		}
    	
    	return result;
	}
	public void setAddedCount(int addedCount) {
		this.addedCount = addedCount;
	}
	public int getAddedCount() {
		return addedCount;
	}
	public void setModifiedCount(int modifiedCount) {
		this.modifiedCount = modifiedCount;
	}
	public int getModifiedCount() {
		return modifiedCount;
	}
	public void setDeletedCount(int deletedCount) {
		this.deletedCount = deletedCount;
	}
	public int getDeletedCount() {
		return deletedCount;
	}
	
	protected List<String> getStringList(String str){
		List<String> returnList = new ArrayList<String>();
		if (Utility.isSet(str)){
			String[] list = str.split(",");
			for (String temp : list){
				if (Utility.isSet(temp))
					returnList.add(temp.trim());
			}
		}
		return returnList;
	}
	
	protected String getDelimilatedString(List<String> strList) {
		String returnStr = null;
		for (String str : strList){
			if (returnStr == null)
				returnStr = str;
			else
				returnStr += ","+str;
		}
		return returnStr;
	}
	
}
