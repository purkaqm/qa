package selenium.objects;

public class FileAttachment implements IAttachment{

	private String location;
	private String title;
	private String description;
	private Boolean ppt;
	private String status;

	public FileAttachment(String location){
		this.location = location;
		this.title = null;
		this.description = null;
		this.ppt = null;
		this.status = null;
	}

	public FileAttachment(String location, String title){
		this.location = location;
		this.title = title;
		this.description = null;
		this.ppt = null;
		this.status = null;
	}

	public String getPath() {
		return location;
	}

	public void setPath(String path) {
		this.location = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getPpt() {
		return ppt;
	}

	public void setPpt(Boolean ppt) {
		this.ppt = ppt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
