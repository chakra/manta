/*
 * ReportingUtils.java
 *
 * Created on February 3, 2003, 10:43 PM
 */

package com.espendwise.manta.util;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.GenericReportColumnView;

//import org.apache.poi.ss.usermodel.IndexedColors;

public class ReportingUtil {
	public static final String DEFAULT_REPORT_NAME="Detail";
	
    public static final String PAGE_TITLE = "PageTitle";
    public static final String PAGE_FOOTER = "PageFooter";
    public static final String TABLE_HEADER = "TableHeader";
    public static final String TABLE_DATA = "TableData";
    public static final String NEGATIVE_NUMBER = "NegativeNumber";
    public static final String GROUP_HEADER = "GroupHeader";
    public static final String GROUP_TOTAL = "GroupFooter";

    public static final String DATE_STYLE = "DateStyle";
    public static final String TIME_STYLE = "TimeStyle";
    public static final String INTEGER_STYLE = "IntegerStyle";
    public static final String INTEGER_SEPARATOR_STYLE = "IntegerSeparatorStyle";
    public static final String FLOAT_STYLE = "FloatStyle";
    public static final String FLOAT_SEPARATOR_STYLE = "FloatSeparatorStyle";
    public static final String ACCOUNTING_STYLE = "AccountingStyle";
    public static final String NEGATIVE_PERCENT_STYLE = "NegativePercentStyle";
    public static final String PERCENT_STYLE = "PercentStyle";

    public interface ALIGN {
    	public static final String LEFT = "LEFT",
                                   RIGHT = "RIGHT",
                                   CENTER = "CENTER",
                                   GENERAL = "GENERAL",
                                   JUSTIFY = "JUSTIFY";
    }

    public interface DATA_CATEGORY {
        public static final String DATE = "DATE",
                                   TIME = "TIME",
                                   NUMBER = "NUMBER",
                                   INTEGER = "INTEGER",
                                   FLOAT = "FLOAT",
                                   CARRENCY = "CARRENCY",
                                   ACCOUNTING = "ACCOUNTING",
                                   PERCENTAGE_NEGATIVE = "PERCENTAGE_NEGATIVE",
                                   PERCENTAGE = "PERCENTAGE";
    }


    public static int DEFAULT_COLUMN_WIDTH = 100;
    public static int MAX_DOWLOAD_REPORT_ROWS = 65000;

    private ReportingUtil() {
    }

    /**
     *Creates a GenericReportColumnView from the suplied input.  This is a convinience method
     *to create this object in one line as opposed to 5
     */
    public static GenericReportColumnView createGenericReportColumnView(
    String pColumnClass, String pColumnName, int pColumnPrecision, int pColumnScale,String pColumnType){
        return createGenericReportColumnView(pColumnClass, pColumnName,
					     pColumnPrecision, pColumnScale,pColumnType,"*", false, null);
    }
    public static GenericReportColumnView createGenericReportColumnView(
    String pColumnClass, String pColumnName, int pColumnPrecision, int pColumnScale,String pColumnType,
      String pColumnWidth, boolean pTotalRequestFlag){
        return createGenericReportColumnView(pColumnClass, pColumnName,
					pColumnPrecision, pColumnScale,pColumnType, pColumnWidth, pTotalRequestFlag, null);
    }
    public static GenericReportColumnView createGenericReportColumnView(
    String pColumnClass, String pColumnName, int pColumnPrecision, int pColumnScale,String pColumnType,
      String pColumnWidth, boolean pTotalRequestFlag, String pColumnFormat){
      return createGenericReportColumnView(pColumnClass, pColumnName,
                                      pColumnPrecision, pColumnScale,pColumnType, pColumnWidth, pTotalRequestFlag, pColumnFormat,null,null);

    }

    public static GenericReportColumnView createGenericReportColumnView(
    String pColumnName, String pColumnHeaderStyle, String pColumnDataStyle, String pColumnWidth ){
        return createGenericReportColumnView( null, pColumnName,
               0,0,null,pColumnWidth,false, null,  pColumnHeaderStyle, pColumnDataStyle );
    }

    /**
     *Creates a GenericReportColumnView from the suplied input.  This is a convinience method
     *to create this object in one line as opposed to 5
     */
    public static GenericReportColumnView createGenericReportColumnView(
    String pColumnClass, String pColumnName, int pColumnPrecision, int pColumnScale,String pColumnType,
      String pColumnWidth, boolean pTotalRequestFlag, String pColumnFormat, String pColumnHeaderStyle, String pColumnDataStyle){
        GenericReportColumnView vw = new GenericReportColumnView();
        vw.setColumnClass(pColumnClass);
        vw.setColumnName(pColumnName);
        vw.setColumnPrecision(pColumnPrecision);
        vw.setColumnScale(pColumnScale);
        vw.setColumnType(pColumnType);
        vw.setColumnWidth(pColumnWidth);
        vw.setTotalRequestFlag(pTotalRequestFlag);
        vw.setColumnFormat(pColumnFormat);
        vw.setColumnHeaderStyleName(pColumnHeaderStyle);
        vw.setColumnDataStyleName(pColumnDataStyle);
        return vw;
    }
    
    public static void initializeResponseForExcelExport(WebRequest request, HttpServletResponse response, String fileName) {
        response.setContentType("application/x-excel");
    	String browser = Utility.strNN((String) request.getHeader("User-Agent")).toUpperCase();
    	boolean isMSIE6 = browser.indexOf("MSIE 6") >= 0;
    	boolean isMSIE8 = browser.indexOf("MSIE 8") >= 0;
    	if (isMSIE6 || isMSIE8) {
    		response.setHeader("Pragma", "public");
    		response.setHeader("Expires", "0");
    		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    		response.setHeader("Cache-Control", "public");
    	}
    	else {
    		response.setHeader("extension", "xls");
    	}
    	response.setHeader("Content-disposition", "attachment; filename=" + fileName);
    }

    // Column name based methods.
    public static boolean isColumnForMoney(String pColName) {
	if ( pColName.toLowerCase().endsWith("_money") ||
	     pColName.endsWith("$") ) {
	    return true;
	}
	return false;
    }
    
    public static boolean isColumnForPercent(String pColName) {
	if ( pColName.toLowerCase().endsWith("_pct") ||
	     pColName.endsWith("%") ) {
	    return true;
	}
	return false;
    }
    
    public static boolean isColumnForTime(String pColName) {
	if ( pColName.toLowerCase().endsWith("time")) {
	    return true;
	}
	return false;
    }
    
    public static String extractColumnName(String pColNameIn) {
	if (pColNameIn.toLowerCase().endsWith("_pct") ) {
	    pColNameIn = pColNameIn.substring(0, pColNameIn.length()-4);
	}
	if (pColNameIn.toLowerCase().endsWith("_money") ) {
	    pColNameIn = pColNameIn.substring(0, pColNameIn.length()-6);
	}
	if (pColNameIn.endsWith("$") || pColNameIn.endsWith("%") ) {
	    pColNameIn = pColNameIn.substring(0, pColNameIn.length()-1);
	}
	return pColNameIn;
    }
    
    public static String makeColumnKey(String pColName) {
    	return "report.column." + pColName;
    }

}
