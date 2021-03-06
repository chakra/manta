package com.espendwise.manta.model.view;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
/**
 * UiOptionView generated by hbm2java
*/
public class UiOptionView extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String LOGO = "logo";
    public static final String TITLE = "title";
    public static final String FOOTER = "footer";

    private String logo;
    private String title;
    private String footer;

    public UiOptionView() {
    }
	
    public UiOptionView(String logo) {
        this.setLogo(logo);
    }

    public UiOptionView(String logo, String title, String footer) {
        this.setLogo(logo);
        this.setTitle(title);
        this.setFooter(footer);
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
        setDirty(true);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        setDirty(true);
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
        setDirty(true);
    }




}


