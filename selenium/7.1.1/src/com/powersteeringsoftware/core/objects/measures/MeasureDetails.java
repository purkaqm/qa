package com.powersteeringsoftware.core.objects.measures;

public class MeasureDetails {

	private String name="";
	private String description="";
	private String unitsOfMeasure = "";

	public MeasureDetails(String _name, String _description, String _unitsOfMEasure){
		name = _name;
		description =  _description;
		unitsOfMeasure = _unitsOfMEasure;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnitsOfMeasure() {
		return unitsOfMeasure;
	}

	public void setUnitsOfMeasure(String unitsOfMeasure) {
		this.unitsOfMeasure = unitsOfMeasure;
	}


}
