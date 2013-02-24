<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ page import="uk.co.kyleharrison.omc.model.*"%>
<%@ page import="uk.co.kyleharrison.omc.stores.*"%>
<%@ page import="uk.co.kyleharrison.omc.connectors.*"%>
<%@ page import="java.util.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>On My Campus - Profile</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="stylesheet" type="text/css" href="/OMC/css/main_style.css">
<link rel="stylesheet" type="text/css" href="/OMC/css/tweet_style.css">
<link rel="stylesheet" type="text/css" href="/OMC/css/profile_style.css">
<%@include file="include/header.jsp"%>


</head>

<body>
	<div id="top_bar">

		<div id="top_bar_title">
			<span class="blue_font">O</span><span class="white_font">N</span> <span
				class="blue_font">M</span><span class="white_font">Y</span> <span
				class="blue_font">C</span><span class="white_font">AMPUS</span>
			<!--end title-->

		</div>
		<div class="LogOut">
			<nav>
			<ul>
				<li><a href="/OMC/Profile">Profile</a></li>
				<li><a href="/OMC/Message">Tweet</a></li>
				<li><a href="/OMC/">Timeline</a></li>
				<li>
					<form name="logout_form" method="post" action="./Logout">
						<input type="hidden" id="logout" name="logout"> <input
							class="button_astext" type="submit" value="Log out">
					</form>
				</li>
			</ul>
			</nav>
		</div>
	</div>

	<div id="content_panel">


		<div id="profile_pane">
			<!--  tweet HEADER -->


			<!--  tweet TWO -->



			<div class="tweet_title">
				<div class="tweet_left">
					<div class="tweet_title_label">
						<span class="blue_font">Profile</span>
					</div>
				</div>

			</div>

			<%
				Session thisSession = (Session) session.getAttribute("Session");
				UserStore profile = null;
				String firstname = null;
				String surname = null;
				String bio = null;
				String email = null;
				String username = null;
				String location = null;
				String age = null;
				String joined = null;
				String posts = null;
				String followers = null;
				String followees = null;
				UserConnector UC = new UserConnector();

				if (thisSession != null) {
					try
					{
					username = thisSession.getUsername();
					profile = UC.getUserByUsername(username);
					email = profile.getEmail();
					profile = UC.getProfileByEmail(email);

					firstname = profile.getName();
					surname = profile.getSurname();
					location = profile.getLocation();
					bio = profile.getBio();
					age = profile.getAge();
					joined = profile.getJoined();
					posts = profile.getPosts();
					followers = profile.getFollowers();
					followees = profile.getFollowees();
					}
					catch(Exception e)
					{
						System.out.println("Error in profile "+e.getMessage());
						
						firstname = profile.getName();
						surname = profile.getSurname();
						location = profile.getLocation();
						bio = profile.getBio();
						age = profile.getAge();
						joined = profile.getJoined();
						posts = "0";
						followers = "0";
						followees = "0";
					}

				} else {
					System.out.println("Session null" + thisSession);
					username = "error";
					bio = "null";
				}
			%>

			<div class="profile">
				<center>
					<div class="big_text">
						User :
						<%=username%></div>
					<img src="/OMC/img/avatar.png" alt="">
				</center>

				<div id="information">
					<span class="big_text">About Me:</span>

					<div id="information_layer">
						<%=bio%>
					</div>
				</div>

				<div id="details_right">
					<%=firstname%>
				</div>
				<div id="details_left">
					<span class="big_title">First Name</span>
				</div>
				<br />

				<div id="details_right">
					<%=surname%>
				</div>

				<div id="details_left">
					<span class="big_title">Surname</span>
				</div>
				<br />

				<div id="details_right">
					<%=age%>
				</div>
				<div id="details_left">
					<span class="big_title">Age</span>
				</div>
				<br />

				<div id="details_right">
					<%=location%>
				</div>
				<div id="details_left">
					<span class="big_title">Location</span>
				</div>
				<br />

				<div id="details_right">
					<%=email%>
				</div>
				<div id="details_left">
					<span class="big_title">E-mail</span>
				</div>
				<br />

				<div id="details_right">
					<%=joined%>
				</div>
				<div id="details_left">
					<span class="big_title">Joined</span>
				</div>
				<br />

				<div id="details_right">
					<%=posts%>
				</div>
				<div id="details_left">
					<span class="big_title">Posts</span>
				</div>
				<br />

				<div id="details_right">
					<%=followers%>
				</div>
				<div id="details_left">
					<span class="big_title">Followers</span>
				</div>
				<br />

				<div id="details_right">
					<%=followees%>
				</div>
				<div id="details_left">
					<span class="big_title">Followees</span>
				</div>
				<br />

				<div id="button_banner">
					<center>

						<form name="edit_profile" method="post" action="./Profile">
							<input class="blue_button" name="edit" type="submit"
								value="Edit Profile">
						</form>

					</center>
				</div>

			</div>
			<!--  tweet Footer -->

			<div class="tweet_footer">
				<div class="tweet_left">
					<div id="tweet_title_label"></div>
				</div>
			</div>
		</div>

	</div>
	<!--  Content Panel -->
</body>
</html>