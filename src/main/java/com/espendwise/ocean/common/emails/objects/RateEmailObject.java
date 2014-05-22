package com.espendwise.ocean.common.emails.objects;

public interface RateEmailObject extends EmailObject {
	String getName();
	String getValue();
	String getRateType();
}
