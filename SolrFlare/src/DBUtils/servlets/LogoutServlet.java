package DBUtils.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import util.DBUtils;

@WebServlet(name = "Logout", urlPatterns = { "/Logout" })
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(LogoutServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Connection connection = (Connection)
		// request.getServletContext().getAttribute("DBConnection");
		// DBUtils dbUtils = new DBUtils(connection);
		// dbUtils.insertUserDocMapInDB();

		response.setContentType("text/html");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {
					logger.info("JSESSIONID=" + cookie.getValue());
					break;
				}
			}
		}
		// invalidate the session if exists
		HttpSession session = request.getSession(false);
		if (session != null) {
			if (session.getAttribute("user") != null)
				session.setAttribute("user", null);
			logger.info("User=" + session.getAttribute("name"));
			Object obj = session.getAttribute("isLoggedIn");
			if (obj != null) {
				boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
				if (session != null && isLoggedIn) {
					session.invalidate();
				}
			}
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.write("success");
	}

}