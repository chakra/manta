package com.espendwise.manta.web.tags;


import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

public class TabRowTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(TabRowTag.class);

    private TabTag ancestor;

    @Override
    public int doStartTag() throws JspException {

        TabTag ancestorTag = (TabTag) findAncestorWithClass(this, TabTag.class);

        logger.debug("doStartTag()=> ancestorTag: " + ancestorTag);

        if (ancestorTag == null) {
            String message = "Tag must be nested within a TabTag tag.";
            logger.error(message, new Exception(message));
            return (SKIP_PAGE);
        }

        setAncestor(ancestorTag);
        getAncestor().addRow();

        return BodyTagSupport.EVAL_PAGE;
    }


    public void setAncestor(TabTag ancestor) {
        this.ancestor = ancestor;
    }

    public TabTag getAncestor() {
        return ancestor;
    }

    @Override
    public void release() {
        super.release();
        this.ancestor = null;
    }

}
