package com.espendwise.manta.model.view;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
/**
 * CountryView generated by hbm2java
*/
public class CountryView extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String COUNTRY_ID = "countryId";
    public static final String SHORT_DESC = "shortDesc";
    public static final String UI_NAME = "uiName";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String LOCALE_CD = "localeCd";
    public static final String INPUT_DATE_FORMAT = "inputDateFormat";
    public static final String INPUT_TIME_FORMAT = "inputTimeFormat";
    public static final String USES_STATE = "usesState";

    private Long countryId;
    private String shortDesc;
    private String uiName;
    private String countryCode;
    private String localeCd;
    private String inputDateFormat;
    private String inputTimeFormat;
    private String usesState;

    public CountryView() {
    }
	
    public CountryView(Long countryId) {
        this.setCountryId(countryId);
    }

    public CountryView(Long countryId, String shortDesc, String uiName, String countryCode, String localeCd, String inputDateFormat, String inputTimeFormat, String usesState) {
        this.setCountryId(countryId);
        this.setShortDesc(shortDesc);
        this.setUiName(uiName);
        this.setCountryCode(countryCode);
        this.setLocaleCd(localeCd);
        this.setInputDateFormat(inputDateFormat);
        this.setInputTimeFormat(inputTimeFormat);
        this.setUsesState(usesState);
    }

    public Long getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
        setDirty(true);
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
        setDirty(true);
    }

    public String getUiName() {
        return this.uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
        setDirty(true);
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        setDirty(true);
    }

    public String getLocaleCd() {
        return this.localeCd;
    }

    public void setLocaleCd(String localeCd) {
        this.localeCd = localeCd;
        setDirty(true);
    }

    public String getInputDateFormat() {
        return this.inputDateFormat;
    }

    public void setInputDateFormat(String inputDateFormat) {
        this.inputDateFormat = inputDateFormat;
        setDirty(true);
    }

    public String getInputTimeFormat() {
        return this.inputTimeFormat;
    }

    public void setInputTimeFormat(String inputTimeFormat) {
        this.inputTimeFormat = inputTimeFormat;
        setDirty(true);
    }

    public String getUsesState() {
        return this.usesState;
    }

    public void setUsesState(String usesState) {
        this.usesState = usesState;
        setDirty(true);
    }




}


