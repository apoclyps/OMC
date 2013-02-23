package uk.co.kyleharrison.omc.stores;

import java.text.SimpleDateFormat;
import java.util.Date;



/*
 * Tweetstore, comparable for sorting by time.
 */
public class TweetStore implements Comparable<TweetStore>
{
	private String tweetID = "";
	private String user = "";
	private Long timeStamp = (long) 0;
	private String content = "";
	private String avatarurl = "";
	private String tags = "";
	
	private Long sort = timeStamp;
	
	public void setTweetID(String tweetID) {
		this.tweetID = tweetID;
	}
	public String getTweetID() {
		return tweetID;
	}
	public void setUser(String user) {
		this.user = user.toLowerCase();
	}
	public String getUser() {
		return user;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
		sort = timeStamp;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public String getTimeStampString() {
		Date now = new Date(timeStamp);
		return new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(now);
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	@Override
	public int compareTo(TweetStore o) {
		// TODO Auto-generated method stub
		return (int) (o.sort - this.sort);
	}
	
	public void switchToTimeOrdering()
	{
		sort = timeStamp;
	}

	public void setAvatarUrl(String avatarurl) {
		this.avatarurl = avatarurl;
	}
	public String getAvatarUrl() {
		return avatarurl;
	}

	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
}
