package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.EmailData;
import com.espendwise.manta.util.FileAttach;

public interface EmailDAO {

    public EmailData create(EmailData email);

    public EmailData update(EmailData emailData);

    public EmailData updateEntityAddress(Long busEntityId, EmailData emailData);
    
    public EmailData updateUserAddress(Long userId, EmailData emailData);
    
    public EmailData updateUserEmail(Long userId, EmailData emailData);
    
    public EmailData findByEmailId(Long emailId);
    
    public boolean wasThisEmailSent(String pSubject, String pToEmailAddress);
    
    public void send(String pToEmailAddress,
            String pFromEmailAddress,
            String pSubject,
            String pMsg,
            String pMsgFormat) throws Exception;
    public void send(String pToEmailAddress,
            String pFromEmailAddress,
            String pCcEmailAddress,
            String pSubject,
            String pMsg,
            String pMsgFormat) throws Exception;
    
    public void send(String pToEmailAddress,
            String pFromEmailAddress,
            String pCcEmailAddress,
            String pSubject,
            String pMsg,
            String pMsgFormat,
            FileAttach[] pAttachments) throws Exception;

}
