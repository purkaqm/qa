package selenium.objects;

public interface IAttachment {

	public abstract String getTitle();
	public abstract void setTitle(String title);
	public abstract String getDescription();
	public abstract void setDescription(String description);
	public abstract String getStatus();
	public abstract void setStatus(String status);
	public abstract Boolean getPpt();
	public abstract void setPpt(Boolean ppt);

}
