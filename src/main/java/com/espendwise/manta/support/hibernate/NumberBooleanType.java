package com.espendwise.manta.support.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class NumberBooleanType implements Serializable, UserType {

    public static final int[] SQL_TYPES = new int[]{Types.INTEGER};

    @Override
    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    @Override
    public Object deepCopy(Object obj) {
        return obj;
    }

    @Override
    public Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return x == y || !(x == null || y == null) && x.equals(y);
    }

    @Override
    public int hashCode(Object x) {
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
        return nullSafeGet(resultSet, names[0]);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
        nullSafeSet(preparedStatement, o, i);
    }


    @Override
    public Object replace(Object original, Object target, Object owner) {
        return original;
    }

    @Override
    public Class<Boolean> returnedClass() {
        return Boolean.class;
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Object nullSafeGet(ResultSet resultSet, String name) throws SQLException {
        return 1 == resultSet.getInt(name);
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, Types.INTEGER);
        } else {
            if ((Boolean) value) {
                statement.setInt(index, 1);
            } else {
                statement.setInt(index, 0);
            }
        }
    }


}





