package com.espendwise.manta.web.controllers;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.model.data.GenericReportData;
import com.espendwise.manta.model.data.GroupData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.UserGroupInfLineView;
import com.espendwise.manta.model.view.UserGroupInformationView;
import com.espendwise.manta.service.GroupService;
import com.espendwise.manta.service.StoreService;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.GroupSearchCriteria;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.FilterResultWebForm;
import com.espendwise.manta.web.forms.UserGroupFilterForm;
import com.espendwise.manta.web.forms.UserGroupFilterResultForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
@RequestMapping(UrlPathKey.USER.GROUP)
@SessionAttributes({SessionKey.USER_GROUP_FILTER, SessionKey.USER_GROUP_FILTER_RESULT})
@AutoClean({SessionKey.USER_GROUP_FILTER, SessionKey.USER_GROUP_FILTER_RESULT})
public class UserGroupController extends BaseController {

    private static final Logger logger = Logger.getLogger(UserGroupController.class);

    private UserService userService;
    private StoreService storeService;
    private GroupService groupService;

    @Autowired
    public UserGroupController(UserService userService, StoreService storeService, GroupService groupService) {
        this.userService = userService;
        this.storeService = storeService;
        this.groupService = groupService;
    }

    @InitBinder
    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String view(@ModelAttribute(SessionKey.USER_GROUP_FILTER) UserGroupFilterForm filterForm) {
        return "user/group";
    }

    private String handleValidationException(WebRequest request, ValidationException e) {
        logger.info("handleValidationException()=> exception: " + e);
        return "user/group";
    }


    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String findGroups(WebRequest request,
                             @ModelAttribute(SessionKey.USER_GROUP_FILTER) UserGroupFilterForm filterForm,
                             @ModelAttribute(SessionKey.USER_GROUP_FILTER_RESULT) UserGroupFilterResultForm resultForm) throws Exception {


        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(filterForm);
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "user/group";
        }

        resultForm.reset();

        AppUser appUser = getAppUser();
        UserData user = userService.findByUserId(filterForm.getUserId());        
        
        GroupSearchCriteria criteria = new GroupSearchCriteria();
        criteria.setGroupStatus(RefCodeNames.GROUP_STATUS_CD.ACTIVE);
        criteria.setGroupType(RefCodeNames.GROUP_TYPE_CD.USER);
        if (!appUser.isStoreAdmin()){
        	criteria.setUserId(user.getUserId());
        } else {
        	List<Long> storeIds = Utility.toIds(storeService.findUserStores(user.getUserId()));
        	criteria.setStoreIds(storeIds);
        }
        
        List<GroupData> optionalGroupList = groupService.findGroupsByCriteria(criteria);
        
        criteria = new GroupSearchCriteria();
        criteria.setGroupStatus(RefCodeNames.GROUP_STATUS_CD.ACTIVE);
        criteria.setGroupNameIn(Utility.toList(user.getUserTypeCd(), Constants.DEFAULT_GROUP));
        List<GroupData> defGroups = groupService.findGroupsByCriteria(criteria);
        optionalGroupList.addAll(defGroups);
        
        Map<Long, GroupData> optionGroups = new HashMap<Long, GroupData>();
        for (GroupData groupD : optionalGroupList){
        	optionGroups.put(groupD.getGroupId(), groupD);
        }

        Map<Long, GroupData> selectedGroups = userService.findGroupsUserIsMemberOf(user.getUserId(), user.getUserTypeCd());
        
        List<Pair<Long, String>> options = filterUserGroups(optionGroups, filterForm);
        List<Pair<Long, String>> selected = filterUserGroups(selectedGroups, filterForm);

        SelectableObjects<Pair<Long, String>> selectableObj = new SelectableObjects<Pair<Long, String>>(
                options,
                selected,
                AppComparator.PAIR_OBJ1_LONG_COMPARATOR,
                filterForm.isOnlyAssociatedGroups() ? 1 : 0
        );

        List<UserGroupInformationView> groupsInfo = userService.findUserGroupInformation(user.getUserId(), user.getUserTypeCd());


        FilterResultWebForm<UserGroupInfLineView> groupInfForm = new FilterResultWebForm<UserGroupInfLineView>(new ArrayList<UserGroupInfLineView>());

        List<Long> groupConfigIdOnly = Utility.obj1Only(selectableObj.getSelected());

        if (Utility.isSet(groupsInfo)) {
            Iterator<UserGroupInformationView> groupsInfoIt = groupsInfo.iterator();
            while (groupsInfoIt.hasNext()) {
                UserGroupInformationView inf = groupsInfoIt.next();
                if (!groupConfigIdOnly.contains(inf.getGroup().getGroupId())) {
                    groupsInfoIt.remove();
                } else{

                    List<String> functions = inf.getApplicationFunctionNames();

                    if (Utility.isSet(functions)) {
                        for (String f : functions) {
                            groupInfForm.getResult().add(
                                    new UserGroupInfLineView(
                                            inf.getGroup().getGroupId(),
                                            inf.getGroup().getShortDesc(),
                                            f,
                                            null
                                    )
                            );
                        }
                    }

                    if (Utility.isSet(inf.getReports())) {
                        for (GenericReportData report : inf.getReports()) {
                            groupInfForm.getResult().add(
                                    new UserGroupInfLineView(
                                            inf.getGroup().getGroupId(),
                                            inf.getGroup().getShortDesc(),
                                            null,
                                            report.getName()
                                    )
                            );
                        }
                    }

                }
            }
        }

