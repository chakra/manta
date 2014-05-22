package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.data.EmailData;
import com.espendwise.manta.model.data.GroupAssocData;
import com.espendwise.manta.model.data.PhoneData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.data.UserAssocData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.AllStoreIdentificationView;
import com.espendwise.manta.model.view.AllUserIdentView;
import com.espendwise.manta.model.view.UserIdentView;
import com.espendwise.manta.service.AllUserService;
import com.espendwise.manta.service.DatabaseAccess;
import com.espendwise.manta.service.MainDbService;
import com.espendwise.manta.service.StoreService;
import com.espendwise.manta.service.UserLogonService;
import com.espendwise.manta.service.UserService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.rules.UserUpdateConstraint;
import com.espendwise.manta.web.forms.UserForm;
import com.espendwise.manta.web.resolver.UserWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;
import com.espendwise.manta.web.util.WebWarning;


@Controller
@RequestMapping(UrlPathKey.USER.IDENTIFICATION)
public class UserController extends BaseController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private UserService userService;
    private UserLogonService userLogonService;
    private MainDbService mainDbService;
    private AllUserService allUserService;
    private StoreService storeService;

    @Autowired
    public UserController(UserService userService, 
                          UserLogonService userLogonService,
                          MainDbService mainDbService,
                          AllUserService allUserService,
                          StoreService storeService) {

        this.userService = userService;
        this.userLogonService = userLogonService;
        this.mainDbService = mainDbService;
        this.allUserService = allUserService;
        this.storeService = storeService;

    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new UserWebUpdateExceptionResolver());
        
        return "user/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.USER_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.USER) UserForm form, Model model) throws Exception {

        logger.info("save()=> BEGIN, userForm: " + form);

        WebErrors webErrors = new WebErrors(request);

        Long userId = form.getUserId();
        
        if (form.isIsClone()) {
            form.setUserId(Long.valueOf(0));
        }

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);


        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);

            //userStoreAssocsShow(form);
            form.setMainDbAlive(mainDbIsAlive());
            form.setMultiStoreDb(multiStoreDb());
            form.setAvailableLanguages(WebFormUtil.getStoreAvailableLanguages(storeService, getStoreId()));
            
            model.addAttribute(SessionKey.USER, form);
            return "user/edit";
        } else if (!multiStoreDb()) {
        	try {
                ServiceLayerValidation validation = new ServiceLayerValidation();
                validation.addRule(new UserUpdateConstraint(form.getUserId(), form.getUserLogonName()));
                validation.validate();
            } catch (ValidationException e) {
            	logger.info("save() =======> handleValidationException ");
                form.setMainDbAlive(mainDbIsAlive());
                form.setMultiStoreDb(multiStoreDb());
                form.setAvailableLanguages(WebFormUtil.getStoreAvailableLanguages(storeService, getStoreId()));
                return handleValidationException(e, request);
            } catch (Exception e) {
            	logger.info("save() =======> Exception :" + e.getMessage());
            }    
        }

        UserIdentView userView = null;
        if (userId != null && userId > 0) {
            userView = userService.findUserToEdit(getStoreId(), userId);
        }
        else if (form.getSourceUserId() != null && form.getSourceUserId() > 0) {
            userView = userService.findUserToEdit(getStoreId(), form.getSourceUserId());
        }
        else {
            userView = new UserIdentView();
        }

        userView = WebFormUtil.fillOutUserIdenView(userView,
                form,
                getAppLocale(),
                multiStoreDb()
        );

        try {

            if (!multiStoreDb()) {
            	//NOTE - do NOT change the calls to the userService below unless you must make
            	//corresponding changes to the UserHistorian class.  Otherwise history will
            	//no longer be tracked for user creation/modification.
                if (userView.getUserData().getUserId() == null) {
                    userView = userService.createUserIdent(userView);
                } else {
                    userView = userService.modifyUserIdent(userView);
                }

            } else {

                // save user to the Main DS
                AllUserIdentView allUserView = allUserService.findViewByName(userView.getUserData().getUserName());

                allUserView = WebFormUtil.fillOutAllUserView(allUserView, form, getAppLocale());

                allUserService.saveUserIdentToMain(DatabaseAccess.getMainUnit(), allUserView);

                // save user to other DS, allow create user only in the associated stores
                Set<String> assocDs = new HashSet<String>();
                if (Utility.isSet(userView.getUserStoreAssocs())) {
                    for (AllStoreIdentificationView assoc : userView.getUserStoreAssocs()) {
                        assocDs.add(assoc.getDsName());
                    }
                }
                Set<String> userPresentDs = userService.getAllUserPresentDS(userView.getUserData().getUserId());
                if ((Utility.isSet(assocDs) && Utility.isNew(userView.getUserData())) || Utility.isSet(userPresentDs)) { // need to be saved or updated
                    if (Utility.isSet(assocDs)) {
                        for (String ds : assocDs) {
                            userView = userService.saveUserIdentToDs(ds, userView, true);
                            userPresentDs.remove(ds);
                        }
                    }
                    if (Utility.isSet(userPresentDs)) {
                        for (String ds : userPresentDs) {
                            userView = userService.saveUserIdentToDs(ds, userView, false);
                        }
                    }
                }

            }

        } catch (ValidationException e) {

            form.setMainDbAlive(mainDbIsAlive());
            form.setMultiStoreDb(multiStoreDb());

            return handleValidationException(e, request);

        }

        form.setMainDbAlive(mainDbIsAlive());
        form.setMultiStoreDb(multiStoreDb());

        if (form.isIsClone()) {
            form.setIsClone(false);
            form.setSourceUserId(null);
        }

        logger.info("redirect(()=> redirect to: " + userView.getUserData().getUserId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(userView.getUserData().getUserId()));
    }

    @Clean(SessionKey.USER_HEADER)
    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    public String clone(@ModelAttribute(SessionKey.USER) UserForm form, Model model) throws Exception {

        logger.info("clone()=> BEGIN");

        String title = AppI18nUtil.getMessage("admin.global.text.cloned", new String[]{form.getUserLogonName()});

        //userStoreAssocsShow(form);
        
        form.setSourceUserId(form.getUserId());
        form.setUserId(null);
        form.setUserLogonName(title);
        form.setIsClone(true);

        form.setMainDbAlive(mainDbIsAlive());
        form.setMultiStoreDb(multiStoreDb());
        form.setAvailableLanguages(WebFormUtil.getStoreAvailableLanguages(storeService, getStoreId()));

        model.addAttribute(SessionKey.USER, form);

        logger.info("clone()=> END.");

        return "user/edit";
    }

    @RequestMapping(value = "/create")
    public String create(WebRequest request,
                         @ModelAttribute(SessionKey.USER) UserForm form,
                         Model model) throws Exception {

        logger.info("create()=> BEGIN");
        
        if (multiStoreDb() && !mainDbIsAlive()) {

            WebErrors webErrors = new WebErrors(request);

            webErrors.putMessage("exception.web.error.mainDbNotAccessable");

            model.addAttribute(SessionKey.USER, form);
            //return redirect(UrlPathAssistent.basePath() + "/user");
            return "user/edit";
        }
        
        UserIdentView userInfo = new UserIdentView(new UserData(),
                                              new EmailData(),
                                              new AddressData(),
                                              new PhoneData(),
                                              new PhoneData(),
                                              new PhoneData(),
                                              new EmailData(),
                                              new EmailData(),
                                              new ArrayList<UserAssocData>(),
                                              new ArrayList<PropertyData>(),
                                              new ArrayList<AllStoreIdentificationView>(),
                                              new ArrayList<GroupAssocData>());

        Long globalEntityId = getAppUser().getContext(AppCtx.STORE).getGlobalEntityId();
        WebFormUtil.fillOutUserForm(
                userLogonService,
                storeService,
                globalEntityId,
                getStoreId(),
                form,
                userInfo,
                getAppLocale(),
                mainDbIsAlive(),
                multiStoreDb()
        );
        
        //userStoreAssocsShow(form);
        model.addAttribute(SessionKey.USER, form);

        logger.info("create()=> END.");

        return "user/edit";
    }
    
    @RequestMapping(value = "/reset")
    public String resetForm(@ModelAttribute(SessionKey.USER) UserForm form, @PathVariable("userId") Long userId, Model model) {

        logger.info("resetForm()=> BEGIN");
        
        UserIdentView userInfo;

        if (userId > 0) {

            userInfo = userService.findUserToEdit(getStoreId(), userId);

        } else {

            userInfo = new UserIdentView(new UserData(),
                                         new EmailData(),
                                         new AddressData(),
                                         new PhoneData(),
                                         new PhoneData(),
                                         new PhoneData(),
                                         new EmailData(),
                                         new EmailData(),
                                         new ArrayList<UserAssocData>(),
                                         new ArrayList<PropertyData>(),
                                         new ArrayList<AllStoreIdentificationView>(),
                                         new ArrayList<GroupAssocData>());
        }

        Long globalEntityId = getAppUser().getContext(AppCtx.STORE).getGlobalEntityId();

        form.reset();
        WebFormUtil.fillOutUserForm(userLogonService,
                storeService,
                globalEntityId,
                getStoreId(),
                form,
                userInfo,
                getAppLocale(),
                mainDbIsAlive(),
                multiStoreDb()
        );
        
        //userStoreAssocsShow(form);
        model.addAttribute(SessionKey.USER, form);

        logger.info("resetForm()=> END.");

        return "user/edit";
    }


    @ResponseBody
    @RequestMapping(value = "/loginas", method = RequestMethod.POST)
    public String loginAs(@ModelAttribute(SessionKey.USER) UserForm form) {

        logger.info("loginAs()=> BEGIN, target: " + form.getLoginAsTarget()+", asUserId: "+form.getUserId());

        String entryPoint = null;
        String token = null;

        if (Constants.LOGIN_AS_TARGET.PROCUREMENT.equals(form.getLoginAsTarget())) {

            token = userLogonService.provideTokenForExternalAccess(
                    getAppUser().getContext(AppCtx.STORE).getDataSource().getDataSourceIdent().getDataSourceName(),
                    getStoreId(),
                    getAppUser().getUserId(),
                    form.getUserId()
            );

            entryPoint = AppResourceHolder
                    .getAppResource()
                    .getApplicationSettings()
                    .getSettings(Constants.APPLICATION_SETTINGS.PROCUREMENT_ENTRY_POINT);

        } else if (Constants.LOGIN_AS_TARGET.SERVICES.equals(form.getLoginAsTarget())) {

            token = userLogonService.provideTokenForExternalAccess(
                    getAppUser().getContext(AppCtx.STORE).getDataSource().getDataSourceIdent().getDataSourceName(),
                    getStoreId(),
                    getAppUser().getUserId(),
                    form.getUserId()
            );

            entryPoint = AppResourceHolder
                    .getAppResource()
                    .getApplicationSettings()
                    .getSettings(Constants.APPLICATION_SETTINGS.SERVICES_ENTRY_POINT);

        }

        String link = UrlPathAssistent.urlPathWithParams(entryPoint, new Pair<String, String>(Constants.ACCESS_TOKEN, token));

        logger.info("loginAs()=> END, link: " + link);

        return link;

    }


    @RequestMapping(value = "/changeUserType")
    public String changeUserType(@ModelAttribute(SessionKey.USER) UserForm form, @PathVariable("userId") Long userId, Model model) {

        logger.info("changeUserType()=> BEGIN");
        
        userStoreAssocsShow(form, userId);

        form.setMainDbAlive(mainDbIsAlive());
        form.setMultiStoreDb(multiStoreDb());
        form.setAvailableLanguages(WebFormUtil.getStoreAvailableLanguages(storeService, getStoreId()));

        model.addAttribute(SessionKey.USER, form);

        logger.info("changeUserType()=> END.");

        return "user/edit";
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(WebRequest request,
                       @ModelAttribute(SessionKey.USER) UserForm form,
                       @PathVariable("userId") Long userId,
                       Model model) {

        logger.info("show()=> BEGIN");

        if (multiStoreDb() && !mainDbIsAlive()) {
            WebErrors webErrors = new WebErrors(request);
            webErrors.putWarning(new WebWarning("exception.web.error.mainDbNotAccessable"));
        }

        UserIdentView userInfo = userService.findUserToEdit(getStoreId(), userId);

        List<String> managableUserTypes = userService.findManagableUserTypesFor(getUserId());

        if (userInfo != null && userInfo.getUserData() != null && managableUserTypes.contains(userInfo.getUserData().getUserTypeCd())) {

            Long globalEntityId = getAppUser().getContext(AppCtx.STORE).getGlobalEntityId();
            WebFormUtil.fillOutUserForm(
                    userLogonService,
                    storeService,
                    globalEntityId,
                    getStoreId(),
                    form,
                    userInfo,
                    getAppLocale(),
                    mainDbIsAlive(),
                    multiStoreDb()
            );

        }


        //userStoreAssocsShow(form);
        model.addAttribute(SessionKey.USER, form);

        logger.info("show()=> END.");

        return "user/edit";
    }


    @ModelAttribute(SessionKey.USER)
    public UserForm initModel(@PathVariable("userId") Long userId) {
        
        UserForm form = new UserForm();
        
        if (multiStoreDb() && mainDbIsAlive()) {
            prepareStoreAssocLists(form, userId);
        }  else if(!multiStoreDb()){
            prepareStoreAssocLists(form, userId);
        }

        form.initialize();

        return form;
    }

    private void prepareStoreAssocLists (UserForm form, Long userId) {
        String userName = "";

        AppStoreContext storeContext = getAppUser().getContext(AppCtx.STORE);
        if (userId != null && userId > 0) {
            UserData userData = userService.findByUserId(userId);
            if (userData != null) {
                userName = userData.getUserName();
                form.setUserType(userData.getUserTypeCd());
            }
        } else {
            if (Utility.isSet(form.getUserLogonName())) {
                userName = form.getUserLogonName();
            }
            if (Utility.isSet(form.getUserType())) {
                form.setUserType(form.getUserType());
            }
        }

        WebFormUtil.setUserStoreAssocLists(
                userLogonService,
                mainDbService,
                storeService,
                form,
                storeContext,
                userId,
                userName,
                true,
                multiStoreDb()
        );

        if (multiStoreDb()) {
            form.setAllUserData(mainDbService.findByUserName(userName));
            userStoreAssocsShow(form);
        } else {
            userStoreAssocsShow(form, userId);
        }

    }


    private void userStoreAssocsShow(UserForm form, Long userId) {

        List<AllStoreIdentificationView> adminStores = form.getAllAdminStores();
        List<AllStoreIdentificationView> userStores = Utility.isSet(form.getUserStores()) ? form.getUserStores() : form.getCurrentStore();

        Long defaultStoreId = userService.findUserDefaultStore(userId);

        SelectableObjects<AllStoreIdentificationView> storeAssocs = new SelectableObjects<AllStoreIdentificationView>(
                adminStores,
                userStores,
                AppComparator.ALL_STORE_IDENTIFICATION_COMPARATOR
        );

        form.setDefaultStoreId(defaultStoreId != null ? String.valueOf(defaultStoreId) : String.valueOf(form.getCurrentStore().get(0).getMainStoreId()));
        form.setEntities(storeAssocs);

    }

    private void userStoreAssocsShow(UserForm form) {
        List<AllStoreIdentificationView> adminStores;
        List<AllStoreIdentificationView> userStores;
        
        //if (RefCodeNames.USER_TYPE_CD.ADMINISTRATOR.equals(form.getUserType()) ||
        //    RefCodeNames.USER_TYPE_CD.STORE_ADMINISTRATOR.equals(form.getUserType()) ||
        //    RefCodeNames.USER_TYPE_CD.SYSTEM_ADMINISTRATOR.equals(form.getUserType())) {
        //    adminStores = form.getAllAdminStores();
        //} else {
        //    adminStores = form.getCurrentStore();
        //}
        
        adminStores = form.getAllAdminStores();
        
        if (Utility.isSet(form.getUserStores())) {
            userStores = form.getUserStores();
        } else {
            userStores = form.getCurrentStore();
        }
        if (form.getAllUserData() != null &&
            form.getAllUserData().getDefaultStoreId() != null) {
            form.setDefaultStoreId(form.getAllUserData().getDefaultStoreId().toString()); // user Default Store
        } else {
            form.setDefaultStoreId(form.getCurrentStore().get(0).getMainStoreId().toString()); // current Store of Administrator
        }

        SelectableObjects<AllStoreIdentificationView> storeAssocs = new SelectableObjects<AllStoreIdentificationView>(
                    adminStores,
                    userStores,
                    AppComparator.ALL_STORE_IDENTIFICATION_COMPARATOR);
        form.setEntities(storeAssocs);

    }
    
    private boolean mainDbIsAlive () {
        return mainDbService.isAliveMainUnit();
    }


    private boolean multiStoreDb() {

        String mainDbJdbcUrl =
                AppResourceHolder
                        .getAppResource()
                        .getApplicationSettings()
                        .getSettings(Constants.APPLICATION_SETTINGS.MAIN_DB_JDBC_UTL);

        return Utility.isSet(mainDbJdbcUrl);
    }

}
