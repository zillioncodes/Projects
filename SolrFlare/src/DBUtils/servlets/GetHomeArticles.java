package DBUtils.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import util.ClusterUtils;
import util.DBUtils;

@WebServlet("/GetHomeArticles")
public class GetHomeArticles extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(GetHomeArticles.class);

	public GetHomeArticles() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String category = request.getParameter("category");
		User userMetadata = (User) request.getSession().getAttribute("user");
		Connection con = (Connection) getServletContext().getAttribute("DBConnection");
		DBUtils dbUtils = null;
		String data = "";
		try {
			dbUtils = new DBUtils(con);

			String userId = null;
			if (userMetadata != null)
				userId = userMetadata.getUserId();
			// userId = "3002";

			/* Get list of similar users */
			List<String> similarUsers = ClusterUtils.getOtherUsersInMyCluster(userId);
			System.out.println(similarUsers);

			/*
			 * Get doc IDs for these users, of articles not read by current
			 * user
			 */
			if (similarUsers != null && !similarUsers.isEmpty()) {
				List<String> docIds = dbUtils.getDocIdsFromSimilarUsers(userId, similarUsers, category);

				int i = 0;
				for (String docId : docIds) {
					String docData = getArticleData(docId);
					if (docData != null)
						data += "<div>" + docData + "</div>";
					if (++i >= 3)
						break;
					if (i != docIds.size())
						data += "<br>";
				}
			} else {
				System.err.println("No similar users were found.");
			}

			PrintWriter out = response.getWriter();
			out.write(data);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getArticleData(String docId) {
		StringBuilder writer = null;
		try {
			URL url = new URL("http://localhost:8983/solr/get?wt=json&id=" + docId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String thisLine = null;
			StringBuilder output = new StringBuilder();
			while ((thisLine = br.readLine()) != null) {
				output.append(thisLine + "\n");
			}
			conn.disconnect();

			JSONObject jsonObject = new JSONObject(output.toString());
			JSONObject tsmresponse = jsonObject.getJSONObject("doc");
			writer = new StringBuilder();
			// for (int i = 0; i < tsmresponse.length(); i++) {
			if (tsmresponse != null) {
				writer.append("<h4>" + tsmresponse.getJSONArray("title").getString(0) + "</h4>");
				String desc = tsmresponse.getString("description");
				if (desc.length() > 200) {
					writer.append(desc.substring(0, 200));
					writer.append("<span style=\"display:none;\">" + desc.substring(200) + "</span>");
				} else {
					writer.append(desc);
				}
				writer.append(" <a href=\"moreClick();\" class=\"more\">more</a>");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer != null ? writer.toString() : null;
	}
}