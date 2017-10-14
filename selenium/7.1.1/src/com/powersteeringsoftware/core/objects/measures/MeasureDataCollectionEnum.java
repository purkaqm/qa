package com.powersteeringsoftware.core.objects.measures;

public enum MeasureDataCollectionEnum {
	MANUAL(0, "manual"), FORMULA(1, "auto");

	private int index;
	String htmlId;

	MeasureDataCollectionEnum(int indx, String id) {
		index = indx;
		htmlId = id;
	}

	public int getIndex() {
		return index;
	}

	public String getHtmlId() {
		return htmlId;
	}
}
