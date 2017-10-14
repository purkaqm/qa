package com.powersteeringsoftware.libs.objects.rw;

import java.util.HashSet;
import java.util.Set;

public class RWColumnGroup {
	private String groupId;
	private String groupName;
	private boolean customColumnsGroup;
	private boolean selectAllColumn;
	private String option;
	private Set<RWColumn> columns = new HashSet<RWColumn>();
	
	public RWColumnGroup(String name){
			groupName = name;
	}

	public RWColumnGroup(String name, boolean allColumnSelected){
		this(name);
		this.selectAllColumn = allColumnSelected;
	}

	
	public String getName(){
		return groupName;
	}
	
	public boolean isCustomColumnGroup(){
		return customColumnsGroup;
	}
	
	public boolean getAllColumnsSelected(){
		return selectAllColumn;
	}
	
	public Set<RWColumn> getColumns(){
		return columns;
	}
	
	
	public void addColumn(RWColumn column){
		this.columns.add(column);
	}
	
	
	public void setId(String id){
		this.groupId = id;
	}

	public String getId(){
		return groupId != null ? groupId : groupName;
	}
	
	public void setGroupAsCustom(boolean custom){
		this.customColumnsGroup = custom;
	}

	public void setOption(String option){
		this.option = option;
	}
	
	public String getOption(){
		return this.option;
	}
	
	@Override
	public String toString(){
		return getName();
	}
	
} // class RWColumnGroup 
