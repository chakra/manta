package com.espendwise.manta.web.tags;


import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.ClassField;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.AppI18nUtil;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmailObjectHelpTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(EmailObjectHelpTag.class);

    private static final String STYLE_TOP = "top";
    private static final String STYLE_FIRST = "first";

    private static final String TEMPLATE = "<table id=\"helpboard\" class=\"helpboard-root\">%1$s</table>";
    private static final String SUB_TEMPLATE = "%1$s";
    private static final String ITEM_TEMPLATE = "\n<tr><td class=\"%1$s helpTarget\">%2$s</td><td  class=\"helpSeparator\">-</td><td class=\"helpText\">%3$s</td></tr>%4$s";

    public String target;
    public Class targetClass;

    private static final Comparator<? super ClassField> CMP = new Comparator<ClassField>() {
        public int compare(ClassField o1, ClassField o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    private static final String MESSAGE_KEY_PREFIX = "admin.template.email.emailObject";

    @Override
    public int doStartTag() throws JspException {

        setTargetClass(null);

        List<Class> metaObjects = AppResourceHolder
                .getAppResource()
                .getDbConstantsResource()
                .getEmailObjects();

        for (Class object : metaObjects) {
            if (object.getName().equalsIgnoreCase(target)) {
                setTargetClass(object);
            }
        }

        return TagSupport.EVAL_PAGE;
    }

    @Override
    public int doEndTag() throws JspException {

        if (targetClass != null) {

            List<ClassField> fields = Utility.findAllGetterFieldsNames(getTargetClass());

            if (Utility.isSet(fields)) {
                StringBuilder linesBuilder = new StringBuilder();
                buildItems(linesBuilder, fields, null);
                write(String.format(TEMPLATE, linesBuilder));
            }

        }

        return super.doEndTag();
    }

    private void buildItems(StringBuilder buffer, List<ClassField> items, String path) {

        if (Utility.isSet(items)) {

            int i = 0;

            Collections.sort(items, CMP);

            for (ClassField item : items) {

                boolean hasChilds = Utility.isSet(item.getInnerFields());

                StringBuilder sb = new StringBuilder();

                String field = Utility.javaBeanPath(path, item.getName());
                String key = Utility.javaBeanPath(MESSAGE_KEY_PREFIX, getTargetClass().getSimpleName(), field);
                String itemHelp = AppI18nUtil.getMessageOrDefault(
                        key,
                        field
                );

                if (hasChilds) {
                    buildItems(sb, item.getInnerFields(), field);
                }


                String itemStyle = path == null ? i == 0 ? STYLE_FIRST : STYLE_TOP : Constants.EMPTY;


                buffer.append(
                        String.format(ITEM_TEMPLATE,
                                itemStyle,
                                field,
                                itemHelp,
                                hasChilds ? String.format(SUB_TEMPLATE,  sb) : Constants.EMPTY
                        )
                );

                i++;

            }
        }

    }


    private void write(String helpString) {
        try {
            pageContext.getOut().write(helpString);
        } catch (IOException e) {  //ignore

        }
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public void release() {
        super.release();
        this.target = null;
        this.targetClass = null;
    }
}
