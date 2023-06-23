package crawlfacebook.example.creawdata.model;

public class Feed {
	private long postId;
	private String contentText;
	private String type;
	private String url;
	public Feed() {
		// TODO Auto-generated constructor stub
	}
	
	public Feed(long postId, String contentText, String type, String url) {
		super();
		this.postId = postId;
		this.contentText = contentText;
		this.type = type;
		this.url = url;
	}

	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	public String getContentText() {
		return contentText;
	}
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
