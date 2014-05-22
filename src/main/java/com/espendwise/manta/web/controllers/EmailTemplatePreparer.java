package com.espendwise.manta.web.controllers;


import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.TemplateService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.EmailTemplateForm;
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
@Controller("emailTemplatePreparer")
@AutoClean(value = {SessionKey.EMAIL_TEMPLATE_HEADER}, controller = EmailTemplateController.class)
public class EmailTemplatePreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(EmailTemplatePreparer.class);

    @Autowired
    public TemplateService templateService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.info("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.EMAIL_TEMPLATE_HEADER);

        logger.info("execute()=> END.");

    }


    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.EMAIL_TEMPLATE_HEADER)
    public Object initHeader() {
        Long objId = UrlPathAssistent.getPathId(IdPathKey.EMAIL_TEMPLATE_ID, webRequest);
        if (Utility.longNN(objId) > 0) {
            EmailTemplateForm detailForm = (EmailTemplateForm) webRequest.getAttribute(SessionKey.EMAIL_TEMPLATE, WebRequest.SCOPE_REQUEST);
            return Utility.isSet(detailForm) ? new EntityHeaderView(detailForm.getTemplateId(), detailForm.getTemplateName())
                    : templateService.findTemplateHeader(objId);
        } else {
            return null;
        }
    }


}
