package com.espendwise.manta.web.controllers;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.ApplicationContextSecurityFilter;
import com.espendwise.manta.auth.ApplicationUserManager;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.view.LogonOptionView;
import com.espendwise.manta.service.UserLogonService;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.web.forms.LoginForm;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.web.util.WebTool;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(HomeController.class);

    @Autowired
    private UserLogonService userLogonService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletRequest request, Locale locale, Model model) {
        logger.info("home()=> Welcome home! the client locale is " + locale.toString());
        return redirect(UrlPathAssistent.basePath());
    }

    @RequestMapping(value = "/login.do")
    public String logon(HttpServletRequest request, Locale locale, Model model) {

        logger.info("logon()=> BEGIN");

        AppUser user = getAppUser();

        logger.info("logon()=> Welcome! the client locale is " + locale + ", application user is : " + user);

        LoginForm form = new LoginForm();

        if (user != null && user.getContext(AppCtx.STORE) != null) {

            form = new LoginForm();

            AppStoreContext storeContext = user.getContext(AppCtx.STORE);

            form.setDatasource(storeContext.getDataSource().getDataSourceIdent().getDataSourceName());
            form.setStoreId(storeContext.getStoreId());
            form.setUiOptions(storeContext.getUiOptions());
            form.setCountry(user.getLocale().getCountry());
            form.setLanguage(user.getLocale().getLanguage());

        } else {

            ApplicationUserManager userManager = getUserManager();

            userManager.clearAppUserSession(request);

            LogonOptionView logonOptions = userLogonService.getLogonOptions(request.getServerName(), locale);
            if (logonOptions == null) {
                throw new ApplicationRuntimeException(
                        new ApplicationExceptionCode<ExceptionReason.SystemReason>(
                                ExceptionReason.SystemReason.CANT_RETRIEVE_LOGON_UI_OPTIONS
                        )
                );
            }

            logger.info("logon()=> logonOptions = " + logonOptions);

            form.setDatasource(logonOptions.getDatasource());
            form.setStoreId(logonOptions.getStoreId());
            form.setUiOptions(logonOptions.getUiOption());
            form.setCountry(logonOptions.getLocale().getCountry());
            form.setLanguage(logonOptions.getLocale().getLanguage());

        }

        model.addAttribute("login", form);

        logger.info("logon()=> END, OK! user is ready for logon auth");

        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/resources/{datasource}/{storeId}/logo")
    public byte[] logo(HttpServletRequest request,
                       @PathVariable String datasource,
                       @PathVariable Long storeId,
                       @RequestParam String path,
                       @RequestParam String country,
                       @RequestParam String language) {

        logger.info("logo()=> BEGIN" +
                ", datasource: " + datasource +
                ", storeId: " + storeId +
                ", path: " + path +
                ", country: " + country +
                ", language: " + language

        );

        byte[] logo = userLogonService.getBestLogo(datasource,
                storeId,
                path,
                new Locale(language, country)
        );

        if(logo == null || logo.length == 0) {
            logo = WebTool.getDefaultLogo(request);
        }
        
        logger.info("logo()=> END, logo.Size: " + (logo != null ? logo.length : "null"));

        return logo;
    }


    @RequestMapping(value = "/error")
    public String error(WebRequest request) throws Throwable {

        String excKey = ApplicationContextSecurityFilter.APPLICATION_SECURITY_LAST_EXCEPTION_KEY;

        logger.info("error()=> check exc, excKey: " + excKey);

        Throwable e = (Throwable) request.getAttribute(excKey, WebRequest.SCOPE_SESSION);
        logger.info("error()=> exception:  " + e);
        if (e != null) {
            logger.info("error()=> throw e");
            request.removeAttribute(excKey, WebRequest.SCOPE_SESSION);
            throw e;
        } else {
            logger.info("error()=> no exceptions found, return to home");
            return redirect("/");
        }

    }

}
