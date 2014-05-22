package com.espendwise.manta.web.util.pdf;

import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;


import com.espendwise.manta.model.data.*;

import com.espendwise.manta.model.view.DistributorInfoView;
import com.espendwise.manta.model.view.StoreIdentView;
import com.espendwise.manta.util.OrderUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.WebSort;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import java.util.Locale;
import java.util.Date;
import java.math.BigDecimal;
import java.io.OutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.DateFormat;
import java.util.List;

/**
*Constructs a pdf document in the form of a pdf document.  It understands the various
 *propriatary objects, and uses them to construct a purchase order document.
 * @author  bstevens
 */
public class PdfTemporaryPO extends PdfSuper {
    //static configuration variables
    //configures the percentage widths of item columns in the document

    private static int itmColumnWidth[] = {5,5,10,32,5,5,10,8,4,8,8};
    private static int kNumColumns = 11;

    /** Creates a new instance of PdfPurchaseOrder */
    public PdfTemporaryPO() {
    }

    //utility function to make an item Element.
    private Table makePoItemElement(OrderItemData pItm, Locale pLocale) throws DocumentException {
        NumberFormat nf = NumberFormat.getInstance(pLocale);
        Table itmTbl = new PTable(kNumColumns);
        itmTbl.setWidth(100);
        itmTbl.setWidths(itmColumnWidth);
        itmTbl.getDefaultCell().setBorder(borderType);

        //***********First add the cleanwise sku information and general item information
        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        itmTbl.addCell(makePhrase(Long.toString(pItm.getOrderLineNum()), normal, false));
        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        itmTbl.addCell(makePhrase(Long.toString(pItm.getItemSkuNum()), normal, false));
        itmTbl.addCell(makePhrase(pItm.getDistItemSkuNum(), normal, false));
        itmTbl.addCell(makePhrase(pItm.getItemShortDesc(), normal, false));
        itmTbl.addCell(makePhrase(pItm.getDistItemUom(), normal, false));
        itmTbl.addCell(makePhrase(pItm.getDistItemPack(), normal, false));
        itmTbl.addCell(makePhrase(pItm.getItemSize(), normal, false));

        //get and add itemCost
        BigDecimal dic = getPoCost(pItm);
        
        dic.setScale(2, BigDecimal.ROUND_HALF_UP);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String lnCstS = nf.format(dic);

        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        itmTbl.addCell(makePhrase(lnCstS,normal,false));

        itmTbl.addCell(makePhrase(Long.toString(pItm.getDistItemQuantity()), normal, false));

        //get, add, and calculate line item total
        BigDecimal lnTot = getPoCost(pItm);
        
        // The cost is base on the item quantity, irrespective
        // of any uom conversion for a distributor.
        lnTot = lnTot.multiply(new BigDecimal(Long.toString(getPoQty(pItm))));
        String lnTotS = nf.format(lnTot);
        itmTbl.addCell(makePhrase(lnTotS, normal, false));

        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        itmTbl.addCell(makePhrase("[    ]", smallHeading,false));
        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_LEFT);

