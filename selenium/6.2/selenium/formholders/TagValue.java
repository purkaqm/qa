package selenium.formholders;

public class TagValue {
	String tagValueName;
	String tagValueParent;

	protected TagValue(String tagValueName, String tagValueParent){
		this.tagValueName = tagValueName;
		this.tagValueParent = tagValueParent;
	}

	public String getTagValueName() {
		return tagValueName;
	}

	public String getTagValueParent() {
		return tagValueParent;
	}


}
