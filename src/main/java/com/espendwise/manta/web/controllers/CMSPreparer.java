package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.CMSService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.CMSForm;
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
@Controller("cmsPreparer")
@AutoClean(value = {SessionKey.CMS_HEADER}, controller = CMSController.class)
public class CMSPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(CMSPreparer.class);

    @Autowired
    public CMSService cmsService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.info("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.CMS_HEADER);

        logger.info("execute()=> END.");

    }


    public void handleHeader(String modelAttribute) {

        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);

        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.CMS_HEADER)
    public Object initHeader() {

        Long objId = UrlPathAssistent.getPathId(IdPathKey.GLOBAL_STORE_ID, webRequest);

        if (Utility.longNN(objId) > 0) {

            CMSForm detailForm = (CMSForm) webRequest.getAttribute(SessionKey.CMS, WebRequest.SCOPE_REQUEST);

            return Utility.isSet(detailForm)
                    ? new EntityHeaderView(detailForm.getPrimaryEntityId(), detailForm.getPrimaryEntityName())
                    : cmsService.findPrimaryEntityHeader(objId);

        } else {

            return null;

        }
    }


}
