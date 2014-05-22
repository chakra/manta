package com.espendwise.manta.web.controllers;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.ApplicationUserManager;
import com.espendwise.manta.auth.AuthUser;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStjohnInstanceContext;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.i18n.UserMessageResource;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.ocean.common.webaccess.BasicResponseValue;
import com.espendwise.ocean.common.webaccess.LoginCredential;
import com.espendwise.ocean.common.webaccess.LoginData;
import com.espendwise.ocean.common.webaccess.ObjectTokenType;
import com.espendwise.ocean.common.webaccess.ResponseError;
import com.espendwise.ocean.common.webaccess.RestRequest;
import com.espendwise.ocean.common.webaccess.WebAccessException;
import com.espendwise.webservice.restful.value.BaseRequestData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public abstract class BaseController extends ControllerSupport {

    private static final Logger logger = Logger.getLogger(BaseController.class);

    @Autowired
    private AuthUser authUser;

    @Autowired
    private ApplicationUserManager userManager;

    @Autowired
    protected UserMessageResource messageResource;

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    public UserMessageResource getMessageResource() {
        return messageResource;
    }

    public void setMessageResource(UserMessageResource messageResource) {
        this.messageResource = messageResource;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public AppUser getAppUser() {
        return authUser.getAppUser();
    }

    public AppLocale getAppLocale() {
        return getAppUser() != null ? new AppLocale(getAppUser().getLocale()) : null;
    } 
    
    public Long getUserId() {
        return getAppUser() != null ? getAppUser().getUserId() : null;
    }

    public Long getStoreId() {
        return getAppUser() != null ? getAppUser().getContext(AppCtx.STORE).getStoreId() : null;
    }

    public String getStoreName() {
        return getAppUser() != null ? getAppUser().getContext(AppCtx.STORE).getStoreName() : null;
    }
    
    public ApplicationUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(ApplicationUserManager userManager) {
        this.userManager = userManager;
    }
    
    private String getAccessToken(WebErrors webErrors){
    	String accessToken = null;
        AppStjohnInstanceContext stJohnCtx = getAppUser().getContext(AppCtx.STJOHN_INSTANCE);
        
        if (stJohnCtx != null) {
            if (stJohnCtx.getLoginCredential() != null) {
                if (Utility.isSet(stJohnCtx.getLoginCredential().getAccessToken())) {
                     accessToken = stJohnCtx.getLoginCredential().getAccessToken();
                }
            }
        }

        if (!Utility.isSet(accessToken)) {
            Pair<String, List<ResponseError>> response = loginToStjohn();
            if (Utility.isSet(response.getObject1())) {
                accessToken = response.getObject1();
            } else if (Utility.isSet(response.getObject2())) {
                for (ResponseError error : response.getObject2()) {
                    if (Utility.isSet(error.getKey())) {
                        webErrors.putError(new WebError(error.getKey(), Args.typed(error.getArgs())));
                    } else if (Utility.isSet(error.getMessage())) {
                        webErrors.putError(new WebError("dummyMessageKey", null, error.getMessage()));
                    }
                }
            } else {
                webErrors.putError(new WebError("web.error.authorization.failed", Args.typed("STJOHN")));
            }
        }
        return accessToken;
    }
    
    private Pair<String, List<ResponseError>> loginToStjohn() {

        AuthUser authUser = getAuthUser();
        AppUser appUser = authUser.getAppUser();

        LoginData loginData = new LoginData();

        loginData.setUserName(appUser.getUserName());
        loginData.setPassword(authUser.getPassword());
        loginData.setCountry(appUser.getLocale().getCountry());
        loginData.setLanguage(appUser.getLocale().getLanguage());
        loginData.setEncrypted(true);

        logger.info("logonToStjohn()=> loginData:  " + loginData);

        
        String hostAddress = AppResourceHolder
                            .getAppResource()
                            .getApplicationSettings()
                            .getSettings(Constants.APPLICATION_SETTINGS.STJOHN_HOST_ADDRESS);

        RestRequest request = new RestRequest(hostAddress, "/service/login");
        
        Pair<String, List<ResponseError>> response;
        try {
            LoginCredential loginResponse = request.doPut(loginData, new ObjectTokenType<BasicResponseValue<LoginCredential>>() {});
            
            AppStjohnInstanceContext stJohnCtx = appUser.getContext(AppCtx.STJOHN_INSTANCE);
        
            stJohnCtx.setLoginCredential(loginResponse);

            response = new Pair<String, List<ResponseError>>(loginResponse.getAccessToken(), null);
        } catch (WebAccessException e) {
            response = new Pair<String, List<ResponseError>>(null, e.getErrors());
        } catch (Exception e) {
            response = new Pair<String, List<ResponseError>>(null, null);
        }

        return response;
    }
    
    private LoginData getLoginData(){
    	LoginData loginData = new LoginData();        
        AuthUser authUser = getAuthUser();
        loginData.setUserName(authUser.getUsername());
        loginData.setPassword(authUser.getPassword());
        loginData.setCountry(authUser.getAppUser().getLocale().getCountry());
        loginData.setLanguage(authUser.getAppUser().getLocale().getLanguage());
        loginData.setEncrypted(true);
        return loginData;
    }
    
    public Object requestStjohnService (BaseRequestData crData, String path, WebErrors webErrors) {
        String hostAddress = AppResourceHolder
                            .getAppResource()
                            .getApplicationSettings()
                            .getSettings(Constants.APPLICATION_SETTINGS.STJOHN_HOST_ADDRESS);

        String accessToken = getAccessToken(webErrors);
        if (!Utility.isSet(accessToken)) {
        	return null;
        }
        crData.setAccessToken(accessToken);
        LoginData loginData = getLoginData();            
        crData.setLoginData(loginData);
        
        RestRequest request = new RestRequest(hostAddress, path);
        
        Pair<Integer, List<ResponseError>> response;
        Integer responseStatus = null;
        Object responseObj = null;
        try {
            responseObj = request.doPut(crData, new ObjectTokenType<BasicResponseValue<Object>>() {});
            if (responseObj instanceof Map){
            	Map responseMap = (Map) responseObj;
            	responseStatus = new Integer(responseMap.get("responseStatus").toString());            	
            }else{
            	String temp = responseObj.toString();
            	int i = temp.indexOf('.');
            	if (i >0)
            		temp = temp.substring(0, i);
            	responseStatus = new Integer(temp);
            }
            response = new Pair<Integer, List<ResponseError>>(responseStatus, null);
        } catch (WebAccessException e) {
            response = new Pair<Integer, List<ResponseError>>(e.getStatus(), e.getErrors());
        } catch (Exception e) {
            response = new Pair<Integer, List<ResponseError>>(BasicResponseValue.STATUS.EXCEPTION, null);
        }
        switch (response.getObject1().intValue()) {
	        case BasicResponseValue.STATUS.ACCESS_DENIED:
	            webErrors.putError(new WebError("web.error.access.denied", Args.typed("STJOHN")));
	            break;
	        case BasicResponseValue.STATUS.ACCESS_TOKEN_EXPIRED:
	            webErrors.putError(new WebError("web.error.accessToken.expired", Args.typed("STJOHN")));
	            break;
	        case BasicResponseValue.STATUS.ERROR:
	            if (Utility.isSet(response.getObject2())) {
	                for (ResponseError error : response.getObject2()) {
	                    if (Utility.isSet(error.getKey())) {                            	
	                    	if (error.getArgs()!=null && error.getArgs().length>=2 && error.getArgs()[1] instanceof LinkedHashMap){
	                    		LinkedHashMap error1 = (LinkedHashMap) error.getArgs()[1];
	                    		String temp = I18nUtil.getMessage(getAppUser().getLocale(), (String)error1.get("key"), ((ArrayList)error1.get("args")).toArray());
	                    		error.getArgs()[1] = temp;
	                    	}
	                        webErrors.putError(new WebError(error.getKey(), Args.typed(error.getArgs())));
	                    } else if (Utility.isSet(error.getMessage())) {
	                        webErrors.putError(new WebError("dummyMessageKey", null, error.getMessage()));
	                    }
	                }
	            } else {
	                webErrors.putError(new WebError("web.error.processing.request", Args.typed("STJOHN")));
	            }
	            break;
	        case BasicResponseValue.STATUS.EXCEPTION:
	            webErrors.putError(new WebError("web.error.communication.problem", Args.typed("STJOHN")));
	            break;
	        case BasicResponseValue.STATUS.OK:
	            return responseObj;
	        default:
	    }
        return null;
    }


}