        return itmTbl;
    }

    private void drawPoHeader(Document document,
                              PdfTempPoStruct p850,
                              DistributorInfoView pDist,
                              Locale pLocale,
                              int pPageNumber,
                              byte[] pImageData,
                              boolean isStateProvinceRequired)
    throws DocumentException {
        DateFormat lDateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT,pLocale);
        //add the logo
        try{
            Image i = Image.getInstance(pImageData);
            float x = document.leftMargin();
            float y = document.getPageSize().getHeight() - i.getHeight() - 5;
            i.setAbsolutePosition(x,y);
            document.add(i);
        }catch (Exception e){
            e.printStackTrace();
        }

        //deal with po header info
        //construct the table/cells with all of the header information
        //must be the pdf specific type or the table will not use the full width of the page.
        //unfortuanately this means we are limited to PDF rendering only.
        PdfPTable headerInfo = new PdfPTable(4);
        headerInfo.setWidthPercentage(100);
        headerInfo.getDefaultCell().setBorder(borderType);
        headerInfo.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

        //add content, reads right to left, not vertically with content
        headerInfo.addCell(new Phrase());
        headerInfo.addCell(new Phrase());
        headerInfo.addCell(makePhrase("PAGE: ",smallHeading,true));
        headerInfo.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        headerInfo.addCell(makePhrase(SPACE + Integer.toString(pPageNumber),normal,true));

        headerInfo.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        headerInfo.addCell(new Phrase());
        headerInfo.addCell(new Phrase());
        headerInfo.addCell(makePhrase("TEMPORARY PO DATE:",smallHeading,true));//XXX: Hardcoded terms.
        headerInfo.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        Date currentDate = new Date();
        headerInfo.addCell(makePhrase(SPACE + lDateFormatter.format(currentDate),normal,true));

        headerInfo.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

        // Request from Frank M.
        headerInfo.addCell(new Phrase());
        headerInfo.addCell(new Phrase());
        headerInfo.addCell(makePhrase("TARGET DELIVERY DATE:",
                                      smallHeading,
                                      true));
        headerInfo.addCell(makePhrase(" ", smallHeading, true));

        document.add(headerInfo);

        //draw a line  XXX this is somewhat of a hack, as there seems to be no way to draw a line in the
        //current iText API that is relative to your current position in the document
        document.add(makeLine());

        //deal with the billing and shipping information header
        PdfPTable addrHeader = new PdfPTable(2);
        addrHeader.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        addrHeader.setWidthPercentage(90);
        addrHeader.getDefaultCell().setBorder(borderType);
        addrHeader.addCell(makePhrase("VENDOR NAME:", smallHeading, false));
        addrHeader.addCell(makePhrase("SHIP TO:", smallHeading, false));
        document.add(addrHeader);

        //deal with the billing and shipping information header
        PdfPTable addresses = new PdfPTable(2);
        addresses.setWidthPercentage(90);
        addresses.getDefaultCell().setBorder(borderType);
        addresses.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        addresses.addCell(makePhrase(pDist.getBusEntityData().getShortDesc(), normal, false));
        addresses.addCell(makePhrase(p850.getAccountName(), normal, false));

        addresses.addCell(makePhrase(pDist.getPrimaryAddress().getAddress1(), normal, false));
        addresses.addCell(makePhrase(p850.getOrderD().getOrderSiteName(), normal, false));

        addresses.addCell(makePhrase(pDist.getPrimaryAddress().getAddress2(),normal,false));
        addresses.addCell(makePhrase(p850.getShipAddr().getAddress1(),normal,false));

        addresses.addCell(makePhrase(pDist.getPrimaryAddress().getAddress3(), normal, false));
        addresses.addCell(makePhrase(p850.getShipAddr().getAddress2(),normal,false));

        addresses.addCell(makePhrase(pDist.getPrimaryAddress().getAddress4(), normal, false));
        addresses.addCell(makePhrase(p850.getShipAddr().getAddress3(), normal, false));

        if (Utility.isSet(p850.getShipAddr().getAddress4())) {
            addresses.addCell(SPACE);
            addresses.addCell(makePhrase(p850.getShipAddr().getAddress4(),normal,false));
        }

        addresses.addCell(makePhrase(pDist.getPrimaryAddress().getCity() + "      " +
        (isStateProvinceRequired && Utility.isSet(pDist.getPrimaryAddress().getStateProvinceCd())?pDist.getPrimaryAddress().getStateProvinceCd() + SPACE: "") +
        pDist.getPrimaryAddress().getPostalCode(), normal, false));

        addresses.addCell(makePhrase(p850.getShipAddr().getCity() + "      " +
        (isStateProvinceRequired && Utility.isSet(p850.getShipAddr().getStateProvinceCd())?p850.getShipAddr().getStateProvinceCd() + SPACE:"") +
        p850.getShipAddr().getPostalCode(), normal, false));
        document.add(addresses);

        //deal with the notcie header
        PdfPTable noticeHeader = new PdfPTable(1);
        noticeHeader.setWidthPercentage(90);
        noticeHeader.getDefaultCell().setBorder(borderType);
        noticeHeader.getDefaultCell().setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        noticeHeader.addCell(makePhrase(SPACE,smallHeading,true));
        noticeHeader.addCell(makePhrase("The following information MUST be included on the packing slip:  ",smallHeading,true));
        document.add(noticeHeader);

        PdfPTable notice = new PdfPTable(4);
        notice.setWidthPercentage(90);
        notice.getDefaultCell().setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        notice.getDefaultCell().setBorder(borderType);
        notice.addCell(makePhrase("Customer PO #: ", smallHeading, true));
        notice.addCell(makePhrase(p850.getOrderD().getRequestPoNum(), normal, false));
        notice.addCell(makePhrase(SPACE, smallHeading, true));
        notice.addCell(makePhrase(SPACE, smallHeading, true));
        document.add(notice);

        //add the item header line
        PdfPTable itemHeader = null;
        if (p850.isService()) {
            itemHeader = new PdfPTable(7);
            itemHeader.setWidthPercentage(100);
            itemHeader.setWidths(getServColumnWidth());
            itemHeader.getDefaultCell().setBorderWidth(2);
            itemHeader.getDefaultCell().setBackgroundColor(java.awt.Color.black);
            itemHeader.getDefaultCell().setHorizontalAlignment(Cell.ALIGN_CENTER);
            itemHeader.getDefaultCell().setVerticalAlignment(Cell.ALIGN_MIDDLE);
            itemHeader.getDefaultCell().setBorderColor(java.awt.Color.white);
            itemHeader.addCell(makePhrase("LINE", itemHeading, false));
            itemHeader.addCell(makePhrase("DIST SKU", itemHeading, false));
            itemHeader.addCell(makePhrase("ASSET", itemHeading, false));
            itemHeader.addCell(makePhrase("NUMBER", itemHeading, false));
            itemHeader.addCell(makePhrase("SERIAL", itemHeading, false));
            itemHeader.addCell(makePhrase("SERVICE & DESCRIPTION", itemHeading, false));
            itemHeader.addCell(makePhrase("NET AMOUNT", itemHeading, false));
        } else {
            itemHeader=new PdfPTable(kNumColumns);
            itemHeader.setWidthPercentage(100);
                  itemHeader.setWidths(itmColumnWidth);
                  itemHeader.getDefaultCell().setBorderWidth(2);
                  itemHeader.getDefaultCell().setBackgroundColor(java.awt.Color.black);
                  itemHeader.getDefaultCell().setHorizontalAlignment(Cell.ALIGN_CENTER);
                  itemHeader.getDefaultCell().setVerticalAlignment(Cell.ALIGN_MIDDLE);
                  itemHeader.getDefaultCell().setBorderColor(java.awt.Color.white);
                  itemHeader.addCell(makePhrase("LINE", itemHeading, false));
                  itemHeader.addCell(makePhrase("CW SKU", itemHeading, false));
                  itemHeader.addCell(makePhrase("DIST SKU", itemHeading, false));
                  itemHeader.addCell(makePhrase("PRODUCT NAME", itemHeading, false));
                  itemHeader.addCell(makePhrase("UOM", itemHeading, false));
                  itemHeader.addCell(makePhrase("PACK", itemHeading, false));
                  itemHeader.addCell(makePhrase("SIZE", itemHeading, false));
                  itemHeader.addCell(makePhrase("CLEANWISE COST", itemHeading, false));
                  itemHeader.addCell(makePhrase("QTY", itemHeading, false));
                  itemHeader.addCell(makePhrase("NET AMOUNT", itemHeading, false));
                  itemHeader.addCell(makePhrase("ACCEPTED", itemHeading, false));
        }
        document.add(itemHeader);
    }


    /**Takes in an instance of @see OutboundEDIRequestData, @see DistributorData, and @see StoreData
     *and converts them into an outbound purchase order pdf.  This document
     *looks very similar to the output when printing from directly from Lawson.
     *@Param pDist the distributor this fax is going to.
     *@Param p850s the purchase orders to translate.
     *@Param pStore the store this po is comming from.
     *@Param pOut the output stream to write the pdf to.
     *@Param pImageName the image to be placed in the upper left corner, if null left off pdf.
     *@throws IOException if any error occurs.
     */
    
    /** Temporary commented !!!!!!
    public void constructPdfPO(DistributorInfoView pDist,
                               List<OutboundEDIRequestData> p850s,
                               StoreIdentView pStore,
                               OutputStream pOut,
                               byte[] pImageData) throws IOException {
        if (p850s.size() == 0){
            throw new IllegalArgumentException("OutboundEDIRequestDataVector size cannot be emtpy");
        }
        ArrayList lpos = new ArrayList();
        for (int i = 0, len = p850s.size(); i < len; i++) {
            OutboundEDIRequestData ediReq = p850s.get(i);
            PdfTemporaryPO.PdfTempPoStruct po = new PdfTemporaryPO.PdfTempPoStruct(ediReq.getOrderD(),
                                                                                   ediReq.getOrderItemDV(),
                                                                                   ediReq.getShipAddr(),
                                                                                   ediReq.getAccountName());
            lpos.add(po);
        }
        generatePdf(pDist, lpos, pStore, pOut, pImageData);
    }
    */

    /**Takes in an instance of
     *@see OrderData @see PurchaseOrderData,@see OrderItemDataVector
     *@see OrderAddressData, @see DistributorData, and @see StoreData
     *and converts them into an outbound purchase order pdf.  This document
     *looks very similar to the output when printing from directly from Lawson.
     *@Param pDist the distributor this fax is going to.
     *@Param p850s the purchase orders to translate.
     *@Param pStore the store this po is comming from.
     *@Param pOut the output stream to write the pdf to.
     *@Param pImageName the image to be placed in the upper left corner, if null left off pdf.
     *@throws IOException if any error occurs.
     */
    public void constructPdfPO(DistributorInfoView pDist,
                               OrderData pOrderData,
                               OrderAddressData pShipAddr,
                               List<OrderItemData> pOrderItemDataList,
                               String pAccountName,
                               StoreIdentView pStore,
                               OutputStream pOut,
                               byte[] pImageData) throws IOException {
        
        ArrayList lpo = new ArrayList();
        lpo.add(new PdfTemporaryPO.PdfTempPoStruct(pOrderData,
                                                   pOrderItemDataList,
                                                   pShipAddr,
                                                   pAccountName));
        generatePdf(pDist, lpo, pStore, pOut, pImageData);
    }

    public void constructPdfPO(DistributorInfoView pDist,
                               OrderData pOrderData,
                               OrderAddressData pShipAddr,
                               List<OrderItemData> pOrderItemDataList,
                               String pAccountName,
                               StoreIdentView pStore,
                               HashMap pAssetInfo,
                               boolean pServiceFl,
                               OutputStream pOut,
                               byte[] pImageData) throws IOException {
    
        ArrayList lpo = new ArrayList();
        lpo.add(new PdfTemporaryPO.PdfTempPoStruct(pOrderData,
                                                   pOrderItemDataList,
                                                   pShipAddr,
                                                   pAccountName,
                                                   pAssetInfo,
                                                   pServiceFl));
        generatePdf(pDist, lpo, pStore, pOut, pImageData);
    }

    //main worker method.  The public methods will call this method
    //to generate the pdf after the object has been converted.
    private void generatePdf(DistributorInfoView pDist,
                             ArrayList p850s,
                             StoreIdentView pStore,
                             OutputStream pOut,
                             byte[] pImageData) throws IOException {

        try {
            //we keep track of the page number rather than useing the built in pdf functionality
            //as we have to maintain pages between pos, so we may be on the 3rd po, but on the
            //first page of that po, and on the 9th page of the pdf.
            int pageNumber = 1;
            
            //create the header and footer
            Phrase headPhrase = new Phrase(makeChunk("Temporary PO",heading,true));
            HeaderFooter header = new HeaderFooter(headPhrase,false);
            header.setAlignment(HeaderFooter.ALIGN_RIGHT);

            String addr;
            Phrase footPhrase;
            if (pStore == null){
                footPhrase = makePhrase(SPACE, normal, false);
            } else {
                footPhrase = makeStoreFooter(pStore);
            }
            HeaderFooter footer = new HeaderFooter(footPhrase,false);
            footer.setAlignment(HeaderFooter.ALIGN_CENTER);

            //setup the borders from the header
            header.setBorder(borderType);
            footer.setBorder(HeaderFooter.TOP);

            //setup the document
            Document document = new Document(PageSize.A4.rotate(), 10, 15, 30, 15);
            //Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, pOut);
            document.setHeader(header);
            document.setFooter(footer);
            document.open();
            String onum = "";

            for (int j = 0, len2 = p850s.size(); j < len2; j++){
                PdfTempPoStruct l850 = (PdfTempPoStruct) p850s.get(j);

                WebSort.sort(l850.getOrderItemList(), OrderItemData.ORDER_LINE_NUM);

                //XXX po locale, or distributor locale?
                Locale lLocale = Utility.parseLocaleCode(l850.getOrderD().getLocaleCd());

                drawPoHeader(document,
                             l850,
                             pDist,
                             lLocale,
                             pageNumber,
                             pImageData,
                             OrderUtil.isStateProvinceRequired(pStore.getCountryProperties()));
                onum = l850.getOrderD().getOrderNum();

                //now add the items
                BigDecimal poTotal = new BigDecimal(0);
                for (int i = 0, len = l850.getOrderItemList().size(); i < len; i++){
                    document.add(makeBlankLine());
                    OrderItemData itm = l850.getOrderItemList().get(i);

                    Table itmTable=null;
                    if (l850.isService()) {
                        itmTable = makePoItemElement(itm, l850.getAssetInfo(), lLocale);
                    } else {
                        itmTable = makePoItemElement(itm, lLocale);
                    }
                    //if the item data will not fit onto the page, make a new page, and redraw
                    //the header.
                    if (writer.fitsPage(itmTable,document.bottomMargin())) {
                        document.add(itmTable);
                    } else {
                        document.newPage();
                        pageNumber = pageNumber + 1;

                        drawPoHeader(document,
                                     l850,
                                     pDist,
                                     lLocale,
                                     pageNumber,
                                     pImageData,
                                     OrderUtil.isStateProvinceRequired(pStore.getCountryProperties()));
                        document.add(makeBlankLine());
                        document.add(itmTable);
                    }

                    BigDecimal lnTot = getPoCost(itm);
                    lnTot = lnTot.multiply(new BigDecimal(Long.toString(getPoQty(itm))));
                    poTotal = poTotal.add(lnTot);

                }

                NumberFormat nf = NumberFormat.getInstance(lLocale);
                nf.setMinimumFractionDigits(2);
                nf.setMaximumFractionDigits(2);

                PdfPTable subt = new PdfPTable(2);
                subt.getDefaultCell().setBorder(borderType);
                subt.setWidthPercentage(50);
                subt.getDefaultCell().setHorizontalAlignment(Rectangle.ALIGN_RIGHT);
                subt.addCell(makePhrase("Total Amount: ", smallHeading, true));
                subt.getDefaultCell().setHorizontalAlignment(Rectangle.ALIGN_LEFT);
                subt.addCell(makePhrase(nf.format(poTotal), smallHeading, true));
                subt.getDefaultCell().setHorizontalAlignment(Rectangle.ALIGN_RIGHT);
                subt.addCell(makePhrase("Order: ", smallHeading, true));
                subt.getDefaultCell().setHorizontalAlignment(Rectangle.ALIGN_LEFT);
                subt.addCell(makePhrase(onum, smallHeading, true));
                document.add(subt);

                if (j < len2) {
                    pageNumber = 1;
                    document.newPage();
                }
            }

            //close out the document
            document.close();

        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
    }

    private Table makePoItemElement(OrderItemData pItm, HashMap pAssetInfo, Locale pLocale) throws DocumentException {

        NumberFormat nf = NumberFormat.getInstance(pLocale);
        Table itmTbl = new PTable(7);
        itmTbl.setWidth(100);
        itmTbl.setWidths(getServColumnWidth());
        itmTbl.getDefaultCell().setBorder(borderType);

        //***********First add the cleanwise sku information and general item information
        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        itmTbl.addCell(makePhrase(Long.toString(pItm.getOrderLineNum()), normal, false));
        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        itmTbl.addCell(makePhrase(pItm.getDistItemSkuNum(), normal, false));

        String assetName = "UNKNOWN";
        String assetNum = "UNKNOWN";
        String assetSerialNum = "UNKNOWN";
        if (pAssetInfo != null) {
            AssetData assetData = (AssetData) pAssetInfo.get(String.valueOf(pItm.getAssetId()));
            if (assetData != null) {
                assetName = assetData.getShortDesc();
                assetNum = assetData.getAssetNum();
                assetSerialNum = assetData.getSerialNum();
            }
        }
        itmTbl.addCell(makePhrase(assetName, normal, false));
        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        itmTbl.addCell(makePhrase(assetNum, normal, false));
        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        itmTbl.addCell(makePhrase(assetSerialNum, normal, false));
        itmTbl.addCell(makePhrase(pItm.getItemShortDesc(), normal, false));

        //get and add itemCost
        BigDecimal dic = getPoCost(pItm);
        dic.setScale(2, BigDecimal.ROUND_HALF_UP);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String lnCstS = nf.format(dic);

        itmTbl.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        //get, add, and calculate line item total
        BigDecimal lnTot = getPoCost(pItm);

        lnTot = lnTot.multiply
                (new BigDecimal(Long.toString(getPoQty(pItm))));
        String lnTotS = nf.format(lnTot);
        itmTbl.addCell(makePhrase(lnTotS, normal, false));


        return itmTbl;
    }

    public int[] getServColumnWidth() {
        return new int[] {4,7,20,17,17,25,10};
    }

    /**
     *Private ineer class to represent the data the po needs to print, and the
     *relationship between the data.  This makes converting between different
     *objects (outboundEdiRequests, and PurchaseOrderDesc/orderItemVectors, etc.)
     *easier
     */
    public class PdfTempPoStruct{
        private OrderData mOrderD;
        private List<OrderItemData> mOrderItemDV;
        private OrderAddressData mShipAddress;
        private String mAccountName;
        private BigDecimal mSubTotal = new BigDecimal(0);
        private BigDecimal mTotal = new BigDecimal(0);
        private HashMap mAssetInfo;
        private boolean mService;

        public PdfTempPoStruct(OrderData pOrderD,
                               List<OrderItemData> pOrderItemList,
                               OrderAddressData pShipAddress,
                               String pAccountName  ) {
        mAccountName = pAccountName;
        mOrderD = pOrderD;
        mOrderItemDV = pOrderItemList;
        mShipAddress = pShipAddress;

            if (null != pOrderItemList) {
                for (int i = 0; i < pOrderItemList.size(); i++) {
                    OrderItemData orderItemD = (OrderItemData)pOrderItemList.get(i);
                    BigDecimal lineTotal = getPoCost(orderItemD);
                    lineTotal.multiply(new BigDecimal(getPoQty(orderItemD)));
                    mSubTotal = mSubTotal.add(lineTotal);
                }
            }
            mTotal = mSubTotal;
        }

        public PdfTempPoStruct(OrderData pOrderD,
                               List<OrderItemData> pOrderItemList,
                               OrderAddressData pShipAddress,
                               String pAccountName,
                               HashMap pAssetInfo,
                               boolean pService) {
            mAccountName = pAccountName;
            mOrderD = pOrderD;
            mOrderItemDV = pOrderItemList;
            mShipAddress = pShipAddress;
            mAssetInfo = pAssetInfo;
            mService = pService;
            
            if (null != pOrderItemList) {
                for (int i = 0; i < pOrderItemList.size(); i++) {
                    OrderItemData orderItemD = (OrderItemData)pOrderItemList.get(i);
                    BigDecimal lineTotal = getPoCost(orderItemD);
                    lineTotal.multiply(new BigDecimal(getPoQty(orderItemD)));
                    mSubTotal = mSubTotal.add(lineTotal);
                }
            }
            mTotal = mSubTotal;
        }

        public String getAccountName() {
            return mAccountName;
        }

        private OrderAddressData getShipAddr(){
            return mShipAddress;
        }

        public OrderData getOrderD(){
            return mOrderD;
        }
        
        private List<OrderItemData> getOrderItemList(){
            return mOrderItemDV;
        }
        
        private BigDecimal getSubTotal() {
            return mSubTotal;
        }
        
        private BigDecimal getTotal() {
            return mTotal;
        }

        public HashMap getAssetInfo() {
            return mAssetInfo;
        }

        public void setAssetInfo(HashMap pAssetInfo) {
            this.mAssetInfo = pAssetInfo;
        }

        public boolean isService() {
            return mService;
        }

        public void setService(boolean pService) {
            this.mService = pService;
        }
    }

    private Long getPoQty(OrderItemData oi)  {
        if (oi.getDistItemQuantity() > 0) {
            return oi.getDistItemQuantity();
        } else {
            return oi.getTotalQuantityOrdered();
        }
    }

    private BigDecimal getPoCost(OrderItemData oi){
        if (RefCodeNames.ORDER_ITEM_STATUS_CD.CANCELLED.equals(oi.getOrderItemStatusCd())) {
            return ZERO;
        }
        // STJ-5229 
        //if(oi.getDistUomConvCost() != null && oi.getDistUomConvCost().compareTo(ZERO) > 0){
        //    return oi.getDistUomConvCost();
        //}else{
            return oi.getDistItemCost();
        //}
    }
}
