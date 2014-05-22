package com.espendwise.manta.web.controllers;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.AuthUser;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStjohnInstanceContext;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.service.*;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.OrderForm;
import com.espendwise.manta.web.forms.OrderItemNoteFilterForm;
import com.espendwise.manta.web.forms.OrderPrintTempPoForm;
import com.espendwise.manta.web.resolver.OrderWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.util.pdf.PdfTemporaryPO;
import com.espendwise.ocean.common.webaccess.*;
import com.espendwise.webservice.restful.value.OrderCancelRequestData;
import com.espendwise.webservice.restful.value.OrderChangeRequestData;
import com.espendwise.webservice.restful.value.OrderItemActionDescData;
import com.espendwise.webservice.restful.value.OrderItemDescData;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;



@Controller
@RequestMapping(UrlPathKey.ORDER.IDENTIFICATION)
@SessionAttributes({SessionKey.ORDER})
public class OrderController extends BaseController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    private OrderService orderService;
    private UserService userService;
    private PropertyService propertyService;
    private BusEntityService busEntityService;
    private DistributorService distributorService;
    private StoreService storeService;
    private AssetService assetService;

    @Autowired
    public OrderController(OrderService orderService,
                           UserService userService,
                           PropertyService propertyService,
                           BusEntityService busEntityService,
                           DistributorService distributorService,
                           StoreService storeService,
                           AssetService assetService) {

        this.orderService = orderService;
        this.userService = userService;
        this.propertyService = propertyService;
        this.busEntityService = busEntityService;
        this.distributorService = distributorService;
        this.storeService = storeService;
        this.assetService = assetService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new OrderWebUpdateExceptionResolver());
        
        return "order/edit";
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateOrder(WebRequest request,
                       @ModelAttribute(SessionKey.ORDER) OrderForm form,
                       Model model) throws Exception {

        logger.info("save()=> BEGIN, orderForm: " + form);
        WebErrors webErrors = new WebErrors(request);
        
        AppUser user = getAppUser();

        if (!user.isQneOfAdmin()) {
            webErrors.putError("validation.web.error.unauthorizedAccess");
            return "order/edit";
        }
        
        String orderStatus = form.getOrderInfo().getOrderData().getOrderStatusCd();
        if (RefCodeNames.ORDER_STATUS_CD.RECEIVED.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.ORDERED.equals(orderStatus)  ||
            RefCodeNames.ORDER_STATUS_CD.READY_TO_SEND_TO_CUST_SYS.equals(orderStatus) ||
            RefCodeNames.ORDER_STATUS_CD.SENT_TO_CUST_SYSTEM.equals(orderStatus)) {

            webErrors.putError(new WebError("validation.web.error.wrongOrderStatusToModify", Args.typed(orderStatus)));
            return "order/edit";
        }

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);

            return "order/edit";
        }

        if (Utility.isSet(form.getTotalFreightCost())) {
            form.setTotalFreightCostValue(BigDecimal.valueOf(Double.parseDouble(form.getTotalFreightCost())));
        }
        

        if (Utility.isSet(form.getTotalMiscCost())) {
            form.setTotalMiscCostValue(BigDecimal.valueOf(Double.parseDouble(form.getTotalMiscCost())));
        }

        if (Utility.isSet(form.getSmallOrderFee())) {
            form.setSmallOrderFeeValue(BigDecimal.valueOf(Double.parseDouble(form.getSmallOrderFee())));
        }

        if (Utility.isSet(form.getFuelSurCharge())) {
            form.setFuelSurChargeValue(BigDecimal.valueOf(Double.parseDouble(form.getFuelSurCharge())));
        }
        
        if (Utility.isSet(form.getDiscount())) {
            form.setDiscountValue(BigDecimal.valueOf(Double.parseDouble(form.getDiscount())));
        }

        if (Utility.isSet(form.getTotalTaxCost())) {
            form.setTotalTaxCostValue(BigDecimal.valueOf(Double.parseDouble(form.getTotalTaxCost())));
        }

        if (Utility.isSet(form.getTotalAmount())) {
            form.setTotalAmountValue(BigDecimal.valueOf(Double.parseDouble(form.getTotalAmount())));
        }

        Long newSiteId = null;
        if (Utility.isSet(form.getFilteredLocationCommaIds())) {
            String[] tokens = form.getFilteredLocationCommaIds().split(",");

            if (tokens.length > 0) {
                try {
                    newSiteId = Long.valueOf(tokens[0].trim());
                    form.setNewSiteId(newSiteId.toString());
                } catch (NumberFormatException e) {}
            }
        }

        List<Long> orderItemsToDelete = new ArrayList<Long>();
        List<OrderItemIdentView> toDelete;
        if (form.getSimpleServiceOrder()) {
            toDelete = form.getSelectToCancel().getSelected();
        } else {
            toDelete = form.getOrderItems();
        }
        if (Utility.isSet(toDelete)) {
            for(OrderItemIdentView orderItem : toDelete) {
                if (RefCodeNames.ORDER_ITEM_STATUS_CD.CANCELLED.equals(orderItem.getNewItemStatus()) &&
                    orderItem.getOrderItem().getOrderItemId() != null &&
                    orderItem.getOrderItem().getOrderItemId() != 0L &&
                   !RefCodeNames.ORDER_ITEM_STATUS_CD.CANCELLED.equals(orderItem.getOrderItem().getOrderItemStatusCd())) {
                        orderItemsToDelete.add(orderItem.getOrderItem().getOrderItemId());
                }
            }
        }
        
        OrderChangeRequestView changeRequestView = new OrderChangeRequestView();
        changeRequestView.setOrderInfo(form.getOrderInfo());
        changeRequestView.setOrderItems(form.getOrderItems());
        changeRequestView.setOrderItemsDelete(orderItemsToDelete);
        changeRequestView.setUserName(user.getUserName());
        if (Utility.isSetIgnorePattern(form.getNewOrderDate(), AppI18nUtil.getDatePatternPrompt())) {
            changeRequestView.setNewOrderDate(AppI18nUtil.parseDateNN(getAppLocale(), form.getNewOrderDate()));
        }
        if (form.getNewSiteId() != null) {
            changeRequestView.setNewSiteId(Long.valueOf(form.getNewSiteId()));
        } else { // always set siteId to follow StJohn logic and get site info validated
            changeRequestView.setNewSiteId(form.getOrderInfo().getOrderData().getSiteId());
        }
        changeRequestView.setRebillOrder(form.getRebillOrder());
        changeRequestView.setTotalFreightCostValue(form.getTotalFreightCostValue());
        changeRequestView.setTotalMiscCostValue(form.getTotalMiscCostValue());
        changeRequestView.setSmallOrderFeeAmountValue(form.getSmallOrderFeeValue());
        changeRequestView.setFuelSurchargeAmountValue(form.getFuelSurChargeValue());
        changeRequestView.setDiscountAmountValue(form.getDiscountValue());
    
        try {
            orderService.updateOrder(changeRequestView);
        } catch (ValidationException e) {
            return handleValidationException(e, request);
        }
        
        
        OrderChangeRequestData crData = new OrderChangeRequestData();
        
        // OrderData
        OrderData orderD = form.getOrderInfo().getOrderData();
        if (!form.getApplyBudget()) {
            orderD.setOrderBudgetTypeCd(RefCodeNames.ORDER_BUDGET_TYPE_CD.NON_APPLICABLE);
        } else {
            orderD.setOrderBudgetTypeCd(null);
        }

        // Workflow Indicator
        if (Utility.isSet(form.getWorkflowStatus())) {
            String workflowStatusNew = null;
            for (Pair<String, String> workflowInd : form.getWorkflowStatuses()) {
                if (workflowInd.getObject1().equals(form.getWorkflowStatus())) {
                    workflowStatusNew = workflowInd.getObject2();
                    break;
                }
            }
            if (workflowStatusNew != null) {
                orderD.setWorkflowInd(workflowStatusNew);
            }
        }

        // Status
        if (Utility.isSet(form.getOrderStatus())) {
            String orderStatusNew = null;
            for (Pair<String, String> orderS : form.getOrderStatuses()) {
                if (orderS.getObject1().equals(form.getOrderStatus())) {
                    orderStatusNew = orderS.getObject2();
                }
            }
            if (orderStatusNew != null) {
                orderD.setOrderStatusCd(orderStatusNew);
            }
        }

        orderD.setModBy(getAppUser().getUserName());

        crData.setOrderData(orderD);
        
        // OrderMeta
        crData.setOrderMeta(form.getOrderInfo().getOrderMeta());
        
        // OrderItems
        crData.setOrderItems(form.getOrderInfo().getOrderItems());
        
        // OrderItemsToDelete
        crData.setDeleteOrderItemIds(orderItemsToDelete);
        
        // NewOrderDate
        if (Utility.isSetIgnorePattern(form.getNewOrderDate(), AppI18nUtil.getDatePatternPrompt())) {
            crData.setNewOrderDate(AppI18nUtil.parseDateNN(getAppLocale(), form.getNewOrderDate()));
        }
        
        // New SiteId
        if (newSiteId != null) {
            crData.setNewSiteId(newSiteId);
        }
        
        // Rebill Order
        crData.setRebillOrder(form.getRebillOrder());
        
        // Amounts
        crData.setTotalFreightCostValue(form.getTotalFreightCostValue());
        crData.setTotalMiscCostValue(form.getTotalMiscCostValue());
        crData.setSmallOrderFeeValue(form.getSmallOrderFeeValue());
        crData.setFuelSurChargeValue(form.getFuelSurChargeValue());
        crData.setDiscountValue(form.getDiscountValue());
        
        // OrderItems
        List<OrderItemIdentView> orderItems = form.getOrderItems();
        List<OrderItemDescData> orderItemsDesc = new ArrayList<OrderItemDescData>();
        if (Utility.isSet(orderItems)) {
            SimpleDateFormat systemSdf = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN);
            for(OrderItemIdentView orderItem : orderItems) {
                OrderItemDescData orderItemDesc = new OrderItemDescData();
                
                orderItemDesc.setOrderItem(orderItem.getOrderItem());
                orderItemDesc.setOrderItemSubstitutionList(orderItem.getOrderItemSubstitutions());
                orderItemDesc.setOrderItemActionList(orderItem.getOrderItemActions());
                
                if (Utility.isSet(orderItem.getOrderItemActionDescs())) {
                    List<OrderItemActionDescData> orderItemActionDescsD = new ArrayList<OrderItemActionDescData>();
                    
                    OrderItemActionDescData orderItemActionDescD;
                    for (OrderItemActionDescView orderItemActionDescView : orderItem.getOrderItemActionDescs()) {
                        orderItemActionDescD = new OrderItemActionDescData();
                        orderItemActionDescD.setOrderItemAction(orderItemActionDescView.getOrderItemAction());
                        orderItemActionDescD.setItemDistCost(orderItemActionDescView.getItemDistCost());
                        orderItemActionDescD.setItemPack(orderItemActionDescView.getItemPack());
                        orderItemActionDescD.setItemShortDesc(orderItemActionDescView.getItemShortDesc());
                        orderItemActionDescD.setItemSkuNum(orderItemActionDescView.getItemSkuNum());
                        orderItemActionDescD.setDistItemSkuNum(orderItemActionDescView.getDistItemSkuNum());
                        orderItemActionDescD.setItemUom(orderItemActionDescView.getItemUom());
                        
                        orderItemActionDescsD.add(orderItemActionDescD);
                    }
                    orderItemDesc.setOrderItemActionDescList(orderItemActionDescsD);
                }
                
                orderItemDesc.setInvoiceDistDetailList(orderItem.getInvoiceDistDetails());
                orderItemDesc.setInvoiceCustDetailList(orderItem.getInvoiceCustDetails());
                orderItemDesc.setOrderItemNotes(orderItem.getOrderItemNotes());
                orderItemDesc.setPurchaseOrderData(orderItem.getPurchaseOrder());
                orderItemDesc.setInvoiceDistDataList(orderItem.getInvoiceDistDatas());
                orderItemDesc.setDistPoNote(orderItem.getDistPoNote());
                orderItemDesc.setAssetInfo(orderItem.getAssetInfo());
                orderItemDesc.setItemInfo(orderItem.getItemInfo());
                orderItemDesc.setOpenLineStatusCd(orderItem.getOpenLineStatusCd());
                orderItemDesc.setOrderFreight(orderItem.getOrderFreight());
                orderItemDesc.setOrderHandling(orderItem.getOrderHandling());
                orderItemDesc.setOrderDiscount(orderItem.getOrderDiscount());
                orderItemDesc.setOrderFuelSurcharge(orderItem.getOrderFuelSurcharge());
                orderItemDesc.setOrderSmallOrderFee(orderItem.getOrderSmallOrderFee());

                orderItemDesc.setNewInvoiceDistDetail(orderItem.getNewInvoiceDistDetail());
                orderItemDesc.setWorkingInvoiceDistDetailData(orderItem.getWorkingInvoiceDistDetail());

                orderItemDesc.setItemQuantityS(orderItem.getItemQuantity());
                orderItemDesc.setCwCostS(orderItem.getNewCwCost());
                orderItemDesc.setDistLineNumS(orderItem.getDistLineNum());
                orderItemDesc.setItemSkuNumS(orderItem.getNewItemSkuNum());
                orderItemDesc.setItemIdS(orderItem.getNewItemId());
                orderItemDesc.setOrderItemIdS(orderItem.getOrderItemIdStr());
                orderItemDesc.setLineTotalS(orderItem.getLineTotal());
                orderItemDesc.setItemPriceS(orderItem.getNewItemPrice());
                orderItemDesc.setReSale(orderItem.isReSale());

                orderItemDesc.setDistName(orderItem.getDistName());
                orderItemDesc.setDistId(orderItem.getDistId());
                if (orderItem.getNewDistView() != null) {
                    DistributorListView distView = orderItem.getNewDistView();
                    if (distView.getDistributorName() != null) {
                        orderItemDesc.setNewDistName(distView.getDistributorName());
                    }
                    if (distView.getDistributorId() != null) {
                        orderItemDesc.setDistIdS(distView.getDistributorId().toString());
                    }
                }
                if (orderItem.getNewAssetView() != null) { 
                    AssetListView assetView = orderItem.getNewAssetView();
                    if (assetView.getAssetName() != null) {
                        orderItemDesc.setNewAssetName(assetView.getAssetName());
                    }
                    if (assetView.getAssetId() != null) {
                        orderItemDesc.setAssetIdS(assetView.getAssetId().toString());
                    }
                }
                if (orderItem.getNewServiceView() != null) { 
                    ServiceListView serviceView = orderItem.getNewServiceView();
                    if (serviceView.getServiceName() != null) {
                        orderItemDesc.setNewServiceName(serviceView.getServiceName());
                    }
                }
                
                orderItemDesc.setHasNote(orderItem.isHasNote());
                if (orderItem.getOrderItem().getTargetShipDate() != null) {
                    orderItemDesc.setTargetShipDateString(systemSdf.format(orderItem.getOrderItem().getTargetShipDate()));
                }
                orderItemDesc.setShipStatus(orderItem.getShipStatus());
                orderItemDesc.setQtyReturnedString(orderItem.getQtyReturned());
                orderItemDesc.setDeliveryDate(orderItem.getDeliveryDate());

                orderItemDesc.setItemStatus(orderItem.getNewItemStatus());
                orderItemDesc.setPoItemStatus(orderItem.getNewPoItemStatus());

                orderItemDesc.setDistRuntimeDisplayName(orderItem.getDistRuntimeDisplayName());
                orderItemDesc.setCalculatedSalesTax(orderItem.getCalculatedSalesTax());
                orderItemDesc.setStandardProductList(orderItem.getStandardProductListFlag());
                orderItemDesc.setCatalogItemSkuNum(orderItem.getCatalogItemSkuNum());
                orderItemDesc.setTaxExempt(orderItem.isTaxExempt());
                orderItemDesc.setShoppingHistory(orderItem.getShoppingHistory());
                orderItemDesc.setNewOrderItemActionQtyS(orderItem.getNewOrderItemActionQtyStr());
                orderItemDesc.setTargetShipDate(orderItem.getOrderItem().getTargetShipDate());
                orderItemDesc.setItemQuantityRecvdS(orderItem.getItemQuantityReceived());
                orderItemDesc.setActualCost(orderItem.getActualCost());
                if (orderItem.getActualQty() != null) {
                    orderItemDesc.setActualQty(orderItem.getActualQty().intValue());
                }
                if (orderItem.getNewOrderItemActionQty() != null) {
                    orderItemDesc.setNewOrderItemActionQty(orderItem.getNewOrderItemActionQty().intValue());
                }
                
                orderItemsDesc.add(orderItemDesc);
            }
        }
        crData.setOrderItemsDesc(orderItemsDesc);

        requestStjohnService(crData, "/service/updateOrder", webErrors);

        if (webErrors.size(MessageType.ERROR) == 0) {
            prepareOrderDetails(form);
            
            SuccessActionMessage message = new SuccessActionMessage("admin.order.updatedAndSubmittedForProcessing");
            WebAction.success(request, message);
        }
        
        logger.info("save()=> END.");
        
        return "order/edit";
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(WebRequest request,
                       @ModelAttribute(SessionKey.ORDER) OrderForm form,
                       @PathVariable("orderId") Long orderId,
                       Model model) {

        logger.info("show()=> BEGIN");
        
        form.setOrderId(orderId);

        prepareOrderDetails(form);
        
        model.addAttribute(SessionKey.ORDER, form);

        logger.info("show()=> END.");
        
        return "order/edit";
    }
    
    @RequestMapping(value = "/printOrderDetails", method = RequestMethod.POST)
    public String printOrderDetails(WebRequest request,
                       @ModelAttribute(SessionKey.ORDER) OrderForm form,
                       @PathVariable("orderId") Long orderId,
                       Model model) {

        logger.info("printOrderDetails()=>");

        return "order/printOrderDetails";
    }
    
    @RequestMapping(value = "/backToEdit", method = RequestMethod.POST)
    public String backToEdit(WebRequest request,
                       @ModelAttribute(SessionKey.ORDER) OrderForm form,
                       @PathVariable("orderId") Long orderId,
                       Model model) {

        logger.info("backToEdit()=>");

        return "order/edit";
    }
    
    @RequestMapping(value = "/prepareTempPo", method = RequestMethod.GET)
    public String prepareTempPo(HttpSession session,
                                @ModelAttribute(SessionKey.ORDER) OrderForm detailForm,
                                @ModelAttribute(SessionKey.ORDER_PRINT_TEMP_PO) OrderPrintTempPoForm printForm,
                                Model model) throws Exception {

        if (printForm == null) {
            printForm = new OrderPrintTempPoForm();
        }
        
        if (detailForm != null) {
            List<Long> distributorIds = new ArrayList<Long>();
            printForm.setOrderId(detailForm.getOrderInfo().getOrderData().getOrderId());
            
            List<OrderItemIdentView> orderItems = detailForm.getOrderItems();
            if (Utility.isSet(orderItems)) {
                for (OrderItemIdentView orderItem : orderItems) {
                    if (orderItem.getDistId() != null) {
                        distributorIds.add(orderItem.getDistId());
                    }
                }
                if (Utility.isSet(distributorIds)) {
                    List<BusEntityData> distributorsInfo = busEntityService.find(distributorIds, RefCodeNames.BUS_ENTITY_TYPE_CD.DISTRIBUTOR);
                    printForm.setDistributorsInfo(distributorsInfo);
                }
                
                printForm.setInit(true);
            }
        }

        model.addAttribute(SessionKey.ORDER_PRINT_TEMP_PO, printForm);

        return "order/printOrderTempPo";
    }
    
    @RequestMapping(value = "/printTempPo", method = RequestMethod.POST)
    public String printTempPo(WebRequest webRequest,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session,
                              @ModelAttribute(SessionKey.ORDER) OrderForm form,
                              @ModelAttribute(SessionKey.ORDER_PRINT_TEMP_PO) OrderPrintTempPoForm printForm) throws Exception {
        
        Long storeId = getStoreId();
        
        StoreIdentView storeView = storeService.findStoreIdent(storeId);

        PdfTemporaryPO pdf = new PdfTemporaryPO();

        AppUser appUser = getAppUser();
        AppStoreContext storeContext = appUser.getContext(AppCtx.STORE);

        byte[] storeLogoData = storeContext.getUiOptions().getLogoBinaryData();
        if (storeLogoData == null || storeLogoData.length == 0) {
            storeLogoData = WebTool.getDefaultLogo(request);
        }

        //sets the content type so the browser knows this is a pdf
        response.setContentType("application/pdf");
        String browser = (String) request.getHeader("User-Agent");
        boolean isMSIE6 = (browser != null) && (browser.indexOf("MSIE 6") >= 0);
        if (!isMSIE6)  {
            response.setHeader("extension", "pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + request.getServletPath().substring(request.getServletPath().lastIndexOf('/') + 1) + ".pdf");
        }

        //gets the references to the objects our po needs
        OrderData orderD = form.getOrderInfo().getOrderData();

        Long selectedDistributorId = printForm.getSelectedDistributorId();
        List<BusEntityData> distributorsInfo = printForm.getDistributorsInfo();
        DistributorInfoView distributorView;

        List<OrderItemIdentView> orderItems = form.getOrderItems();
        List<OrderItemData> distOrderItems = new ArrayList<OrderItemData>();

        if (Utility.isSet(selectedDistributorId)) {
            distributorView = distributorService.findDistributorInfo(selectedDistributorId);
            String distErpNum = distributorView.getBusEntityData().getErpNum();

            if (Utility.isSet(orderItems)) {
                for (OrderItemIdentView orderItemIdent : orderItems) {
                    if (distErpNum.equals(orderItemIdent.getOrderItem().getDistErpNum())) {
                        distOrderItems.add(orderItemIdent.getOrderItem());
                    }
                }
            }
        } else {
            distributorView = new DistributorInfoView();
            if (Utility.isSet(orderItems)) {
                for (OrderItemIdentView orderItemIdent : orderItems) {
                    distOrderItems.add(orderItemIdent.getOrderItem());
                }
            }
        }

        printForm.setDistributorView(distributorView);
        printForm.setDistOrderItems(distOrderItems);

        Set<Long> itemIds = new HashSet<Long>();
        Set<Long> assetIds = new HashSet<Long>();
        for(OrderItemData orderItem : distOrderItems) {
            itemIds.add(orderItem.getItemId());
            assetIds.add(orderItem.getAssetId());
        }

        List<ItemData> items = orderService.getItemDataList(itemIds);
        boolean isSimpleServiceOrder = OrderUtil.isSimpleServiceOrder(items);
        HashMap<Long, AssetData> assetInfo = null;
        if (isSimpleServiceOrder) {
            List<AssetData> assets = assetService.getAssetDataList(assetIds);
            if (Utility.isSet(assets)) {
                assetInfo = new HashMap<Long, AssetData>();
                for (AssetData asset : assets) {
                    assetInfo.put(asset.getAssetId(), asset);
                }
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (isSimpleServiceOrder) {
            pdf.constructPdfPO(distributorView,
                               orderD,
                               form.getOrderInfo().getShipTo(),
                               distOrderItems,
                               form.getOrderInfo().getAccountName(),
                               storeView,
                               assetInfo,
                               isSimpleServiceOrder,
                               out,
                               storeLogoData);
        } else {
            pdf.constructPdfPO(distributorView,
                               orderD,
                               form.getOrderInfo().getShipTo(),
                               distOrderItems,
                               form.getOrderInfo().getAccountName(),
                               storeView,
                               out,
                               storeLogoData);
        }

        response.setContentLength(out.size());
        out.writeTo(response.getOutputStream());
        response.flushBuffer();
        response.getOutputStream().close();
        
        return "order/edit";     
    }
    
    @RequestMapping(value = "/viewItemNote", method = RequestMethod.GET)
    public String viewItemNote(WebRequest webRequest, HttpSession session,
                        @RequestParam(value = "index", required = false) Integer index,
                        @RequestParam(value = "view", required = false) String view,
                        @ModelAttribute(SessionKey.ORDER) OrderForm form,
                        @ModelAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER) OrderItemNoteFilterForm locateForm,
                        Model model) {

        logger.info("viewItemNote()=> BEGIN");
        
        if (locateForm == null) {
            locateForm = new OrderItemNoteFilterForm();
            locateForm.initialize();
        }
        
        boolean viewB = true;

        if (Utility.isSet(view)) {
            viewB = Boolean.parseBoolean(view);
        }
        locateForm.setView(viewB);
        locateForm.setType("orderItemNote");

        if (index != null) {     
            OrderItemIdentView orderItemView = form.getOrderItems().get(index);
            locateForm.setOrderItemViews(Utility.toList(orderItemView));
        }
        
        model.addAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER, locateForm);
        logger.info("viewItemNote()=> END. ");
        
        return "order/orderNote";
    }
    
    @RequestMapping(value = "/addItemNote", method = RequestMethod.GET)
    public String addItemNote(WebRequest webRequest, HttpSession session,
                        @RequestParam(value = "view", required = false) String view,
                        @ModelAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER) OrderItemNoteFilterForm locateForm,
                        Model model) {

        logger.info("addItemNote()=> BEGIN");
        
        if (locateForm == null) {
            locateForm = new OrderItemNoteFilterForm();
            locateForm.initialize();
        }
        
        boolean viewB = true;
        if (Utility.isSet(view)) {
            viewB = Boolean.parseBoolean(view);
        }
        locateForm.setView(viewB);
        locateForm.setType("orderItemNote");
        locateForm.setOrderItemNoteField("");
        
        model.addAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER, locateForm);
        logger.info("addItemNote()=> END. ");
        
        return "order/orderNote";
    }
    
    @RequestMapping(value = "/saveItemNote", method = RequestMethod.POST)
    public String saveItemNote(WebRequest webRequest, HttpSession session,
                        @RequestParam(value = "index", required = false) Integer index,
                        @ModelAttribute(SessionKey.ORDER) OrderForm form,
                        @ModelAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER) OrderItemNoteFilterForm locateForm,
                        Model model) {

        logger.info("addItemNote()=> BEGIN");
        
        if (locateForm != null) {
            String itemNote = locateForm.getOrderItemNoteField();

            if (index != null) {
                if (form != null) {
                    OrderItemIdentView orderItem = form.getOrderItems().get(index);
                    List<OrderPropertyData> orderItemNotes = orderItem.getOrderItemNotes();
                    
                    OrderPropertyData orderProperty = new OrderPropertyData();
                    orderProperty.setOrderItemId(orderItem.getOrderItem().getOrderItemId());
                    orderProperty.setOrderPropertyTypeCd(RefCodeNames.ORDER_PROPERTY_TYPE_CD.ORDER_NOTE);
                    orderProperty.setOrderPropertyStatusCd(RefCodeNames.ORDER_PROPERTY_STATUS_CD.ACTIVE);

                    orderProperty.setShortDesc(itemNote.length() > 250 ? itemNote.substring(0, 250) : itemNote);
                    orderProperty.setValue(itemNote);
                    
                    OrderPropertyData savedNote = propertyService.saveOrderProperty(locateForm.getOrderId(), orderProperty);
                
                    if (savedNote != null) {
                        orderItemNotes.add(orderProperty);
                        if (!orderItem.isHasNote()) {
                            orderItem.setHasNote(true);
                        }
                    }
                }
            }
        }

        logger.info("saveItemNote()=> END. ");
        
        return "order/edit";
    }
    
    @RequestMapping(value = "/viewOrderNote", method = RequestMethod.GET)
    public String viewOrderNote(WebRequest webRequest, HttpSession session,
                        @RequestParam(value = "view", required = false) String view,
                        @ModelAttribute(SessionKey.ORDER) OrderForm form,
                        @ModelAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER) OrderItemNoteFilterForm locateForm,
                        Model model) {

        logger.info("viewOrderNote()=> BEGIN");
        
        if (locateForm == null) {
            locateForm = new OrderItemNoteFilterForm();
            locateForm.initialize();
        }
        
        boolean viewB = true;

        if (Utility.isSet(view)) {
            viewB = Boolean.parseBoolean(view);
        }
        locateForm.setView(viewB);
        locateForm.setType("orderNote");
  
        OrderIdentView orderView = form.getOrderInfo();
        List<OrderPropertyData> orderProperties = orderView.getOrderProperties();
        if (Utility.isSet(orderProperties)) {
            List<OrderPropertyData> orderNotes = new ArrayList<OrderPropertyData>();
            for (OrderPropertyData property : orderProperties) {
                if (RefCodeNames.ORDER_PROPERTY_TYPE_CD.ORDER_NOTE.equals(property.getOrderPropertyTypeCd()) ||
                    RefCodeNames.ORDER_PROPERTY_TYPE_CD.CUSTOMER_CART_COMMENTS.equals(property.getOrderPropertyTypeCd())) {
                    orderNotes.add(property);
                }
            }
            WebSort.sort(orderNotes, OrderPropertyData.ORDER_PROPERTY_ID);
            locateForm.setOrderNotes(orderNotes);
        }
        
        locateForm.setOrderItemViews(form.getOrderItems());
        
        model.addAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER, locateForm);
        logger.info("viewOrderNote()=> END. ");
        
        return "order/orderNote";
    }
    
    @RequestMapping(value = "/addOrderNote", method = RequestMethod.GET)
    public String addOrderNote(WebRequest webRequest, HttpSession session,
                        @RequestParam(value = "view", required = false) String view,
                        @ModelAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER) OrderItemNoteFilterForm locateForm,
                        Model model) {

        logger.info("addOrderNote()=> BEGIN");
        
        if (locateForm == null) {
            locateForm = new OrderItemNoteFilterForm();
            locateForm.initialize();
        }
        
        boolean viewB = true;
        if (Utility.isSet(view)) {
            viewB = Boolean.parseBoolean(view);
        }
        locateForm.setView(viewB);
        locateForm.setType("orderNote");
        locateForm.setOrderItemNoteField("");
        
        model.addAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER, locateForm);
        logger.info("addOrderNote()=> END. ");
        
        return "order/orderNote";
    }
    
    @RequestMapping(value = "/saveOrderNote", method = RequestMethod.POST)
    public String saveOrderNote(WebRequest webRequest, HttpSession session,
                        @ModelAttribute(SessionKey.ORDER) OrderForm form,
                        @ModelAttribute(SessionKey.ORDER_ITEM_NOTE_FILTER) OrderItemNoteFilterForm locateForm,
                        Model model) {

        logger.info("saveOrderNote()=> BEGIN");
        
        if (locateForm != null) {
            String itemNote = locateForm.getOrderItemNoteField();

            if (form != null) {
                OrderIdentView orderView = form.getOrderInfo();
                List<OrderPropertyData> orderProperties = orderView.getOrderProperties();

                OrderPropertyData orderProperty = new OrderPropertyData();
                orderProperty.setOrderId(orderView.getOrderData().getOrderId());
                orderProperty.setOrderPropertyTypeCd(RefCodeNames.ORDER_PROPERTY_TYPE_CD.ORDER_NOTE);
                orderProperty.setOrderPropertyStatusCd(RefCodeNames.ORDER_PROPERTY_STATUS_CD.ACTIVE);

                orderProperty.setShortDesc(itemNote.length() > 250 ? itemNote.substring(0, 250) : itemNote);
                orderProperty.setValue(itemNote);

                OrderPropertyData savedNote = propertyService.saveOrderProperty(locateForm.getOrderId(), orderProperty);
                
                if (savedNote != null) {
                    orderProperties.add(savedNote);
                }
            }
        }

        logger.info("saveOrderNote()=> END. ");
        
        return "order/edit";
    }

    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public String addItem(WebRequest webRequest,
                          HttpSession session,
                          @ModelAttribute(SessionKey.ORDER) OrderForm form,
                          Model model) {

        logger.info("addItem()=> BEGIN");
        
        AppUser user = getAppUser();
        
        if (!user.isQneOfAdmin()) { 
            WebErrors webErrors = new WebErrors(webRequest);
            
            webErrors.putError("validation.web.error.unauthorizedAccess");
            return "order/edit";
        }
        
        Long orderId = form.getOrderInfo().getOrderData().getOrderId();

        // only order with PENDING_APPROVAL status can be updated
        // ??????????????????????????????????????????????????????
        // commented out
        
        List<OrderItemIdentView> orderItems = form.getOrderItems();
        if (!Utility.isSet(orderItems)) {
            orderItems = new ArrayList<OrderItemIdentView>();
        }

        // get the maxium lineNum
        Long maxLineNum = 0L;
        Long maxCustLine = 0L;
        String saleType = null;
        for (OrderItemIdentView orderItem : orderItems) {
            saleType = orderItem.getOrderItem().getSaleTypeCd();
            if (maxLineNum < orderItem.getOrderItem().getOrderLineNum()) {
                maxLineNum = orderItem.getOrderItem().getOrderLineNum();
            }
            if (maxCustLine < orderItem.getOrderItem().getCustLineNum()) {
                maxCustLine = orderItem.getOrderItem().getCustLineNum();
            }
        }

        OrderItemIdentView newOrderItemView = new OrderItemIdentView();
        newOrderItemView.setAssetInfo(new AssetData());
        newOrderItemView.setItemInfo(new ItemData());
        OrderItemData newOrderItemD = new OrderItemData();
        newOrderItemD.setOrderId(orderId);
        newOrderItemD.setOrderLineNum(maxLineNum + 1);
        newOrderItemD.setCustLineNum(maxCustLine + 1);
        newOrderItemD.setSaleTypeCd(saleType);
        newOrderItemD.setTotalQuantityOrdered(1L);
        newOrderItemD.setAddBy(user.getUserName());
        newOrderItemView.setOrderItem(newOrderItemD);
        
        List<OrderItemData> orderItemsData = form.getOrderInfo().getOrderItems();
        if (Utility.isSet(orderItemsData)) {
            orderItemsData.add(newOrderItemD);
        }

        orderItems.add(newOrderItemView);

        form.setOrderItems(orderItems);

        SelectableObjects selectToCancel = form.getSelectToCancel();

        selectToCancel = new SelectableObjects(orderItems,
                                               selectToCancel.getSelected(),
                                               AppComparator.ORDER_ITEM_ERP_PO_NUM_COMPARATOR);
        
        form.setSelectToCancel(selectToCancel);
        
        logger.info("addItem()=> END" );
        
        return "order/edit";
    }
    
    
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public String cancelOrder(WebRequest webRequest,
                          HttpSession session,
                          @PathVariable("orderId") Long orderId,
                          @ModelAttribute(SessionKey.ORDER) OrderForm form,
                          Model model) {
        logger.info("cancelOrder()=> BEGIN" );
        
        WebErrors webErrors = new WebErrors(webRequest);
        
        AppUser user = getAppUser();
        if (!user.isQneOfAdmin()) { 
            webErrors.putError("validation.web.error.unauthorizedAccess");

            return "order/edit";
        }
        
        OrderCancelRequestData cancelRequest = new OrderCancelRequestData();        
        cancelRequest.setOrderId(form.getOrderInfo().getOrderData().getOrderId().intValue());        
        requestStjohnService(cancelRequest, "/service/cancelOrder", webErrors);

        if (webErrors.size(MessageType.ERROR) == 0) {
            SuccessActionMessage message = new SuccessActionMessage("admin.order.updatedAndSubmittedForProcessing");
            WebAction.success(webRequest, message);
        }

        logger.info("cancelOrder()=> END" );
        
        return "order/edit";
    }
    
    @RequestMapping(value = "/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.ORDER) OrderForm form,
                                       @PathVariable String field) throws Exception {
        WebSort.sort(form.getOrderItems(), OrderItemIdentView.ORDER_ITEM + "." + field, null, OrderItemData.class);

        return "order/edit";
    }
    
    @RequestMapping(value = "/clear/location", method = RequestMethod.POST)
    public String clearLocationFilter(WebRequest request, @ModelAttribute(SessionKey.ORDER) OrderForm form) throws Exception {
        form.setFilteredLocations(Utility.emptyList(SiteListView.class));
        form.setLocationFilter(null);
        return redirect(request, UrlPathKey.ORDER.IDENTIFICATION);
    }


    @ModelAttribute(SessionKey.ORDER)
    public OrderForm initModel(@PathVariable("orderId") Long orderId) {
        
        OrderForm form = new OrderForm();
        
        form.setOrderId(orderId);
        form.initialize();

        return form;
    }
    
    private void prepareOrderDetails(OrderForm form) {
        Long orderId = form.getOrderId();

        OrderIdentView orderInfo = orderService.findOrderToEdit(getStoreId(), orderId);
        List<OrderItemIdentView> orderItems = orderService.findOrderItems(orderId, null, null);

        List<PropertyData> storeProperties = storeService.findStoreProperties(getStoreId(),
                                             Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.STORE_TYPE_CD));
        String storeType = "";
        if (Utility.isSet(storeProperties)) {
            storeType = storeProperties.get(0).getValue();
        }
        
        if (RefCodeNames.STORE_TYPE_CD.DISTRIBUTOR.equals(storeType)) {
            //sort the items by distributor and po number.
            Collections.sort(orderItems, AppComparator.ORDER_ITEM_ERP_PO_NUM_COMPARATOR);
        }
        
        List<ItemData> items = new ArrayList<ItemData>();
        if (Utility.isSet(orderItems)) {
            for (OrderItemIdentView orderItem : orderItems) {
                if (Utility.isSet(orderItem.getItemInfo())) {
                    items.add(orderItem.getItemInfo());
                }
            }
        }
        Boolean isSimpleServiceOrder = OrderUtil.isSimpleServiceOrder(items);
        
        Boolean showDistNote = false;
        List<PropertyData> showDistrNotesToCust = storeService.findStoreProperties(getStoreId(),
                                             Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.SHOW_DISTR_NOTES_TO_CUSTOMER));
        if (Utility.isSet(showDistrNotesToCust)) {
            if (Utility.isTrue(showDistrNotesToCust.get(0).getValue())) {
                showDistNote = true;
            }
        }
        
        Boolean reBillOrder = false;
        List<OrderPropertyData> reBillOrderProps = orderService.findOrderProperties(getStoreId(),
                                             Utility.toList(RefCodeNames.ORDER_PROPERTY_TYPE_CD.REBILL_ORDER));
        if (Utility.isSet(reBillOrderProps)) {
            if (Utility.isTrue(reBillOrderProps.get(0).getValue())) {
                reBillOrder = true;
            }
        }
        
        List<OrderPropertyData> customerOrderNotes = orderService.findOrderProperties(getStoreId(),
                                             Utility.toList(RefCodeNames.ORDER_PROPERTY_TYPE_CD.CUSTOMER_ORDER_NOTE));
        UserData placedByUser = null;
        if (orderInfo != null) {
            placedByUser = userService.findByUserName(orderInfo.getOrderData().getAddBy(), false);
        }
        String orderPlacedBy = "";
        if (Utility.isSet(placedByUser)) {
            if (Utility.isSet(placedByUser.getLastName())) {
                orderPlacedBy += placedByUser.getLastName();
            }
            if (Utility.isSet(placedByUser.getFirstName()) && orderPlacedBy.length() > 0) {
                orderPlacedBy = placedByUser.getFirstName() + " " + orderPlacedBy;
            }
        }
        
        List<NoteJoinView> siteCRCNotes = orderService.getSiteCrcNotes(orderInfo.getOrderData().getSiteId());
        
        List<InvoiceCustView> invoicesInfo = orderService.getInvoiceCustByWebOrderNum(orderInfo.getOrderData().getOrderNum(), getStoreId(), true);
        
        if (orderInfo != null && orderInfo.getOrderData() != null) {
            DbConstantResource resource = AppResourceHolder.getAppResource().getDbConstantsResource();
            WebFormUtil.fillOutOrderForm (form,
                                          orderInfo,
                                          orderItems,
                                          customerOrderNotes,
                                          siteCRCNotes,
                                          invoicesInfo,
                                          storeType,
                                          orderPlacedBy,
                                          isSimpleServiceOrder,
                                          showDistNote,
                                          reBillOrder,
                                          getAppLocale(),
                                          resource);
        }

        SelectableObjects selectToCancel = new SelectableObjects(orderItems,
                                                                 new ArrayList<OrderItemIdentView>(),
                                                                 AppComparator.ORDER_ITEM_ERP_PO_NUM_COMPARATOR);
        form.setSelectToCancel(selectToCancel);
    }

}
