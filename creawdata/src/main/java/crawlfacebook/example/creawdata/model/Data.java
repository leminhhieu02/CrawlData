package crawlfacebook.example.creawdata.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="data")
public class Data {
	@Id
	private long groupId;

	private String groupName;

	private long postId;

	private String contentText;

	private String type;

	private String url;
	
	private Date time;
	public Data() {
		// TODO Auto-generated constructor stub
	}
	public Data(long groupId, String groupName, long postId, String contentText, String type, String url, Date time) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.postId = postId;
		this.contentText = contentText;
		this.type = type;
		this.url = url;
		this.time = time;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
