package com.espendwise.manta.web.tags;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuItemTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(MenuItemTag.class);

    private String name;
    private String href;
    private String path;

    private List<MenuItem> childItems;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<MenuItem> getChildItems() {
        return childItems;
    }

    public void setChildItems(List<MenuItem> childItems) {
        this.childItems = childItems;
    }


    @Override
    public int doStartTag() throws JspException {

        childItems = new ArrayList<MenuItem>();

        AppUser appUser = (AppUser) pageContext.findAttribute(Constants.APP_USER_JSP_SCOPE);
        AppStoreContext storeContext = (AppStoreContext) pageContext.findAttribute(Constants.STORE_CTX_JSP_SCOPE);

        MenuItemTag ancestor = (MenuItemTag) findAncestorWithClass(this, MenuItemTag.class);

        logger.debug("doStartTag()=> ancestor: " + ancestor);

        if (ancestor != null) {

            String i18Name = I18nUtil.getMessage(appUser,
                    getName(),
                    null,
                    null,
                    true
            );

            MenuItem item = new MenuItem(getName(), i18Name == null ? getName() : i18Name, getHref(), getPath(), getChildItems());
            ancestor.getChildItems().add(item);

        } else {

            MenuTag parentAncestor = (MenuTag) findAncestorWithClass(this, MenuTag.class);
            if (parentAncestor == null) {
                String message = "Tag must be nested within a MenuTag tag or  MenuItemTag parent";
                logger.error(message, new Exception(message));
                return (SKIP_PAGE);
            }

            String i18Name = I18nUtil.getMessage(appUser,
                    getName(),
                    null,
                    null,
                    true
            );

            MenuItem item = new MenuItem(getName(), i18Name == null ? getName() : i18Name, getHref(), getPath(), getChildItems());
            parentAncestor.getItems().add(item);
        }


        return TagSupport.EVAL_PAGE;
    }


    @Override
    public void release() {
        super.release();
        name = null;
        href = null;
        path = null;
        childItems = null;
    }

    public static class MenuItem {

        public static final Comparator<? super MenuItem> I18N_COMPARATOR = new Comparator<MenuItem>() {
            public int compare(MenuItem o1, MenuItem o2) {
                return Utility.strNN(o1.getI18nName()).toLowerCase().compareTo(Utility.strNN(o2.getI18nName()).toLowerCase());
            }
        };

        private String name;
        private String i18nName;
        private String href;
        private String path;
        private List<MenuItem> childItems = null;

        private MenuItem(String name, String i18nName, String href, String path, List<MenuItem> childItems) {
            this.name = name;
            this.i18nName = i18nName;
            this.href = href;
            this.path = path;
            this.childItems = childItems;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<MenuItem> getChildItems() {
            return childItems;
        }

        public String getI18nName() {
            return i18nName;
        }

        public void setI18nName(String i18nName) {
            this.i18nName = i18nName;
        }
    }
}
                               