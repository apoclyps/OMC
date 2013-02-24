package uk.co.kyleharrison.omc.servlets;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.prettyprint.hector.api.exceptions.HectorException;

import uk.co.kyleharrison.omc.connectors.TweetConnector;
import uk.co.kyleharrison.omc.model.Session;
import uk.co.kyleharrison.omc.stores.TweetStore;
import uk.co.kyleharrison.omc.utils.StringSplitter;

/**
 * Servlet implementation class NewPostController
 */
@WebServlet("/NewPostController")
public class TweetController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private HashMap CommandsMap = new HashMap();  
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TweetController() {
        super();
        CommandsMap.put("",0);
        CommandsMap.put("#id",1);
        CommandsMap.put("#name",2);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();

		if(session.getAttribute("isActive")!= null)
		{
		
		StringSplitter SS = new StringSplitter();
		String args[]  = SS.SplitRequestPath(request);
		
		//System.out.println("Args = "+args.length);
		
		for(int i=0;i<args.length;i++)
		{
			//System.out.println("Args = "+i + " "+ args[i].toString());
		}
		
		if(args.length==3)
		{
			if(args[2]=="id")
			{
				String username = request.getPathInfo();
				username = username.replace("/", "");
			//	System.out.println("id" +username);
			}
			else if(args[3]=="name")
			{
				String username = request.getPathInfo();
				username = username.replace("/", "");
				//System.out.println("name" +username);
			}
		}
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/tweet.jsp");
		rd.forward(request, response);
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
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("isActive")!= null)
		{
			
			Session currentUserSession = (Session)session.getAttribute("session");
			
			String username = currentUserSession.getUsername();
			String body = request.getParameter("body");
			String tags = request.getParameter("tags");
					
			try
			{
				TweetConnector TC = new TweetConnector();
				TweetStore TS = new TweetStore();
				
				TS.setUser(username);
				TS.setTweetID(username);
				TS.setContent(body);
				TS.setTags(tags);
				
				boolean TweetAdded = TC.addTweet(TS);
				
				if(TweetAdded)
				{
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/post_success.jsp");
					rd.forward(request, response);
				}
				else
				{
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/post_fail.jsp");
					rd.forward(request, response);
				}
			}catch(HectorException e)
			{
				System.out.println("Exception creating post");
			}
		}else
		{
			System.out.println("error tweeting");
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/Home");
			rd.forward(request, response);
		}
	}

}
