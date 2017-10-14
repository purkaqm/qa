package selenium.formholders;

public class LocalFileHolder extends DocumentHolder {

	public LocalFileHolder(String location, String title) {
		super(location, title);
		this.Type = DocumentHolder.TYPES.LOCAL;
	}

}
