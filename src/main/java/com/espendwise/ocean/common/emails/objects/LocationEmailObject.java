package com.espendwise.ocean.common.emails.objects;

import java.util.List;

public interface LocationEmailObject extends EmailObject {
	// Location: [insert Site Name]
	String getSiteName();

	// Location Number: [insert Site Budget Reference Number]
	String getSiteBudgetReferenceNumber();

	// Address
	AddressEmailObject getAddress();

	// Custom Field Label: [insert Custom Asset Location field]*
	List<CustomFieldEmailObject> getCustomAssetLocationFields();

}