        WebSort.sort(groupInfForm, UserGroupInfLineView.REPORT_NAME);
        WebSort.sort(groupInfForm, UserGroupInfLineView.GROUP_PERMISSION);

        resultForm.setUserId(user.getUserId());
        resultForm.setUserType(user.getUserTypeCd());
        resultForm.setGroups(selectableObj);
        resultForm.setGroupsInformation(groupInfForm);

        WebSort.sort(resultForm, Pair.OBJECT2);

        return "user/group";

    }

    private List<Pair<Long, String>> filterUserGroups(Map<Long, GroupData> userGroups, UserGroupFilterForm filterForm) {

        List<Pair<Long, String>> resultUserGroups = new ArrayList<Pair<Long, String>>();

        String filterType = Utility.isSet(filterForm.getGroupId()) ? Constants.FILTER_TYPE.ID : filterForm.getGroupNameFilterType();
        String filterValue = Utility.isSet(filterForm.getGroupId()) ? filterForm.getGroupId() : filterForm.getGroupName();

        if (userGroups != null) {

            for (Long groupId : userGroups.keySet()) {

                String groupName = userGroups.get(groupId).getShortDesc();
                if (Utility.isSet(filterValue)) {
                    if (Constants.FILTER_TYPE.START_WITH.equals(filterType) && groupName.toUpperCase().startsWith(filterValue.toUpperCase())) {
                        resultUserGroups.add(new Pair<Long, String>(groupId, groupName));
                    } else if (Constants.FILTER_TYPE.CONTAINS.equals(filterType) && groupName.toUpperCase().contains(filterValue.toUpperCase())) {
                        resultUserGroups.add(new Pair<Long, String>(groupId, groupName));
                    } else if (Constants.FILTER_TYPE.ID.equals(filterType) && groupId.longValue() == Parse.parseLong(filterValue)) {
                        resultUserGroups.add(new Pair<Long, String>(groupId, groupName));
                    }

                } else {
                    resultUserGroups.add(new Pair<Long, String>(groupId, groupName));
                }
            }

            Collections.sort(resultUserGroups, AppComparator.PAIR_OBJ2_STR_COMPARATOR);

        }

        return resultUserGroups;
    }

    @SuccessMessage
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(WebRequest request,
                         @PathVariable Long userId,
                         @ModelAttribute(SessionKey.USER_GROUP_FILTER_RESULT) UserGroupFilterResultForm resultForm) throws Exception {

        logger.info("update()=> BEGIN");

        Long storeId = getStoreId();

        logger.info("update()=> userId: " + userId + ", storeId: " + storeId);

        List<Pair<Long, String>> selected = resultForm.getGroups().getNewlySelected();
        List<Pair<Long, String>> deselected = resultForm.getGroups().getDeselected();

        AppUser appUser = getAppUser();
        UserData user = userService.findByUserId(resultForm.getUserId());

        UpdateRequest<Long> updateRequest = new UpdateRequest<Long>();

        updateRequest.setToDelete(Utility.emptyList(Long.class));
        updateRequest.setToCreate(Utility.emptyList(Long.class));

        Set<Long> userGroups = new HashSet<Long>(
                userService
                        .findGroupsUserIsMemberOf(resultForm.getUserId(), resultForm.getUserType())
                        .keySet()
        );

        for (Pair<Long, String> pair : selected) {
            if (!userGroups.contains(pair.getObject1())) {
                updateRequest.getToCreate().add(pair.getObject1());
                userGroups.add(pair.getObject1());
            }
        }

        for (Pair<Long, String> pair : deselected) {
            if (userGroups.contains(pair.getObject1())) {
                updateRequest.getToDelete().add(pair.getObject1());
                userGroups.remove(pair.getObject1());
            }
        }

        try {

            userService.configureUserGroups(
                    user.getUserId(),
                    storeId,
                    updateRequest
            );

        } catch (ValidationException e) {

            return handleValidationException(request, e);

        }

        logger.info("update()=> END.");

        return "user/group";


    }


    @RequestMapping(value = "/groupInformation/sortby/{field}", method = RequestMethod.GET)
    public String sortInformation(WebRequest request,
                                  @PathVariable String field,
                                  @ModelAttribute(SessionKey.USER_GROUP_FILTER_RESULT) UserGroupFilterResultForm resultForm) throws Exception {
        WebSort.sort(resultForm.getGroupsInformation(), field);
        return "user/group";
    }


    @ModelAttribute(SessionKey.USER_GROUP_FILTER_RESULT)
    public UserGroupFilterResultForm init(HttpSession session) {

        UserGroupFilterResultForm form = (UserGroupFilterResultForm) session.getAttribute(SessionKey.USER_GROUP_FILTER_RESULT);

        if (form == null) {
            form = new UserGroupFilterResultForm();
        }

        if (Utility.isSet(form.getResult())) {
            form.reset();
        }


        return form;

    }

    @ModelAttribute(SessionKey.USER_GROUP_FILTER)
    public UserGroupFilterForm initFilter(HttpSession session, @PathVariable Long userId) {

        logger.info("initFilter()=> init....");

        UserGroupFilterForm form = (UserGroupFilterForm) session.getAttribute(SessionKey.USER_GROUP_FILTER);

        if (form == null || !form.isInitialized()) {
            form = new UserGroupFilterForm(userId);
            form.initialize();
        }

        logger.info("initFilter()=> form: " + form);
        logger.info("initFilter()=> init.OK!");

        return form;

    }

}
