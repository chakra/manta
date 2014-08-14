package com.espendwise.manta.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.espendwise.manta.dao.EmailDAO;
import com.espendwise.manta.dao.EmailDAOImpl;
import com.espendwise.manta.dao.EventDAO;
import com.espendwise.manta.dao.EventDAOImpl;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.view.BatchOrderView;
import com.espendwise.manta.model.view.CatalogManagerView;
import com.espendwise.manta.model.data.EventPropertyData;
import com.espendwise.manta.util.Constants;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

@Service
public class EventServiceImpl extends DataAccessService implements EventService {
	private static final Logger logger = Logger.getLogger(EventServiceImpl.class);
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String MESSAGE_CONTENTS = "MESSAGE_CONTENTS";
	public static final String messageTemplate = getMessageTemplate();
	
	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)   
    public BatchOrderView saveBatchOrder(BatchOrderView batchOrderView, Locale locale) throws Exception {
		EntityManager entityManager = getEntityManager();        

		EventDAO eventDao = new EventDAOImpl(entityManager);
		batchOrderView = eventDao.saveBatchOrder(batchOrderView);
		
		EmailDAO emailDao = new EmailDAOImpl(entityManager);
		String storeName = batchOrderView.getStoreName();
		SimpleDateFormat dateFormat = new SimpleDateFormat(I18nUtil.getDatePattern() + " " + I18nUtil.getTimePattern());
		String sendTime = dateFormat.format(new Date());				
		
		String toEmailAddress = "orderline.notifications@veritivcorp.com";
		String subject = "BATCH ORDER REQUEST for " + storeName + " - Sent " + sendTime;
		StringBuffer automatedMessage = new StringBuffer();
		automatedMessage.append("This is an automated email.  Do not reply to this email.")
		.append(LINE_SEPARATOR)
		.append(LINE_SEPARATOR)
		.append("This note is to inform you that " + storeName + " has uploaded a batch order file for processing.")
		.append(LINE_SEPARATOR)
		.append(LINE_SEPARATOR)
		.append("Event ID:  " + batchOrderView.getEventId()).append(LINE_SEPARATOR)
		.append("Primary Entity ID:  " + batchOrderView.getStoreId()).append(LINE_SEPARATOR)
		.append("Primary Entity Name:  " + storeName).append(LINE_SEPARATOR)
		.append("File Name:  " + batchOrderView.getFileName()).append(LINE_SEPARATOR)
		.append("Order Count:  " + batchOrderView.getOrderCount()).append(LINE_SEPARATOR)
		.append("Apply to Budget:  " + batchOrderView.getApplyToBudget()).append(LINE_SEPARATOR)
		.append("Send Confirmation:  " + batchOrderView.getSendConfirmation()).append(LINE_SEPARATOR)
		.append("Process On:  " + batchOrderView.getProcessOn()).append(LINE_SEPARATOR)
		.append("Process When:  " + batchOrderView.getProcessWhen()).append(LINE_SEPARATOR);
		
		String message = new String(messageTemplate);
		message = message.replaceFirst(MESSAGE_CONTENTS, automatedMessage.toString());								
		logger.info("Send email to " + toEmailAddress + ": " + subject);
				
		emailDao.send(toEmailAddress, "noreply@orderline.xpedx.com", subject, message, Constants.EMAIL_FORMAT_PLAIN_TEXT);
		
		return batchOrderView;
    }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)   
	public void cancellEvent(List<Long> selected) throws Exception {
		EntityManager entityManager = getEntityManager();
		EventDAO eventDao = new EventDAOImpl(entityManager);
		eventDao.cancellEvent(selected);		
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)   
	public List<EventPropertyData> getEventProperties(List<Long> selected, List<String> propNames) throws Exception {
		EntityManager entityManager = getEntityManager();
		EventDAO eventDao = new EventDAOImpl(entityManager);
		return eventDao.getEventProperties(selected, propNames);		
	}

	@Override
	public List<BatchOrderView> getBatchOrders(Long storeId, String statusCd) throws Exception {
		EntityManager entityManager = getEntityManager();
		EventDAO eventDao = new EventDAOImpl(entityManager);
		return eventDao.getBatchOrders(storeId, statusCd);		
	}
	
	private static String getMessageTemplate() {
		StringBuffer automatedMessage = new StringBuffer();
		automatedMessage.append("*************************************************************************************")
		.append(LINE_SEPARATOR)
		.append(LINE_SEPARATOR)
		.append(MESSAGE_CONTENTS)
		.append(LINE_SEPARATOR)
		.append(LINE_SEPARATOR)
		.append("Thank you.")
		.append(LINE_SEPARATOR)
		.append(LINE_SEPARATOR)		
		.append("*************************************************************************************");
		return automatedMessage.toString();
	}

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)   
    public CatalogManagerView saveCatalog(CatalogManagerView catalogView, Locale locale, String eventStatus) throws Exception {
		EntityManager entityManager = getEntityManager();        

		EventDAO eventDao = new EventDAOImpl(entityManager);
		catalogView = eventDao.saveCatalog(catalogView, eventStatus);
		
		EmailDAO emailDao = new EmailDAOImpl(entityManager);
		String storeName = catalogView.getStoreName();
		SimpleDateFormat dateFormat = new SimpleDateFormat(I18nUtil.getDatePattern() + " " + I18nUtil.getTimePattern());
		String sendTime = dateFormat.format(new Date());				
		
		String toEmailAddress = "orderline.notifications@veritivcorp.com";
		String subject = "CATALOG REQUEST for " + storeName + " - Sent " + sendTime;
		StringBuffer automatedMessage = new StringBuffer();
		automatedMessage.append("This is an automated email.  Do not reply to this email.")
		.append(LINE_SEPARATOR)
		.append(LINE_SEPARATOR)
		.append("This note is to inform you that " + storeName + " has uploaded a catalog file for processing.")
		.append(LINE_SEPARATOR)
		.append(LINE_SEPARATOR)
		.append("Event ID:  " + catalogView.getEventId()).append(LINE_SEPARATOR)
		.append("Primary Entity ID:  " + catalogView.getStoreId()).append(LINE_SEPARATOR)
		.append("Primary Entity Name:  " + storeName).append(LINE_SEPARATOR)
		.append("File Name:  " + catalogView.getFileName()).append(LINE_SEPARATOR)
		.append("Catalog Count:  " + catalogView.getCatalogCount()).append(LINE_SEPARATOR)
		.append("Effective Date:  " + catalogView.getEffectiveDate()).append(LINE_SEPARATOR)
		.append("Time Zone:  " + catalogView.getTimeZone()).append(LINE_SEPARATOR);
		
		String message = new String(messageTemplate);
		message = message.replaceFirst(MESSAGE_CONTENTS, automatedMessage.toString());								
		logger.info("Send email to " + toEmailAddress + ": " + subject);
				
		emailDao.send(toEmailAddress, "noreply@orderline.xpedx.com", subject, message, Constants.EMAIL_FORMAT_PLAIN_TEXT);
		
		return catalogView;
    }
	
	@Override
	public List<CatalogManagerView> getCatalogs(Long storeId, String statusCd) throws Exception {
		EntityManager entityManager = getEntityManager();
		EventDAO eventDao = new EventDAOImpl(entityManager);
		return eventDao.getCatalogs(storeId, statusCd);		
	}
    
}
