package com.espendwise.manta.web.tags;

import com.espendwise.manta.util.arguments.ArgumentType;

import javax.servlet.jsp.JspException;


public class CurrencyFormatTag extends AppFormatTag{
        @Override
        public int doStartTag() throws JspException {
            setType(ArgumentType.CURRENCY);
            return super.doStartTag();
        }
}
