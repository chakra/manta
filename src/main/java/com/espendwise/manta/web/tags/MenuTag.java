package com.espendwise.manta.web.tags;


import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.UrlPathAssistent;
import org.apache.log4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Very simple solution for navigation menu building
 */
public class MenuTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(MenuTag.class);

    private static final String STYLE_TOP = "top";
    private static final String STYLE_FIRST = "first";
    private static final String STYLE_EXPANDED = "expanded";
    private static final String STYLE_HREF = "href";
    private static final String STYLE_SELECTED = "selected";
    private static final String STYLE_DISPLAY_NONE = "hide";

    private static final String EMPTY_HREF = "#";
    private static final String MENU_TEMPLATE = "<ul id=\"navi\" class=\"navi-menu\">%1$s</ul>";
    private static final String SUB_NENU_TWMPLATE = "<ul class=\"%1$s\">%2$s</ul>";

    private static final String MENU_ITEM_TEMPLATE = "<li class=\"%1$s\">\n" +
            "    <a href=\"%2$s\" class=\"%3$s\">%4$s</a>\n" +
            "%5$s" + "\n</li>";


    private org.springframework.web.util.UrlPathHelper urlPathHelper = new org.springframework.web.util.UrlPathHelper();

    private List<MenuItemTag.MenuItem> items;
    private String basePath;

    public List<MenuItemTag.MenuItem> getItems() {
        return items;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getBasePath() {
        return basePath;
    }

    @Override
    public int doStartTag() throws JspException {
        logger.debug("doStartTag()=> BEGIN");
        items = new ArrayList<MenuItemTag.MenuItem>();
        logger.debug("doStartTag()=> END");
        return TagSupport.EVAL_PAGE;
    }


    @Override
    public int doEndTag() throws JspException {

        logger.debug("doEndTag()=> BEGIN");

        try {
            StringBuilder items = buildItems(getItems());
            pageContext.getOut().write(String.format(MENU_TEMPLATE, items));
        } catch (IOException e) {
            throw new JspException(e);

        }

        logger.debug("doEndTag()=> END.");

        return super.doEndTag();

    }


    private StringBuilder buildItems(List<MenuItemTag.MenuItem> items) {
        StringBuilder buffer = new StringBuilder();
        buildItems(buffer, items, true);
        return buffer;
    }

    private boolean buildItems(StringBuilder buffer, List<MenuItemTag.MenuItem> items, boolean top) {

        boolean oneOfITemsSelected = false;

        if (Utility.isSet(items)) {

            int  i = 0;

            Collections.sort(items, MenuItemTag.MenuItem.I18N_COMPARATOR);

            for (MenuItemTag.MenuItem item : items) {

                boolean hasChilds = Utility.isSet(item.getChildItems());
                if (!hasChilds && item.getPath().equals("#"))
                	continue;

                StringBuilder sb = new StringBuilder();

                boolean selected =  (hasChilds ? buildItems(sb, item.getChildItems(), false) : isUriStartWith(item.getPath()));
                boolean expanded = Utility.isSet(item.getChildItems()) && selected;

                oneOfITemsSelected = oneOfITemsSelected || selected;

                String itemStyle = top ? i==0 ? STYLE_FIRST : STYLE_TOP : Constants.EMPTY;
                String subMenuStyle = !expanded ? STYLE_DISPLAY_NONE : Constants.EMPTY;

                String itemTextStyle = !hasChilds && selected ? STYLE_SELECTED : Constants.EMPTY;
                itemTextStyle += (expanded ? (Utility.isSet(itemTextStyle) ? Constants.SPACE : Constants.EMPTY) + STYLE_EXPANDED : Constants.EMPTY);
                itemTextStyle += (Utility.isSet(item.getHref()) ? (Utility.isSet(itemTextStyle) ? Constants.SPACE : Constants.EMPTY) + STYLE_HREF : Constants.EMPTY);

                buffer.append(
                        String.format(MENU_ITEM_TEMPLATE,
                                itemStyle,
                                Utility.strNN(item.getHref(), EMPTY_HREF),
                                itemTextStyle,
                                item.getI18nName(),
                                hasChilds ? String.format(SUB_NENU_TWMPLATE, subMenuStyle, sb) : Constants.EMPTY
                        )
                );

                i++;

            }
        }

        return oneOfITemsSelected;
    }

    private boolean isUriStartWith(String matchPath) {

        ServletRequest request = pageContext.getRequest();

        Map<String, String> pathVars = UrlPathAssistent.getPathVariables(request);
        if (pathVars != null) {
            matchPath = UrlPathAssistent.createPath(matchPath, pathVars);
        }

        String pathForRequest = urlPathHelper.getOriginatingServletPath((HttpServletRequest) request);
        return pathForRequest.startsWith(getBasePath() + matchPath);

    }

    @Override
    public void release() {
        super.release();
        this.items = null;
        this.basePath = null;
    }
}


