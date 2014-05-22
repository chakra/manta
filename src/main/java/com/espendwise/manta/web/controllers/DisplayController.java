package com.espendwise.manta.web.controllers;

import com.espendwise.manta.auth.AuthUser;
import com.espendwise.manta.auth.AuthUserContext;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.view.DisplaySettingsView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.WebTool;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(UrlPathKey.DISPLAY_CONTROL)
public class DisplayController extends BaseController {

    private static final Logger logger = Logger.getLogger(DisplayController.class);

    @ResponseBody
    @RequestMapping(value = "/wideScreen.do", method = RequestMethod.GET)
    public String wideScreen() {

        AuthUser authUser = getAuthUser();

        DisplaySettingsView displaySettings = authUser
                .getAuthUserContext()
                .get(AuthUserContext.DISPlAY_SETTINGS);

        if (displaySettings != null) {
            String screenStyle = Utility.isSet(displaySettings.getWideScreen()) ? Constants.EMPTY : Constants.WIDE_SCREEN_STYLE;
            displaySettings.setWideScreen(screenStyle);
            return displaySettings.getWideScreen();
        }

        return Constants.EMPTY;
    }


    @ResponseBody
    @RequestMapping(value = "/logo.do", method = RequestMethod.GET)
    public byte[] logo(HttpServletRequest request) {

        AuthUser authUser = getAuthUser();
        AppStoreContext storeContext = authUser.getAppUser().getContext(AppCtx.STORE);

        byte[] logo = storeContext.getUiOptions().getLogoBinaryData();
        if (logo == null || logo.length == 0) {
            logo = WebTool.getDefaultLogo(request);
        }

        return logo;
    }
    
    
}
