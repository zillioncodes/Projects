import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;

@WebServlet("/FacebookDataToDB")
public class FacebookDataToDB extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FacebookDataToDB() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String about = request.getParameter("about");
		String likes = request.getParameter("likes");
		String img = request.getParameter("img");

		PrintWriter out = response.getWriter();

		try {
			Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			
			if (about != null && likes != null) {
				System.out.println("\nabout: " + about);
				System.out.println("likes: " + likes);

				getFacebookUserData(about, likes);
				
					out.println("success");

					
			} else {
				System.err.println("Null json values received!");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// resultSet is initialised before the first data set
		while (resultSet.next()) {
			// it is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g., resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summary = resultSet.getString("summary");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summary: " + summary);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}

	private void getFacebookUserData(String userProfile, String likes) throws JSONException {

		System.out.println("\nAthletes..");
		JSONObject userProfileObj = new JSONObject(userProfile);
		JSONArray athletes = userProfileObj.getJSONArray("favorite_athletes");
		for (int n = 0; n < athletes.length(); n++) {
			JSONObject object = athletes.getJSONObject(n);
			System.out.println(object.get("name"));
		}

		System.out.println("\nTeams..");
		JSONObject userProfileObj1 = new JSONObject(userProfile);
		JSONArray athletes1 = userProfileObj1.getJSONArray("favorite_teams");
		for (int n = 0; n < athletes1.length(); n++) {
			JSONObject object = athletes1.getJSONObject(n);
			System.out.println(object.get("name"));
		}

		System.out.println("\nInspirational people..");
		JSONObject userProfileObj2 = new JSONObject(userProfile);
		JSONArray athletes2 = userProfileObj2.getJSONArray("inspirational_people");
		for (int n1 = 0; n1 < athletes2.length(); n1++) {
			JSONObject object3 = athletes2.getJSONObject(n1);
			System.out.println(object3.get("name"));
		}

		if (!likes.equals("nodata")) {
			System.out.println("\nLikes..");
			JSONObject rootOfPage = new JSONObject(likes);
			JSONArray geodata = rootOfPage.getJSONArray("data");

			for (int n2 = 0; n2 < geodata.length(); n2++) {
				JSONObject object2 = geodata.getJSONObject(n2);
				System.out.print(object2.get("name"));
				System.out.print(" --> " + object2.get("category") + "\n");
			}
		}
	}
}
