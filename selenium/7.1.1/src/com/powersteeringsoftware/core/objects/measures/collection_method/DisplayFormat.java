package com.powersteeringsoftware.core.objects.measures.collection_method;

/**
 * Measure display format area in Collection Method module
 * @author selyaev_ag
 *
 */
public class DisplayFormat {

	public enum DisplayFormatValue{
		INTEGER, FLOAT, MONETARY, PERCENT
	}
	public enum Precision{
		NONE, ONE, TWO, THREE, FOUR, FIFE, SIX
	}
	public enum Scale{
		NONE, THOUSANDS, MILLIONS
	}

	private DisplayFormatValue displayFormatValue = DisplayFormatValue.INTEGER;
	private Precision precision = Precision.NONE;
	private Scale scale = Scale.NONE;

	public DisplayFormat(DisplayFormatValue _displayFormatValue){
		displayFormatValue = _displayFormatValue;
	}

	public DisplayFormat(){
	}

	public Precision getPrecision() {
		return precision;
	}

	public void setPrecision(Precision _precision) {
		this.precision = _precision;
	}

	public Scale getScale() {
		return scale;
	}

	public void setScale(Scale scale) {
		this.scale = scale;
	}

	public DisplayFormatValue getDisplayFormatValue() {
		return displayFormatValue;
	}

	public void setDisplayFormatValue(DisplayFormatValue displayFormatValue) {
		this.displayFormatValue = displayFormatValue;
	}

}
