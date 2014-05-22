package com.espendwise.tools.gencode.hbmxml;


import org.hibernate.HibernateException;

public class BinderException extends HibernateException {

    public BinderException(Throwable root) {
        super(root);
    }

    public BinderException(String string, Throwable root) {
        super(string, root);
    }

    public BinderException(String s) {
        super(s);
    }
}
