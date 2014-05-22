package com.espendwise.ocean.common.emails.objects;

public interface ServiceTypeEmailObject extends EmailObject {
	
    String getName();
    String getAdminName();
    boolean isFreeform();
    boolean isRecurringService();
    boolean isPeriodic();
    String getFullName();
	
}
