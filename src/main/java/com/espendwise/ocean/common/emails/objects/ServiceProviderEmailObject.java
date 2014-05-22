package com.espendwise.ocean.common.emails.objects;

public interface ServiceProviderEmailObject extends EmailObject {
	//  Service Provider:  [insert Service Provider Name]  - ???
	String getName();
	//  Contact Name: [insert service provider First and Last Name]
	String getFirstName();
	String getLastName();
	//  Contact Phone: [insert Service Provider telephone number]
	String getTelephoneNumber();
	//  Contact Email: [insert Service Provider email address]
	String getEmailAddress();

}
