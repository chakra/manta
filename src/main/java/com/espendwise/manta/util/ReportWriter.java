package com.espendwise.manta.util;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.espendwise.manta.model.view.GenericReportCellView;
import com.espendwise.manta.model.view.GenericReportColumnView;
import com.espendwise.manta.model.view.GenericReportResultView;
import com.espendwise.manta.model.view.GenericReportStyleView;

public class ReportWriter {

	public static String TIMES_FONT = "Times New Roman" ;
    public interface DEFAULT_FONT {
    	public static final String SIZE = "10",
                                   NAME = TIMES_FONT;
    }
    public static double DEFAULT_WIDTH_FACTOR = 1.5;
    public static Map fontCollection;
    public static Map styleCollection;

	private ReportWriter() {
	}

    public static void writeExcelReport(GenericReportResultView repRes, OutputStream pOut) throws Exception {
    	writeExcelReport(repRes, pOut, ".xls");
    }

	/**
	 *Spools the GenericReportResultView to the specified output stream.
	 *
	 *@param GenericReportResultView
	 *@param OutputStream the output stream to write the Excep report to
	 */
	public static void writeExcelReport(GenericReportResultView repRes, OutputStream pOut, String xslType) throws Exception {
		HSSFWorkbook workbook = null;
		
        if(Utility.isSet(xslType) && xslType.equals(".xlsx")) {
        } 
        else {
        	workbook = new HSSFWorkbook();
        }
        
        if (repRes == null) {
        	workbook.write(pOut);
            return;
        }

        int columnCount = repRes.getColumnCount();
        List<GenericReportColumnView> header = repRes.getHeader();

        fontCollection = createFonts(workbook, repRes.getUserStyle());
        styleCollection = createStyles(workbook, repRes.getUserStyle());

		//prepare formats
        HSSFCellStyle[] columnXlsStyle = new HSSFCellStyle[columnCount];
		for (int ii = 0; ii < columnCount; ii++) {
			GenericReportColumnView column = (GenericReportColumnView) header.get(ii);
			String colName = column.getColumnName();
			String colClass = column.getColumnClass();
			int colScale = column.getColumnScale();
            int colPrecision = column.getColumnPrecision();
            if (colClass != null ) {
            	if (colClass.equals("java.math.BigDecimal")) {
            		String columnFormat = column.getColumnFormat();
                    if (Utility.isSet(columnFormat)) {
                    	HSSFCellStyle style = workbook.createCellStyle();
                        style.setFont((HSSFFont)fontCollection.get(ReportingUtil.TABLE_DATA));
                        style.setDataFormat(workbook.createDataFormat().getFormat(columnFormat));
                        columnXlsStyle[ii] = style;
                    } 
                    else {
                    	if (ReportingUtil.isColumnForMoney(colName)) {
                    		colName = ReportingUtil.extractColumnName(colName);
                            columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.ACCOUNTING_STYLE);
                        } 
                    	else if (ReportingUtil.isColumnForPercent(colName)) {
                    		colName = ReportingUtil.extractColumnName(colName);
                            columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.PERCENT_STYLE);
                        } 
                    	else if (colPrecision == 0) {
                            columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.INTEGER_STYLE);
                        } 
                    	else {
                            columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.FLOAT_STYLE);
                        }
                    }
			    } 
            	else if (colClass.equals("java.sql.Timestamp")) {
            		if (ReportingUtil.isColumnForTime(colName)) {
            			columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.TIME_STYLE);
            		} 
            		else {
            			columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.DATE_STYLE);
            		}
            	} 
            	else if (colClass.equals("java.lang.Integer")) {
            		String columnFormat = column.getColumnFormat();
                    if (Utility.isSet(columnFormat)) {
                    	HSSFCellStyle style = workbook.createCellStyle();
                        style.setFont((HSSFFont)fontCollection.get(ReportingUtil.TABLE_DATA));
                        style.setDataFormat(workbook.createDataFormat().getFormat(columnFormat));
                        columnXlsStyle[ii] = style;
                    } 
                    else {
                    	columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.INTEGER_STYLE);
                    }
            	} 
            	else {
            		columnXlsStyle[ii] = null;
            	}
            }
        }

		int pageSize = 65000;
		int shift = 0;
		List table = repRes.getTable();
		int pageCount = table.size() / pageSize;
		if (table.size() % pageSize > 0) {
			pageCount++;
		}
		if (pageCount == 0) {
			pageCount++;
		}
		for (int curPage = 0; curPage < pageCount; curPage++) {
			String sheetName = repRes.getName();
			if (sheetName == null || sheetName.trim().length() == 0) {
				sheetName = "Sheet" + curPage;
			}
			if(sheetName.length() > 29) {
				sheetName = sheetName.substring(0,29);
			}
            HSSFSheet sheet = workbook.createSheet(sheetName);
			//Make header
			for (int ii = 0; ii < columnCount; ii++) {
				GenericReportColumnView column = (GenericReportColumnView) header.get(ii);
				String colName = column.getColumnName();
				colName = ReportingUtil.extractColumnName(colName);
				String tColKey = "";
				if (colName.toLowerCase().startsWith("rowInfo_currency")) {
					tColKey = ReportingUtil.makeColumnKey("Currency");
				} else {
					tColKey = ReportingUtil.makeColumnKey(colName);
				}
				setHeader(sheet, ii, colName, (HSSFCellStyle)styleCollection.get(ReportingUtil.TABLE_HEADER));
			}

			int startRow = curPage * pageSize;
			int endRowNext = startRow + pageSize;
			if (endRowNext > table.size()) {
				endRowNext = table.size();
			}
			for (int ii = endRowNext - 1; ii >= startRow; ii--) {
				List repRow = (List) table.get(ii);
                HSSFRow row = sheet.createRow(startRow + ii + 1);
				for (int jj = 0; jj < repRow.size(); jj++) {
					GenericReportColumnView col = (GenericReportColumnView) header.get(jj);
					Object obj = repRow.get(jj);
					String thisCurrencyCode = null;
					if (obj instanceof java.lang.String) {
						String t = (java.lang.String) obj;
						if (t.startsWith("rowInfo_currency")) {
							thisCurrencyCode = t.substring(19);
						}
					}
					if (null != thisCurrencyCode) {
						setCellNoNull(sheet, jj, row, thisCurrencyCode, null);
					} 
					else {
						setCellNoNull(sheet, jj, row, obj, columnXlsStyle[jj]);
					}
				}
			}
		}
		workbook.write(pOut);
	}

	/**
	 *Spools the GenericReportResultView to the specified output stream.
	 *
	 *@param GenericReportResultView
	 *@param OutputStream the output stream to write the Excep report to
	 */
	public static void writeExcelReport(GenericReportResultView repRes, String pSheetName, HSSFWorkbook pWorkbook) throws Exception {
		int columnCount = repRes.getColumnCount();
        List<GenericReportColumnView> header = repRes.getHeader();
        fontCollection = createFonts(pWorkbook, repRes.getUserStyle());
        styleCollection = createStyles(pWorkbook, repRes.getUserStyle() );
		//Determine XLS column type
        HSSFCellStyle[] columnXlsStyle = new HSSFCellStyle[columnCount];
        int tableWidth = 0;
		for (int ii = 0; ii < columnCount; ii++) {
			GenericReportColumnView column = (GenericReportColumnView) header.get(ii);
			String colName = column.getColumnName();
			String colClass = column.getColumnClass();
			int colScale = column.getColumnScale();
            int colPrecision = column.getColumnPrecision();
            int colWidth = 0;
            try {
            	colWidth = new Integer(column.getColumnWidth()).intValue();
            }
            catch (Exception e) {
               	//nothing to do, default to the original setting of 0
            }
            String colDataStyle = column.getColumnDataStyleName();
            if (Utility.isSet(colDataStyle) && styleCollection.containsKey(colDataStyle)) {
            	columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(colDataStyle);
            } 
            else if (colClass != null) {
            	if (colClass.equals("java.math.BigDecimal")) {
            		String columnFormat = column.getColumnFormat();
                    if (Utility.isSet(columnFormat)) {
                    	HSSFCellStyle style = pWorkbook.createCellStyle();
                        style.setFont((HSSFFont)fontCollection.get(ReportingUtil.TABLE_DATA));
                        style.setDataFormat(pWorkbook.createDataFormat().getFormat(columnFormat));
                        columnXlsStyle[ii] = style;
                    } 
                    else {
                    	if (ReportingUtil.isColumnForMoney(colName)) {
                    		colName = ReportingUtil.extractColumnName(colName);
                            columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.ACCOUNTING_STYLE) ;
                        } 
                    	else if (ReportingUtil.isColumnForPercent(colName)) {
                            colName = ReportingUtil.extractColumnName(colName);
                            columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.PERCENT_STYLE) ;
                        } 
                    	else if (colScale == 0) {
                            columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.INTEGER_STYLE) ;
                        } 
                    	else {
                            columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.FLOAT_STYLE) ;
                        }
                    }
                } 
            	else if (colClass.equals("java.math.BigDecimal.Separator")) {
            		if (colPrecision == 0) {
            			HSSFCellStyle style = (HSSFCellStyle)styleCollection.get(ReportingUtil.INTEGER_SEPARATOR_STYLE);
                        columnXlsStyle[ii] = style ;
                    } 
            		else {
                        HSSFCellStyle style = (HSSFCellStyle)styleCollection.get(ReportingUtil.FLOAT_SEPARATOR_STYLE);
                        columnXlsStyle[ii] = style ;
                    }
                } 
            	else if (colClass.equals("java.sql.Timestamp")) {
                	if (ReportingUtil.isColumnForTime(colName)) {
                		columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.TIME_STYLE) ;
                	} 
                	else {
                		columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.DATE_STYLE) ;
                	}
                } 
            	else if (colClass.equals("java.lang.Integer")) {
                	String columnFormat = column.getColumnFormat();
                    if (Utility.isSet(columnFormat)) {
                    	HSSFCellStyle style = pWorkbook.createCellStyle();
                        style.setFont((HSSFFont)fontCollection.get(ReportingUtil.TABLE_DATA));
                        style.setDataFormat(pWorkbook.createDataFormat().getFormat(columnFormat));
                        columnXlsStyle[ii] = style;
                    } 
                    else {
                    	columnXlsStyle[ii] = (HSSFCellStyle)styleCollection.get(ReportingUtil.INTEGER_STYLE) ;
                    }
                } 
            	else {
            		columnXlsStyle[ii] = null;
                }
            }
            tableWidth += colWidth;
 		}

		//format
		int pageSize =65000;
		int shift = 0;
		List table = repRes.getTable();
		int pageCount = 0;
		if (table != null) {
			pageCount = table.size() / pageSize;
			if (table.size() % pageSize > 0) {
				pageCount++;
			}
		}

		if (pageCount == 0) {
			pageCount++;
		}
		for (int curPage = 0; curPage < pageCount; curPage++) {
			String name = pSheetName;
			if (curPage > 0) {
				name = pSheetName + "." + curPage;
            }
            HSSFSheet sheet = pWorkbook.createSheet(name);
            // Make Title
            List<GenericReportColumnView> title = repRes.getTitle();
            int titleRow = 0;
            if (title != null && curPage == 0) {
            	HSSFCellStyle titleStyle = (HSSFCellStyle)styleCollection.get(ReportingUtil.PAGE_TITLE);
                int columnCountTitle = title.size();
                for (int ii = 0; ii < columnCountTitle; ii++) {
                	GenericReportColumnView column = (GenericReportColumnView) title.get(ii);
                    String colTitleStyleS = column.getColumnHeaderStyleName();
                    HSSFCellStyle colTitleStyle = titleStyle;
                    if (Utility.isSet(colTitleStyleS) && styleCollection.containsKey(colTitleStyleS)) {
                    	colTitleStyle = (HSSFCellStyle)styleCollection.get(colTitleStyleS);
                    }
                    String colName = column.getColumnName();
                    setTitle(sheet, ii, titleRow, colName, colTitleStyle );
                }
                titleRow = columnCountTitle + 1;
            }
			//Make header
            HSSFRow rowHeader = sheet.createRow(0 + titleRow);
            HSSFCellStyle headerStyle = (HSSFCellStyle)styleCollection.get(ReportingUtil.TABLE_HEADER);
            Map userStyles =  repRes.getUserStyle();
			for (int ii = 0; ii < columnCount; ii++) {
				GenericReportColumnView column = (GenericReportColumnView) header.get(ii);
                String colHeaderStyleS = column.getColumnHeaderStyleName();
                GenericReportStyleView styleView = null;
                if (userStyles != null) {
                	styleView = (GenericReportStyleView)userStyles.get(colHeaderStyleS);
                }
                String xlsColumnWidth = (styleView != null) ? styleView.getWidth() : "";
                HSSFCellStyle colHeaderStyle = headerStyle;
                if (Utility.isSet(colHeaderStyleS) && styleCollection.containsKey(colHeaderStyleS)) {
                	colHeaderStyle = (HSSFCellStyle)styleCollection.get(colHeaderStyleS);
                }
				String colName =  column.getColumnName();
				int colWidth = 0;
				try {
					colWidth = new Integer(column.getColumnWidth()).intValue();
				}
				catch (Exception e) {
				   	//nothing to do, default to the original setting of 0
				}
                setColWidth(sheet,ii, colWidth, xlsColumnWidth, repRes.getWidthFactor(), tableWidth, columnCount);
                setHeader(sheet, ii, titleRow, colName, colHeaderStyle);
			}

            if (repRes.getFreezePositionColumn()>0 || repRes.getFreezePositionRow() > 0) {
            	sheet.createFreezePane(repRes.getFreezePositionColumn(),repRes.getFreezePositionRow());
            }
            
            //Make table
            int startRow = 0;
            int endRowNext = startRow + pageSize;
			if (table != null) {
				if ((curPage + 1) * pageSize - table.size()  < 0) {
					endRowNext = startRow + pageSize;
                } 
				else {
					endRowNext = table.size()- curPage * pageSize;
                }
			} 
			else {
				endRowNext = 0;
			}

			for (int ii = endRowNext - 1; ii >= startRow; ii--) {
				List repRow = (List) table.get(ii + curPage * pageSize);
                HSSFRow row = null;
                if (curPage == 0) {
                	row = sheet.createRow(startRow + ii + titleRow + 1);
                } 
                else {
                    row = sheet.createRow(startRow + ii );
                }
                for (int jj = 0; jj < repRow.size() && jj < columnXlsStyle.length; jj++) {
					GenericReportColumnView col = (GenericReportColumnView) header.get(jj);
					Object obj = repRow.get(jj);
					String thisCurrencyCode = null;
                    HSSFCellStyle cellStyle = columnXlsStyle[jj];
                    if (obj instanceof java.lang.String) {
						String t = (java.lang.String) obj;
						if (t.startsWith("rowInfo_currencyCd=")) {
							thisCurrencyCode = t.substring(19);
						}
					}
                    if (obj instanceof HashMap){
                    	Object cellValue = ((HashMap)obj).get("BOLD") ;
                        if (cellValue != null) {
                        	HSSFCellStyle style = pWorkbook.createCellStyle();
                            if (columnXlsStyle[jj] != null ) {
                            	String cellFormat = columnXlsStyle[jj].getDataFormatString();
                                if (Utility.isSet(cellFormat)) {
                                	style.setDataFormat(pWorkbook.createDataFormat().getFormat(cellFormat));
                                }
                            }
                            style.setFont((HSSFFont)fontCollection.get(ReportingUtil.GROUP_TOTAL));
                            cellStyle = style;
                            obj = cellValue;
                        }
                    }
                    if (obj instanceof GenericReportCellView) {
                    	GenericReportCellView cellView = (GenericReportCellView)obj;
                        Object cellValue = cellView.getDataValue();
                        HSSFCellStyle style = pWorkbook.createCellStyle();
                        //updating style for row element
                        HSSFCellStyle newStyle = (HSSFCellStyle)styleCollection.get(cellView.getStyleName());
                        // cell's data format
                        String cellFormat = "";
                        if (newStyle != null && newStyle.getDataFormatString() != null) {
                        	cellFormat = newStyle.getDataFormatString();
                        } 
                        else if (columnXlsStyle[jj] != null) {
                            cellFormat = columnXlsStyle[jj].getDataFormatString();
                        }
                        if (Utility.isSet(cellFormat)) {
                        	style.setDataFormat(pWorkbook.createDataFormat().getFormat(cellFormat));
                        }
                        // cell's font
                        if (cellView.getStyleName() != null && fontCollection.get(cellView.getStyleName()) != null) {
                            style.setFont( (HSSFFont) fontCollection.get(cellView.getStyleName()));
                        } 
                        else {
                        	style.setFont((HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
                        }
                        // cell's color
                        if (newStyle != null && (newStyle.getFillPattern() != HSSFCellStyle.NO_FILL)) {
                        	style.setFillForegroundColor(newStyle.getFillForegroundColor());
                            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        }
                        // cell's alignment
                        if (newStyle != null && newStyle.getAlignment() >= 0){
                        	style.setAlignment(newStyle.getAlignment());
                        }
                        /** @todo change style for MERGE  */
                        cellStyle = style;
                        obj = cellValue;
                    }
					if (null != thisCurrencyCode) {
						setCellNoNull(sheet, jj, row, thisCurrencyCode, null);
					} 
					else {
                        setCellNoNull(sheet, jj, row, obj, cellStyle);
					}
				}
			}
		}
	}
	
	private static HSSFFont newCellFont (HSSFWorkbook wb, GenericReportStyleView userFont) {
		String fontTypeS = userFont.getFontType();
        short fontType = (fontTypeS != null && fontTypeS.equals("BOLD")) ? HSSFFont.BOLDWEIGHT_BOLD : HSSFFont.BOLDWEIGHT_NORMAL;
        int fontIndex  = (userFont.getFontSize()!=-1) ? userFont.getFontSize() : Integer.parseInt(DEFAULT_FONT.SIZE);
        String fontName = (userFont.getFontName()!=null) ? userFont.getFontName() : DEFAULT_FONT.NAME;
        IndexedColors color = (userFont.getFontColor()!=null) ? IndexedColors.getColor(userFont.getFontColor()) : null;
        short fontColor = (color != null) ? color.getIndex() : HSSFFont.COLOR_NORMAL;
        return createCellFont( wb,  fontIndex,  fontName,  fontType,  fontColor);
    }

    private static HSSFFont createCellFont (HSSFWorkbook wb, int fontIndex, String fontName, short fontType) {
    	return createCellFont( wb,  fontIndex,  fontName,  fontType,  HSSFFont.COLOR_NORMAL);
    }

    private static HSSFFont createCellFont (HSSFWorkbook wb, int fontIndex, String fontName, short fontType, short fontColor){
    	HSSFFont font = wb.findFont(fontType, fontColor, (short)fontIndex,fontName,false,false,HSSFFont.SS_NONE,HSSFFont.U_NONE  );
        if (font == null ) {
        	font = wb.createFont();
            font.setColor(fontColor);
            font.setItalic(false);
            font.setStrikeout(false);
            font.setTypeOffset(HSSFFont.SS_NONE);
            font.setUnderline(HSSFFont.U_NONE);
        }
        font.setFontHeightInPoints((short)fontIndex);
        font.setFontName(fontName);
        font.setBoldweight(fontType);
        return font;
    }

    private static Map createFonts(HSSFWorkbook wb, Map userFontDesc) {
    	Map fonts = new HashMap();
        int defFontSize  = Integer.parseInt(DEFAULT_FONT.SIZE);
        HSSFFont pageTitle = createCellFont(wb, defFontSize,DEFAULT_FONT.NAME, HSSFFont.BOLDWEIGHT_BOLD );
        HSSFFont tableHeader = createCellFont(wb, defFontSize, DEFAULT_FONT.NAME, HSSFFont.BOLDWEIGHT_BOLD );
        HSSFFont tableData = createCellFont(wb, defFontSize, DEFAULT_FONT.NAME, HSSFFont.BOLDWEIGHT_NORMAL );
        HSSFFont groupTotal = createCellFont(wb, defFontSize, DEFAULT_FONT.NAME, HSSFFont.BOLDWEIGHT_BOLD );
        HSSFFont negativeNumber = createCellFont(wb, defFontSize, DEFAULT_FONT.NAME, HSSFFont.BOLDWEIGHT_NORMAL , HSSFFont.COLOR_RED);
        if (userFontDesc != null) {
        	Map.Entry entry = null;
            Iterator it = userFontDesc.entrySet().iterator();
            while (it.hasNext()) {
            	entry = (Map.Entry) it.next();
            	fonts.put(entry.getKey(), newCellFont(wb, (GenericReportStyleView)entry.getValue()));
            }
        }
        if (! fonts.containsKey(ReportingUtil.PAGE_TITLE)) {
        	fonts.put(ReportingUtil.PAGE_TITLE, pageTitle);
        }
        if (! fonts.containsKey(ReportingUtil.TABLE_HEADER)) {
        	fonts.put(ReportingUtil.TABLE_HEADER, tableHeader);
        }
        if (! fonts.containsKey(ReportingUtil.TABLE_DATA)) {
        	fonts.put(ReportingUtil.TABLE_DATA, tableData);
        }
        if (! fonts.containsKey(ReportingUtil.GROUP_TOTAL)) {
            fonts.put(ReportingUtil.GROUP_TOTAL, groupTotal);
        }
        if (! fonts.containsKey(ReportingUtil.NEGATIVE_NUMBER)) {
            fonts.put(ReportingUtil.NEGATIVE_NUMBER, negativeNumber);
        }
        return fonts;
    }

    private static Map createStyles(HSSFWorkbook wb, Map userStyleDesc) {
    	Map newStyles = new HashMap();
        HSSFCellStyle style = wb.createCellStyle();
        HSSFDataFormat df = wb.createDataFormat();
        if (userStyleDesc != null) {
        	Map.Entry entry = null;
            Iterator it = userStyleDesc.entrySet().iterator();
            while (it.hasNext()) {
            	entry = (Map.Entry) it.next();
            	GenericReportStyleView styleView = (GenericReportStyleView)entry.getValue();
            	if (styleView != null) {
            		short align = IndexedAligns.valueOf(styleView.getAlignment());
            		IndexedColors color = (styleView.getFillColor() != null) ? IndexedColors.getColor(styleView.getFillColor()) : null ;
            		String dataCategory = styleView.getDataCategory();
            		String dataFormat = styleView.getDataFormat();
            		style = wb.createCellStyle();
            		if (Utility.isSet(dataCategory)) {
            			if (dataCategory.equals(ReportingUtil.DATA_CATEGORY.DATE)) {
            				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            				style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            			}
            			if (dataCategory.equals(ReportingUtil.DATA_CATEGORY.TIME)) {
            				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            				style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            			}
            			if (dataCategory.equals(ReportingUtil.DATA_CATEGORY.INTEGER)) {
            				style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            				style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            			}
            			if (dataCategory.equals(ReportingUtil.DATA_CATEGORY.FLOAT)) {
            				style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            				style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            			}
            			if (dataCategory.equals(ReportingUtil.DATA_CATEGORY.PERCENTAGE)) {
            				style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            				style.setDataFormat(df.getFormat("0.00%"));
            				style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            			}
            			if (dataCategory.equals(ReportingUtil.DATA_CATEGORY.ACCOUNTING)) {
            				style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            				String fmt = "##,##0.00";
            				style.setDataFormat(df.getFormat(fmt));
            				style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            			}
            		}
            		style.setAlignment(align);
            		style.setFont( (HSSFFont) fontCollection.get(entry.getKey()));
            		if (Utility.isSet(dataFormat)) {
            			style.setDataFormat(df.getFormat(dataFormat));
            		}
            		if (color != null) {
            			style.setFillForegroundColor(color.getIndex());
            			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            		}
            		style.setWrapText(styleView.isWrap());
            		newStyles.put(entry.getKey(), style);
            	}
            }
        }
        return addDefaultStyles( wb, newStyles );
    }

    private static Map addDefaultStyles(HSSFWorkbook wb, Map pStyles) {
    	HSSFDataFormat df = wb.createDataFormat();
        HSSFCellStyle style = wb.createCellStyle();
        if (pStyles == null) {
        	pStyles = new HashMap();
        }
        if (!pStyles.containsKey(ReportingUtil.PAGE_TITLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.PAGE_TITLE));
            pStyles.put(ReportingUtil.PAGE_TITLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.TABLE_HEADER)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_HEADER));
            pStyles.put(ReportingUtil.TABLE_HEADER, style);
        }
        if (!pStyles.containsKey(ReportingUtil.DATE_STYLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            style.setDataFormat(df.getFormat("MM/dd/yyyy"));
            pStyles.put(ReportingUtil.DATE_STYLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.TIME_STYLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            style.setDataFormat(df.getFormat("H:mm"));
            pStyles.put(ReportingUtil.TIME_STYLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.INTEGER_STYLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            pStyles.put(ReportingUtil.INTEGER_STYLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.INTEGER_SEPARATOR_STYLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            /** @todo  Select currency from DB */
            String fmt = "#,##0";
            style.setDataFormat(df.getFormat(fmt));
            pStyles.put(ReportingUtil.INTEGER_SEPARATOR_STYLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.PERCENT_STYLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            style.setDataFormat(df.getFormat("0.00%"));
            pStyles.put(ReportingUtil.PERCENT_STYLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.NEGATIVE_PERCENT_STYLE)) {
            style = wb.createCellStyle();
            HSSFCellStyle positiveStyle = (HSSFCellStyle)pStyles.get(ReportingUtil.PERCENT_STYLE);
            style.setAlignment(positiveStyle.getAlignment());
            style.setDataFormat(positiveStyle.getDataFormat());
            style.setFont((HSSFFont) fontCollection.get(ReportingUtil.NEGATIVE_NUMBER));
            pStyles.put(ReportingUtil.NEGATIVE_PERCENT_STYLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.FLOAT_STYLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            style.setFont( (HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            pStyles.put(ReportingUtil.FLOAT_STYLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.FLOAT_SEPARATOR_STYLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            style.setFont((HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            style.setDataFormat(df.getFormat("#,##0.00"));
            pStyles.put(ReportingUtil.FLOAT_SEPARATOR_STYLE, style);
        }
        if (!pStyles.containsKey(ReportingUtil.ACCOUNTING_STYLE)) {
            style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            style.setFont((HSSFFont) fontCollection.get(ReportingUtil.TABLE_DATA));
            pStyles.put(ReportingUtil.ACCOUNTING_STYLE, style);
        }
        return pStyles;
    }

	/**
	 *Spools the GenericReportResultView to the specified output stream.
	 *
	 *@param GenericReportResultView
	 *@param OutputStream the output stream to write the Excep report to
	 */
	public static void writeReport(List<GenericReportResultView> repResults, OutputStream pOut, String pFormat) throws Exception {

		if (!pFormat.startsWith(".")) {
			pFormat = "." + pFormat;
		}

		GenericReportResultView firstResult = (GenericReportResultView) repResults.get(0);
		if (repResults.size() > 0 && firstResult.getRawOutput() != null && firstResult.getRawOutput().length > 0) {
			pOut.write(firstResult.getRawOutput());
			pOut.flush();
		}
		else if (".xls".equals(pFormat)|| ".xlsx".equals(pFormat)) {
			writeExcelReportMulti(repResults, pOut);
		} 
		else {
			throw new Exception("Unsupported format (" + pFormat + ") for report");
		}
	}

	public static void writeExcelReportMulti(List<GenericReportResultView> repResults, OutputStream pOut) throws Exception {
		writeExcelReportMulti(repResults, pOut, ".xls");
	}

	public static void writeExcelReportMulti(List<GenericReportResultView> repResults, OutputStream pOut, String xlsType ) throws Exception {

		GenericReportResultView repRes = (repResults != null && repResults.size()>0)? (GenericReportResultView) repResults.get(0) : null;
        xlsType = (repRes != null) ? repRes.getReportFormat() : null;
        HSSFWorkbook workbook = null;
        if (Utility.isSet(xlsType) && xlsType.equals(".xlsx")) {
        } 
        else {
        	workbook = new HSSFWorkbook();
        }

		//format
        if (repResults != null) {
        	for (int ii = repResults.size()-1; ii >=0 ; ii--) {
        		repRes = (GenericReportResultView) repResults.get(ii);
                String name = repRes.getName();
                if (name == null || name.trim().length() == 0) {
                	name = "Sheet" + ii + ".";
                }
                if (name.length() > 29) {
                	name = name.substring(0,29);
                }
                writeExcelReport(repRes, name, workbook);
            }
        }
        
        workbook.write(pOut);
	}
	
	private static void setTitle(HSSFSheet pSheet, int pCol, int pRow, String pColName, HSSFCellStyle pTitleStyle) throws Exception {
		if (pCol < 0) {
			return;
        }
		// pivot columns into rows for report title!
        int iCol = 0;
        int iRow = pRow + pCol;
        HSSFRow row = pSheet.createRow(iRow);
        HSSFCell cell = row.createCell(iCol);
        cell.setCellValue(pColName);
        cell.setCellStyle(pTitleStyle);
    }

       private static void setColWidth(HSSFSheet pSheet, int pCol, int pColWidth, String pXlsColWidth, double pWidthFactor, int pAllColumnsWidth, int pColumnCount) {
          int defWidth = pSheet.getDefaultColumnWidth();
          defWidth = (defWidth > 0) ? defWidth : 8;


          double nWidthFactor = DEFAULT_WIDTH_FACTOR;
          if (pWidthFactor > 0 ){
            nWidthFactor = pWidthFactor;
          }

          String width ="";
          if (Utility.isSet(pXlsColWidth)){
            width = pXlsColWidth;
          } else if (pColWidth > 0 && nWidthFactor > 0){
            int  pxWidth = (int)((defWidth * pColumnCount) * ((double)pColWidth/(double)pAllColumnsWidth));
            width = Double.toString(pxWidth * nWidthFactor);
          }

          if (Utility.isSet(width)){
            BigDecimal bd = new BigDecimal(width);
            bd = bd.setScale(0,BigDecimal.ROUND_HALF_UP);
            int w = bd.intValue();
            pSheet.setColumnWidth(pCol, 256 * w);
          }
        }

        private static void setHeader(HSSFSheet pSheet, int pCol,
                    String pColName, HSSFCellStyle pHeaderStyle) throws Exception {
                setHeader( pSheet, pCol, 0, pColName, pHeaderStyle) ;
        }

	private static void setHeader(HSSFSheet pSheet, int pCol, int pRow,
                        String pColName, HSSFCellStyle pHeaderStyle) throws Exception {
		if (pCol < 0) {
		   return;
                }
                HSSFRow row = pSheet.getRow(pRow);
                HSSFCell cell = row.createCell(pCol);
                cell.setCellValue(pColName);
                cell.setCellStyle(pHeaderStyle);

	}

	private static void setCell(HSSFSheet pSheet, int pCol, HSSFRow pRow, Object pValue, HSSFCellStyle pStyle) throws Exception {
		if (pCol < 0) {
			return;
        }
        HSSFCell cell = pRow.createCell(pCol);
        HSSFCellStyle style = pStyle;
		if (pValue instanceof Date && pValue != null) {
			java.util.Date dateVal = (java.util.Date) pValue;
            cell.setCellValue(dateVal);
		} 
		else if (pValue instanceof BigDecimal && pValue != null) {
			BigDecimal amt = (BigDecimal) pValue;
            cell.setCellValue(amt.doubleValue());
            style = changeStyle(amt.signum(), pStyle); // if NEGATIVE_NUMBER style
		} 
		else if (pValue instanceof Integer && pValue != null) {
			Integer amt = (Integer) pValue;
            cell.setCellValue(amt.intValue());
            style = changeStyle(Integer.signum(amt.intValue()), pStyle);  // if NEGATIVE_NUMBER style
		} 
		else {
            cell.setCellValue(String.valueOf(pValue));
 		}
        if (style !=null) {
        	cell.setCellStyle(style);
        }
	}

    private static HSSFCellStyle changeStyle(int pSignum, HSSFCellStyle pStyle) {
    	HSSFCellStyle newStyle = pStyle;
        if (pStyle !=null && pStyle.equals((HSSFCellStyle)styleCollection.get(ReportingUtil.NEGATIVE_PERCENT_STYLE))) {
        	if (pSignum >= 0) {
            	newStyle = (HSSFCellStyle)styleCollection.get(ReportingUtil.PERCENT_STYLE);
            }
        }
        return newStyle;
    }

    private static void setCellNoNull(HSSFSheet pSheet, int pCol, HSSFRow pRow, Object pValue, HSSFCellStyle pStyle) throws Exception {
    	if (pValue == null) {
    		return;
        }
        setCell(pSheet, pCol, pRow, pValue, pStyle);
    }

    public enum IndexedColors  {
    	BLACK ("BLACK", HSSFColor.BLACK.index),
    	WHITE ("WHITE", HSSFColor.WHITE.index),
    	RED("RED", HSSFColor.RED.index),
    	BRIGHT_GREEN("BRIGHT_GREEN", HSSFColor.BRIGHT_GREEN.index),
    	BLUE("BLUE", HSSFColor.BLUE.index),
    	YELLOW("YELLOW", HSSFColor.YELLOW.index),
    	PINK("PINK", HSSFColor.PINK.index),
    	TURQUOISE("TURQUOISE", HSSFColor.TURQUOISE.index),
    	DARK_RED("DARK_RED", HSSFColor.DARK_RED.index),
    	GREEN("GREEN", HSSFColor.GREEN.index),
    	DARK_BLUE("DARK_BLUE", HSSFColor.DARK_BLUE.index),
    	DARK_YELLOW("DARK_YELLOW", HSSFColor.DARK_YELLOW.index),
    	VIOLET("VIOLET", HSSFColor.VIOLET.index),
    	TEAL("TEAL", HSSFColor.TEAL.index),
    	GREY_25_PERCENT("GREY_25_PERCENT", HSSFColor.GREY_25_PERCENT.index),
    	GREY_50_PERCENT("GREY_50_PERCENT", HSSFColor.GREY_50_PERCENT.index),
    	CORNFLOWER_BLUE("CORNFLOWER_BLUE", HSSFColor.CORNFLOWER_BLUE.index),
    	MAROON("MAROON", HSSFColor.MAROON.index),
        LEMON_CHIFFON("LEMON_CHIFFON", HSSFColor.LEMON_CHIFFON.index),
        ORCHID("ORCHID", HSSFColor.ORCHID.index),
        CORAL("CORAL", HSSFColor.CORAL.index),
        ROYAL_BLUE("ROYAL_BLUE", HSSFColor.ROYAL_BLUE.index),
        LIGHT_CORNFLOWER_BLUE("LIGHT_CORNFLOWER_BLUE", HSSFColor.LIGHT_CORNFLOWER_BLUE.index),
        SKY_BLUE("SKY_BLUE", HSSFColor.SKY_BLUE.index),
        LIGHT_TURQUOISE("LIGHT_TURQUOISE", HSSFColor.LIGHT_TURQUOISE.index),
        LIGHT_GREEN("LIGHT_GREEN", HSSFColor.LIGHT_GREEN.index),
        LIGHT_YELLOW("LIGHT_YELLOW", HSSFColor.LIGHT_YELLOW.index),
        PALE_BLUE("PALE_BLUE", HSSFColor.PALE_BLUE.index),
        ROSE("ROSE", HSSFColor.ROSE.index),
        LAVENDER("LAVENDER", HSSFColor.LAVENDER.index),
        TAN("TAN", HSSFColor.TAN.index),
        LIGHT_BLUE("LIGHT_BLUE", HSSFColor.LIGHT_BLUE.index),
        AQUA("AQUA", HSSFColor.AQUA.index),
        LIME("LIME", HSSFColor.LIME.index),
        GOLD("GOLD", HSSFColor.GOLD.index),
        LIGHT_ORANGE("LIGHT_ORANGE", HSSFColor.LIGHT_ORANGE.index),
        ORANGE("ORANGE", HSSFColor.ORANGE.index),
        BLUE_GREY("BLUE_GREY", HSSFColor.BLUE_GREY.index),
        GREY_40_PERCENT("GREY_40_PERCENT", HSSFColor.GREY_40_PERCENT.index),
        DARK_TEAL("DARK_TEAL", HSSFColor.DARK_TEAL.index),
        SEA_GREEN("SEA_GREEN", HSSFColor.SEA_GREEN.index),
        DARK_GREEN("DARK_GREEN", HSSFColor.DARK_GREEN.index),
        OLIVE_GREEN("OLIVE_GREEN", HSSFColor.OLIVE_GREEN.index),
        BROWN("BROWN", HSSFColor.BROWN.index),
        PLUM("PLUM", HSSFColor.PLUM.index),
        INDIGO("INDIGO", HSSFColor.INDIGO.index),
        GREY_80_PERCENT("GREY_80_PERCENT", HSSFColor.GREY_80_PERCENT.index),
        AUTOMATIC("AUTOMATIC", HSSFColor.AUTOMATIC.index);

    	String value;
    	short index;
    	
    	private IndexedColors(String pName, short pIndex) {
    		value = pName;
    		index = pIndex;
    	}
    	
    	public String getValue() {
    		return value;
        }
    	
    	public short getIndex() {
    		return index;
    	}

    	public static IndexedColors getColor(String pName) {
    		if (Utility.isSet(pName)) {
    			for (IndexedColors color : IndexedColors.values()) {
    				if (pName.equals(color.getValue())) {
    					return color;
    				}
    			}
    		}
    		return IndexedColors.AUTOMATIC;
    	}
    }

    private final static class IndexedAligns  {

    	public static short valueOf(String name) {
    		if (Utility.isSet(name)) {
    			if (name.equals(ReportingUtil.ALIGN.CENTER)) {
    				return HSSFCellStyle.ALIGN_CENTER;
    			} 
    			else if (name.equals(ReportingUtil.ALIGN.LEFT)) {
    				return HSSFCellStyle.ALIGN_LEFT;
    			} 
    			else if (name.equals(ReportingUtil.ALIGN.RIGHT)) {
    				return HSSFCellStyle.ALIGN_RIGHT;
    			} 
    			else if (name.equals(ReportingUtil.ALIGN.GENERAL)) {
    				return HSSFCellStyle.ALIGN_GENERAL;
    			} 
    			else if (name.equals(ReportingUtil.ALIGN.JUSTIFY)) {
    				return HSSFCellStyle.ALIGN_JUSTIFY;
    			}
    		}
    		return HSSFCellStyle.ALIGN_RIGHT;
    	}
    }

}
