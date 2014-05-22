package com.espendwise.manta.web.controllers;


import org.apache.log4j.Logger;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.ScheduleView;
import com.espendwise.manta.service.ScheduleService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.forms.ScheduleForm;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;

@Scope("request")
@Controller("corporateSchedulePreparer")
@AutoClean(value = {SessionKey.CORPORATE_SCHEDULE_HEADER}, controller = CorporateScheduleController.class)
public class CorporateSchedulePreparer extends AdminPortalPreparer {

    private static final Logger logger = Logger.getLogger(CorporateSchedulePreparer.class);

    @Autowired
    public ScheduleService scheduleService;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {
        logger.info("execute()=> BEGIN");
        super.execute(tilesContext, attributeContext);
        handleHeader(SessionKey.CORPORATE_SCHEDULE_HEADER);
        logger.info("execute()=> END.");
    }

    public void handleHeader(String modelAttribute) {
        Object header = webRequest.getAttribute(modelAttribute, WebRequest.SCOPE_SESSION);
        if (header == null) {
            header = initHeader();
            webRequest.setAttribute(modelAttribute, header, WebRequest.SCOPE_SESSION);
        }
    }

    @ModelAttribute(SessionKey.CORPORATE_SCHEDULE_HEADER)
    public Object initHeader() {
        Long objId = UrlPathAssistent.getPathId(IdPathKey.CORPORATE_SCHEDULE_ID, webRequest);
        if (Utility.longNN(objId) > 0) {
            ScheduleForm detailForm = (ScheduleForm) webRequest.getAttribute(SessionKey.CORPORATE_SCHEDULE, WebRequest.SCOPE_REQUEST);
            if (Utility.isSet(detailForm)) {
            	return new EntityHeaderView(detailForm.getScheduleId(), detailForm.getScheduleName());
            }
            else {
            	ScheduleView scheduleView = scheduleService.findSchedule(objId);
            	//if the schedule cannot be found or doesn't belong to the current store, return null
            	if (scheduleView == null || scheduleView.getSchedule() == null ||
            			!authUser.getAppUser().getContext(AppCtx.STORE).getStoreId().equals(scheduleView.getSchedule().getBusEntityId())) {
            		return null;
            	}
            	else {
                    return scheduleService.findScheduleHeader(objId);
            	}
            }
        } 
        else {
            return null;
        }
    }

}
