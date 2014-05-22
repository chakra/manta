package com.espendwise.ocean.common.emails;


import com.espendwise.ocean.common.emails.objects.ServiceTicketEmailObject;
import com.espendwise.ocean.common.emails.objects.internal.OrderEmailObject;
import com.espendwise.ocean.common.emails.objects.InvoiceEmailObject;

public enum EmailMeta {
	INVOICE_EMAIL_META(InvoiceEmailObject.class, EmailTypes.InvoiceEmails.values()),
    ORDER_EMAIL_META(OrderEmailObject.class, EmailTypes.OrderEmails.values()),
    SERVICE_TICKET_META(ServiceTicketEmailObject.class, EmailTypes.ServiceTicketEmails.values());

    private Class serviceClass;
    private EmailType[] emailTypes;

    private EmailMeta(Class serviceClass, EmailType... emailTypes) {
        this.serviceClass = serviceClass;
        this.emailTypes = emailTypes;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public EmailType[] getEmailTypes() {
        return emailTypes;
    }

    public void setEmailTypes(EmailType[] emailTypes) {
        this.emailTypes = emailTypes;
    }

}
