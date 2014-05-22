package com.espendwise.manta.web.controllers;


import org.apache.log4j.Logger;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.view.CostCenterHeaderView;
import com.espendwise.manta.service.CostCenterService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.CostCenterForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;

@Scope("request")
@Controller("costCenterPreparer")
@AutoClean(value = {SessionKey.COST_CENTER_HEADER}, controller = CostCenterController.class)
public class CostCenterPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(CostCenterPreparer.class);

    @Autowired
    public CostCenterService costCenterService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.debug("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.COST_CENTER_HEADER);

        logger.debug("execute()=> END.");

    }

    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.COST_CENTER_HEADER)
    public Object initHeader() {

        Long costCenterId = UrlPathAssistent.getPathId(IdPathKey.COST_CENTER_ID, webRequest);
        if (Utility.longNN(costCenterId ) > 0 ) {

            CostCenterForm detailForm = (CostCenterForm) webRequest.getAttribute(SessionKey.COST_CENTER, WebRequest.SCOPE_REQUEST);

            return  Utility.isSet(detailForm)
                    ? new CostCenterHeaderView(detailForm.getCostCenterId(), detailForm.getCostCenterName())
                    : costCenterService.findCostCenterHeader(costCenterId);

        } else {

            return null;

        }
    }


}
