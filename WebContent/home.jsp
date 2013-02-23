<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
     <%@ page import="uk.co.kyleharrison.omc.model.*" %>
     <%@ page import="uk.co.kyleharrison.omc.stores.*" %>
     <%@ page import="uk.co.kyleharrison.omc.connectors.*" %>
     <%@ page import="java.util.*" %>
      
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>On My Campus - Home</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="shortcut icon" href="img/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/main_style.css">
<link rel="stylesheet" type="text/css" href="css/tweet_style.css">

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
									<li><a href="home.jsp">Profile</a></li>
									<li><a href="friends.jsp">Friends</a></li>
									<li><a href="timeline.jsp">Timeline</a></li>
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


		
					
					<!--  POST ENDS HERE -->
		
			<div id="tweet_pane">
			<!--  tweet HEADER -->
										<%
											Session thisSession = (Session)request.getAttribute("Session");
																												String username = thisSession.getUsername();
										%> 	
			

					
					<!--  tweet TWO -->
			<%
				TweetConnector TC = new TweetConnector();
			
				TweetStore TS = new TweetStore();
			
				
			%>
						
							
			<div class="tweet_title">
							<div class="tweet_left">
								<div class="tweet_title_label"> <span class="blue_font">Post a Tweet</span></div>
			</div>
			</div>		
							<div class="tweet_post">
					<div class="big_text">Share with the Campus...</div>
					<form name="post_tweet" method="POST" action="./Message">
						
						<textarea id="body_text" name="body" rows="5" cols="30" placeholder="What's going on around Campus <%=username%>?"></textarea><br/>
						<!--  <div class="small_text">What's going on around Campus?</div>-->
						
						<div class="big_text"># Tag </div>
						<!-- <div class="small_text">May the # be with you!</div>-->
						<input type="text" id="tags_text" name="tags" placeholder="May the # be with you!"><br/>
						
						<center><input class="blue_button" id="make_new_post" type="submit" value=" Submit Post"></center>
					</form>
				
				</div>
					
					<!--  tweet Footer -->

					<div class="tweet_footer">
						<div class="tweet_left">
							<div id="tweet_title_label"> </div>
						</div>
					</div>
					
					
			
			
	 
	 <!--  tweet HEADER -->

			<div class="tweet_title">
							<div class="tweet_left">
								<div class="tweet_title_label"> <span class="blue_font">Tweets</span></div>
							</div>
			</div>
	 	<%
	 				
					List<TweetStore> posts = TC.getTweets(username);
					Iterator<TweetStore> iterator;
					
					
					
					iterator = posts.iterator();
					while (iterator.hasNext())
					{ 
						TweetStore row = (TweetStore)iterator.next();
						String full_name = row.getUser();
						String tags_2 = row.getTags();
						String body = row.getContent();
						String date = row.getTimeStampString();
						
						%>
						<div class="tweet">
								<div class="tweet_left">
									<div class="tweet_avatar">
										<img src="img/avatar.png" />
									</div>
								</div>
							
								<div class="tweet_right">
									<div class="tweet_username">
									<%=full_name%> says..
									</div>
									
									<div class="tweet_date">
										<%= date %>
									</div>
									
									<div class="tweet_body">
										<%=body %>
									</div>
									<br>
									<div class="tweet_tags">
										<%=tags_2%>
									</div>
								</div>
							</div>	
						<%
					}
					
					%>
					
										<!--  tweet Footer -->

					<div class="tweet_footer">
						<div class="tweet_left">
							<div id="tweet_title_label"> </div>
						</div>
					</div>
			 <!-- End TWEET PANEL -->		
			</div>		
	 
		 	</div><!--  Content Panel -->
</body>
</html>