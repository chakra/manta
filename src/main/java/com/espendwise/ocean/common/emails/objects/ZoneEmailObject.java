package com.espendwise.ocean.common.emails.objects;

import java.util.List;

public interface ZoneEmailObject extends EmailObject {
	
    String getName();
    //  Status:  [insert Zone Status]
    String getStatus();
    //  Scheduled Date: [insert Scheduled Date]
    String getScheduledDate();
    //  Custom Field Label: [insert Custom Service Ticket field]*
    List<RecurringRecordEmailObject> getRecords();
    String getRejectionReason();
    List<RateEmailObject> getRates();

}
