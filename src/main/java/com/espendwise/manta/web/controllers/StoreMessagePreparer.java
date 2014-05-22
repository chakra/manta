package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.StoreMessageService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.StoreMessageForm;
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
@Controller("storeMessagePreparer")
@AutoClean(value = {SessionKey.STORE_MESSAGE_HEADER}, controller = StoreMessageController.class)
public class StoreMessagePreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(StoreMessagePreparer.class);

    @Autowired
    public StoreMessageService storeMessageService;


    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.info("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.STORE_MESSAGE_HEADER);

        logger.info("execute()=> END.");

    }

    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.STORE_MESSAGE_HEADER)
    public Object initHeader() {
        Long objId = UrlPathAssistent.getPathId(IdPathKey.STORE_MESSAGE_ID, webRequest);
        if (Utility.longNN(objId) > 0) {
            StoreMessageForm detailForm = (StoreMessageForm) webRequest.getAttribute(SessionKey.STORE_MESSAGE, WebRequest.SCOPE_REQUEST);
            return Utility.isSet(detailForm) ? new EntityHeaderView(detailForm.getStoreMessageId(), detailForm.getName())
                    : storeMessageService.findStoreMessageHeader(objId);
        } else {
            return null;
        }
    }

}
