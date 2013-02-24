package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.co.kyleharrison.omc.connectors.CassandraConnector;
import uk.co.kyleharrison.omc.model.Session;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		attemptLogin(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		attemptLogin(request, response);
		
	}
	
	private void attemptLogin(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException 
	{
			HttpSession session = req.getSession();
		
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
			if(session.getAttribute("isActive")== null)
			{
				//System.out.println("User :\t"+username + " Attempting Login \n With Password :\t " + password);
				
				if(username !=null && password != null)
				{
					CassandraConnector CC = new CassandraConnector();
					
					if (CC.connect())
					{
						if(CC.attemptLogin(username, password))
						{
							Session thisSession = new Session();
							thisSession.setUsername(username);
	
							session.setAttribute("session", thisSession);
							session.setAttribute("isActive", true);
	
							DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
							Date date = new Date();
							System.out.println("User Login :"+thisSession.getUsername()+"\t Time : "+dateFormat.format(date));
							
							Session currentUserSession = (Session)session.getAttribute("session");
							currentUserSession.setUsername(req.getParameter("username"));
							session.setAttribute("Session", currentUserSession);

						}				
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
						rd.forward(req, response);
					}
					else
					{					
						session.setAttribute("invalid_login", true);
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
						rd.forward(req, response);
					}
				}
				else{
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
					rd.forward(req, response);
				}
			}else
			{
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
				rd.forward(req, response);
			}
	}

}
