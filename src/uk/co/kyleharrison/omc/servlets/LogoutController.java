package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/LogoutController")
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	
		logOut(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logOut(request, response);
	}
	
	public void logOut(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException
	{
		java.util.Date date= new java.util.Date();
		System.out.println("Log Out Controller : " + new Timestamp(date.getTime()));
		HttpSession session = req.getSession();
				
		if (req.getParameter("logout") != null)
		{
			session.removeAttribute("session");
			req.getSession().invalidate();
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
			rd.forward(req, response);			
		}	
	}

}
