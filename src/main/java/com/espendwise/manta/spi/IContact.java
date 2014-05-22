package com.espendwise.manta.spi;


public interface IContact {

    public static final String FIRST_NAME = "firsrName";
    public static final String LAST_NAME = "lastName";
    public static final String TELEPHONE = "telephone";
    public static final String MOBILE = "mobile";
    public static final String FAX = "fax";
    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";

    public String getFirstName();

    public String getLastName();

    public String getTelephone();

    public String getMobile();

    public String getFax();

    public String getEmail();

    public IAddress getAddress();


}
