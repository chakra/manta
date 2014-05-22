package com.espendwise.manta.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.GregorianCalendar;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.EventData;
import com.espendwise.manta.model.data.EventEmailData;
import com.espendwise.manta.model.data.EventPropertyData;
import com.espendwise.manta.model.data.ProcessData;
import com.espendwise.manta.model.view.BatchOrderView;
import com.espendwise.manta.model.view.EventEmailView;
import com.espendwise.manta.model.view.CatalogManagerView;
import com.espendwise.manta.util.FileAttach;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.Constants;



@Repository
public class EventDAOImpl extends DAOImpl implements EventDAO {

    private static final Logger logger = Logger.getLogger(EventDAOImpl.class);
    public static final String TYPE_PROCESS             = "PROCESS";
    public static final String TYPE_EMAIL 				= "EMAIL";
    
    public static final String PROPERTY_PROCESS_TEMPLATE_ID = "PROCESS_TEMPLATE_ID";
    public static final String PROCESS_VARIABLE 		= "PROCESS_VARIABLE";
    public static final String STORAGE_DB 				= "DB";

	public EventDAOImpl() {
        this(null);
    }

    public EventDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public static EventData createEventData(String type){
    	return createEventData(type, RefCodeNames.EVENT_STATUS_CD.STATUS_READY);
    }
    public static EventData createEventData(String type, String status){
    	EventData eventData = new EventData();
		eventData.setType(type);
        eventData.setStatus(status);
        eventData.setAttempt(1);
        eventData.setEventPriority(new Long(50));
        return eventData;
    }
	@Override
	public BatchOrderView saveBatchOrder(BatchOrderView batchOrderView) throws Exception {
		EventData eventData = createEventData(TYPE_PROCESS, RefCodeNames.EVENT_STATUS_CD.STATUS_HOLD);
		        
        SimpleDateFormat dateFormat = new SimpleDateFormat(I18nUtil.getDatePattern() + " " + I18nUtil.getTimePattern());
        Date processTime = dateFormat.parse(batchOrderView.getProcessOn() + " 18:00");		
		eventData.setProcessTime(processTime);
		create(eventData);
		batchOrderView.setEventId(eventData.getEventId());
		
		ProcessDAO processDao = new ProcessDAOImpl(em);
		ProcessData process = processDao.getProcessByName(RefCodeNames.PROCESS_NAMES.PROCESS_BATCH_ORDERS);
		EventPropertyData eventProperty = createEventPropertyData(eventData.getEventId(), "process_id",
		             PROPERTY_PROCESS_TEMPLATE_ID,
		             process.getProcessId().intValue(),
		             1);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "storeId",
	            PROCESS_VARIABLE,
	            batchOrderView.getStoreId().intValue(),
	            1);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "fileName",
	            PROCESS_VARIABLE,
	            batchOrderView.getFileName(),
	            2);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "applyToBudget",
	            PROCESS_VARIABLE,
	            batchOrderView.getApplyToBudget(),
	            3);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "sendConfirmation",
	            PROCESS_VARIABLE,
	            batchOrderView.getSendConfirmation(),
	            4);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "dataContents",
	             PROCESS_VARIABLE, 
	             batchOrderView.getFileBinaryData(),
	             5);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "processOn",
	             PROCESS_VARIABLE, batchOrderView.getProcessOn(),
	             6);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "processWhen",
	             PROCESS_VARIABLE, batchOrderView.getProcessWhen(),
	             7);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "orderCount",
	             PROCESS_VARIABLE, batchOrderView.getOrderCount(),
	             8);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "dateFormat",
	             PROCESS_VARIABLE, batchOrderView.getDateFormat(),
	             9);
		create(eventProperty);
		return batchOrderView;
	}
	
	public final static EventPropertyData createEventPropertyData(Long eventId,
            String shortDesc, String eventTypeCd, Object value, int num)
            throws IOException {
        EventPropertyData epd = new EventPropertyData();
        epd.setEventId(eventId);
        epd.setType(eventTypeCd);
        epd.setShortDesc(shortDesc);
        epd.setVarClass(value.getClass().getName());
        epd.setNum(num);
        epd.setStorageTypeCd(STORAGE_DB);
        if (value instanceof String) {
            epd.setStringVal((String) value);
        } else if (value instanceof Integer) {
            epd.setNumberVal((Integer) value);
        } else if (value instanceof Date) {
            epd.setDateVal((java.util.Date) value);
        } else {
            epd.setBlobValue(objectToBytes(value));
        }
        return epd;
    }
	
	public final static byte[] objectToBytes(Object pObj)
    throws java.io.IOException {
		java.io.ByteArrayOutputStream oStream = new java.io.ByteArrayOutputStream();
		java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(oStream);
		os.writeObject(pObj);
		os.flush();
		os.close();
		return oStream.toByteArray();
	}
	
	public void cancellEvent(List<Long> selected) throws Exception {
		for (Long eventId : selected){
			EventData eventD = em.find(EventData.class, eventId);
			if (eventD != null){
				eventD.setStatus(RefCodeNames.EVENT_STATUS_CD.STATUS_DELETED);
				update(eventD);
			}
		}
	}
	public List<EventPropertyData> getEventProperties(List<Long> selected, List<String> propNames) throws Exception {
        StringBuilder baseQuery= new StringBuilder(
       			"Select eventProp from EventPropertyData eventProp " +
                " where eventProp.eventId = (:eventId) " +
                " and eventProp.shortDesc in (:propNames)" );
       Query q = em.createQuery(baseQuery.toString());
       List<EventPropertyData> result = null;	
       for (Long eventId : selected){

	       q.setParameter("eventId", eventId);
	       q.setParameter("propNames", propNames);
	           
	        result = (List<EventPropertyData>) q.getResultList();

			//List<EventPropertyData> eventPropertyD = em.find(EventData.class, eventId);
			break;
		}
		return result;
	}
	
	public List<BatchOrderView> getBatchOrders(Long storeId, String statusCd) throws Exception{
		List<BatchOrderView> batchOrders = new ArrayList<BatchOrderView>();
		SimpleDateFormat sdf = new SimpleDateFormat(I18nUtil.getDatePattern());
		
		Query query = em.createNativeQuery("select e.event_id, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'fileName' and event_id = e.event_id) fileName, \n" +
				"(select max(number_val) from clw_event_property where short_desc = 'orderCount' and event_id = e.event_id) orderCount, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'applyToBudget' and event_id = e.event_id) applyToBudget, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'sendConfirmation' and event_id = e.event_id) sendConfirmation, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'processOn' and event_id = e.event_id) processOn, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'processWhen' and event_id = e.event_id) processWhen, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'dateFormat' and event_id = e.event_id) dateFormat \n" +
				"from clw_event e, clw_event_property p, clw_event_property s \n" +
				"where e.event_id = p.event_id \n" +
				"and p.type = 'PROCESS_TEMPLATE_ID' \n" +
				"and p.number_val = (:templateId) \n" +
				"and e.status = (:status) \n" +
				"and e.event_id = s.event_id \n" +
				"and s.short_desc = 'storeId' \n" +
				"and s.number_val = (:storeId)");
		
		ProcessDAO processDao = new ProcessDAOImpl(em);
		ProcessData process = processDao.getProcessByName(RefCodeNames.PROCESS_NAMES.PROCESS_BATCH_ORDERS);
		long templateId = process.getProcessId();
		
		query.setParameter("templateId", templateId);
		query.setParameter("status", statusCd);
		query.setParameter("storeId", storeId);
		
		List<Object[]> results  = query.getResultList();
		for (Object[] result : results){
			BatchOrderView batchOrder = new BatchOrderView();
			batchOrder.setEventId(((BigDecimal)result[0]).longValue());
			batchOrder.setFileName(result[1].toString());
			batchOrder.setOrderCount((result[2]==null? 0 : ((BigDecimal)result[2]).intValue()));
			batchOrder.setApplyToBudget(result[3].toString());
			batchOrder.setSendConfirmation(result[4].toString());
			batchOrder.setProcessOn(result[5].toString());
			batchOrder.setProcessWhen(result[6].toString());
			String dateFormat = result[7].toString();
			if (dateFormat != null && !dateFormat.equals(I18nUtil.getDatePattern())){
				SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat);
				Date date = sdf1.parse(batchOrder.getProcessOn());
				batchOrder.setProcessOn(sdf.format(date));				
			}
			batchOrders.add(batchOrder);
		}
		
		logger.info("getBatchOrders(): " + batchOrders.size() + " batch order returned");
		return batchOrders;
	}
	
	@Override
	public EventEmailView addEventEmail(EventEmailData eventEmailData, FileAttach[] fileAttachments) throws IOException{
		EventData eventData = EventDAOImpl.createEventData(EventDAOImpl.TYPE_EMAIL);
		byte[] attachments = null;
        byte[] longText = null;
        if(fileAttachments != null && fileAttachments.length > 0) {
        	attachments = objectToBytes(fileAttachments);
            eventEmailData.setAttachments(attachments);
        }
        eventData = create(eventData);

        if (eventEmailData != null) {
        	logger.info("Email TEXT : "+	eventEmailData.getText());	
            eventEmailData.setEventId(eventData.getEventId());
            eventEmailData.setStorageTypeCd(STORAGE_DB);           
            if (eventEmailData.getText() != null
                    && eventEmailData.getText().getBytes("UTF-8").length > 4000) {
            	longText = eventEmailData.getText().getBytes("UTF-8");
                eventEmailData.setLongText(longText);
                eventEmailData.setText("");
            }
            if (attachments != null) {
                eventEmailData.setAttachments(attachments);
                eventEmailData.setStorageTypeCd(STORAGE_DB);
            }
            
            create(eventEmailData);
        }

        EventEmailView eventEmailView = new EventEmailView(eventData, eventEmailData, fileAttachments);
        
        return eventEmailView;
		
	}
	@Override
	public CatalogManagerView saveCatalog(CatalogManagerView catalogView, String eventStatus) throws Exception {
		EventData eventData = createEventData(TYPE_PROCESS, eventStatus);
		        
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat(I18nUtil.getDatePattern() + " " + I18nUtil.getTimePattern());
        dateFormatLocal.setTimeZone(TimeZone.getTimeZone(catalogView.getTimeZone()));
        SimpleDateFormat dateFormatEST = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN + " " + Constants.SYSTEM_TIME_PATTERN);
        dateFormatEST.setTimeZone(TimeZone.getTimeZone("EST"));
        //--------recalculate the event time to Eastern Standard Time --------- 
        GregorianCalendar calLocal = new GregorianCalendar(TimeZone.getTimeZone(catalogView.getTimeZone()));
        calLocal.setTime(dateFormatLocal.parse(catalogView.getEffectiveDate() + " 00:00"));
        //Date processTimeLocal = calLocal.getTime();
        long processTimeLocal = calLocal.getTimeInMillis();
        logger.info("########### processTime Local: " + dateFormatLocal.format(calLocal.getTime()));
       
        GregorianCalendar calEST = new GregorianCalendar(TimeZone.getTimeZone("EST"));
        //calEST.setTime(processTimeLocal);
        calEST.setTimeInMillis(processTimeLocal);
        Date processTimeEST = calEST.getTime();
        logger.info("########### processTime EST: " + dateFormatEST.format(processTimeEST));
        //====
        
        
        
        //---------------------------------------------------------------------
		
        eventData.setProcessTime(processTimeEST);
		create(eventData);
		catalogView.setEventId(eventData.getEventId());
		
		ProcessDAO processDao = new ProcessDAOImpl(em);
		ProcessData process = processDao.getProcessByName(RefCodeNames.PROCESS_NAMES.PROCESS_UPDATE_CATALOGS);
		EventPropertyData eventProperty = createEventPropertyData(eventData.getEventId(), "process_id",
		             PROPERTY_PROCESS_TEMPLATE_ID,
		             process.getProcessId().intValue(),
		             1);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "storeId",
	            PROCESS_VARIABLE,
	            catalogView.getStoreId().intValue(),
	            1);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "fileName",
	            PROCESS_VARIABLE,
	            catalogView.getFileName(),
	            2);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "dataContents",
	             PROCESS_VARIABLE, 
	             catalogView.getFileBinaryData(),
	             5);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "effectiveDate",
	             PROCESS_VARIABLE, catalogView.getEffectiveDate(),
	             6);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "timeZone",
	             PROCESS_VARIABLE, catalogView.getTimeZone(),
	             7);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "catalogCount",
	             PROCESS_VARIABLE, catalogView.getCatalogCount(),
	             8);
		create(eventProperty);
		eventProperty = createEventPropertyData(eventData.getEventId(), "dateFormat",
	             PROCESS_VARIABLE, catalogView.getDateFormat(),
             9);
		create(eventProperty);
		return catalogView;
	}
	public List<CatalogManagerView> getCatalogs(Long storeId, String statusCd) throws Exception{
		List<CatalogManagerView> catalogs = new ArrayList<CatalogManagerView>();
		SimpleDateFormat sdf = new SimpleDateFormat(I18nUtil.getDatePattern());
		
		Query query = em.createNativeQuery("select e.event_id, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'fileName' and event_id = e.event_id) fileName, \n" +
				"(select max(number_val) from clw_event_property where short_desc = 'catalogCount' and event_id = e.event_id) catalogCount, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'effectiveDate' and event_id = e.event_id) effectiveDate, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'timeZone' and event_id = e.event_id) timeZone, \n" +
				"(select max(string_val) from clw_event_property where short_desc = 'dateFormat' and event_id = e.event_id) dateFormat \n" +
				"from clw_event e, clw_event_property p, clw_event_property s \n" +
				"where e.event_id = p.event_id \n" +
				"and p.type = 'PROCESS_TEMPLATE_ID' \n" +
				"and p.number_val = (:templateId) \n" +
				"and e.status = (:status) \n" +
				"and e.event_id = s.event_id \n" +
				"and s.short_desc = 'storeId' \n" +
				"and s.number_val = (:storeId)");
		
		ProcessDAO processDao = new ProcessDAOImpl(em);
		ProcessData process = processDao.getProcessByName(RefCodeNames.PROCESS_NAMES.PROCESS_UPDATE_CATALOGS);
		long templateId = process.getProcessId();
		
		query.setParameter("templateId", templateId);
		query.setParameter("status", statusCd);
		query.setParameter("storeId", storeId);
		
		List<Object[]> results  = query.getResultList();
		for (Object[] result : results){
			CatalogManagerView catalog = new CatalogManagerView();
			catalog.setEventId(((BigDecimal)result[0]).longValue());
			catalog.setFileName(result[1].toString());
			catalog.setCatalogCount((result[2]==null? 0 : ((BigDecimal)result[2]).intValue()));
			catalog.setEffectiveDate(result[3].toString());
			catalog.setTimeZone(result[4].toString());
			String dateFormat = (Utility.isSet(result[5]))? result[5].toString() : I18nUtil.getDatePattern();
			if (dateFormat != null && !dateFormat.equals(I18nUtil.getDatePattern())){
				SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat);
				Date date = sdf1.parse(catalog.getEffectiveDate());
				catalog.setEffectiveDate(sdf.format(date));				
			}
			catalogs.add(catalog);
		}
		
		logger.info("getCatalogs(): " + catalogs.size() + " catalogs returned");
		return catalogs;
	}
	
	
	
}
