package com.espendwise.manta.web.controllers;

import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.GroupForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.model.view.GroupHeaderView;
import org.apache.log4j.Logger;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

@Scope("request")
@Controller("groupPreparer")
@AutoClean(value = {SessionKey.GROUP_HEADER}, controller = GroupController.class)
public class GroupPreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(GroupPreparer.class);

    @Autowired
    public GroupService groupService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        logger.debug("execute()=> BEGIN");

        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.GROUP_HEADER);

        logger.debug("execute()=> END.");

    }

    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.GROUP_HEADER)
    public Object initHeader() {

        Long groupId = UrlPathAssistent.getPathId(IdPathKey.GROUP_ID, webRequest);
        if (Utility.longNN(groupId ) > 0 ) {

            GroupForm detailForm = (GroupForm) webRequest.getAttribute(SessionKey.GROUP, WebRequest.SCOPE_REQUEST);

            return  Utility.isSet(detailForm)
                    ? new GroupHeaderView(detailForm.getGroupId(), detailForm.getGroupName(), detailForm.getGroupType())
                    : groupService.findGroupHeader(groupId);

        } else {

            return null;

        }
    }


}
