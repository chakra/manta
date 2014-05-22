package com.espendwise.manta.auth;


import java.io.Serializable;

public class AuthDataSourceIdent implements Serializable {

    public static final String JDBC_URL = "jdbcUrl";

    private String dataSourceName;
    private String jdbcUrl;


    public AuthDataSourceIdent(String dataSourceName, String jdbcUrl) {
        this.dataSourceName = dataSourceName;
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.dataSourceName = datasourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthDataSourceIdent)) return false;

        AuthDataSourceIdent that = (AuthDataSourceIdent) o;

        if (dataSourceName != null ? !dataSourceName.equals(that.dataSourceName) : that.dataSourceName != null)
            return false;
        if (jdbcUrl != null ? !jdbcUrl.equals(that.jdbcUrl) : that.jdbcUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dataSourceName != null ? dataSourceName.hashCode() : 0;
        result = 31 * result + (jdbcUrl != null ? jdbcUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthDataSourceIdent{" +
                "dataSourceName='" + dataSourceName + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                '}';
    }
}
