package com.espendwise.manta.web.tags;


import com.espendwise.manta.util.RefCodeNamesKeys;
import com.espendwise.manta.util.Utility;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class RefCdTag  extends TagSupport {

    private String code;
    private String var;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
                    Utility.strNN(RefCodeNamesKeys.findRefCodeByValue(getCode(), getValue()), getValue())
            );
        }

        return super.doEndTag();
    }

    @Override
    public void release() {
        super.release();
        this.var = null;
        this.code = null;
        this.value = null;
    }
}
