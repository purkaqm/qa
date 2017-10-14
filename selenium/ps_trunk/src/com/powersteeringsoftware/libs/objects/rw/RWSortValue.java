package com.powersteeringsoftware.libs.objects.rw;

public class RWSortValue {
	public enum RWSortOrder{ASC, DESC}
	
	private RWSortOrder sortOrder;
	boolean custom;
	
	public RWSortValue(String sortOrder, boolean custom){
		this(RWSortOrder.valueOf(sortOrder), custom);
	}

	public RWSortValue(RWSortOrder sortOrder, boolean custom){
		this.sortOrder = sortOrder;
		this.custom = custom;
	}

	public RWSortOrder getSortOrder(){
		return sortOrder;
	}
	
	public boolean isCustomSort(){
		return custom;
	}
} // class RWSortValue 
