package com.espendwise.manta.web.tags;


import com.espendwise.manta.util.RefCodeNamesKeys;
import com.espendwise.manta.util.Utility;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class RefCdMapTag  extends TagSupport {

    private String code;
    private String var;
    private String invert;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInvert() {
        return invert;
    }

    public void setInvert(String invert) {
        this.invert = invert;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }


    @Override
    public int doEndTag() throws JspException {

        if (Utility.isSet(getVar())) {
            pageContext.setAttribute(
                    getVar(),
                    RefCodeNamesKeys.toMap(RefCodeNamesKeys.getRefCodeValues(getCode()), Utility.isTrue(getInvert()))
            );
        }

        return super.doEndTag();
    }

    @Override
    public void release() {
        super.release();
        this.var = null;
        this.code = null;
        this.invert = null;
    }
}
