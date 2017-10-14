package selenium.formholders;

public class CustomFieldHolder {
	private String fieldName = null;
	private Boolean isApplyPermissions = null;
	private Boolean isRequired = null;
	private Boolean isHideOnSummary = null;
	private Boolean isDefaultIncludeInReport = null;
	private Boolean isLocked = null;
	private String description = null;

	private int dataType;
	private String dataTypeString1;
	private String dataTypeString2;

	public CustomFieldHolder(String fieldName, String description){
		this.fieldName = fieldName;
		this.description = description;
	}

	public void setupAsYesNoButton(){
		this.dataType = 1;
		this.dataTypeString1 = null;
		this.dataTypeString2 = null;
	}

	public void setupAsCheckboxes(String checkboxesString){
		this.dataType = 2;
		this.dataTypeString1 = checkboxesString;
		this.dataTypeString2 = null;
	}

	public void setupAsDate(){
		this.dataType = 3;
		this.dataTypeString1 = null;
		this.dataTypeString2 = null;
	}

	public void setupAsMenu(String menuString){
		this.dataType = 4;
		this.dataTypeString1 = menuString;
		this.dataTypeString2 = null;
	}

	public void setupAsMonetary(String monetaryString1, String monetaryString2){
		this.dataType = 5;
		this.dataTypeString1 = monetaryString1;
		this.dataTypeString2 = monetaryString2;
	}

	public void setupAsNumbers(String numbersString1, String numbersString2){
		this.dataType = 6;
		this.dataTypeString1 = numbersString1;
		this.dataTypeString2 = numbersString2;
	}

	public void setupAsPercentage(String percentageString1, String percentageString2){
		this.dataType = 7;
		this.dataTypeString1 = percentageString1;
		this.dataTypeString2 = percentageString2;
	}

	public void setupAsTextfield(String textfieldString){
		this.dataType = 8;
		this.dataTypeString1 = textfieldString;
		this.dataTypeString2 = null;
	}

	public void setupAsURL(){
		this.dataType = 9;
		this.dataTypeString1 = null;
		this.dataTypeString2 = null;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Boolean getIsApplyPermissions() {
		return isApplyPermissions;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public Boolean getIsHideOnSummary() {
		return isHideOnSummary;
	}

	public Boolean getIsDefaultIncludeInReport() {
		return isDefaultIncludeInReport;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public String getDescription() {
		return description;
	}

	public int getDataType() {
		return dataType;
	}

	public String getDataTypeString1() {
		return dataTypeString1;
	}

	public String getDataTypeString2() {
		return dataTypeString2;
	}

	public void setIsApplyPermissions(Boolean isApplyPermissions) {
		this.isApplyPermissions = isApplyPermissions;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public void setIsHideOnSummary(Boolean isHideOnSummary) {
		this.isHideOnSummary = isHideOnSummary;
	}

	public void setIsDefaultIncludeInReport(Boolean isDefaultIncludeInReport) {
		this.isDefaultIncludeInReport = isDefaultIncludeInReport;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

}
