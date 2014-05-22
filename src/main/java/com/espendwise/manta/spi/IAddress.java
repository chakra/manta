package com.espendwise.manta.spi;


import java.io.Serializable;

public interface IAddress extends Serializable {

    public static final String ADDRESS1 = "address1";
    public static final String ADDRESS2 = "address2";
    public static final String ADDRESS3 = "address3";
    public static final String ADDRESS4 = "address4";
    public static final String CITY = "city";
    public static final String STATE_PROVINCE_CD = "stateProvinceCd";
    public static final String COUNTRY_CD = "countryCd";
    public static final String COUNTY_CD = "countyCd";
    public static final String POSTAL_CODE = "postalCode";

    public String getAddress1();

    public String getAddress2();

    public String getAddress3();

    public String getAddress4();

    public String getCity();

    public String getStateProvinceCd();

    public String getCountryCd();

    public String getCountyCd();

    public String getPostalCode();


}
