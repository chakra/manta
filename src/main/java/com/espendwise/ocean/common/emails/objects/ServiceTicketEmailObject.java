package com.espendwise.ocean.common.emails.objects;

import java.util.List;


public interface ServiceTicketEmailObject extends EmailObject {
	
	//	  Service Ticket Number: [insert Service Ticket Number]
    String getServiceTicketNumber();
    
    //  Site
    LocationEmailObject getLocation();
    
    //    Service Type
    ServiceTypeEmailObject getServiceType();
    
    //    PO Number: [insert PO Number]
    String getPONumber();
    
    //    Rate: [insert Contract Rate] - ???
    ContractEmailObject getContract();
    
    //    Additional Charges: [insert Additional Charges]
    String getAdditionalCharges();
    
    //    Service Ticket Status: [insert Status]
    String getStatus();
    
    //    Requested By: [insert Add By Username]
    String getAddBy();
    
    //    Requested Date/Time:   [insert Add By Date/Time]
    String getAddDate();
    
    //    Comments:  [insert Comments]
    String getComments();
    
    // Zones Ticket flag
    boolean isTicketWithZones();
    
    //    ZONE*
    List<ZoneEmailObject> getZones();
    
    //    SERVICE PROVIDER
    ServiceProviderEmailObject getServiceProvider();
    
    //    SERVICE Vendor
    ServiceVendorEmailObject getServiceVendor();
    
    //    CONTACT
    ContactEmailObject getContact();
    
    //    ASSET
    AssetEmailObject getAsset();
	
    String getSentDate();
    AccountEmailObject getAccount();
    String getScheduleDate();
    UserEmailObject getUser();
    UserEmailObject getModByUser();
    UserEmailObject getAddByUser();
    LinkEmailObject getLink();

	String getSentTime();
    
}
