package com.powersteeringsoftware.libs.objects.rw;

import java.util.HashMap;
import java.util.Map;

public class RWColumn {
	public static final String COLUMN_NAME_SEPARATOR = " / ";
	private String name;
	private String group;
	private Map<String, String> parameters;
	private RWFilterValue filterValue;
	private RWSortValue sortValue;
	private RWSummaryValue summaryValue;
	
	public RWColumn(String name){
		this.name = name;
	}

	public RWColumn(String name, String group){
		this(name);
		this.group = group;
	}
	
	public RWColumn(String name, String group, RWFilterValue filterValue){
		this(name, group);
		this.filterValue = filterValue;
	}

	public RWColumn(String name, RWSortValue sortValue){
		this(name);
		this.sortValue = sortValue;
	}

	public RWColumn(String name, RWSummaryValue summaryValue){
		this(name);
		this.summaryValue = summaryValue;
	}

	public void setParameters(Map<String, String> parameters){
		this.parameters = new HashMap<String, String>(parameters);
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getGroup(){
		return this.group;
	}
	
	public String getFullColumnName(){
		return getGroup() != null ? 
				    getGroup() + COLUMN_NAME_SEPARATOR + getName():
					getName();
	}
	
	public boolean isFiltered(){
		return ( getFilterValue() != null );
	}
	
	public RWFilterValue getFilterValue(){
		return filterValue;
	}
	
	public RWSortValue getSortValue(){
		return sortValue;
	}
	
	public RWSummaryValue getSummaryValue(){
		return this.summaryValue;
	}
	
	public String getOption(){
		return this.parameters.get("option");
	}
	
	public String getParameter(String paramName){
		return this.parameters.get(paramName);
	}
	
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof RWColumn){
			return ((RWColumn)obj).getFullColumnName().equals(this.getFullColumnName());
		}else
			return false;
	}
	
	@Override
	public String toString(){
		return getFullColumnName();
	}
} // class RWColumn 
