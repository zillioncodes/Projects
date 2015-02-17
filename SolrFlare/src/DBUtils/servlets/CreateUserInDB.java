package DBUtils.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import util.ServletUtils;

@WebServlet(name = "CreateUserInDB", urlPatterns = { "/CreateUserInDB" })
public class CreateUserInDB extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(CreateUserInDB.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String isFacebookUser = request.getParameter("isfacebookuser");
		String errorMsg = null;
		PrintWriter out = response.getWriter();

		/* Timestamp */
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		
		if (email == null || email.equals("")) {
			errorMsg = "Email ID can't be null or empty.";
		}
		if (password == null || password.equals("")) {
			errorMsg = "Password can't be null or empty.";
		}
		if (name == null || name.equals("")) {
			errorMsg = "Name can't be null or empty.";
		}

		if (errorMsg != null) {
			out.println(errorMsg);
		} else {
			Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			PreparedStatement ps = null;
			try {
				ps = con.prepareStatement("insert into user(idUser,nameUser,email,password,lastLogin,active) values (?,?,?,?,?,?)");
				ps.setString(1, username);
				ps.setString(2, name);
				ps.setString(3, email);
				ps.setString(4, password);
				ps.setString(5, datetime);
				ps.setString(6, "Y");

				ps.execute();

				logger.info("User registered with email=" + email);
				out.println("success");

				request.getSession().setAttribute("isLoggedIn", true);

				request.setAttribute("username", username);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/GetUserPreferencesServlet");
				rd.include(request, response);
				
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Database connection problem");
				throw new ServletException("DB Connection problem.");
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException("Problem creating new user.");
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("SQLException in closing PreparedStatement");
				}
			}
		}
		
		boolean isFBUser = isFacebookUser.equals("true") ? true : false;
		User userdata = new User(username, name, email, password, datetime, "Y", isFBUser, null, null, null);
		ServletUtils.addUserToSession(username, userdata, request);
	}
}