package selenium.formholders;

public class NewGatedProjectDateField {
	private String dateFieldName;
	private String dateFieldValue;

	protected NewGatedProjectDateField(String dateFieldName, String dateFieldValue){
		this.dateFieldName = dateFieldName;
		this.dateFieldValue = dateFieldValue;
	}

	public String getDateFieldName() {
		return dateFieldName;
	}

	public String getDateFieldValue() {
		return dateFieldValue;
	}

}
