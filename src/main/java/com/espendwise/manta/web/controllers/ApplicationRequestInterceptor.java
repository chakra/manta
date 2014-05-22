package com.espendwise.manta.web.controllers;


import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.alert.MessageType;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.web.util.BindingWebErrors;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.SuccessActionMessage;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ApplicationRequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(ApplicationRequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod specificHandler = (HandlerMethod) handler;
            handleCleanBefore(request,  specificHandler);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {

        if (mv != null) {

            if (handler instanceof HandlerMethod) {
            	
            	HandlerMethod specificHandler = (HandlerMethod) handler;

                handleCleanAfter(mv, request, specificHandler);
                handleSuccessMessage(mv, request, specificHandler);

            }

        }
    }

    private void handleSuccessMessage(ModelAndView mv, HttpServletRequest request, HandlerMethod handler) {

        SuccessMessage successMessage = handler.getMethodAnnotation(SuccessMessage.class);

        if (successMessage != null && !hasErrors(request, mv, handler)) {

        	if(mv.getModel().get("catalogCount") != null && mv.getModel().get("catalogIds") != null){
        	
        	String catalogCount =mv.getModel().get("catalogCount").toString();
        	String catalogIds =mv.getModel().get("catalogIds").toString();
        	
	        	if(catalogCount != null){
	        		Object[] paramList = null;
	        		paramList = new Object[]{catalogCount, catalogIds};
	        		String key = successMessage.value();
	        		SuccessActionMessage message = key != null && key.length() > 0 ? new SuccessActionMessage(key) : new SuccessActionMessage("admin.masterItems.catalogs.itemassignedtocatalog",Args.typed(paramList));
	        		WebAction.error(request.getSession(false), message);
	        		//WebAction.success(request.getSession(false), message);
	        		//WebErrors webErrors = new WebErrors(request);
	        	   // webErrors.putError(new WebError("admin.masterItems.catalogs.itemassignedtocatalog",Args.typed(paramList)));
	        		//webErrors.putMessage(message);
	        	    
	        	}
        	
        	}
        	else{
		        	String key = successMessage.value();
		
		            SuccessActionMessage message = key != null && key.length() > 0 ? new SuccessActionMessage(key) : new SuccessActionMessage("admin.successMessage.success");
		
		            int scope = successMessage.scope() >= 0 ? successMessage.scope()
		                    : mv.getViewName().startsWith("redirect:")
		                    ? WebRequest.SCOPE_SESSION
		                    : WebRequest.SCOPE_REQUEST;
		
		            if (WebRequest.SCOPE_REQUEST == scope) {
		                logger.info("handleSuccessMessage()=> success WebRequest.SCOPE_REQUEST: " + message);
		                WebAction.success(request, message);
		            } else if (WebRequest.SCOPE_SESSION == scope) {
		                logger.info("handleSuccessMessage()=> success WebRequest.SCOPE_SESSION: " + message);
		                WebAction.success(request.getSession(false), message);
		                
		            }

        	}
        }
    }


    private void handleCleanAfter( ModelAndView mv, HttpServletRequest request,HandlerMethod method) {

        Clean clean = method.getMethodAnnotation(Clean.class);
        clean = clean == null ? method.getBean().getClass().getAnnotation(Clean.class) : clean;
        if (clean != null && !clean.before()) {
            if (clean.ignoreErrors() || !hasErrors(request, mv,  method)) {
                String[] keys = clean.value();
                if (keys != null && keys.length > 0) {
                    for (String key : keys) {
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            session.removeAttribute(key);
                        }
                    }
                }
            }
        }
    }

    private void handleCleanBefore(HttpServletRequest request,HandlerMethod method) {

        Clean clean = method.getMethodAnnotation(Clean.class);
        clean = clean == null ? method.getBean().getClass().getAnnotation(Clean.class) : clean;
        if (clean != null && clean.before()) {
            if (clean.ignoreErrors() || !hasWebErrors(request)) {
                String[] keys = clean.value();
                if (keys != null && keys.length > 0) {
                    for (String key : keys) {
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            session.removeAttribute(key);
                            logger.info("handleCleanBefore()=> CLEAN MODEL ATTRIBUTE: "+key);
                        }
                    }
                }
            }
        }
    }

    public boolean hasErrors(HttpServletRequest request, ModelAndView mv, HandlerMethod specificHandler) {
        return hasWebErrors(request) || hasBindingErrors(mv, specificHandler);
    }

    public boolean hasWebErrors(HttpServletRequest request) {
        return new WebErrors(request).size(MessageType.ERROR) > 0
                || new WebErrors(request).size(MessageType.SYSTEM_ERROR) > 0;
    }

    public boolean hasBindingErrors(ModelAndView mv, HandlerMethod method) {

        MethodParameter[] parameters = method.getMethodParameters();

        if (parameters != null) {
            for (int i = 0; i < parameters.length - 1; i++) {
                MethodParameter p = parameters[i];
                ModelAttribute model = p.getParameterAnnotation(ModelAttribute.class);
                if (model != null) {
                    if (BindingResult.class.isAssignableFrom(parameters[i + 1].getParameterType())) {
                        BindingWebErrors errors = new BindingWebErrors(BindingResultUtils.getBindingResult(mv.getModel(), model.value()));
                        if (errors.size(MessageType.ERROR) > 0 || errors.size(MessageType.SYSTEM_ERROR) > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}