package com.espendwise.ocean.common.emails.objects;

public interface ContactEmailObject extends EmailObject {
	//  Contact Name: [insert primary user First and Last Name]
	String getFirstName();
	String getLastName();
	//  Address	
	AddressEmailObject getAddress();
	//  Telephone Number:  [insert primary user Phone Number]
	String getPhoneNumber();
	//  Mobile Number:  [insert primary user Mobile Number]
	String getMobileNumber();
	//  Email:  [insert primary user Email address]
	String getEmailAddress();

}
