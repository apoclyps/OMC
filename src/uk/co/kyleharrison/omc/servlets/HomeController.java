package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.co.kyleharrison.omc.model.Session;

/**
 * Servlet implementation class HomeController
 */
@WebServlet("/HomeController")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeController() {
        super();
        // TODO Auto-generated constructor stub
        //System.out.println("Home Controller");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		directToHome(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		directToHome(request, response);
	}
	
	private void directToHome(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		HttpSession session = req.getSession();
		Session currentUserSession = (Session)session.getAttribute("Session");
		
	//System.out.println("Home controller");
		try
		{
			//System.out.println("currentSession = "+currentUserSession.toString());
		if(currentUserSession != null)
		{
			//System.out.println("currentSession = "+currentUserSession.getUsername());
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/home.jsp");
			rd.forward(req, res);
		}			
		else
		{
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
			rd.forward(req, res);
		}
		}catch (Exception e)
		{
			System.out.print(e.getMessage()+"Home Controller");
		}
	}

}
