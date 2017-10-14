package com.powersteeringsoftware.core.objects.measures.collection_method;

public class AbstractCollectionMethod {

	private DisplayFormat displayFormat = new DisplayFormat();
	private EffectiveDates effectiveDates = new EffectiveDates();


	public AbstractCollectionMethod(){
	}

	public DisplayFormat getDisplayFormat() {
		return displayFormat;
	}

	public void setDisplayFormat(DisplayFormat displayFormat) {
		this.displayFormat = displayFormat;
	}

	public EffectiveDates getEffectiveDates() {
		return effectiveDates;
	}

	public void setEffectiveDates(EffectiveDates effectiveDates) {
		this.effectiveDates = effectiveDates;
	}


}
