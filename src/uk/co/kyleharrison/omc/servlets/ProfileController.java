package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.co.kyleharrison.omc.connectors.CassandraConnector;
import uk.co.kyleharrison.omc.connectors.UserConnector;
import uk.co.kyleharrison.omc.model.Session;
import uk.co.kyleharrison.omc.stores.UserStore;

/**
* Servlet implementation class ProfileController
*/
@WebServlet("/ProfileController")
public class ProfileController extends HttpServlet {
private static final long serialVersionUID = 1L;
       
    /**
* @see HttpServlet#HttpServlet()
*/
    public ProfileController() {
        super();
    }
	
	/**
	* @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

			if(session.getAttribute("isActive")!= null)
			{
				if ((Session)session.getAttribute("session") != null)
				{
					Session thisSession = (Session)session.getAttribute("session");
					request.setAttribute("session", thisSession);
					UserStore profile = null;
					
					CassandraConnector connector = new CassandraConnector();
				
					if (connector.connect())
					{
						session.setAttribute("profile", profile);
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/profile.jsp");
						rd.forward(request, response);
					}
				}
			}
			else
			{
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
				rd.forward(request, response);
			}
	}
	
	/**
	* @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] Edit = request.getParameterValues("edit");
		//System.out.println("Edit Profile : "+Edit[0]);
		if(Edit[0].equals("Edit Profile"))
		{
			//System.out.println("Edit Profile"+Edit[0]);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/editprofile.jsp");
			rd.forward(request, response);
		}
		else if(Edit[0].equals("Save Profile"))
		{

			boolean error = false;
			try{
				UserConnector UC = new UserConnector();
				UserStore Author = new UserStore();
				HttpSession session = request.getSession();
				Session currentUserSession = (Session)session.getAttribute("session");

				String username = currentUserSession.getUsername();
				Author = UC.getUserByUsername(username);
				String email = Author.getEmail();
				Author = UC.getProfileByEmail(email);
				
				Author.setBio(request.getParameter("bio").toString());
				Author.setAge(request.getParameter("age").toString());
				Author.setName(request.getParameter("firstname").toString());
				Author.setSurname(request.getParameter("surname").toString());
				Author.setLocation(request.getParameter("location").toString());
							
				UC.updateUserProfile(Author);
	
			}catch(Exception e)
			{
				System.out.println("Error in profile controller "+e.getMessage());
				error = true;
			}
			
			if(!error)
			{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/editprofile_success.jsp");
			rd.forward(request, response);
			}
			else
			{
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/editprofile_failed.jsp");
				rd.forward(request, response);
			}
		}
		else
		{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
			rd.forward(request, response);
		}
		

		

	}

}