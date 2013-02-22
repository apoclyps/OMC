package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.kyleharrison.omc.model.Register;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String reg_first_name = request.getParameter("reg_first_name");
		String reg_surname = request.getParameter("reg_surname");
		String reg_username = request.getParameter("reg_username");
		String reg_password = request.getParameter("reg_password");
		String reg_password_confirm = request.getParameter("reg_password_confirm");
		String reg_email = request.getParameter("reg_email");
		
		String default_avatar = "/OMC/img/avatar.png";
		
		Register newAccount = new Register(reg_first_name, reg_surname, reg_username, reg_password, reg_email, default_avatar);
		
		if (newAccount.execute())
		{
			System.out.println("newAccount.execute() succeeded - new account created.");
			request.setAttribute("account_created", true);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
			rd.forward(request, response);
		}
		else
		{
			System.out.println("newAccount.execute() failed.");
		}
		
	}

}
