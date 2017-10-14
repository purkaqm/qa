package com.powersteeringsoftware.libs.objects.rw;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.powersteeringsoftware.libs.elements.rw.RWColumnFilterControl.RWFilterType;

/**
 * Container to store a value for a RW filter   
 *
 */
public class RWFilterValue {
	public static String DATE_PATTERN="";
	public static String VALUE_ALL = "All";
	
	private static String CLAUSE = "clause"; 
	private static String CRITERION = "criterion";

	private RWFilterType filterType;
	private List<String>	 values = new LinkedList<String>();
	private Map<String, String> parameters;

	public RWFilterValue(String strFilterType){	
		this.filterType = (RWFilterType.valueOf(strFilterType));
	}

	public RWFilterValue(String strFilterType, String value, Map<String, String> parameters){
		this(strFilterType);
		setParameters(parameters);
		this.addValue(value);
	}
	
	public RWFilterType getFilterType(){
		return this.filterType;
	}
	
	public void setParameters(Map<String, String> parameters){
		this.parameters = new HashMap<String, String>(parameters);
	}
	
	public void addValue(String value){
		values.add(value);
	}
	
	public String getValue(){
		return values.size() > 0 ? values.get(0) : null;
	}
	
	public boolean isValueAllSelected(){
		return VALUE_ALL.equals(getValue());
	}
	
	public List<String> getValues(){
		return new LinkedList<String>(this.values);
	}
	
	public String getClause(){
		return getParameterValue(CLAUSE);
	}
	
	public String getCriterion(){
		return getParameterValue(CRITERION); 
	}
	
	public String getParameterValue(String paramName){
		return parameters != null ? parameters.get(paramName) : null;
	}
	
	public boolean isCriterionExist(){
		return getCriterion() != null;
	}
	
	public boolean isClauseExist(){
		return getClause() != null;
	}
	
} // class RWFilterValue 
