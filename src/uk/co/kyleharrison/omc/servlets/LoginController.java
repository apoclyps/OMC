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

import uk.co.kyleharrison.omc.model.Session;
import uk.co.kyleharrison.omc.model.Login;

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
        System.out.println("Login Controller");
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
			String isActive = req.getParameter("isActive");
			
			if(isActive==null)
			{
				System.out.println("User :\t"+username + " Attempting Login \n With Password :\t " + password);
				
				if(username !=null && password != null )
				{
					Login login = new Login(username, password);
					
					boolean success = false;
					
					if (login.setup())
					{
						success = login.execute();
					}
					
					//System.out.println("Login Controller -"+ success);
					if (success)
					{
						Session thisSession = login.createSession();
						session.setAttribute("session", thisSession);
						System.out.println("isActive login"+session.getAttribute("isActive"));
						session.setAttribute("isActive", true);
						System.out.println("isActive login"+session.getAttribute("isActive"));
						
						DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
						Date date = new Date();
						System.out.println("User Login :"+thisSession.getUsername()+"\t Time : "+dateFormat.format(date));
						
						
						Session currentUserSession = (Session)session.getAttribute("session");
						currentUserSession.setUsername(req.getParameter("username"));
						System.out.println("Username set on login : "+req.getParameter("username"));
						session.setAttribute("Session", currentUserSession);

						RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
						rd.forward(req, response);
					}
					else
					{					
						req.setAttribute("invalid_login", true);
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
