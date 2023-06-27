package crawlfacebook.example.creawdata.model;





import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="data")
public class Group {
	
	private String groupId;

	private String groupName;
	@Id
	private String postId;

	private String contentText;

	private String type;

	private String url;
	
	private Date time;
	public Group() {
		// TODO Auto-generated constructor stub
	}
	public Group(String groupId, String groupName, String postId, String contentText, String type, String url, Date time) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.postId = postId;
		this.contentText = contentText;
		this.type = type;
		this.url = url;
		this.time = time;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
