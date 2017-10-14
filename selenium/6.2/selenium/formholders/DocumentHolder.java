package selenium.formholders;

public abstract class DocumentHolder {
	enum TYPES{
		LOCAL,
		URL
	}

	protected TYPES Type;
	private String location = null;
	private String title = null;
	private String description = null;
	private String status = null;
	private Boolean pptImage = null;

	public DocumentHolder(String location, String title){
		this.location = location;
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getPptImage() {
		return pptImage;
	}

	public void setPptImage(Boolean pptImage) {
		this.pptImage = pptImage;
	}

	public String getLocation() {
		return location;
	}

	public String getTitle() {
		return title;
	}

	public TYPES getType() {
		return Type;
	}

}
