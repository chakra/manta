package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.view.UserHeaderView;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.UserForm;
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
@Controller("userPreparer")
@AutoClean(value = {SessionKey.USER_HEADER}, controller = UserController.class)
public class UserPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(UserPreparer.class);

    @Autowired
    public UserService userService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.info("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.USER_HEADER);

        logger.info("execute()=> END.");

    }


    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.USER_HEADER)
    public Object initHeader() {
        Long objId = UrlPathAssistent.getPathId(IdPathKey.USER_ID, webRequest);
        if (Utility.longNN(objId) > 0) {
            UserForm detailForm = (UserForm) webRequest.getAttribute(SessionKey.USER, WebRequest.SCOPE_REQUEST);
            return Utility.isSet(detailForm) ? new UserHeaderView(detailForm.getUserId(), detailForm.getUserLogonName())
                    : userService.findUserHeader(objId);
        } else {
            return null;
        }
    }


}
