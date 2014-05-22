package com.espendwise.manta.util;

import org.apache.log4j.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.espendwise.manta.loader.IInputStreamParser;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Deals with reading an excel file.  It supports xls and xlsx format.
 */
public class ExcelReader {
	protected Logger log = Logger.getLogger(this.getClass());
	Workbook workbook = null;
    private ArrayList mTwoDArray = new ArrayList();
    private boolean fileRead = false;
    private int currRowIdx = -100;
    private List currRow = null;
    private DataFormatter formatter = new DataFormatter(true);
    private FormulaEvaluator formulaEvaluator = null;

    /**
     *Reads in a given cell from an Excel stream.
     * @throws IOException 
     * @throws InvalidFormatException 
     */
    public Object getCell(int rowIndex,int columnIndex) throws InvalidFormatException, IOException {
        if(fileRead){
            //use read in version
            if(mTwoDArray.size() >= rowIndex){
                List row = (List) mTwoDArray.get(rowIndex);
                if(row.size() >= columnIndex){
                    return row.get(columnIndex);
                }
            }

            return null;
        }        
        
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell((short)columnIndex);
        if(cell.getCellType() != Cell.CELL_TYPE_FORMULA) {
            return this.formatter.formatCellValue(cell);
        }
        else {
            return this.formatter.formatCellValue(cell, formulaEvaluator);
        }
    }

    //safely adds the element to our 2d list at the specified location
    //does some caching of row lookups in the List so as to avoid many get(x) calls
    private void addAt(Object value, int rowIndex, int columnIndex){
        if(currRowIdx != rowIndex){
            //if the current row being requested is the last one requested just use our
            //existing reference to it.
            while (mTwoDArray.size() <= rowIndex){
                mTwoDArray.add(new ArrayList());
            }
            currRow = (ArrayList) mTwoDArray.get(rowIndex);
        }

        if(currRow.size() < columnIndex){
            while (currRow.size() < columnIndex){
                currRow.add(null);
            }
            currRow.add(value);
        }else{
            currRow.add(columnIndex, value);
        }
    }

    
    /**
     *Reads in the excel file and calls the listners parseLine(List) method for each row.  It needs
     *to actually read in the entire file as there is no gaurentee as to the order of an Excel file,
     *i.e. a row may be physically at the top of the file, but is actually in the middle when the file
     *is displayed on screen.  The listner is gurenteed to have it's parseline method called for rows
     *in order (1,2,3,4 etc).  Subsequent calls to this method will not reflect changes in the underlying
     *stream.  In fact after processing this the stream is closed.
     *
     * @throws Exception 
     */

    public void processFile(IInputStreamParser pListener) throws Exception{

        if(!fileRead){
        	Sheet sheet = workbook.getSheetAt(0);
            if(sheet.getPhysicalNumberOfRows() > 0) {

                // Note down the index number of the bottom-most row and
                // then iterate through all of the rows on the sheet starting
                // from the very first row - number 1 - even if it is missing.
                // Recover a reference to the row and then call another method
                // which will strip the data from the cells and build lines
                // for inclusion in the resylting CSV file.
                int lastRowNum = sheet.getLastRowNum();
                for(int j = 0; j <= lastRowNum; j++) {
                    Row row = sheet.getRow(j);
                    if (row == null)
                    	continue;
                    int lastCellNum = row.getLastCellNum();
                    for(int i = 0; i <= lastCellNum; i++) {
                    	Cell cell = row.getCell(i);
                        if (cell != null)
                        	addAt(getCell(j,i), j, i);
                    }

                }
            }

            

            log.info("done reading file.  Processed " + mTwoDArray.size() + " Rows");
            fileRead = true;
        }
        if(pListener != null){
        	int currentLineNumber = 0;
        	try{
	            Iterator it = mTwoDArray.iterator();
	            while(it.hasNext()){
	                pListener.parseLine((List) it.next());
	                currentLineNumber++;
	            }
        	}catch(Exception e){
    			log.error("Error parsing line: "+(currentLineNumber));
    			throw e;
    		}
            log.info("done calling listner");
        }        
    }

    /**
     * @throws IOException 
     * @throws InvalidFormatException 
     */
    public ExcelReader(InputStream inStream) throws InvalidFormatException, IOException  {    	  
        // Open the workbook 
        this.workbook = WorkbookFactory.create(inStream);
        formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
    }

}

