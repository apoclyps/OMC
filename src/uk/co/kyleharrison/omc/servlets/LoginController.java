package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.co.kyleharrison.omc.model.Session;
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
			
			//System.out.println("User :\t"+username + " Attempting Login \n With Password :\t " + password);
			
			if(username !="" && password != "")
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
					
					session.setAttribute("isActive", true);
					
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
	}

}
