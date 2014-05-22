package com.espendwise.manta.web.util.pdf;

import com.espendwise.manta.model.view.StoreIdentView;
import com.espendwise.manta.util.OrderUtil;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.*;
import com.espendwise.manta.util.Utility;

import java.math.BigDecimal;
import java.util.Properties;

public abstract class PdfSuper {
    static final BigDecimal ZERO = new BigDecimal(0);
    static final String SPACE = " ";
    static final String LINE_BREAK = "\n";
    static final String UTF_8 = "utf-8";
    protected Font normal = FontFactory.getFont(FontFactory.COURIER,10);
    protected Font normalBold = FontFactory.getFont(FontFactory.COURIER_BOLD,10);
    private Properties miscProperties = new Properties();

    public void setMiscProperties(Properties v) {
        miscProperties = v;
    }

    public Properties getMiscProperties() {
        return miscProperties;
    }

    public void setMiscProperty(String pName, Object pValue) {
        if (miscProperties == null) {
            miscProperties = new Properties();
        }
        miscProperties.put(pName, pValue);
    }

    public Object getMiscProperty(String pName) {
        return getMiscProperty(pName, null);
    }

    public Object getMiscProperty(String pName, String defValue) {
        if (miscProperties == null) {
            return null;
        }
        if (!miscProperties.containsKey(pName)) {
            return null;
        }
        return miscProperties.getProperty(pName, defValue);
    }

    public void setNormalFontSize(int pSize) {
        normal.setSize((float)pSize);
        normalBold.setSize((float)pSize);
    }
    
    public void setNormalFont(String pFont) {
        normal.setFamily(pFont);
        normalBold.setFamily(pFont);
    }

    public void setNormalFont(Font pFont, Font pBoldFont) {
        normal = pFont;
        normalBold = pBoldFont;
    }

