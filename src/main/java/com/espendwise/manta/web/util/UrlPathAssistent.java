package com.espendwise.manta.web.util;


import com.espendwise.manta.auth.ApplicationContextSecurityFilter;
import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.web.controllers.UrlPathKey;
import org.apache.log4j.Logger;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlPathAssistent {
    
    private static final Logger logger = Logger.getLogger(UrlPathAssistent.class);

    public static Map<String, String> getPathVariables(ServletRequest request) {
        Map<String, Object> context = (Map<String, Object>) request.getAttribute(ApplicationContextSecurityFilter.REQUEST_CONTEXT);
        return (Map<String, String>) (context != null
                ? context.get(ApplicationContextSecurityFilter.REQUEST_CONTEXT_ATTRIBUTE.PATH_VARIABLES)
                : null);
    }

    public static Map<String, String> getPathVariables(WebRequest request) {
        Map<String, Object> context = (Map<String, Object>) request.getAttribute(ApplicationContextSecurityFilter.REQUEST_CONTEXT, WebRequest.SCOPE_REQUEST);
        return (Map<String, String>) (context != null
                ? context.get(ApplicationContextSecurityFilter.REQUEST_CONTEXT_ATTRIBUTE.PATH_VARIABLES)
                : null);
    }

    public static Long getPathId(String idName, WebRequest webRequest) {

        Long id = null;

        Map<String, ?> variables = getPathVariables(webRequest);

        if (variables != null && variables.containsKey(idName)) {
            String idParam = String.valueOf(variables.get(idName));
            try {
                id = Parse.parseLong(idParam);
            } catch (AppParserException e) { //ignore  }
            }
        }

        return id == null || !(id > 0) ? null : id;

    }


    public static String createPath(String template, Map<String, String> uriTemplateVariables) {
        String path = template;
        if (uriTemplateVariables != null) {
            for (Map.Entry<String, String> e : uriTemplateVariables.entrySet()) {
                logger.debug("createPath()=> set path param: " + "{" + e.getKey() + "}");
                String p = "\\{" + e.getKey() + "\\}";
                path = path.replaceAll(p, e.getValue());
            }
        }
        return path;
    }

    public static String createPath(WebRequest request, String template) {
        return createPath(template, getPathVariables(request));
    }

    public static String basePath() {
        AppStoreContext store = Auth.getAppUser() == null ? null : Auth.getAppUser().getContext(AppCtx.STORE);
        Map<String, String> m = new HashMap<String, String>();
        m.put(IdPathKey.GLOBAL_STORE_ID, String.valueOf(store == null ? Constants.EMPTY : store.getGlobalEntityId()));
        return createPath(UrlPathKey.INSTANCE, m);
    }

    public static String extractPathWithinPattern(PathMatcher matcher, String registeredPattern, String urlPath) {
        String p = matcher.extractPathWithinPattern(registeredPattern, urlPath);
        return registeredPattern + (urlPath.endsWith("/") && (p.isEmpty() && !p.endsWith("/")) ? "/" : "");
    }

    public static String urlPath(List<?> elements) {
        return elements != null && elements.size() > 0
                ? StringUtils.collectionToDelimitedString(elements, "/")
                : Constants.EMPTY;
    }

    public static String urlPath(Object... elements) {
        return elements != null && elements.length > 0
                ? StringUtils.collectionToDelimitedString(Arrays.asList(elements), "/")
                : Constants.EMPTY;
    }

    public static String urlPathWithParams(String link, Pair<String, String> p) {
        return Utility.isSet(link) ? link + "?" + p.getObject1() + "=" + p.getObject2() : Constants.EMPTY;
    }
    public static String urlPathWithParams(String link, Pair<String, String>... params) {
        if(Utility.isSet(link)){
            String pstr = Constants.EMPTY;
            for(Pair p: params){   pstr = "?" + p.getObject1()+"="+p.getObject2(); }
            return link + pstr;
        }  else {
            return Constants.EMPTY;
        }
    }

}
