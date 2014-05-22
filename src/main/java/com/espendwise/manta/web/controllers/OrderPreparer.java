package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.view.OrderHeaderView;
import com.espendwise.manta.service.OrderService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.OrderForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import org.apache.log4j.Logger;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

@Scope("request")
@Controller("orderPreparer")
@AutoClean(value = {SessionKey.ORDER_HEADER}, controller = OrderController.class)
public class OrderPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(OrderPreparer.class);

    @Autowired
    public OrderService orderService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.info("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.ORDER_HEADER);

        logger.info("execute()=> END.");

    }


    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.ORDER_HEADER)
    public Object initHeader() {
        Long objId = UrlPathAssistent.getPathId(IdPathKey.ORDER_ID, webRequest);
        if (Utility.longNN(objId) > 0) {
            OrderForm detailForm = (OrderForm) webRequest.getAttribute(SessionKey.ORDER, WebRequest.SCOPE_REQUEST);
            return Utility.isSet(detailForm) ? new OrderHeaderView(detailForm.getOrderId()) : orderService.findOrderHeader(objId);
        } else {
            return null;
        }
    }


}
