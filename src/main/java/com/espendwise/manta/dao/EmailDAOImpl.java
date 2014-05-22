package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.EmailData;
import com.espendwise.manta.model.data.EventEmailData;
import com.espendwise.manta.util.FileAttach;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EmailDAOImpl extends DAOImpl implements EmailDAO {

    public EmailDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public EmailData findByEmailId(Long emailId) {
        return em.find(EmailData.class, emailId);
    }

    @Override
    public EmailData create(EmailData emailData) {
        return super.create(emailData);
    }

    @Override
    public EmailData update(EmailData emailData) {
        return super.update(emailData);
    }

    @Override
    public EmailData updateEntityAddress(Long busEntityId, EmailData emailData) {

        if (emailData != null && Utility.longNN(busEntityId) > 0) {

            emailData.setBusEntityId(busEntityId);

            if (emailData.getEmailId() == null) {
                emailData = super.create(emailData);
            } else {
                emailData = super.update(emailData);
            }

        }

        return emailData;

    }
    
    @Override
    public EmailData updateUserAddress(Long userId, EmailData emailData) {

        if (emailData != null && Utility.longNN(userId) > 0) {

            emailData.setUserId(userId);

            if (emailData.getEmailId() == null) {
                emailData = super.create(emailData);
            } else {
                emailData = super.update(emailData);
            }

        }

        return emailData;

    }
    
    @Override
    public EmailData updateUserEmail(Long userId, EmailData emailData) {
        if (emailData != null && Utility.longNN(userId) > 0) {
            
            if (emailData.getEmailId() != null && emailData.getEmailAddress() == null) {
                EmailData presentEmail = findByEmailId(emailData.getEmailId());
                em.remove(presentEmail);
                
                return null;
            }

            emailData.setUserId(userId);

            if (emailData.getEmailId() == null) {
                emailData = super.create(emailData);
            } else {
                emailData = super.update(emailData);
            }

        }

        return emailData;
    }
    
    /**
     * Checks if a given email has already been sent to the supplied email address.
     * @param pSubject subject to check for, subject should be assumed to be unique to a particular event (i.e. have an order number in it)
     * @param pToEmailAddress The to address to check for
     * @return true if the email was sent.
     */    
    @Override
    public boolean wasThisEmailSent(String pSubject, String pToEmailAddress) {
    	String toAddress = (pToEmailAddress!= null && pToEmailAddress.length()>255)?
                pToEmailAddress.substring(0,255):pToEmailAddress;
        if ( toAddress == null || toAddress.length() == 0 ) {
        	toAddress = "none";
        }
        String subject = (pSubject!=null && pSubject.length()>255)?
        		pSubject.substring(0,255):pSubject;

		Query query = em.createQuery("Select email.emailId from EmailData email " +
				"where email.toAddress = (:toAddress) " +
				"and email.subject = (:subject)" );

		query.setParameter("toAddress", toAddress);
		query.setParameter("subject", subject);
				
		Long emailId = (Long) query.getSingleResult();		
		if ( emailId != null){
			return false;
		}
		return true;
    }
    
    /**
     *  Send the email message
     *
     *@param  pToEmailAddress
     *@param  pSubject
     *@param  pMsg
     *@param  pMsgFormat
     */
    @Override
    public void send(String pToEmailAddress,
            String pFromEmailAddress,
            String pSubject,
            String pMsg,
            String pMsgFormat) throws Exception{
    	send(pToEmailAddress, pFromEmailAddress, null,pSubject,pMsg,pMsgFormat);
    	
    }
    @Override
    public void send(String pToEmailAddress,
            String pFromEmailAddress,
            String pCcEmailAddress,
            String pSubject,
            String pMsg,
            String pMsgFormat) throws Exception{

        send(pToEmailAddress, pFromEmailAddress, pCcEmailAddress, pSubject,pMsg,pMsgFormat, null);
    }
    
    /**
     *  Send the email message
     *
     *@param  pToEmailAddress
     *@param  pFromEmailAddress
     *@param  pCcEmailAddress
     *@param  pSubject
     *@param  pMsg
     *@param  pMsgFormat
     */
    @Override
    public void send(String pToEmailAddress,
            String pFromEmailAddress,
            String pCcEmailAddress,
            String pSubject,
            String pMsg,
            String pMsgFormat,
            FileAttach[] pAttachments) throws Exception
    {
           
    	EventEmailData eventEmailData = new EventEmailData();
    	String toAddress = (pToEmailAddress!= null && pToEmailAddress.length()>255)?
                      pToEmailAddress.substring(0,255):pToEmailAddress;
	    if ( toAddress == null || toAddress.length() == 0 ) {
	    	toAddress = "none";
	    }
	    eventEmailData.setToAddress(toAddress);
	    String ccAddress = (pCcEmailAddress!= null && pCcEmailAddress.length()>255)?
	                      pCcEmailAddress.substring(0,255):pCcEmailAddress;
	    eventEmailData.setCcAddress(ccAddress);
	    eventEmailData.setFromAddress(pFromEmailAddress);
	    String subject = (pSubject!=null && pSubject.length()>255)?
	                    pSubject.substring(0,255):pSubject;
	    eventEmailData.setSubject(subject);
	    eventEmailData.setText(pMsg);
	    eventEmailData.setEmailStatusCd(RefCodeNames.EVENT_STATUS_CD.STATUS_READY);
	   
	    EventDAO eventDao = new EventDAOImpl(em);
	    eventDao.addEventEmail(eventEmailData, pAttachments);
    }
}
