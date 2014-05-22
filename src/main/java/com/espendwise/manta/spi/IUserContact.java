package com.espendwise.manta.spi;


public interface IUserContact {

    public static final String ESCALATION_EMAIL = "escalationEmail";
    public static final String TEXTING_ADDRESS = "textingAddress";

    public IContact getContact();
    
    public String getEscalationEmail();

    public String getTextingAddress();

}
