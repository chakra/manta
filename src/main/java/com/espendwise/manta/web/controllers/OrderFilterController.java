package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.model.view.AccountListView;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.model.view.OrderListView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.service.OrderService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.DbConstantResource;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.OrderListViewCriteria;
import com.espendwise.manta.web.forms.OrderFilterForm;
import com.espendwise.manta.web.forms.OrderFilterResultForm;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.ORDER.FILTER)
@SessionAttributes({SessionKey.ORDER_FILTER_RESULT, SessionKey.ORDER_FILTER})
@AutoClean(SessionKey.ORDER_FILTER_RESULT)
public class OrderFilterController extends BaseController {

    private static final Logger logger = Logger.getLogger(OrderFilterController.class);

    private OrderService orderService;

    @Autowired
    public OrderFilterController(OrderService orderService) {
        this.orderService = orderService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }   
    
    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public String filter(@ModelAttribute(SessionKey.ORDER_FILTER) OrderFilterForm filterForm) {
        return "order/filter";
    }

    //request method changed to POST due to MANTA-625
    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String findOrders(WebRequest request,
                            @ModelAttribute(SessionKey.ORDER_FILTER_RESULT) OrderFilterResultForm form,
                            @ModelAttribute(SessionKey.ORDER_FILTER) OrderFilterForm filterForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "order/filter";
        }

        form.reset();
        AppLocale locale = getAppLocale();
        AppUser appUser = getAppUser();

        OrderListViewCriteria criteria = new OrderListViewCriteria(Utility.toList(getStoreId()), Constants.FILTER_RESULT_LIMIT.ORDER);

        criteria.setUserId(appUser.getUserId());
        criteria.setUserType(appUser.getUserTypeCd());
        criteria.setOrderFromDate(parseDateNN(locale, filterForm.getOrderFromDate()));
        criteria.setOrderToDate(parseDateNN(locale, filterForm.getOrderToDate()));
        criteria.setOutboundPoNum(filterForm.getOutboundPONum());
        criteria.setCustomerPoNum(filterForm.getCustomerPONum());
        criteria.setErpPoNum(filterForm.getErpPONum());
        criteria.setWebOrderConfirmationNum(filterForm.getWebOrderConfirmationNum());
        criteria.setRefOrderNum(filterForm.getRefOrderNum());
        criteria.setSiteZipCode(filterForm.getSiteZipCode());
        if (Utility.isSet(filterForm.getOrderMethod())) {
            criteria.setMethod((String)RefCodeNames.ORDER_SOURCE_CD.class.getField(filterForm.getOrderMethod()).get(null));
        }
        
        List<Pair<String, String>> selectedStatuses = filterForm.getSearchOrderStatuses().getSelected();
        if (Utility.isSet(selectedStatuses)) {
            List<String> statusList = new ArrayList<String>();
            for (Pair<String, String> statusPair : selectedStatuses) {
                statusList.add((String)RefCodeNames.ORDER_STATUS_CD.class.getField(statusPair.getObject1()).get(null));
            }
            criteria.setOrderStatuses(statusList);
        }

        criteria.setAccountFilter(Utility.splitLong(filterForm.getAccountFilter()));
        criteria.setDistibutorFilter(Utility.splitLong(filterForm.getDistributorFilter()));
        criteria.setSiteFilter(Utility.splitLong(filterForm.getSiteFilter()));
        //criteria.setUserFilter(Utility.splitLong(filterForm.getUserFilter()));
        if (Utility.isSet(filterForm.getFilteredUsers())) {
            List<String> placedBy = new ArrayList<String>();
            for (UserListView userView : filterForm.getFilteredUsers()) {
                placedBy.add(userView.getUserName());
            }
            criteria.setUserFilter(placedBy);
        }

        List<OrderListView> orders = orderService.findOrdersByCriteria(criteria);
        
        if (Utility.isSet(orders)) {
            for (OrderListView order : orders) {
                if (Utility.isSet(order.getRevisedOrderDate())) {
                    order.setOrderDate(order.getRevisedOrderDate());
                } else {
                    order.setOrderDate(order.getOriginalOrderDate());
                }
            }
        }

