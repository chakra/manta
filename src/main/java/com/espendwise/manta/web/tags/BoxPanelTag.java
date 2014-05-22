package com.espendwise.manta.web.tags;


import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.AppI18nUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class BoxPanelTag extends BodyTagSupport{
    
    public String name;
    public String cssClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public int doEndTag() throws JspException {

        String body = bodyContent.getString();

        String s = "  <div class=\"box "+ Utility.strNN(getCssClass())+"\">\n" +
                "                            <div class=\"boxTop\">\n" +
                "                                <div class=\"topWrapper\"><span class=\"left\">&nbsp;</span>\n" +
                "                                    <span class=\"center\" style=\"\">"+(getName() != null?
                "<span class=\"boxName\">"+ AppI18nUtil.getMessageOrDefault(getName(), getName())+"</span></span>\n":"") +
                "                                    <span class=\"right\">&nbsp;</span>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                            <div class=\"boxMiddle\">\n" +
                "                                <div class=\"middleWrapper\">\n" +
                "                                    <span class=\"left\">&nbsp;</span>\n" +
                "                                    <div class=\"boxContent\">\n"+
                "                                    " + body +
                "                                    </div>\n" +
                "                                    <span class=\"right\">&nbsp;</span></div>\n" +
                "                            </div>\n" +
                "                            <div class=\"boxBottom\">\n" +
                "                                <div class=\"bottomWrapper\"><span class=\"left\">&nbsp;</span>\n" +
                "                                    <span class=\"center\">&nbsp;</span>\n" +
                "                                    <span class=\"right\">&nbsp;</span>\n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                        </div>";

        try {

            pageContext.getOut().write(s);

        } catch (IOException e) { //ignore

        }

        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        this.name = null;
    }
}
