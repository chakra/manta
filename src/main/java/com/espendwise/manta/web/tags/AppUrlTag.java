package com.espendwise.manta.web.tags;


import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.controllers.UrlPathKey;
import com.espendwise.manta.web.util.UrlPathAssistent;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.tag.rt.core.UrlTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class AppUrlTag extends UrlTag {

    private static final Logger logger = Logger.getLogger(AppUrlTag.class);

    @Override
    public int doStartTag() throws JspException {
        if (!Utility.isSet(this.value)) {
            setValue(this.value);
        }
        return super.doStartTag();
    }

    @Override
    public void setValue(String value) throws JspTagException {
        String setV = Utility.strNN(value);
        if (!setV.startsWith(UrlPathKey.INSTANCE_PREFIX)) {
            String basePath = UrlPathAssistent.basePath();
            setV = basePath + (setV.trim().length() == 0 || setV.startsWith("/") ? setV : "/" + setV);
        }
        super.setValue(setV);
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            return super.doEndTag();
        } finally {
            this.value = null;
        }
    }

    @Override
    public void release() {
        super.release();
        this.value = null;
    }
}
