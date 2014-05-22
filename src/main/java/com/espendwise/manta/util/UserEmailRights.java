package com.espendwise.manta.util;


import com.espendwise.manta.model.data.UserData;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UserEmailRights extends HashMap<String, Boolean> {

    private static final Logger logger = Logger.getLogger(UserEmailRights.class);

    public UserEmailRights(String userRights) {
        super();
        for (UserEmailRightCodes code : UserEmailRightCodes.values()) {
            put(code.name(), userRights.contains(code.getTypeCode()));
        }
    }

    public UserData updateRole(UserData user) {

        String role = Utility.strNN(user.getUserRoleCd());

        logger.info("updateRole()> key.props.: "+this);
        logger.info("updateRole()> role: "+role);

        for (Map.Entry<String, Boolean> e : this.entrySet()) {
            logger.info("updateRole()> check entry : "+e);

            UserEmailRightCodes code = UserEmailRightCodes.valueOf(e.getKey());

            logger.info("updateRole()> code: "+code);

            if (code != null) {
                logger.info("updateRole()> ypeCode: "+code.getTypeCode());

                if (role.contains(code.getTypeCode())) {
                    if (!Utility.isTrue(e.getValue())) {
                        role = role.endsWith(code.getTypeCode()) ? role.replace(code.getTypeCode() + "^", "") : role.replace(code.getTypeCode() + "^", "");
                    }
                } else if (Utility.isTrue(e.getValue())) {
                    role = role.endsWith("^")
                            ? role + code.getTypeCode() + "^"
                            : role.length() == 0 ? code.getTypeCode() + "^" : role + "^" + code.getTypeCode() + "^";

                }

            }
        }

        user.setUserRoleCd(role);

        return user;
    }


}
