package selenium.formholders;

public class URLHolder  extends DocumentHolder {

	public URLHolder(String location, String title) {
		super(location, title);
		this.Type = DocumentHolder.TYPES.URL;
	}

}
