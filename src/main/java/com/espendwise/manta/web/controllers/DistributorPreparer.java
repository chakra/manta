package com.espendwise.manta.web.controllers;


import org.apache.log4j.Logger;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.DistributorService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.DistributorForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;

@Scope("request")
@Controller("distributorPreparer")
@AutoClean(value = {SessionKey.DISTRIBUTOR_HEADER}, controller = DistributorController.class)
public class DistributorPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(DistributorPreparer.class);

    @Autowired
    public DistributorService distributorService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.debug("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.DISTRIBUTOR_HEADER);

        logger.debug("execute()=> END.");

    }

    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.DISTRIBUTOR_HEADER)
    public Object initHeader() {

        Long objId = UrlPathAssistent.getPathId(IdPathKey.DISTRIBUTOR_ID, webRequest);

        if (Utility.longNN(objId) > 0) {

            DistributorForm detailForm = (DistributorForm) webRequest.getAttribute(SessionKey.DISTRIBUTOR, WebRequest.SCOPE_REQUEST);

            return Utility.isSet(detailForm)
                    ? new EntityHeaderView(detailForm.getDistributorId(), detailForm.getDistributorName())
                    : distributorService.findDistributorHeader(objId);

        } else {

            return null;

        }
    }


}
