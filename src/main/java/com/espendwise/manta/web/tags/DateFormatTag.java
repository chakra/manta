package com.espendwise.manta.web.tags;


import com.espendwise.manta.util.arguments.ArgumentType;

import javax.servlet.jsp.JspException;

public class DateFormatTag extends AppFormatTag{

    @Override
    public int doStartTag() throws JspException {
        setType(ArgumentType.DATE);
        return super.doStartTag();
    }
}
