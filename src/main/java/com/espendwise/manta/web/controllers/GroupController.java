package com.espendwise.manta.web.controllers;

import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.view.GroupView;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.GroupForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.resolver.GroupWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;


import java.util.List;


@Controller
@RequestMapping(UrlPathKey.GROUP.IDENTIFICATION)
public class GroupController extends BaseController {

    private static final Logger logger = Logger.getLogger(GroupController.class);
    
    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new GroupWebUpdateExceptionResolver());

        return "group/edit";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "group/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.GROUP_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.GROUP) GroupForm groupForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, groupForm: "+groupForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(groupForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.GROUP, groupForm);
            return "group/edit";
        }

        GroupView groupV= null;

        if (!groupForm.isNew()) {
            groupV = groupService.getGroupView(groupForm.getGroupId());
        }
        if (groupV == null) {
        	groupV = new GroupView(new GroupData());
        }
        
        groupV.getGroup().setShortDesc(groupForm.getGroupName());
        groupV.getGroup().setGroupTypeCd(groupForm.getGroupType());
        groupV.getGroup().setGroupStatusCd(groupForm.getGroupStatus());
        groupV.setAssocStoreId(groupForm.getAssocStoreId());
        try {
        	groupV = groupService.saveGroup(groupV);
        } catch (ValidationException e) {
        	e.printStackTrace();
            return handleValidationException(e, request);
        } catch (DatabaseUpdateException e) {
        	e.printStackTrace();
            return handleDatabaseUpdateException(e, request);

        }

        logger.info("redirect(()=> redirect to: " + groupV.getGroup().getGroupId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(groupV.getGroup().getGroupId()));
    }
          
    @Clean(SessionKey.GROUP_HEADER)
    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.GROUP) GroupForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new GroupForm();
        form.initialize();
        form.setAssocStoreName(getStoreName());
    	form.setStoreIdCanAssoc(getStoreId());

        model.addAttribute(SessionKey.GROUP, form);

        logger.info("create()=> END.");

        return "group/edit";

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.GROUP) GroupForm form, @PathVariable(IdPathKey.GROUP_ID) Long groupId, Model model) {

        logger.info("show()=> BEGIN");

        GroupView groupView = groupService.getGroupView(groupId);

        logger.info("show()=> groupData: "+groupView);

        if (groupView  != null) {
            form.setGroupId(groupView.getGroup().getGroupId());
            form.setGroupName(groupView.getGroup().getShortDesc());
            form.setGroupStatus(groupView.getGroup().getGroupStatusCd());
            form.setGroupType(groupView.getGroup().getGroupTypeCd());
            form.setAssocStoreId(groupView.getAssocStoreId());
            form.setAssocStoreName(groupView.getAssocStoreName());
        	form.setStoreIdCanAssoc(groupView.getAssocStoreId());
        }
        
        if (form.getStoreIdCanAssoc()==null || form.getStoreIdCanAssoc().intValue()==0){
        	form.setAssocStoreName(getStoreName());
            form.setStoreIdCanAssoc(getStoreId());
        }
        model.addAttribute(SessionKey.GROUP, form);

        logger.info("show()=> END.");

        return "group/edit";

    }


    @ModelAttribute(SessionKey.GROUP)
    public GroupForm initModel() {

    	GroupForm form = new GroupForm();
        form.initialize();

        return form;

    }

}
