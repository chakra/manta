package com.espendwise.manta.web.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.apache.log4j.Logger;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.AttributeContext;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.forms.MasterItemForm;
import com.espendwise.manta.service.ItemService;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.model.view.EntityHeaderView;

@Scope("request")
@Controller("masterItemPreparer")
@AutoClean(value = {SessionKey.MASTER_ITEM_HEADER}, controller = MasterItemController.class)
public class MasterItemPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(MasterItemPreparer.class);

    @Autowired
    public ItemService itemService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.info("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.MASTER_ITEM_HEADER);

        logger.info("execute()=> END.");

    }


    public void handleHeader(String modelAttribute) {

        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);

        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.ACCOUNT_HEADER)
    public Object initHeader() {

        Long objId = UrlPathAssistent.getPathId(IdPathKey.ITEM_ID, webRequest);

        if (Utility.longNN(objId) > 0) {

            MasterItemForm detailForm = (MasterItemForm) webRequest.getAttribute(SessionKey.MASTER_ITEM, WebRequest.SCOPE_REQUEST);
           

            return Utility.isSet(detailForm)
                    ? new EntityHeaderView(detailForm.getItemId(), detailForm.getItemName())
                    : itemService.findMasterItemHeader(objId);

        } else {

            return null;

        }
    }


}
