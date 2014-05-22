package com.espendwise.manta.web.controllers;


import com.espendwise.manta.auth.AuthenticationAccessTokenException;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.trace.AbstractSystemReasonResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ApplicationRuntimeException;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.web.forms.ExternalErrorForm;
import com.espendwise.manta.web.util.WebMessage;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class PortalHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

    public PortalHandlerExceptionResolver() {
    }

    private static final Logger logger = Logger.getLogger(PortalHandlerExceptionResolver.class);


    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exc) {

        logger.info("doResolveException()=> BEGIN, exc: " + exc);

        exc.printStackTrace();

        ExternalErrorForm form = reaolve(exc);

        if (form.isResolved()) {
            logger.info("doResolveException()=> Exception resolved, END.");
            return new ModelAndView("exception", "exception", form);
        } else {

            logger.info("doResolveException()=> Exception is unresolved, invoke default resolver, END. ");


            ModelAndView errView = super.doResolveException(request,
                    response,
                    handler,
                    exc
            );

            if (errView == null) {
                ExternalErrorForm errorForm = handleUncaughtException(exc);
                return new ModelAndView("exception", "exception", errorForm);
            }

            logger.info("errView:  "+errView);
            return errView;
        }

    }


    public static ExternalErrorForm reaolve(Throwable exception) {

        logger.info("reaolve()=> exception: "+exception);

        ExternalErrorForm errorForm;

        if (exception instanceof ApplicationRuntimeException) {
            errorForm = handleApplicationException((ApplicationRuntimeException) exception);
        } else if (exception instanceof BadCredentialsException) {
            errorForm = handleBadCredentialsException((BadCredentialsException) exception);
        } else if (exception instanceof AuthenticationAccessTokenException) {
            errorForm = handleAuthenticationAccessTokenException((AuthenticationAccessTokenException) exception);
        }  else if (exception instanceof ServletException) {
            errorForm = handleServletException((ServletException) exception);
        } else if (exception instanceof javax.persistence.NoResultException) {
            errorForm = handleNoResultException((javax.persistence.NoResultException) exception);
        } else if (exception instanceof NullPointerException) {
            errorForm = handleNullPointerException((NullPointerException) exception);
        } else {
            errorForm = handleUncaughtException(exception);
        }

        logger.info("reaolve()=> END, "+errorForm);

        return errorForm;

    }


    private static ExternalErrorForm handleNullPointerException(NullPointerException exception) {

        ExternalErrorForm errorForm = new ExternalErrorForm();

        errorForm.setMessage(new WebMessage("exception.web.error.systemError"));
        errorForm.setReasons(Utility.toList(new WebMessage("exception.web.error.nullPointerException")));
        errorForm.setExc(exception);
        errorForm.setResolved(true);

        return errorForm;

    }

    private static ExternalErrorForm handleNoResultException(NoResultException exception) {

        ExternalErrorForm errorForm = new ExternalErrorForm();

        String errKey = Constants.EXCEPTION_I18N_RESOLVER_KEY_PREFIX + exception.getClass().getSimpleName();

        errorForm.setMessage(new WebMessage(errKey));
        errorForm.setReasons(Utility.toList(new WebMessage(null, null, exception.getMessage())));
        errorForm.setExc(exception);
        errorForm.setResolved(true);

        return errorForm;

    }

    private static ExternalErrorForm handleServletException(ServletException exception) {

        ExternalErrorForm errorForm = new ExternalErrorForm();

        errorForm.setMessage(new WebMessage("exception.web.error.systemError"));
        errorForm.setReasons(Utility.toList(new WebMessage(null, null, exception.getMessage())));
        errorForm.setExc(exception);
        errorForm.setResolved(true);

        return errorForm;
    }

    private static ExternalErrorForm handleAuthenticationAccessTokenException(AuthenticationAccessTokenException exception) {

        ExternalErrorForm errorForm = new ExternalErrorForm();

        String errKey = exception.getMessage();

        errorForm.setMessage(new WebMessage(exception.getMessage(), exception.getExcArgs()));
        errorForm.setExc(exception);
        errorForm.setResolved(true);

        return errorForm;
    }


    private static ExternalErrorForm handleBadCredentialsException(BadCredentialsException exception) {

        ExternalErrorForm errorForm = new ExternalErrorForm();

        String errKey = Constants.EXCEPTION_I18N_RESOLVER_KEY_PREFIX + exception.getClass().getSimpleName();

        errorForm.setMessage(new WebMessage(errKey));
        errorForm.setReasons(Utility.toList(new WebMessage(null, null, exception.getMessage())));
        errorForm.setExc(exception);
        errorForm.setResolved(true);

        return errorForm;
    }

    private static ExternalErrorForm handleUncaughtException(Throwable exception) {

        ExternalErrorForm errorForm = new ExternalErrorForm();

        String errKey = Constants.EXCEPTION_I18N_RESOLVER_KEY_PREFIX + exception.getClass().getSimpleName();

        logger.info("handleUncaughtException()=> error message: "+exception.getMessage());

        errorForm.setMessage(new WebMessage(errKey, null, "exception.web.error.unspecifiedError"));
        errorForm.setReasons(Utility.toList(new WebMessage(null, null, exception.getMessage())));
        errorForm.setExc(exception);
        errorForm.setResolved(true);

        return errorForm;

    }

    private static ExternalErrorForm handleApplicationException(ApplicationRuntimeException runtimeException) {

        ExternalErrorForm errorForm = new ExternalErrorForm();

        List<ArgumentedMessage> reasons = new ArrayList<ArgumentedMessage>();

        String errKey = Constants.EXCEPTION_I18N_RESOLVER_KEY_PREFIX + runtimeException.getClass().getSimpleName();

        for (ApplicationExceptionCode code : runtimeException.getExceptionCodes()) {

            if (code.getReason() instanceof ExceptionReason.SystemReason) {

                reasons.add(

                        new AbstractSystemReasonResolver() {

                            @Override
                            public ArgumentedMessage isIllegalValidationResult(ApplicationExceptionCode code) {
                                return new ArgumentedMessageImpl("exception.web.error.illegalValidationResult", code.getArguments());
                            }

                            @Override
                            public ArgumentedMessage isUserDoesNotHaveAccessToStore(ApplicationExceptionCode code) {
                                return new ArgumentedMessageImpl("exception.web.error.userDoesNotHaveAccessToStore", code.getArguments());
                            }

                            @Override
                            public ArgumentedMessage isUserDoesNotHaveAccessToInstance(ApplicationExceptionCode code) {
                                return new ArgumentedMessageImpl("exception.web.error.userDoesNotHaveAccessToInstance", code.getArguments());
                            }

                            @Override
                            public ArgumentedMessage isApplicationIllegalAccessException(ApplicationExceptionCode code) {
                                return new ArgumentedMessageImpl("exception.web.error.applicationIllegalAccessException", code.getArguments());
                            }

                        }.resolve(code)

                );

            }

        }

        errorForm.setMessage(new WebMessage(errKey));
        errorForm.setReasons(reasons);
        errorForm.setExc(runtimeException);
        errorForm.setResolved(true);

        return errorForm;

    }
}
