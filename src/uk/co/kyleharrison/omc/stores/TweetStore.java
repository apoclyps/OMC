package uk.co.kyleharrison.omc.stores;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TweetStore implements Comparable<TweetStore>
{
	private String avatarurl = "";
	private String content = "";
	private String tags = "";
	private Long timeStamp = (long) 0;
	private String tweetID = "";
	private String user = "";
	private Long sort = timeStamp;
	
	@Override
	public int compareTo(TweetStore o) {
		return (int) (o.sort - this.sort);
	}
	public String getAvatarUrl() {
		return avatarurl;
	}
	public String getContent() {
		return content;
	}
	public String getTags() {
		return tags;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public String getTimeStampString() {
		Date now = new Date(timeStamp);
		return new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(now);
	}
	
	public String getTweetID() {
		return tweetID;
	}
	
	public String getUser() {
		return user;
	}
	public void setAvatarUrl(String avatarurl) {
		this.avatarurl = avatarurl;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
		sort = timeStamp;
	}

	public void setTweetID(String tweetID) {
		this.tweetID = tweetID;
	}
	public void setUser(String user) {
		this.user = user.toLowerCase();
	}
}
