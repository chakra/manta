package com.espendwise.ocean.common.emails.objects;

public interface OrderSiteEmailObject extends EmailObject {

	String getSiteName();
	String getSiteBudgetReferenceNumber();
	AddressEmailObject getAddress();

}
