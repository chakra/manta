package com.espendwise.manta.web.tags;

import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.web.util.AppI18nUtil;
import org.apache.taglibs.standard.tag.rt.core.ForEachTag;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class I18nRefCodesTag extends ForEachTag {

    public String i18nprefix;

    public String getI18nprefix() {
        return i18nprefix;
    }

    public void setI18nprefix(String i18nprefix) {
        this.i18nprefix = i18nprefix;
    }

    @Override
    public int doStartTag() throws JspException {

        List<Pair<String, String>> i18nList = new ArrayList<Pair<String, String>>();

        List itemList = (List) rawItems;

        for (Object o : itemList) {

            Pair x = (Pair) o;

            i18nList.add(
                    new Pair<String, String>(
                            AppI18nUtil.getMessageOrDefault(Utility.strNN(getI18nprefix()) + x.getObject1(), String.valueOf(x.getObject1())),
                            String.valueOf(x.getObject2())
                    )
            );

        }

        setItems(
            sort(i18nList)
        );

        return super.doStartTag();
    }

    private Object sort(List<Pair<String, String>> itemList) {
        Collections.sort(itemList, AppComparator.PAIR_OBJ1_STR_COMPARATOR);
        return itemList;
    }

    @Override
    public void release() {
        super.release();
        this.i18nprefix = null;
    }
}
