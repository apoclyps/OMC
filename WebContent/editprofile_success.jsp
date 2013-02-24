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
									<li><a href="/OMC/Profile">Profile</a></li>
									<li><a href="/OMC/Message">Tweet</a></li>
									<li><a href="/OMC/Home">Timeline</a></li>
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
	
							
	
	 <!--  tweet HEADER -->

			<div class="tweet_title">
							<div class="tweet_left">
								<div class="tweet_title_label"> <span class="blue_font">Profile Edit</span></div>
							</div>
			</div>

	 				
					
						<div class="tweet">

									
									<div class="tweet_body">
										<br>
										<center>Your Profile has Successfully been edited.
										<br><br>
										<span class="blue_font"><a href="/OMC/Home">Return Home</a></span>
										<br></br>
										</center>
									</div>
									<br>
							</div>	
					
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