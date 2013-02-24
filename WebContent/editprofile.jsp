<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
     <%@ page import="uk.co.kyleharrison.omc.model.*" %>
     <%@ page import="uk.co.kyleharrison.omc.stores.*" %>
     <%@ page import="uk.co.kyleharrison.omc.connectors.*" %>
     <%@ page import="java.util.*" %>
      
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>On My Campus - Profile </title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="stylesheet" type="text/css" href="/OMC/css/main_style.css">
<link rel="stylesheet" type="text/css" href="/OMC/css/tweet_style.css">
<link rel="stylesheet" type="text/css" href="/OMC/css/profile_style.css">
<%@include file="include/header.jsp" %>

</head>

<body>
		<div id="top_bar">
		
			<div id="top_bar_title">
											<span class="blue_font">O</span><span class="white_font">N</span>
											<span class="blue_font">M</span><span class="white_font">Y</span>
											<span class="blue_font">C</span><span class="white_font">AMPUS</span>
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
											<input type="hidden" id="logout" name="logout">
		        							<input class="button_astext" type="submit" value="Log out" >
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
							<div class="tweet_title_label"> <span class="blue_font">Profile</span></div>
					</div>
			
			</div>		
			
			<%
											Session thisSession = (Session)session.getAttribute("Session");
											UserStore profile= null;
											String firstname= null;
											String surname= null;
											String bio= null;
											String email= null;
											String username= null;
											String location = null;
											String age = null;
											String joined = null;
											String posts = null;
											String followers = null;
											String followees = null;
											UserConnector UC = new UserConnector();
											
											if(thisSession!=null)
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
											else
											{	
												System.out.println("Session null"+thisSession);
												username = "error";
												bio = "null";
											}
			%> 	
			
			
			<div class="profile">
					<center>
					<div class="big_text">User : <%= username %></div>
					<img src="/OMC/img/avatar.png">
					</center>
				
					<form name="logout_form" method="post" action="./Profile">
					
									<div id="information">
										<span class="big_text">About Me:</span>
										<div id="information_layer"><center><textarea type="text" class="body_text" name="bio" cols="45%" rows="5"><%= bio %></textarea></center></div>
									</div>
			
									<div id="details_left"><span class="big_title">First Name</span></div>	
									<div id="details_right"><input type="text" id="firstname" name="firstname" value="<%= firstname %>"></div>
		        					<br></br>
		        					
									<div id="details_right"><input type="text" id="surname" name="surname" value="<%= surname %>"></div>
									<div id="details_left">	<span class="big_title">Surname</span></div>
									<br></br>
								
									<div id="details_right"><input type="text" id="age" name="age" value="<%= age %>"></div>					
									<div id="details_left"><span class="big_title">Age</span></div>
									<br></br>
									
									<div id="details_right"><input type="text" name="location" value="<%= location %>"></div>						
									<div id="details_left"><span class="big_title">Location</span></div>
									<br></br>
									
									<div id="details_right"><%= email %></div>							
									<div id="details_left"><span class="big_title">E-mail</span></div>
									<br></br>
									
									<div id="details_right"><%= joined %></div>						
									<div id="details_left"><span class="big_title">Joined</span></div>
									<br></br>
									
									<div id="details_right"><%= posts %></div>							
									<div id="details_left">	<span class="big_title">Posts</span></div>
									<br></br>
									
									<div id="details_right"><%= followers %></div>					
									<div id="details_left"><span class="big_title">Followers</span></div>
									<br></br>
									
									<div id="details_right"><%= followees %></div>						
									<div id="details_left"><span class="big_title">Followees</span></div>
									<br></br>
									
									
											<center>
											
											<input class="blue_button" id="editName" type="submit" name="edit" value="Save Profile">
											</center>
		        					</form>
								
							

				
				
		</div>			
					<!--  tweet Footer -->

					<div class="tweet_footer">
						<div class="tweet_left">
							<div id="tweet_title_label"> </div>
						</div>
					</div>
				</div>	

	</div><!--  Content Panel -->	 	
</body>
</html>