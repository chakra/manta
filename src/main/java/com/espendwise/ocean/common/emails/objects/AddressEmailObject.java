package com.espendwise.ocean.common.emails.objects;

public interface AddressEmailObject extends EmailObject {
	//  Address 1:  [insert primary user Address 1]
    public String getAddress1();
	//  Address 2:  [insert primary user Address 2]
    public String getAddress2();
	//  Address 3:  [insert primary user Address 3]
    public String getAddress3();
	//  State:  [insert primary user State]
    public String getState();
	//  Postal Code:  [insert primary user Postal Code]
    public String getPostalCode();
	//  City:  [insert primary user City]
    public String getCity();

}