        form.setOrders(orders);

        WebSort.sort(form, OrderListView.WEB_ORDER_NUM);

        return "order/filter";
    }

    @RequestMapping(value = "/filter/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.ORDER_FILTER_RESULT) OrderFilterResultForm form, @PathVariable String field) throws Exception {
        WebSort.sort(form, field);
        return "order/filter";
    }

    @RequestMapping(value = "/filter/clear/account", method = RequestMethod.POST)
    public String clearAccountFilter(WebRequest request, @ModelAttribute(SessionKey.ORDER_FILTER) OrderFilterForm form) throws Exception {
        form.setFilteredAccounts(Utility.emptyList(AccountListView.class));
        form.setAccountFilter(null);
        return redirect(request, UrlPathKey.ORDER.FILTER);
    }
    
    @RequestMapping(value = "/filter/clear/distributor", method = RequestMethod.POST)
    public String clearDistributorFilter(WebRequest request, @ModelAttribute(SessionKey.ORDER_FILTER) OrderFilterForm form) throws Exception {
        form.setFilteredDistributors(Utility.emptyList(DistributorListView.class));
        form.setDistributorFilter(null);
        return redirect(request, UrlPathKey.ORDER.FILTER);
    }
    
    @RequestMapping(value = "/filter/clear/user", method = RequestMethod.POST)
    public String clearUserFilter(WebRequest request, @ModelAttribute(SessionKey.ORDER_FILTER) OrderFilterForm form) throws Exception {
        form.setFilteredUsers(Utility.emptyList(UserListView.class));
        form.setUserFilter(null);
        return redirect(request, UrlPathKey.ORDER.FILTER);
    }
    
    @RequestMapping(value = "/filter/clear/site", method = RequestMethod.POST)
    public String clearSiteFilter(WebRequest request, @ModelAttribute(SessionKey.ORDER_FILTER) OrderFilterForm form) throws Exception {
        form.setFilteredSites(Utility.emptyList(SiteListView.class));
        form.setSiteFilter(null);
        return redirect(request, UrlPathKey.ORDER.FILTER);
    }

    @ModelAttribute(SessionKey.ORDER_FILTER)
    public OrderFilterForm initFilter(HttpSession session) {

        OrderFilterForm orderFilter = (OrderFilterForm) session.getAttribute(SessionKey.ORDER_FILTER);

        if (orderFilter == null || !orderFilter.isInitialized()) {
            orderFilter = new OrderFilterForm();
            orderFilter.initialize();
        }
        
        DbConstantResource resource = AppResourceHolder.getAppResource().getDbConstantsResource();
        List<Pair<String, String>> orderSources = resource.getOrderSources();
        if (Utility.isSet(orderSources)) {
            for (Pair<String, String> orderPair : orderSources) {
                orderPair.setObject2(AppI18nUtil.getMessageOrNull("refcodes.ORDER_SOURCE_CD." + orderPair.getObject1()));
            }
            Collections.sort(orderSources, new Comparator<Pair<String, String>>() {
                @Override
                public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                    return o1.getObject2().compareTo(o2.getObject2());
                }
            });
        }
        orderFilter.setOrderSources(orderSources);
        
        List<Pair<String, String>> orderStatuses = WebFormUtil.getOrderStatusesList(resource);

        SelectableObjects selectedObjects = new SelectableObjects(orderStatuses,
                                                                  orderStatuses,
                                                                  new Comparator<Pair<String, String>>() {
                                                                        public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                                                                            return o1.getObject1().compareTo(o2.getObject1());
                                                                        }
                                                                  });
        orderFilter.setSearchOrderStatuses(selectedObjects);

        return orderFilter;

    }

    @ModelAttribute(SessionKey.ORDER_FILTER_RESULT)
    public OrderFilterResultForm initFilterResult(HttpSession session) {

        OrderFilterResultForm orderFilterResult = (OrderFilterResultForm) session.getAttribute(SessionKey.ORDER_FILTER_RESULT);

        if (orderFilterResult == null) {
            orderFilterResult = new OrderFilterResultForm();
        }
        
        return orderFilterResult;

    }

}
