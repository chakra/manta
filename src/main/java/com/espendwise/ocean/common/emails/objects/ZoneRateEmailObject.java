package com.espendwise.ocean.common.emails.objects;

import java.util.List;

public interface ZoneRateEmailObject extends EmailObject {
    String getName();
	List<RateEmailObject> getRates();
}
