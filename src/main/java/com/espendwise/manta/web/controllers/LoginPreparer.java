package com.espendwise.manta.web.controllers;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.web.forms.ExternalErrorForm;
import org.apache.log4j.Logger;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.WebRequest;

@Scope("request")
@Controller("loginPreparer")
public class LoginPreparer extends ViewPreparerSupport {

    private static final Logger logger = Logger.getLogger(LoginPreparer.class);

    @Autowired
    public WebRequest webRequest;

    @Override
    public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {

        Throwable exception = (Throwable) webRequest.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY, WebRequest.SCOPE_SESSION);
        if (exception != null) {
            ExternalErrorForm error = PortalHandlerExceptionResolver.reaolve(exception);
            tilesContext.getRequestScope().put(Constants.LOGIN_ERROR_JSP_SCOPE, error);
        }

        super.execute(tilesContext, attributeContext);
    }

}
