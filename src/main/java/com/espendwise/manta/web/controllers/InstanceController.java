package com.espendwise.manta.web.controllers;


import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.ApplicationUserManager;
import com.espendwise.manta.auth.AuthDataSourceIdent;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.data.AddressData;
import com.espendwise.manta.model.entity.StoreListEntity;
import com.espendwise.manta.model.view.InstanceView;
import com.espendwise.manta.service.UserLogonService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.HomeViewType;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.StoreListEntityCriteria;
import com.espendwise.manta.util.validation.ApplicationValidator;
import com.espendwise.manta.web.forms.InstanceForm;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebSort;
import com.espendwise.manta.web.validator.SimpleFilterFormFieldValidator;

@Controller
@RequestMapping(UrlPathKey.INSTANCE)
@SessionAttributes(SessionKey.INSTANCE)
@AutoClean
public class InstanceController extends BaseController {

    private static final Logger logger = Logger.getLogger(InstanceController.class);

    private UserLogonService userLogonService;

    @Autowired
    public InstanceController(UserLogonService userLogonService) {
        this.userLogonService = userLogonService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping("")
    public String home(@ModelAttribute(SessionKey.INSTANCE) InstanceForm instanceForm) {
        return "home";
    }

    @RequestMapping(value = "/home/findInstance", method = RequestMethod.GET)
    public String findInstance(WebRequest request, @ModelAttribute(SessionKey.INSTANCE) InstanceForm instanceForm) throws Exception {

        WebErrors webErrors = new WebErrors(request);

        if (instanceForm.getIsSearchView()) {

            ApplicationValidator[] validators = new ApplicationValidator[]{new SimpleFilterFormFieldValidator("admin.home.label.findInstance")};

            List<? extends ArgumentedMessage> validationErrors = WebAction.validate(instanceForm,  validators);
            if (!validationErrors.isEmpty()) {
                webErrors.putErrors(validationErrors);
            }

            if(!webErrors.isEmpty()) {
                return "home";
            }
            
            AppUser user = getAppUser();

            logger.info("findInstance()=> filterType  : " + instanceForm.getFilterType());
            logger.info("findInstance()=> filterValue : " + instanceForm.getFilterValue());
            logger.info("findInstance()=> showInactive: " + instanceForm.getShowInactive());
            logger.info("findInstance()=> user " + user + ", userId: " + user.getUserId());

            StoreListEntityCriteria criteria = new StoreListEntityCriteria();

            criteria.setStoreName(trim(instanceForm.getFilterValue()));
            criteria.setStoreNameFilterType(trim(instanceForm.getFilterType()));
            criteria.setShowInactive(instanceForm.getShowInactive());
            criteria.setLimit(Constants.FILTER_RESULT_LIMIT.STORE);

            List<InstanceView> userStores = userLogonService.findUserStoreDataSources(user.getUserId(), criteria);

            instanceForm.setUserStores(userStores);

            WebSort.sort(instanceForm,
                    Utility.javaBeanPath(InstanceView.PRIMARY_ENTITY_NAME));
        }


        return "home";
    }

    @RequestMapping(value = "/home/changeInstance/{datasource}/{instanceId}", method = RequestMethod.GET)
    public String changeInstance(@PathVariable("datasource") String datasource, @PathVariable("instanceId") Long instanceId) throws Exception {

        logger.info("changeInstance()=> selectedDatasource: " + datasource + ", selectedInstanceId: " + instanceId);

        ApplicationUserManager userManager = getUserManager();
        userManager.changeInstance(getAuthUser(), datasource, instanceId);

        logger.info("changeInstance()=> instance has been changed successfully");

        return redirect(UrlPathAssistent.basePath());

    }

    @RequestMapping(value = "/home/instances/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.INSTANCE) InstanceForm instance, @PathVariable String field) throws Exception {

        logger.info("sort()=> BEGIN, field " + field);

        String fieldExp;

        if(AddressData.ADDRESS1.equals(field)) {
            fieldExp = Utility.javaBeanPath(InstanceView.STORE, Utility.arrayExp(StoreListEntity.ADDRESSES, 0), AddressData.ADDRESS1);
        } else if(AddressData.CITY.equals(field)) {
            fieldExp = Utility.javaBeanPath(InstanceView.STORE,  Utility.arrayExp(StoreListEntity.ADDRESSES, 0),AddressData.CITY);
        } else if(AddressData.STATE_PROVINCE_CD.equals(field)) {
            fieldExp = Utility.javaBeanPath(InstanceView.STORE, Utility.arrayExp(StoreListEntity.ADDRESSES, 0),AddressData.STATE_PROVINCE_CD);
        } else if(InstanceView.DATA_SOURCE_IDENT.equals(field)) {
            fieldExp = Utility.javaBeanPath(InstanceView.DATA_SOURCE_IDENT, AuthDataSourceIdent.JDBC_URL);
        } else if(InstanceView.PRIMARY_ENTITY_ID.equals(field)) {
            fieldExp = Utility.javaBeanPath(field);
        } else if(InstanceView.PRIMARY_ENTITY_NAME.equals(field)) {
            fieldExp = Utility.javaBeanPath(field);
        }  else {
            fieldExp =  Utility.javaBeanPath(InstanceView.STORE, field);
        }

        logger.info("sort()=> fieldExp: "+fieldExp);

        WebSort.sort(instance, fieldExp);

        logger.info("sort()=> END.");

        return "home";
    }

    @ResponseBody
    @RequestMapping(value = "/home/services", method = RequestMethod.GET)
    public void services(HttpServletResponse response) throws Exception {

        logger.info("services()=> BEGIN");


        String token = userLogonService.provideTokenForExternalAccess(
                getAppUser().getContext(AppCtx.STORE).getDataSource().getDataSourceIdent().getDataSourceName(),
                getStoreId(),
                null,
                getUserId()
        );

        String entryPoint = AppResourceHolder
                    .getAppResource()
                    .getApplicationSettings()
                    .getSettings(Constants.APPLICATION_SETTINGS.SERVICES_ENTRY_POINT);


        String link = UrlPathAssistent.urlPathWithParams(entryPoint, new Pair<String, String>(Constants.ACCESS_TOKEN, token));

        logger.info("services()=> END, link: " + link);

        logger.info("services()=> END.");

        response.sendRedirect(link);
    }

    @ResponseBody
    @RequestMapping(value = "/home/procurement", method = RequestMethod.GET)
    public void procurement(HttpServletResponse response) throws Exception {

        logger.info("procurement()=> BEGIN");

        String token  = userLogonService.provideTokenForExternalAccess(
                getAppUser().getContext(AppCtx.STORE).getDataSource().getDataSourceIdent().getDataSourceName(),
                getStoreId(),
                null,
                getAppUser().getUserId()
        );

        String entryPoint = AppResourceHolder
                .getAppResource()
                .getApplicationSettings()
                .getSettings(Constants.APPLICATION_SETTINGS.PROCUREMENT_ENTRY_POINT);


        String link = UrlPathAssistent.urlPathWithParams(entryPoint, new Pair<String, String>(Constants.ACCESS_TOKEN, token));

        logger.info("procurement()=> END, link: " + link);

        logger.info("procurement()=> END.");

        response.sendRedirect(link);
    }


    @ModelAttribute(SessionKey.INSTANCE)
    public InstanceForm init(HttpSession session) {

        logger.info("init()=> BEGIN");

        InstanceForm instance = (InstanceForm) session.getAttribute(SessionKey.INSTANCE);

        logger.info("init()=> instance: " + instance);

        if (instance == null || !instance.isInitialized()) {

            List<InstanceView> userStores;

            AppUser user = getAppUser();

            logger.info("init()=> user - " + user);

            AppStoreContext storeContext = user.getContext(AppCtx.STORE);
            HomeViewType viewType = storeContext.getUiOptions().getHomeViewType();

            instance = new InstanceForm();
            instance.initialize();
            instance.setIsListView(viewType == HomeViewType.LIST_VIEW);
            instance.setIsSearchView(viewType == HomeViewType.SEARCH_VIEW);

            if (viewType == HomeViewType.LIST_VIEW) {
               
                StoreListEntityCriteria criteria = new StoreListEntityCriteria();
                criteria.setLimit(10);
                
                userStores = userLogonService.findUserStoreDataSources(user.getUserId(),criteria );
                instance.setUserStores(userStores);

                WebSort.sort(instance,
                        Utility.javaBeanPath(InstanceView.PRIMARY_ENTITY_NAME));

                logger.info("init()=> userStores,size: " + userStores.size());

            }



        }

        logger.info("init()=> END");

        return instance;

    }

}
