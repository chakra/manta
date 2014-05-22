package com.espendwise.manta.web.tags;


import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.StringWriter;

public class WebContentTag extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(WebContentTag.class);

    public static final String WEB_ERROR_WRITER = WebContentTag.class.getName() + "." + "webErrorWкiter";

    @Override
    public int doStartTag() throws JspException {

        logger.info("doStartTag()=> BEGIN");

        pageContext.setAttribute(WEB_ERROR_WRITER, new StringWriter(), PageContext.REQUEST_SCOPE);

        logger.info("doStartTag()=> END");

        return BodyTagSupport.EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {

        StringWriter webErrorWriter = null;

        try {

            webErrorWriter = (StringWriter) pageContext.findAttribute(WEB_ERROR_WRITER);

            JspWriter out = pageContext.getOut();

            logger.info("doEndTag()=> write errors to output stream ... ");
            out.append(webErrorWriter.getBuffer());

            logger.info("doEndTag()=> write body to output stream ... ");
            getBodyContent().writeOut(out);


        } catch (IOException e) {

            throw new JspException(e);

        } finally {
            try {
                if (webErrorWriter != null) {
                    webErrorWriter.close();
                }
            } catch (IOException e) {  //ignore
            }
        }

        logger.info("doEndTag()=> END.OK,");

        return TagSupport.EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        pageContext.removeAttribute(WEB_ERROR_WRITER);
    }
}
