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
import com.espendwise.manta.web.forms.UploadFileFilterForm;
import com.espendwise.manta.web.forms.ItemLoaderForm;
import com.espendwise.manta.service.UploadFileService;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.model.view.EntityHeaderView;

@Scope("request")
@Controller("uploadFilePreparer")
@AutoClean(value = {SessionKey.UPLOAD_FILE_HEADER}, controller = UploadFileController.class)
public class UploadFilePreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(UploadFilePreparer.class);

    @Autowired
    public UploadFileService uploadFileService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.info("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.UPLOAD_FILE_HEADER);

        logger.info("execute()=> END.");

    }


    public void handleHeader(String modelAttribute) {

        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);

        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.UPLOAD_FILE_HEADER)
    public Object initHeader() {

        Long objId = UrlPathAssistent.getPathId(IdPathKey.UPLOAD_ID, webRequest);

        ItemLoaderForm detailForm = (ItemLoaderForm) webRequest.getAttribute(SessionKey.UPLOAD_FILE, WebRequest.SCOPE_REQUEST);

        if (Utility.longNN(objId) > 0) {

            return (Utility.isSet(detailForm) && Utility.isSet(detailForm.getUploadData()))
                    ? new EntityHeaderView(detailForm.getUploadData().getUploadId(), detailForm.getUploadData().getFileName())
                    : uploadFileService.findUploadFileHeader(objId);

        } else {

             return (Utility.isSet(detailForm) && Utility.isSet(detailForm.getUploadData()))
                    ? new EntityHeaderView(new Long(0), detailForm.getUploadData().getFileName())
                    : null;
        }

    }


}
