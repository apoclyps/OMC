package uk.co.kyleharrison.omc.stores;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserStore {
	private String followees = "0";
	private String followers = "0";
	private String joined;
	private boolean loggedIn = false;
	private String name, surname, email, bio, username, age, avatarurl,
			location = null;
	private String password = null;
	private String posts = "0";

	public UserStore() {
	}

	public String getAge() {
		return String.valueOf(age);
	}

	public String getAvatarUrl() {
		return avatarurl;
	}

	public String getBio() {
		return bio;
	}

	public String getEmail() {
		return email;
	}

	public String getFollowees() {
		return followees;
	}

	public String getFollowers() {
		return followers;
	}

	public String getJoined() {

		return joined;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getPosts() {
		return posts;
	}

	public String getSurname() {
		return surname;
	}

	public String getUserName() {
		return username;
	}

	public boolean isloggedIn() {
		System.out.println("Logged " + loggedIn);
		return loggedIn;
	}

	public void login(String email) {
		this.email = email;
		loggedIn = true;
	}

	public void logout() {
		loggedIn = false;
		email = "";
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setAvatar(String avatarUrl) {
		this.avatarurl = avatarUrl;
		if (avatarUrl.equals("") || avatarUrl == null) {
			this.avatarurl = "/OMC/img/avatar/png";
		}
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFollowees(String followees) {
		this.followees = followees;
	}

	public void setFollowers(String followers) {
		this.followers = followers;
	}

	public void setJoined() {
		System.out.println("joined");
		Date now = new Date();
		this.joined = new SimpleDateFormat("dd MMMMM yyyy").format(now);
		System.out.println("joined on " + joined);
	}

	public void setJoinedDate(String joined) {
		this.joined = joined;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPosts(String posts) {
		this.posts = posts;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setUserName(String username) {
		this.username = username;
	}
}