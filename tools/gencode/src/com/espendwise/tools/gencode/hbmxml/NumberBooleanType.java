package com.espendwise.tools.gencode.hbmxml;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import org.hibernate.*;
import org.hibernate.usertype.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.io.Serializable;

public class NumberBooleanType implements UserType {
    public NumberBooleanType() {
    }
    private static final int[] SQL_TYPES = {Types.INTEGER};

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    private Class targetClass;

    public void setParameterValues(Properties parameters) {
        String targetClassName = parameters.getProperty("targetClass");
        try {
            targetClass = Class.forName(targetClassName);
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Class " + targetClassName + " not found ", e);
        }
    }

    public Class returnedClass() {
        return targetClass;
    }

    public boolean isMutable() {
        return false;
    }

    public Object deepCopy(Object value) {
        return value;
    }

    public Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) {
        return original;
    }

    public boolean equals(Object x, Object y) {
        if (x == y) return true;
        if (x == null || y == null) return false;
        return x.equals(y);
    }

    public int hashCode(Object x) {
        return x.hashCode();
    }

    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws SQLException {
        int value = resultSet.getInt(names[0]);
        if (1 == value) return true;
        else return false;
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, Types.INTEGER);
        } else {
            if((Boolean)value) {
                statement.setInt(index, 1);
            } else {
                statement.setInt(index, 0);
            }
        }
    }
}




