package com.espendwise.manta.util;

import com.espendwise.manta.spi.ICommonProperty;

public class CommonProperty implements ICommonProperty{
   
    private String typeCd;
    private String shortDesc;
    private String value;
    private String status;
    private String locale;

    public CommonProperty() {
    }

    public CommonProperty(String typeCd, String shortDesc, String value, String status, String locale) {
        this.typeCd = typeCd;
        this.shortDesc = shortDesc;
        this.value = value;
        this.status = status;
        this.locale = locale;
    }

    public String getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }


    @Override
    public String toString() {
        return "CommonProperty{" +
                "typeCd='" + typeCd + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
