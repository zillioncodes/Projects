package DBUtils.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import util.DBUtils;

@WebServlet("/AuthenticateUser")
public class AuthenticateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DBUtils.class);
	private Connection connection;

	public AuthenticateUser(String dbURL, String user, String pwd) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = DriverManager.getConnection(dbURL, user, pwd);
	}

	public AuthenticateUser(Connection con) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = con;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public AuthenticateUser() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String responseMessage = "";
		
		ServletContext context = request.getSession().getServletContext();
		this.connection = (Connection) context.getAttribute("DBConnection");
		HttpSession session = request.getSession();
		
		try {
			DBUtils dbUtils = new DBUtils(connection);
			boolean exists = dbUtils.isUserExists(username);
			
			if (exists) {
				session.setAttribute("name", username);
				if (dbUtils.authenticateUser(username, password)) {
					session.setAttribute("isLoggedIn", true);
					responseMessage = "success";
				} else {
					responseMessage = "Invalid password for user '" + username + "'";
				}
			} else {
				responseMessage = "User '" + username + "' does not exist.";
			}
			
			PrintWriter printWriter = response.getWriter();
			printWriter.write(responseMessage);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
