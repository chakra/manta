package com.espendwise.manta.web.tags;


import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.Auth;
import com.espendwise.manta.util.Utility;


/**
 * Check that user has a specified role (or one of a list of roles).
 */

public class HasRole extends TagSupport {

    private final static Log log = LogFactory.getLog(HasRole.class);

    /**
     * Holds value of single role.
     */
    private String role;

    /**
     * Holds list of role name.
     */
    private List<String> roles;

    // ------------------------------------------------------- Public Methods


    /**
     * Checks if the currently logged in user has a specified role.  If there is no
     * user logged in the body will not be rendered.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        if ((!Utility.isSet(getRole()) && !Utility.isSet(getRoles())) || pageContext == null || pageContext.getSession() == null) {
            return SKIP_BODY;
        }

        AppUser appUser = Auth.getAppUser();
        if (appUser == null) {
            return SKIP_BODY;
        }

        String userRole = Utility.strNN(Auth.getAppUser().getUserTypeCd());
        if (Utility.isSet(getRole())) {
        	if (userRole.equalsIgnoreCase(getRole())) {
        		return EVAL_BODY_INCLUDE;
        	}
        }
        if (Utility.isSet(getRoles())) {
        	Iterator<String> roleIterator = getRoles().iterator();
        	while (roleIterator.hasNext()) {
        		String role = roleIterator.next();
            	if (userRole.equalsIgnoreCase(role)) {
            		return EVAL_BODY_INCLUDE;
            	}
        	}
        }

        return SKIP_BODY;
    }


    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
    }


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public List<String> getRoles() {
		return roles;
	}


	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
