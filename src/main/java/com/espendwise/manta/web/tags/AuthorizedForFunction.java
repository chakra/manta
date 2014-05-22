package com.espendwise.manta.web.tags;


import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.Auth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

//import org.apache.struts.util.MessageResources;

/**
 * Check for a valid User logged on in the current session.  If there is no
 * such user, forward control to the logon page.
 */

public class AuthorizedForFunction extends TagSupport {

    private final static Log log = LogFactory.getLog(AuthorizedForFunction.class);

    /**
     * Holds value of property name.
     */
    private String name;

    // ------------------------------------------------------- Public Methods


    /**
     * Checks if the currently logged in user is authorized for the provided function.  If there is no
     * user logged in the body will not be rendered.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        if (getName() == null || pageContext == null || pageContext.getSession() == null) {
            return SKIP_BODY;
        }

        AppUser appUser = Auth.getAppUser();
        if (appUser == null) {
            return SKIP_BODY;
        }


        if (Auth.isAuthorizedForFunction(getName())) {
            return EVAL_BODY_INCLUDE;
        }

        return SKIP_BODY;
    }


    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

}
