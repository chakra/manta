package com.espendwise.ocean.common.emails.objects;

public interface CustomFieldEmailObject extends EmailObject {

	//  Custom Field Label: [insert Custom Service Ticket field]*
	String getLabel();
	String getValue();
	
}
