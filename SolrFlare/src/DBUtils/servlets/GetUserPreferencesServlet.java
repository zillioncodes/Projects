package DBUtils.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import util.DBUtilException;
import util.DBUtils;

@WebServlet(name = "GetUserPreferencesServlet", urlPatterns = { "/GetUserPreferencesServlet" })
public class GetUserPreferencesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(GetUserPreferencesServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("username");
		User user = null;
		Connection con = (Connection) getServletContext().getAttribute("DBConnection");
		DBUtils dbUtils = null;
		try {
			dbUtils = new DBUtils(con);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			if (dbUtils.isUserExists(userId)) {
				HttpSession session = request.getSession();
				try {
					user = dbUtils.populateUserData(userId);
				} catch (DBUtilException e) {
					e.printStackTrace();
				}
				session.setAttribute("user", user);

				// String preferenceList="";
				// List<String> prefList=user.getPrefernceList();
				// for(String list:prefList)
				// {
				// if(preferenceList.equals(""))
				// preferenceList=list;
				// else
				// preferenceList=preferenceList+";"+list;
				// }
				// System.out.println(preferenceList);

				/*
				 * user.printString(); try {
				 * dbUtils.InsertPreCatMapIntoDB(userId, prefCatMap); }
				 * catch (DBUtilException e) { e.printStackTrace(); }
				 */
				// RequestDispatcher rd =
				// getServletContext().getRequestDispatcher("/UserDocCacheServlet");
				// rd.include(request, response);
				// response.sendRedirect("home.jsp");
				logger.info("User found");
			} else {
				// RequestDispatcher rd =
				// getServletContext().getRequestDispatcher("/login.html");
				// PrintWriter out= response.getWriter();
				// out.println("<font color=red>"+errorMsg+"</font>");
				// rd.include(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Database connection problem");
			throw new ServletException("DB Connection problem.");
		}

	}

}