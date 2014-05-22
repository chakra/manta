package com.espendwise.manta.auth;


import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.parser.AppParser;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.AppParserFactory;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class ApplicationContextSecurityFilter extends GenericFilterBean implements ApplicationContextAware {


    public static final String REQUEST_CONTEXT = ApplicationContextSecurityFilter.class.getName() + ".REQUEST_CONTEXT";
    public static final String APPLICATION_SECURITY_LAST_EXCEPTION_KEY = ApplicationContextSecurityFilter.class.getName() + ".APPLICATION_SECURITY_LAST_EXCEPTION_KEY";

    public static interface REQUEST_CONTEXT_ATTRIBUTE {
        public static final String HANDLER = "handler";
        public static final String PATH_VARIABLES = "pathVarriables";
        public static final String HANDLER_MAPPING_PATH = "handlerMappingPath";
        public static final String HANDLER_PATH = "handlerPath";
    }

    private static final Logger logger = Logger.getLogger(ApplicationContextSecurityFilter.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    private AdministratorLogonRoles roles;
    private ApplicationContext applicationContext;
    private ApplicationUserManager userManager;


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        logger.info("doFilter()=> BEGIN");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!logonCompleted()) {
            logger.debug("doFilter()=> END, REDIRECT TO LOGOUT");
            redirectStrategy.sendRedirect(request, response, "/j_spring_security_logout");
            return;
        }

        handleRequestContext(request);

        Map<String, Object> context = (Map<String, Object>) request.getAttribute(REQUEST_CONTEXT);
        if (context != null) {
            try {
                verifyRequestContext(context);
            } catch (Exception e) {
                logger.error("doFilter()=> ERROR " + e);
                handleException(request, response, e);
                return;
            }

        }


        printRequestProfile(request);
        chain.doFilter(request, response);


        logger.info("doFilter()=> END.\n");
    }

    private void printRequestProfile(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        long startTime = System.currentTimeMillis();
        String sessionId = session != null ? session.getId() : "no session";

        AuthUser user = Auth.getAuthUser();
        AppUser appUser = Auth.getAppUser();
        AppStoreContext storeContext = appUser != null ? appUser.getContext(AppCtx.STORE) : null;
        ApplicationDataSource datasource = Auth.getApplicationDataSource();

        String mess = "@@##@@S[Thread <" + String.valueOf(Thread.currentThread().getId()) + "," + Thread.currentThread().getName() + ">";
        mess += " Started at: <" + startTime + ">";
        mess += " Referer: <" + request.getHeader("Referer") + ">";
        mess += " Request URI: <" + request.getRequestURI() + ">";
        mess += " Request Protocol: <" + request.getProtocol() + ">";
        mess += " Servlet Path: <" + request.getServletPath() + ">";
        mess += " Session Id: <" + sessionId + ">";
        mess += session != null ? " Session.HashCode: <" + session.hashCode() + ">" : Constants.EMPTY;
        mess += " User: <" + (user != null ? user.getUsername() + "(" + (appUser != null ? appUser.getUserId() + ")" : Constants.EMPTY) : Constants.EMPTY);
        mess += ", locale - " + (appUser != null ? appUser.getLocale() : "undefined") + ">";
        mess += " Store: <" + (storeContext != null ? storeContext.getStoreId() : Constants.EMPTY) + ">";
        mess += " DataSource: <" + (datasource != null ? datasource.getDataSourceIdent().getDataSourceName() : Constants.EMPTY) + ">";
        mess += "] @@##@@ \n";


        logger.info(mess);
 }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        request.getSession().setAttribute(APPLICATION_SECURITY_LAST_EXCEPTION_KEY, e);
        redirectStrategy.sendRedirect(
                request,
                response,
                "/error"
        );

    }

    private void verifyRequestContext(Map<String, Object> context) throws Exception {

        AuthUser authUser = Auth.getAuthUser();

        Long globalEntityId = authUser.getAppUser().getContext(AppCtx.STORE).getGlobalEntityId();

        logger.debug("verifyRequestContext()=> context globalEntityId: " + globalEntityId);
        logger.debug("verifyRequestContext()=> URI_TEMPLATE_VARIABLES_ATTRIBUTE: " + context.get(REQUEST_CONTEXT_ATTRIBUTE.PATH_VARIABLES));

        String globalIdParam = ((Map<String, String>) context.get(REQUEST_CONTEXT_ATTRIBUTE.PATH_VARIABLES)).get(IdPathKey.GLOBAL_STORE_ID);

        if (Utility.isSet(globalIdParam)) {

            Long requestedGlobalId;
            try {
                AppParser<Long> parser = AppParserFactory.getParser(Long.class);
                requestedGlobalId = parser.parse(globalIdParam);
            } catch (AppParserException e) {
                logger.debug(e.getMessage(), e);
                throw new ServletException("Bad URI");
            }

            logger.debug("verifyRequestContext()=> requested globalId - " + requestedGlobalId + ", current globalId - " + globalEntityId);
            if (globalEntityId.longValue() != requestedGlobalId) {

                ApplicationUserManager userManager = getUserManager();

                logger.debug("verifyRequestContext()=> try to change store ");

                userManager.changeInstance(authUser, requestedGlobalId);

                logger.debug("verifyRequestContext()=> store has been changed successfully ");

            }

            globalEntityId = authUser.getAppUser().getContext(AppCtx.STORE).getGlobalEntityId();
        }

        logger.debug("verifyRequestContext()=> context is valid, globalEntityId: " + globalEntityId);
    }


    private void handleRequestContext(HttpServletRequest request) {

        logger.debug("handleRequesContext()=> BEGIN");
        logger.debug("handleRequestContext()=> URI - " + request.getRequestURI());

        Map<String, Object> context = new HashMap<String, Object>();

        /*      DefaultAnnotationHandlerMapping handlerMapping = applicationContext
                        .getBeansOfType(DefaultAnnotationHandlerMapping.class)
                        .values()
                        .iterator()
                        .next();
        */
        RequestMappingHandlerMapping handlerMapping = applicationContext
                .getBeansOfType(RequestMappingHandlerMapping.class)
                .values()
                .iterator()
                .next();

        logger.debug("handleRequestContext()=> handlerMapping: " + handlerMapping);

        HandlerExecutionChain handlerChain = getHandler(handlerMapping, request);

        logger.debug("handleRequestContext()=> handlerChain: " + handlerChain);

        if (handlerChain != null) {

            HandlerMethod handler = ((HandlerMethod) handlerChain.getHandler());

            logger.debug("handleRequestContext()=> handler: " + handler);

            if (handler != null) {

                RequestMapping handlerRequestMapping = handler.getBean().getClass().getAnnotation(RequestMapping.class);
                RequestMapping handlerRequestMethodMapping = handler.getMethodAnnotation(RequestMapping.class);

                if (handlerRequestMapping != null) {

                    String[] handlerMappingPath = handlerRequestMapping.value();
                    String[] handlerMethodMappingPath = handlerRequestMethodMapping.value();

                    List<String> mappingPath = new ArrayList<String>();

                    if (handlerMappingPath != null) {
                        for (String p : handlerMappingPath) {
                            if (handlerMethodMappingPath != null) {
                                for (String mp : handlerMethodMappingPath) {
                                    mappingPath.add(p + mp);
                                }
                            } else {
                                mappingPath.add(p);
                            }
                        }
                    }
                    PathMatcher matcher = handlerMapping.getPathMatcher();

                    String urlPath = handlerMapping.getUrlPathHelper().getLookupPathForRequest(request);

                    logger.debug("handleRequestContext()=> urlPath: " + urlPath);
                    logger.debug("handleRequestContext()=> mappingPath: " + mappingPath);

                    // Pattern match?
                    List<String> matchingPatterns = new ArrayList<String>();
                    Comparator<String> patternComparator = matcher.getPatternComparator(urlPath);

                    Map<String, String> uriTemplateVariables = new HashMap<String, String>();
                    for (String registeredPattern : mappingPath) {
                        registeredPattern = UrlPathAssistent.extractPathWithinPattern(matcher, registeredPattern, urlPath);
                        if (matcher.match(registeredPattern, urlPath)) {
                            matchingPatterns.add(registeredPattern);
                        }
                    }

                    logger.debug("handleRequestContext()=>  matchingPatterns: " + matchingPatterns);

                    String bestPatternMatch = null;
                    if (!matchingPatterns.isEmpty()) {
                        Collections.sort(matchingPatterns, patternComparator);
                        logger.debug("handleRequestContext()=> Matching patterns for request [" + urlPath + "] are " + matchingPatterns);
                        bestPatternMatch = matchingPatterns.get(0);
                    }

                    logger.debug("handleRequestContext()=>  bestPatternMatch: " + bestPatternMatch);

                    if (bestPatternMatch != null) {

                        for (String matchingPattern : matchingPatterns) {
                            if (patternComparator.compare(bestPatternMatch, matchingPattern) == 0) {
                                uriTemplateVariables.putAll(matcher.extractUriTemplateVariables(matchingPattern, urlPath));
                            }
                        }

                        String handlerPath = UrlPathAssistent.createPath(bestPatternMatch, uriTemplateVariables);

                        context.put(REQUEST_CONTEXT_ATTRIBUTE.HANDLER, handler);
                        context.put(REQUEST_CONTEXT_ATTRIBUTE.PATH_VARIABLES, uriTemplateVariables);
                        context.put(REQUEST_CONTEXT_ATTRIBUTE.HANDLER_MAPPING_PATH, bestPatternMatch);
                        context.put(REQUEST_CONTEXT_ATTRIBUTE.HANDLER_PATH, handlerPath);

                        logger.debug("handleRequestContext()=> URI Template variables for request [" + urlPath + "] are " + uriTemplateVariables);

                        request.setAttribute(REQUEST_CONTEXT, context);

                    }
                }
            }

        }

        logger.debug("handleRequesContext()=> END");

    }

    private HandlerExecutionChain getHandler(RequestMappingHandlerMapping handlerMapping, HttpServletRequest request) {
        try {
            return handlerMapping.getHandler(request);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            return null;
        }
    }

    private boolean logonCompleted() {

        return isAuthenticated() && (
                Auth.getAppUser() != null
                        && Auth.getAppUser().getContext(AppCtx.STORE) != null
                        && Auth.getAppUser().getContext(AppCtx.STORE).getStoreId() != null
        );
    }


    public boolean isAuthenticated() {

        Authentication authentication = Auth.getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (getRoles().getList().contains(authority.getAuthority())) {
                return true;
            }
        }

        return false;
    }


    public void setRoles(AdministratorLogonRoles roles) {
        this.roles = roles;
    }

    public AdministratorLogonRoles getRoles() {
        return roles;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public ApplicationUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(ApplicationUserManager userManager) {
        this.userManager = userManager;
    }

}