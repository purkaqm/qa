package com.powersteeringsoftware.core.objects.measures.collection_method;

import java.util.Date;

public class EffectiveDates {

	public enum EffectiveDatesValue{
		ABSOLUTE, ACTIVE_PROJECT, PROJECT_LIFE_TIME, BASELINE, ALWAYS
	}

	Date startDate = new Date();
	Date endDate = new Date();
	EffectiveDatesValue value = EffectiveDatesValue.ABSOLUTE;

	public EffectiveDates(EffectiveDatesValue _value){
		value = _value;
	}

	public EffectiveDates(){
	}
}