package com.powersteeringsoftware.libs.objects.rw;

import java.util.ArrayList;
import java.util.List;

public class RWChart {
	public enum ChartType{
		NO_CHART("No chart"),
		BAR("Bar"),
		LINE("Line"),
		PIE("Pie");
		
		ChartType(String title){this.title = title;}
		public String getTitle(){return title;}
		private String title;  
	}
	
	public enum ChartOrientation{
		Horizontal,
		Vertical
	}
	
	public enum ChartGrouping{
		NO_GROUPING("No grouping"),
		GROUPED("Grouped"),
		STACKED("Stacked");
		
		ChartGrouping(String title){this.title = title;}
		public String getTitle(){return title;}
		public void setGroupBy(String groupBy){this.groupBy = groupBy;}
		public String getGroupBy(){return groupBy;}
		
		private String title;
		private String groupBy;
	}
	
	private ChartType type;
	private ChartOrientation orientation;
	private ChartGrouping grouping;
	private String axis;
	private List<RWChart.DataSeries> chartData = new ArrayList<RWChart.DataSeries>();
	/* styling options */
	private String position;
	private String size;
	private String textFont;
	private String legendPosition;
	private String labelPosition;
	private String bagroundFade;
	private String fromColor;
	private String toColor;
	
	public ChartType getType() {
		return type;
	}
	public void setType(ChartType type) {
		this.type = type;
	}

	public ChartOrientation getOrientation() {
		return orientation;
	}
	public void setOrientation(ChartOrientation orientation) {
		this.orientation = orientation;
	}

	public ChartGrouping getGrouping() {
		return grouping;
	}
	public void setGrouping(ChartGrouping grouping) {
		this.grouping = grouping;
	}

	public void addDataSeries(String series){
		chartData.add(new DataSeries(series));
	}

	public void addDataSeries(String series, boolean drawAsLine, boolean newScale){
		chartData.add(new DataSeries(series, drawAsLine, newScale));
	}
	public List<DataSeries> getDataSeries(){
		return this.chartData;
	}
	
	
	public String getAxis() {
		return axis;
	}
	public void setAxis(String axis) {
		this.axis = axis;
	}

	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	public String getTextFont() {
		return textFont;
	}
	public void setTextFont(String textFont) {
		this.textFont = textFont;
	}

	public String getLegendPosition() {
		return legendPosition;
	}
	public void setLegendPosition(String legendPosition) {
		this.legendPosition = legendPosition;
	}

	public String getLabelPosition() {
		return labelPosition;
	}
	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	public String getBagroundFade() {
		return bagroundFade;
	}

	public void setBagroundFade(String bagroundFade) {
		this.bagroundFade = bagroundFade;
	}

	public String getFromColor() {
		return fromColor;
	}
	public void setFromColor(String fromColor) {
		this.fromColor = fromColor;
	}

	public String getToColor() {
		return toColor;
	}
	public void setToColor(String toColor) {
		this.toColor = toColor;
	}


	public static class DataSeries{
		private String series;
		private boolean drawAsLine;
		private boolean newScale;
		
		public DataSeries(String series){
			this.series = series;
		}

		public DataSeries(String series, boolean drawAsLine, boolean newScale){
			this(series);
			this.drawAsLine = drawAsLine;
			this.newScale = newScale;
		}

		public String getSeries(){
			return series;
		}
		public boolean isDrawAsLine(){
			return drawAsLine;
		}
		public boolean isNewScale(){
			return newScale;
		}
	} // class DataSeries
} // class RWChart 
