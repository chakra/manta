package com.espendwise.manta.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.apache.log4j.Logger;
import com.espendwise.manta.web.util.*;
//import com.espendwise.manta.web.validator.OrderGuideWebUpdateExceptionResolver;
import com.espendwise.manta.web.validator.OrderGuideFormValidator;
import com.espendwise.manta.web.forms.*;
import com.espendwise.manta.service.OrderGuideService;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.spi.SuccessMessage;

import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.arguments.Args;
import java.math.BigDecimal;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(UrlPathKey.SITE.ORDER_GUIDE_DETAIL)
@SessionAttributes({SessionKey.SITE_ORDER_GUIDE_DETAIL})
public class OrderGuideController extends BaseController {

    private static final Logger logger = Logger.getLogger(OrderGuideController.class);

    private OrderGuideService orderGuideService;
    private CatalogService catalogService;

    @Autowired
    public OrderGuideController(OrderGuideService orderGuideService, CatalogService catalogService) {
        this.orderGuideService = orderGuideService;
        this.catalogService  = catalogService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
       // webErrors.putErrors(ex, new OrderGuideWebUpdateExceptionResolver());

        return "site/orderGuides/edit";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(HttpServletRequest request,
                         @ModelAttribute(SessionKey.SITE_ORDER_GUIDE) SiteOrderGuideForm form,
                         Model model) {
        logger.info("create()=> BEGIN " );

        OrderGuideForm formDetail = new OrderGuideForm();
        formDetail.setSiteId(form.getSiteId());
        formDetail.initialize();

        CatalogData catalogData = orderGuideService.getSiteCatalog(form.getSiteId());
        if (catalogData != null) {
            formDetail.setCatalogId(catalogData.getCatalogId());
            formDetail.setCatalogName(catalogData.getShortDesc());
        }

        PropertyData shareOrderGuideProp = orderGuideService.getShareOrderGuideProperty(form.getSiteId());
        if (shareOrderGuideProp != null) {
            formDetail.setShareBuyerOrderGuide(Utility.isTrue(shareOrderGuideProp.getValue()));
        }

        // get items
        ProductListViewCriteria criteria = new ProductListViewCriteria(getStoreId(), Constants.FILTER_RESULT_LIMIT.ITEM);
        criteria.setStoreId(getStoreId());
        criteria.setCatalogId(formDetail.getCatalogId());

        criteria.setSubstituteItemSKUbyCustSKU(false);
        criteria.setActiveOnly(true);
        criteria.setLimit(Constants.FILTER_RESULT_LIMIT.ITEM);

        List<ProductListView> items = catalogService.findProductsByCriteria(criteria);
        List<OrderGuideItemView> ogItems = WebFormUtil.fillOutOrderGuideItemsList(orderGuideService, formDetail.getCatalogId(), items);
        formDetail.addNewItems(ogItems);
        formDetail.setTotalAmount(new BigDecimal(0));

        model.addAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL, formDetail);

        return "site/orderGuides/edit";
        

    }

