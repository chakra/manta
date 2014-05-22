package com.espendwise.manta.auth;


import java.io.Serializable;
import java.util.List;

public class AuthDatabaseAccessUnit implements Serializable {

    private boolean alive;
    private String unitName;
    private List<AuthMainStoreIdent> mainStoreIdents;
    private AuthDataSourceIdent authDataSourceIdent;

    public AuthDatabaseAccessUnit(String unitName, AuthDataSourceIdent authDataSourceIdent, boolean alive, List<AuthMainStoreIdent> mainStoreIdents) {
        this.unitName = unitName;
        this.authDataSourceIdent = authDataSourceIdent;
        this.alive = alive;
        this.mainStoreIdents = mainStoreIdents;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public List<AuthMainStoreIdent> getMainStoreIdents() {
        return mainStoreIdents;
    }

    public void setMainStoreIdents(List<AuthMainStoreIdent> mainStoreIdents) {
        this.mainStoreIdents = mainStoreIdents;
    }

    public AuthDataSourceIdent getAuthDataSourceIdent() {
        return authDataSourceIdent;
    }

    public void setAuthDataSourceIdent(AuthDataSourceIdent authDataSourceIdent) {
        this.authDataSourceIdent = authDataSourceIdent;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthDatabaseAccessUnit)) return false;

        AuthDatabaseAccessUnit that = (AuthDatabaseAccessUnit) o;

        if (authDataSourceIdent != null ? !authDataSourceIdent.equals(that.authDataSourceIdent) : that.authDataSourceIdent != null)
            return false;
        if (unitName != null ? !unitName.equals(that.unitName) : that.unitName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = unitName != null ? unitName.hashCode() : 0;
        result = 31 * result + (authDataSourceIdent != null ? authDataSourceIdent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthDatabaseAccessUnit{" +
                "alive=" + alive +
                ", unitName='" + unitName + '\'' +
                ", mainStoreIdents=" + mainStoreIdents +
                ", authDataSourceIdent=" + authDataSourceIdent +
                '}';
    }
}
