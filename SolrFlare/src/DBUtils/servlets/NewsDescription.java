package DBUtils.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class NewsDescription
 */
@WebServlet(name = "NewsDescription", urlPatterns = { "/loadNewsDescription" })
public class NewsDescription extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewsDescription() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		PrintWriter printWriter = response.getWriter();
		printWriter.write(String.valueOf(session.getAttribute("desc")));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String description = request.getParameter("description");
		HttpSession session = request.getSession();
		try {
			if(null != description) {
				description = description.replace("<span style=\"display:none;\">", "").replace("</span> <a href=\"#\" class=\"more\">more</a>", "");
			}
			session.setAttribute("desc", description);
			//increase category weight here.
			
			PrintWriter printWriter = response.getWriter();
			printWriter.write("success");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
