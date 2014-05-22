package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.AccountForm;
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
@Controller("accountPreparer")
@AutoClean(value = {SessionKey.ACCOUNT_HEADER}, controller = AccountController.class)
public class AccountPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(AccountPreparer.class);

    @Autowired
    public AccountService accountService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.info("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.ACCOUNT_HEADER);

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

        Long objId = UrlPathAssistent.getPathId(IdPathKey.ACCOUNT_ID, webRequest);

        if (Utility.longNN(objId) > 0) {

            AccountForm detailForm = (AccountForm) webRequest.getAttribute(SessionKey.ACCOUNT, WebRequest.SCOPE_REQUEST);

            return Utility.isSet(detailForm)
                    ? new EntityHeaderView(detailForm.getAccountId(), detailForm.getAccountName())
                    : accountService.findAccountHeader(objId);

        } else {

            return null;

        }
    }


}
