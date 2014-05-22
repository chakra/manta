package com.espendwise.manta.web.controllers;


import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.service.ManufacturerService;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.ManufacturerForm;
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
@Controller("manufacturerPreparer")
@AutoClean(value = {SessionKey.MANUFACTURER_HEADER}, controller = ManufacturerController.class)
public class ManufacturerPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(ManufacturerPreparer.class);

    @Autowired
    public ManufacturerService manufacturerService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.debug("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.MANUFACTURER_HEADER);

        logger.debug("execute()=> END.");

    }

    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.MANUFACTURER_HEADER)
    public Object initHeader() {

        Long objId = UrlPathAssistent.getPathId(IdPathKey.MANUFACTURER_ID, webRequest);

        if (Utility.longNN(objId) > 0) {

            ManufacturerForm detailForm = (ManufacturerForm) webRequest.getAttribute(SessionKey.MANUFACTURER, WebRequest.SCOPE_REQUEST);

            return Utility.isSet(detailForm)
                    ? new EntityHeaderView(detailForm.getManufacturerId(), detailForm.getManufacturerName())
                    : manufacturerService.findManufacturerHeader(objId);

        } else {

            return null;

        }
    }


}
