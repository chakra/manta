package com.espendwise.manta.web.controllers;


import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.SiteForm;
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
@Controller("sitePreparer")
@AutoClean(value = {SessionKey.SITE_HEADER}, controller = SiteController.class)
public class SitePreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(SitePreparer.class);

    @Autowired
    public SiteService siteService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.debug("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.SITE_HEADER);

        logger.debug("execute()=> END.");

    }

    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.SITE_HEADER)
    public Object initHeader() {

        Long siteId = UrlPathAssistent.getPathId(IdPathKey.LOCATION_ID, webRequest);
        Long storeId = authUser.getAppUser().getContext(AppCtx.STORE).getStoreId();


        if (Utility.longNN(siteId ) > 0 && Utility.longNN(storeId) > 0 ) {

            SiteForm detailForm = (SiteForm) webRequest.getAttribute(SessionKey.SITE, WebRequest.SCOPE_REQUEST);

            return Utility.isSet(detailForm)
                    ? detailForm.getSiteHeader()
                    : siteService.findSiteHeader(storeId, siteId);

        } else {

            return null;

        }
    }


}
