package com.espendwise.manta.support.spring;


import org.apache.commons.dbcp.BasicDataSource;

import java.sql.SQLException;

public class EmptyDataSource extends BasicDataSource {


    protected EmptyDataSource() {
        super();
    }

    public void  setCheckoutTimeout(String value){

    }

    public void  setDriverClass(String value) {

    }

    public void  setJdbcUrl(String value){

    }

    public void  setMaxIdleTime(String value){

    }

    public void  setUser(String value) {

    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
