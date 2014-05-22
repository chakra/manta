package com.espendwise.manta.web.tags;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.AppI18nUtil;

public class TabTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(TabTag.class);

    protected static final String LAST_CELL  = "tab-last-cell";
    protected static final String FIRST_CELL  = "tab-first-cell";
    protected static final String LAST_ROW  = "tab-last-row";
    protected static final String ACTIVE = "tab-active";
    protected static final String INACTIVE = "tab-inactive";

    protected static final String EMPTY  = "";
    protected static final String SPACE  = " ";
    protected static final String NEXT_LINE  = "\n";
    protected static final String PX  = "px";

    protected static final String CLEAR = "<div class=\"clear\"></div>";
    protected static final String END_OF_LINE = "<div class=\"endline\" style=\"height:%1$s\"></div>" +
            "";
    protected static final String TAB = "<div class=\"tab %1$s\">\n" +
            "        <span class=\"content %2$s\">\n" +
            "            <div class=\"left\"></div>\n" +
            "            <div class=\"center\"><table style=\"width:1px;height:25px  \"><tr><td style=\"vertical-align: middle;white-space: nowrap\"><a href=\"%4$s\">%3$s</a></td></tr></table></div>\n" +
            "            <div class=\"right\"></div>\n" +
            "         </span>\n" +
            "          <div class=\"space\">%5$s</div>\n" +
  "    </div>";


    private String requestedPath;
    private String tabPath;
    private Tabs tabs;

    @Override
    public int doStartTag() throws JspException {
        logger.debug("doStartTag()=> requestedPath: "+requestedPath);
        this.tabs = new Tabs();
        return BodyTagSupport.EVAL_PAGE;
    }


    @Override
    public int doEndTag() throws JspException {

        StringBuilder sb = new StringBuilder();

        try {
        String basePath = getBasePath();

        logger.debug("doEndTag()()=> basePath: "+basePath);

        for (int rowIndex = 0; rowIndex < tabs.size(); rowIndex++) {

            TabRow row = tabs.get(rowIndex);


            for (int cellIndex = 0; cellIndex < row.size(); cellIndex++) {

                TabCell cell = row.get(cellIndex);

                String active = getRequestedPath().startsWith(cell.getPath()) ? TabTag.ACTIVE : TabTag.INACTIVE;
                String lastRow = rowIndex == tabs.size() - 1 ? TabTag.LAST_ROW : TabTag.EMPTY;
                String lastCell = cellIndex == row.size() - 1 ? TabTag.LAST_CELL : TabTag.EMPTY ;
                String firstCell = cellIndex == 0? TabTag.FIRST_CELL : TabTag.EMPTY ;
                String message = AppI18nUtil.getMessage(cell.getName());

                String s = TabTag.NEXT_LINE;
                
                String link = null;
                if (cell.isRenderLink()) {
                	link = basePath + cell.getHref();
                }
                else {
                	link = "";
                }

                s += String.format(TabTag.TAB,
                        firstCell + TabTag.SPACE + lastRow + TabTag.SPACE + lastCell ,
                        active,
                        message,
                        link,
                        rowIndex != tabs.size() - 1 && cellIndex == row.size() - 1 ? String.format(END_OF_LINE, ((tabs.size() - 1 - rowIndex) * 25) + PX) : TabTag.SPACE
                );

                sb.append(s);
            }

            sb.append(TabTag.NEXT_LINE);
            sb.append(TabTag.CLEAR);

        }

            pageContext.getOut().write(sb.toString());
        } catch (Exception e) {
            logger.error("doEndTag()=> " + e.getMessage(), e);
            throw new JspException(e.getMessage());
        }

        return TagSupport.EVAL_PAGE;
    }

    public String getRequestedPath() {
        return requestedPath;
    }

    public void setRequestedPath(String requestedPath) {
        this.requestedPath = requestedPath;
    }

    @Override
    public void release() {
        super.release();
        this.requestedPath = null;
        this.tabs = null;
    }

    public void addRow() {
       tabs.add(new TabRow());
    }

    public void addCell(String name, String path, String href, boolean renderLink) {
        tabs.get(tabs.size()-1).add(new TabCell(name, path, href, renderLink));
    }

    public Tabs getTabs() {
        return tabs;
    }

    private String getBasePath() throws Exception {

        String tabPath = getTabPath();

        String pattern = tabPath.replace("*", "\\d{1,}");
        String path = (String) pageContext.getRequest().getAttribute("javax.servlet.forward.request_uri");

        if (Utility.isSet(path)) {

            Pattern p = Pattern.compile(pattern);

            Matcher matcher = p.matcher(path);
            if (matcher.find()) {
                return matcher.group();
            }

        }

        return "#";

    }



    private class Tabs extends ArrayList<TabRow> {}

    private class TabRow extends ArrayList<TabCell> {}

    private class TabCell implements Serializable {

        private String href;
        private String name;
        private String path;
        private boolean renderLink;

        public TabCell(String name, String path, String href, boolean renderLink) {
            this.name = name;
            this.path = path;
            this.href = href;
            this.renderLink = renderLink;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }

        public String getHref() {
            return href;
        }
        
        public boolean isRenderLink() {
        	return renderLink;
        }
    }

    public String getTabPath() {
        return tabPath;
    }

    public void setTabPath(String tabPath) {
        this.tabPath = tabPath;
    }
}