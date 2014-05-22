package com.espendwise.ocean.common.emails.objects;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface InvoiceEmailObject extends EmailObject {

	String getInvoiceNumber();

	String getInvoiceDate();

	AccountEmailObject getAccount();

	String getNote();
	
	String getTicketNumbers();
	
	List<TicketInvoiceEmailObject> getTickets();
	
	

}
