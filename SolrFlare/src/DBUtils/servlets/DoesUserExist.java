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

import org.apache.log4j.Logger;

import util.DBUtils;

@WebServlet("/DoesUserExist")
public class DoesUserExist extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DBUtils.class);
	private Connection connection;

	public DoesUserExist(String dbURL, String user, String pwd) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = DriverManager.getConnection(dbURL, user, pwd);
	}

	public DoesUserExist(Connection con) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = con;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public DoesUserExist() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		
		ServletContext context = request.getSession().getServletContext();
		this.connection = (Connection) context.getAttribute("DBConnection");
		
		try {
			DBUtils dbUtils = new DBUtils(connection);
			boolean exists = dbUtils.isUserExists(username);
			
			if (exists)
				request.getSession().setAttribute("isLoggedIn", true);
			
			PrintWriter printWriter = response.getWriter();
			printWriter.write(exists ? "yes" : "no");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