    static Font smallHeading = FontFactory.getFont(FontFactory.HELVETICA_BOLD, UTF_8, true, 9);
    static Font itemHeading = FontFactory.getFont(FontFactory.HELVETICA_BOLD, UTF_8, true, 8);
    static {
        itemHeading.setColor(java.awt.Color.white);
    }
    static Font heading = FontFactory.getFont(FontFactory.HELVETICA_BOLD, UTF_8, true, 18);
    static Font small = FontFactory.getFont(FontFactory.HELVETICA, UTF_8, true, 8);
    static Font smallItalic = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, UTF_8, true, 8);
    static Font smallBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, UTF_8, true, 8);

    int borderType;

    /**utility method to constuct a <i>line</i>.  This is more of a hack than anything else
     * as it makes a table and gives it a bottom border.  There apears to be no way of makeing a
     * line object that is relative as far as the page position goes.*/

    PdfPTable makeLine(int widthPercentage) {
        return makeLine(100);
    }
    
    Object makeLine(Object table) throws DocumentException {
        return makeLineWithWidth(1, table);
    }

    Object makeLineWithWidth(float width, Object table) throws DocumentException {
        if (table instanceof Table) {
            Table line = new Table(1);
            line.setWidth(100);
            line.getDefaultCell().setBorder(Rectangle.BOTTOM);
            line.getDefaultCell().setBorderWidth(width);
            line.addCell("");
    
            return line;
        } else {
            PdfPTable line = new PdfPTable(1);
            line.setWidthPercentage(100);
            line.getDefaultCell().setBorder(Rectangle.BOTTOM);
            line.getDefaultCell().setBorderWidth(width);
            line.addCell("");
            
            return line;
        }
    }

    PdfPTable makeLine() throws DocumentException {
        return (PdfPTable)makeLineWithWidth(1, new PdfPTable(1));
    }
    
    PdfPTable makeLineWithWidth(float width) throws DocumentException {
        return (PdfPTable)makeLineWithWidth(width, new PdfPTable(1));
    }

    Element makeBlankLine(){
        return makeBlankLine(100);
    }

    Element makeBlankLine(int widthPercentage) {
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(widthPercentage);
        line.getDefaultCell().setBorder(borderType);
        line.addCell(" ");
        return line;
    }

    //utility function to create a phrase with a single chunk using makeChunk
    Phrase makePhrase(String s, Font f, boolean newline) {
        if (s == null) {
            return new Phrase(makeChunk("", f, newline));
        }

        return new Phrase(makeChunk(s, f, newline));
    }

    //utility function to check if string is null, and if so return a blank chunk
    Chunk makeChunk(String s, Font f, boolean newline){
        Chunk c;
        if (s == null) {
            //add the font just in case we alter the contents later on
            c = new Chunk("", f);
        } else {
            s = removeUnnecessarySymbols(s);
            c = new Chunk(s, f);
        }
        if (newline) {
            c.append(LINE_BREAK);
        }
        return c;
    }

    public String removeUnnecessarySymbols(String pValue) {
        // remove non-breaking spacing;
        pValue = pValue.replaceAll("&nbsp;", " ");
        return pValue;
    }

    Phrase makeStoreFooter(StoreIdentView pStore) {
        return makeStoreFooter(pStore, null, null);
    }

    Phrase makeStoreFooter(StoreIdentView pStore,
                           Chunk pOptionalBegChunk,
                           Chunk pOptionalEndChunk) {
        String addr = pStore.getPrimaryAddress().getAddress1() + SPACE + pStore.getPrimaryAddress().getAddress2();
        if (pStore.getPrimaryAddress().getAddress1() == null && pStore.getPrimaryAddress().getAddress2() == null) {
            addr = "";
        } else if (pStore.getPrimaryAddress().getAddress1() != null && pStore.getPrimaryAddress().getAddress2() != null) {
            addr = pStore.getPrimaryAddress().getAddress1() + SPACE + pStore.getPrimaryAddress().getAddress2();
        } else if (pStore.getPrimaryAddress().getAddress1() != null) {
            addr = pStore.getPrimaryAddress().getAddress1();
        } else {
            addr = pStore.getPrimaryAddress().getAddress2();
        }
        String stateProvince = null;
        if (OrderUtil.isStateProvinceRequired(pStore.getCountryProperties())) {
            stateProvince = pStore.getPrimaryAddress().getStateProvinceCd();
        }

        return makeFooter(pStore.getStoreBusinessName().getValue(),
                          addr,
                          pStore.getPrimaryAddress().getCity(),
                          stateProvince,
                          pStore.getPrimaryAddress().getPostalCode(),
                          pStore.getPrimaryPhone().getPhoneNum(),
                          pStore.getPrimaryFax().getPhoneNum(),
                          pStore.getStorePrimaryWebAddress().getValue(),pOptionalBegChunk,pOptionalEndChunk);
    }

    Phrase makeFooter(String pStoreName,
                      String pStreetAddr,
                      String pCity,
                      String pStateCd,
                      String pPostalCode,
                      String pPhoneNum,
                      String pFaxNum,
                      String pWebUrl) {
        return makeFooter (pStoreName, pStreetAddr, pCity, pStateCd, pPostalCode, pPhoneNum, pFaxNum, pWebUrl, null, null);
    }

    Phrase makeFooter(String pStoreName,
                      String pStreetAddr,
                      String pCity,
                      String pStateCd,
                      String pPostalCode,
                      String pPhoneNum,
                      String pFaxNum,
                      String pWebUrl,
                      Chunk pOptionalBegChunk,
                      Chunk pOptionalEndChunk) {

        Phrase footPhrase=new Phrase();
        if (pOptionalBegChunk != null) {
            footPhrase.add(pOptionalBegChunk);
        }

        if (Utility.isSetForDisplay(pStoreName)) {
            footPhrase.add(makeChunk(pStoreName, smallBold, false));
        }
        if (Utility.isSetForDisplay(pStreetAddr)) {
            footPhrase.add(makeChunk(" * " + pStreetAddr, small, false));
        }
        if (Utility.isSetForDisplay(pCity)) {
            footPhrase.add(makeChunk(" * " + pCity, small, false));
        }
        if (Utility.isSetForDisplay(pStateCd)) {
            footPhrase.add(makeChunk(", " + pStateCd, small, false));
        }
        if (Utility.isSetForDisplay(pPostalCode)) {
            footPhrase.add(makeChunk(" * " + pPostalCode, small, false));
        }
        if (Utility.isSetForDisplay(pPhoneNum)) {
            footPhrase.add(makeChunk(" * Phone: " + pPhoneNum, small, false));
        }
        if (Utility.isSetForDisplay(pFaxNum)) {
            footPhrase.add(makeChunk(" * Fax: " + pFaxNum, small, false));
        }
        if (Utility.isSetForDisplay(pWebUrl)) {
            footPhrase.add(makeChunk(" * " + pWebUrl, smallItalic, false));
        }
        if (pOptionalEndChunk != null) {
            footPhrase.add(pOptionalEndChunk);
        }

    return footPhrase;
    }

 /** Class PTable extends com.lowagie.text.Table class and makes some settings
  * similar to PdfPTable. Actual to replace iText.jar version from 1.2.2 to 2.1.0
  * @param numColumns the number of columns
  */
    class PTable extends Table {

     /** Constructs a <CODE>PTable</CODE> with <CODE>numColumns</CODE> columns.
     * @param numColumns the number of columns
     */
        public PTable(int numColumns) throws DocumentException {
            super(numColumns);
            super.setPadding(1);
            super.setBorderWidth(0.5f);
        }

        public PTable(int numColumns, boolean setBorder) throws DocumentException {
            super(numColumns);
            super.setPadding(1);
            if (setBorder) {
                super.setBorderWidth(0.5f);
            } else {
                super.setBorder(Rectangle.NO_BORDER);
                super.setBorderWidth(0);
            }
        }

    }

}
