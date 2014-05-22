package com.espendwise.ocean.common.emails.objects;

import java.util.List;

public interface RecurringRecordEmailObject extends EmailObject {
	Integer getRecurringNumber();
	List<CustomFieldEmailObject> getFields();
}
