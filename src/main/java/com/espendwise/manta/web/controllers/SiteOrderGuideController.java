package com.espendwise.manta.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.apache.log4j.Logger;
import com.espendwise.manta.service.OrderGuideService;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.forms.SiteOrderGuideForm;
import com.espendwise.manta.web.forms.OrderGuideForm;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.OrderGuideListViewCriteria;
import com.espendwise.manta.spi.AutoClean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(UrlPathKey.SITE.ORDER_GUIDE)
@SessionAttributes(SessionKey.SITE_ORDER_GUIDE)
@AutoClean(SessionKey.SITE_ORDER_GUIDE)
public class SiteOrderGuideController extends BaseController {

    private static final Logger logger = Logger.getLogger(SiteOrderGuideController.class);

    private OrderGuideService orderGuideService;

    @Autowired
    public SiteOrderGuideController(OrderGuideService orderGuideService) {
        this.orderGuideService = orderGuideService;
    }


    public String handleValidationException(ValidationException ex, WebRequest request) {
        WebErrors webErrors = new WebErrors(request);
        return "site/orderGuides";
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String initShow(HttpServletRequest request,
                           @ModelAttribute(SessionKey.SITE_ORDER_GUIDE) SiteOrderGuideForm form,
                           @PathVariable(IdPathKey.LOCATION_ID) Long siteId) {
        logger.info("initShow()=> BEGIN " );

        OrderGuideListViewCriteria criteria = new OrderGuideListViewCriteria(siteId);
        criteria.setOrderGuideTypeCds(Utility.toList(RefCodeNames.ORDER_GUIDE_TYPE_CD.SITE_ORDER_GUIDE_TEMPLATE,
                                                     RefCodeNames.ORDER_GUIDE_TYPE_CD.BUYER_ORDER_GUIDE));
        List<OrderGuideListView> orderGuides = orderGuideService.findOrderGuidesByCriteria(criteria);

        form.setOrderGuides(orderGuides);

        logger.info("initShow()=> END " );
        return "site/orderGuides";
    }


    @ModelAttribute(SessionKey.SITE_ORDER_GUIDE)
    public SiteOrderGuideForm initModel(@PathVariable(IdPathKey.LOCATION_ID) Long siteId) {
        logger.info("initModel()=> BEGIN " );

        SiteOrderGuideForm form = new SiteOrderGuideForm(siteId);
        logger.info("initModel()=> END " );
        return form;

    }
    @RequestMapping(value = "/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.SITE_ORDER_GUIDE) SiteOrderGuideForm form,
                       @PathVariable String field) throws Exception {
        logger.info("sort()=> BEGIN " );
        WebSort.sort(form, field);
        logger.info("sort()=> END " );

        return "site/orderGuides";
    }



}
