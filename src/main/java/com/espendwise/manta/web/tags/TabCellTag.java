package com.espendwise.manta.web.tags;


import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class TabCellTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(TabCellTag.class);

    private String name;
    private String path;
    private String href;
    private boolean renderLink = true;

    private TabRowTag ancestor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isRenderLink() {
		return renderLink;
	}

	public void setRenderLink(boolean renderLink) {
		this.renderLink = renderLink;
	}

	@Override
    public int doStartTag() throws JspException {

        TabRowTag ancestorTag = (TabRowTag) findAncestorWithClass(this, TabRowTag.class);

        logger.debug("doStartTag()=> ancestorTag: " + ancestorTag);

        if (ancestorTag == null) {
            String message = "Tag must be nested within a TabRowTag tag.";
            logger.error(message, new Exception(message));
            return (SKIP_PAGE);
        }

        setAncestor(ancestorTag);

        return TagSupport.EVAL_PAGE;
    }

    @Override
    public int doEndTag() throws JspException {

        try {

            return super.doEndTag();

        } catch (Exception e) {     //ignore
            logger.error("doEndTag()=> " + e.getMessage(), e);
        } finally {
            getAncestor().getAncestor().addCell(getName(), getPath(), getHref(), isRenderLink());
        }

        return TagSupport.EVAL_PAGE;

    }

    public void setAncestor(TabRowTag ancestor) {
        this.ancestor = ancestor;
    }

    public TabRowTag getAncestor() {
        return ancestor;
    }

    @Override
    public void release() {
        super.release();
        this.ancestor = null;
        this.name = null;
        this.path = null;
        this.href = null;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }

}
