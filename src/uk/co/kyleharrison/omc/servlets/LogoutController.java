package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;
import java.sql.Timestamp;
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
		//RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
		//rd.forward(request, response);
		
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
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		
		try
		{
			if (req.getMethod().equals("POST"))
			{
				HttpSession session = req.getSession();
				
				if (req.getParameter("logout") != null)
				{
					//Session thisSession = (Session)req.getSession();
					session.removeAttribute("session");
					req.getSession().invalidate();
					System.out.println("User Log out :\t Time : "+dateFormat.format(date));
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
					rd.forward(req, response);			
				}
			}
			else
			{
				System.out.println("User Log out :\t Time : "+dateFormat.format(date));
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
				rd.forward(req, response);
			}
		}catch(Exception e)
		{
			System.out.println("Exception E in Log out");
		}

	}

}
