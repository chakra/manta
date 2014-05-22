package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.IAddress;

import java.io.Serializable;

public class AddressInputForm implements ValidForm, IAddress, Serializable {

    private String address1 = null;
    private String address2 = null;
    private String address3 = null;
    private String address4 = null;
    private String city = null;
    private String countryCd = null;
    private String stateProvinceCd = null;
    private Boolean primaryInd = null;
    private String countyCd = null;
    private String postalCode = null;

    public AddressInputForm() {
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCd() {
        return countryCd;
    }

    public void setCountryCd(String countryCd) {
        this.countryCd = countryCd;
    }

    public String getStateProvinceCd() {
        return stateProvinceCd;
    }

    public void setStateProvinceCd(String stateProvinceCd) {
        this.stateProvinceCd = stateProvinceCd;
    }

    public Boolean getPrimaryInd() {
        return primaryInd;
    }

    public void setPrimaryInd(Boolean primaryInd) {
        this.primaryInd = primaryInd;
    }

    public String getCountyCd() {
        return countyCd;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    public void setCountyCd(String countyCd) {
        this.countyCd = countyCd;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "AddressInputForm{" +
                "address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", address4='" + address4 + '\'' +
                ", city='" + city + '\'' +
                ", countryCd='" + countryCd + '\'' +
                ", stateProvinceCd='" + stateProvinceCd + '\'' +
                ", primaryInd=" + primaryInd +
                ", countyCd='" + countyCd + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