    @RequestMapping(value = "/redraw", method = RequestMethod.GET)
    public String redraw(WebRequest webRequest,
                       @ModelAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL)OrderGuideForm form,
                       @PathVariable(IdPathKey.LOCATION_ID) Long siteId,
                       @PathVariable(IdPathKey.ORDER_GUIDE_ID) Long orderGuideId,
                       Model model) {
        logger.info("redraw()=> BEGIN");
        
        return "site/orderGuides/edit";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(WebRequest webRequest,
                       @ModelAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL)OrderGuideForm form,
                       //@ModelAttribute(SessionKey.SITE_ORDER_GUIDE_ITEMS_FILTER_RESULT)OrderGuideItemsFilterResultForm orderGuideItemsForm,
                       @PathVariable(IdPathKey.LOCATION_ID) Long siteId,
                       @PathVariable(IdPathKey.ORDER_GUIDE_ID) Long orderGuideId,
                       Model model) {

        logger.info("show()=> BEGIN" +
                ", siteId: " + siteId +
                ", orderGuideId: " + orderGuideId );

        logger.info("show()=> BEGIN");


        OrderGuideIdentView orderGuide = orderGuideService.findOrderGuideIdentView(orderGuideId, siteId);

        if (orderGuide != null) {
            form.setSiteId(siteId);
            form.setOrderGuideId(orderGuide.getOrderGuideData().getOrderGuideId());
            form.setOrderGuideName(orderGuide.getOrderGuideData().getShortDesc());
            form.setOrderGuideTypeCd(orderGuide.getOrderGuideData().getOrderGuideTypeCd());
            form.setCatalogId(orderGuide.getOrderGuideData().getCatalogId());
            form.setCatalogName(orderGuide.getCatalogName());
            if (Utility.isSet(orderGuide.getShareBuyerOrderGuides())) {
                form.setShareBuyerOrderGuide(Utility.isTrue(orderGuide.getShareBuyerOrderGuides().getValue()));
            }
            List<OrderGuideItemView> items = orderGuide.getItems();
            BigDecimal totalAmount = new BigDecimal(0); 
            if (Utility.isSet(items)) {
                for (OrderGuideItemView item : items) {
                    Long qty = item.getQuantity();
                    BigDecimal price = item.getItemPrice();
                    if (Utility.isSet(qty) && Utility.isSet(price)) {
                        totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(qty)));
                    }  
                }
        	List<OrderGuideItemView> configured = new ArrayList<OrderGuideItemView>();
                SelectableObjects<OrderGuideItemView> selectableObj = new SelectableObjects<OrderGuideItemView>(
                                    items,
                                    configured,
                                    AppComparator.ORDER_GUIDE_ITEMS_COMPARATOR);
        	form.setItems(selectableObj);
                logger.info("show()==>showItems()=> Items : " + items.size());
            } else {
                form.setItems(null);
            }
            form.setTotalAmount(totalAmount);
        }

        //showItems(form.getOrderGuideId(), siteId, form.getCatalogId(), form);

        model.addAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL, form);

        logger.info("show()=> detailForm.hashCode() = " + form.hashCode());

        EntityHeaderView header = new EntityHeaderView(orderGuide.getOrderGuideData().getOrderGuideId(),
                                                       orderGuide.getOrderGuideData().getShortDesc());
        webRequest.setAttribute(SessionKey.ORDER_GUIDE_HEADER, header, WebRequest.SCOPE_SESSION);

        logger.info("show()=> END.");

        return "site/orderGuides/edit";


    }


    private void showItems(Long orderGuideId, Long siteId, Long catalogId, OrderGuideForm form ){
        logger.info("show()==>showItems()=> BEGIN" );
        if (orderGuideId > 0){
        	List<OrderGuideItemView> items = orderGuideService.findOrderGuideItems(orderGuideId, siteId, catalogId);

            if (!Utility.isSet(items)) {
        		logger.info("show()==>showItems()=> END:  NO items. " );
        		form.setItems(null);
        		return;
        	}

        	List<OrderGuideItemView> configured = new ArrayList<OrderGuideItemView>();
            SelectableObjects<OrderGuideItemView> selectableObj = new SelectableObjects<OrderGuideItemView>(
                    items,
                    configured,
                    AppComparator.ORDER_GUIDE_ITEMS_COMPARATOR);

        	form.setItems(selectableObj);
            logger.info("show()==>showItems()=> Items : " + items.size());
        }
       // WebSort.sort(orderGuideItemsForm, OrderGuideItemView.PRODUCT_NAME);

        logger.info("show()==>showItems()=> END" );

    }

    @SuccessMessage
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(WebRequest request,
                       @ModelAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL) OrderGuideForm form,
                       @PathVariable(IdPathKey.LOCATION_ID) Long siteId,
                       Model model) {

        logger.info("save()=> BEGIN" + ", siteId: " + siteId );


        WebErrors webErrors = new WebErrors(request);

        String orderGuideName = Utility.isSet(form.getOrderGuideName()) ? form.getOrderGuideName().trim() : "";
       	try {

	        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form, new OrderGuideFormValidator());
	        
                if (!validationErrors.isEmpty()) {
	            webErrors.putErrors(validationErrors);
                }
                
                if (!Utility.isSet(form.getCatalogId())) {
                    webErrors.putMessage("validation.web.error.noCatalogForLocation");
                }

                if (form.isNew() && Utility.isSet(orderGuideName)) {
                    List<OrderGuideData> orderGuides = orderGuideService.findOrderGuidesByName(orderGuideName,
                                                                                               siteId,
                                                                                               form.getCatalogId());
                    if (Utility.isSet(orderGuides)) {
                        String orderGuideIds = Utility.toCommaString(Utility.toIds(orderGuides));
                        webErrors.putError("validation.web.error.orderGuideExist", Args.typed(orderGuideName, orderGuideIds));
                    }
                }
                
                if (webErrors.size(MessageType.ERROR) > 0) {
	            model.addAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL, form);
	            logger.info("save()=> ERRORS: " + webErrors);
	            return "site/orderGuides/edit";
	        }
        } catch (ValidationException e) {
            return handleValidationException(e, request);
        }

        OrderGuideIdentView orderGuide = new OrderGuideIdentView();
        if (!form.isNew()) {
        	orderGuide = orderGuideService.findOrderGuideIdentView(form.getOrderGuideId(), siteId);
        }
        OrderGuideData ogData = orderGuide.getOrderGuideData();
        if (ogData == null) {
            ogData = new OrderGuideData();
            ogData.setBusEntityId(siteId);
            ogData.setCatalogId(form.getCatalogId());

        }
        ogData.setShortDesc(orderGuideName);
        ogData.setOrderGuideTypeCd(form.getOrderGuideTypeCd());

        orderGuide.setOrderGuideData(ogData);

        if (form.getItems() != null) {
            if (form.isNew()) {
                List<OrderGuideItemView> selectedItems = form.getItems().getSelected();
                List<OrderGuideItemView> newItems = new ArrayList<OrderGuideItemView>();
                for (OrderGuideItemView item : selectedItems) {
                    String qtyStr = item.getQuantityStr();
                    Long qty = new Long(qtyStr);
                    if (qtyStr != null && qty.longValue() > 0) {
                        item.setQuantity(new Long(item.getQuantityStr()));
                        newItems.add(item);
                    }
                }
                orderGuide.setItems(newItems);

            } else {
                List<OrderGuideItemView> newItems = form.getItems().getValues();
                for (OrderGuideItemView item : newItems) {
                    item.setQuantity(new Long(item.getQuantityStr()));
                }
                orderGuide.setItems(newItems);
            }
        }

        try {
            orderGuide = orderGuideService.updateOrderGuide(orderGuide);

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        } catch (DatabaseUpdateException e1) {
        	e1.printStackTrace();
        	//return handleValidationException(e1, request);
        }
        logger.info("save()=> END, redirect to " + orderGuide.getOrderGuideData().getOrderGuideId());

        return redirect("../" + orderGuide.getOrderGuideData().getOrderGuideId());

    }


    @SuccessMessage
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(WebRequest request,
                       @ModelAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL) OrderGuideForm form,
                       //@PathVariable(IdPathKey.LOCATION_ID) Long siteId,
                       //@PathVariable(IdPathKey.ORDER_GUIDE_ID) Long orderGuideId,
                       Model model) {

        logger.info("delete()=> BEGIN" + ", siteId: " + form.getOrderGuideId() );

        if (!form.isNew()) {
            WebErrors webErrors = new WebErrors(request);
            
            OrderGuideIdentView orderGuide = orderGuideService.findOrderGuideIdentView(form.getOrderGuideId(), form.getSiteId());
                
            List<OrderScheduleData> schedules = orderGuideService.findOrderGuideSchedules(form.getOrderGuideId(), form.getSiteId());
                
            if (Utility.isSet(schedules)) {
                String scheduleIds = Utility.toCommaString(Utility.toIds(schedules));
                webErrors.putError("validation.web.error.orderGuideHasSchedule", Args.typed(scheduleIds));
                
                return "site/orderGuides/edit"; 
            }
            
            try {
                orderGuideService.deleteOrderGuide(orderGuide);

            } catch (ValidationException e) {

                return handleValidationException(e, request);

            } catch (Exception e1) {
                e1.printStackTrace();
                //return handleValidationException(e1, request);
            }
        }

        logger.info("delete()=> END, redirect to ");

        return redirect("../");

    }

    @RequestMapping(value = "/deleteItems", method = RequestMethod.POST)
    public String deleteItems(WebRequest request,
                              @PathVariable(IdPathKey.LOCATION_ID) Long siteId,
                              @PathVariable(IdPathKey.ORDER_GUIDE_ID) Long orderGuideId,
                              @ModelAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL) OrderGuideForm form,
                              Model model) {

        logger.info("delete()=> BEGIN");
        List<OrderGuideItemView> selected = form.getItems().getSelected();
        logger.info("delete()=> selected :" + selected);

      	try {
            if (Utility.isSet(selected)) {
       		orderGuideService.deleteOrderGuideItems(selected, form.getOrderGuideId());
            } else {
                return "site/orderGuides/edit";
            }
        } catch (ValidationException e) {
            return handleValidationException(e, request);
        }

        OrderGuideIdentView orderGuide = orderGuideService.findOrderGuideIdentView(orderGuideId, siteId);

        List<OrderGuideItemView> locatedItems = new ArrayList<OrderGuideItemView>();
        if (Utility.isSet(form.getItems())) {
            for (OrderGuideItemView item : form.getItems().getValues()) {
                if (item.getOrderGuideStructureId() == null) {
                    locatedItems.add(item);
                }
            }
        }
        
        SelectableObjects<OrderGuideItemView> selectableObj = null;
        List<OrderGuideItemView> itemsToShow = new ArrayList<OrderGuideItemView>();
        if (orderGuide != null) {
            List<OrderGuideItemView> items = orderGuide.getItems();
            if (Utility.isSet(items)) {
                itemsToShow.addAll(items);
            }
        }
        
        if (Utility.isSet(locatedItems)) {
            itemsToShow.addAll(locatedItems);
        }
                
        if (Utility.isSet(itemsToShow)) {
            selectableObj = new SelectableObjects<OrderGuideItemView>(
                                itemsToShow,
                                new ArrayList<OrderGuideItemView>(),
                                AppComparator.ORDER_GUIDE_ITEMS_COMPARATOR);
            logger.info("delete()==>toShowItems : " + itemsToShow.size());
        }
        
        form.setItems(selectableObj);
        
        //showItems(form.getOrderGuideId(), null, form.getCatalogId(), form);
        
        SuccessActionMessage message;

        WebAction.success(request, new SuccessActionMessage("admin.successMessage.success"));

        logger.info("delete()=> END.");

        return "site/orderGuides/edit";

    }


    @ModelAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL)
    public OrderGuideForm initModel(@PathVariable(IdPathKey.ORDER_GUIDE_ID) Long pOrderGuideId) {

        OrderGuideForm form = new OrderGuideForm();
        form.initialize();

        return form;

    }

    @RequestMapping(value = "/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL) OrderGuideForm form,
                       @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "site/orderGuides/edit";
    }

    @RequestMapping(value = "/addNewItems", method = RequestMethod.POST)
    public String addNewItems(WebRequest webRequest,
                       @ModelAttribute(SessionKey.SITE_ORDER_GUIDE_DETAIL)OrderGuideForm form,
                       @PathVariable(IdPathKey.LOCATION_ID) Long siteId,
                       @PathVariable(IdPathKey.ORDER_GUIDE_ID) Long orderGuideId,
                       Model model) {

        logger.info("reload()=> BEGIN" +
                ", siteId: " + siteId +
                ", orderGuideId: " + orderGuideId );

        logger.info("reload()=> END");

         return redirect("../" + form.getOrderGuideId());
    }


}
