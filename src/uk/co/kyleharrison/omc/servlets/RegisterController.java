package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.kyleharrison.omc.connectors.UserConnector;
import uk.co.kyleharrison.omc.stores.UserStore;

/**
 * Servlet implementation class SignupController
 */
@WebServlet("/RegisterController")
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reg_first_name = request.getParameter("reg_first_name");
		String reg_surname = request.getParameter("reg_surname");
		String reg_username = request.getParameter("reg_username");
		String reg_password = request.getParameter("reg_password");
		String reg_password_confirm = request.getParameter("reg_password_confirm");
		String reg_email = request.getParameter("reg_email");
				
			try
			{
			UserStore Author = new UserStore();
			
			Author.setName(reg_first_name);
			Author.setUserName(reg_username);
			Author.setSurname(reg_surname);
			Author.setEmail(reg_email);
			Author.setBio("Enter Description");
			Author.setAvatar("img/avatar.png");
			Author.setPassword(reg_password);
			Author.setAge("Enter Age");
			Author.setJoined();
			Author.setLocation("Enter Location");
			Author.setPosts("0");
			Author.setFollowees("0");
			Author.setFollowers("0");	
			
			UserConnector UC = new UserConnector();
			UC.addUser(Author);
			
			}
			catch(Exception e)
			{
				System.out.println("Error creating account "+e.getMessage());
			}
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
			rd.forward(request, response);
		
	}

}
