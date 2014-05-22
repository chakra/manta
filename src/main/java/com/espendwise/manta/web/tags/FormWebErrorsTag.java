package com.espendwise.manta.web.tags;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.tags.form.ErrorsTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.tagext.Tag;
import java.io.Writer;


public class FormWebErrorsTag extends ErrorsTag  implements Tag{

    private static final Logger logger = Logger.getLogger(FormWebErrorsTag.class);

    @Override
    protected TagWriter createTagWriter() {
        logger.info("createTagWriter()=> create application error tag witer");
        TagWriter writer = new TagWriter((Writer) pageContext.findAttribute(WebContentTag.WEB_ERROR_WRITER));
        logger.info("createTagWriter()=> writer: " + writer);
        return writer;
    }



}

