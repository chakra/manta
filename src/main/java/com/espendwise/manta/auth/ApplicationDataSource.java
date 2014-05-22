package com.espendwise.manta.auth;

import java.io.Serializable;

public class ApplicationDataSource  implements Serializable {

    private AuthDataSourceIdent dataSourceIdent;

    public ApplicationDataSource() {
        dataSourceIdent = new AuthDataSourceIdent(null, null);
    }

    public ApplicationDataSource( AuthDataSourceIdent applicationDataSourceIdent) {
       this.dataSourceIdent = applicationDataSourceIdent;
    }

    public  AuthDataSourceIdent getDataSourceIdent() {
        return dataSourceIdent;
    }

    public void setDataSourceIdent(AuthDataSourceIdent dataSourceIdent) {
        this.dataSourceIdent = dataSourceIdent;
    }
}
