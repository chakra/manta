package com.espendwise.manta.web.controllers;


import com.espendwise.manta.auth.AuthUser;
import com.espendwise.manta.auth.AuthUserContext;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import org.apache.log4j.Logger;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.WebRequest;

@Controller("adminPortalPreparer")
@Scope("request")
public class AdminPortalPreparer extends ViewPreparerSupport {

    private static final Logger logger = Logger.getLogger(AdminPortalPreparer.class);

    public AdminPortalPreparer() {
    }

    @Autowired
    public WebRequest webRequest;

    @Autowired
    public AuthUser authUser;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        tilesContext.getRequestScope().put(Constants.APP_USER_JSP_SCOPE, authUser.getAppUser());
        tilesContext.getRequestScope().put(Constants.STORE_CTX_JSP_SCOPE, authUser.getAppUser().getContext(AppCtx.STORE));
        tilesContext.getRequestScope().put(Constants.DISPLAY_SETTINGS_JSP_SCOPE, authUser.getAuthUserContext().get(AuthUserContext.DISPlAY_SETTINGS));
        tilesContext.getRequestScope().put(Constants.APPLICATION_RESOURCE_JSP_SCOPE, AppResourceHolder.getAppResource());

    }

}