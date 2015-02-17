package DBUtils.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class MoreLikeThis
 */
@WebServlet("/MoreLikeThis")
public class MoreLikeThis extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MoreLikeThis() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String query = String.valueOf(session.getAttribute("query"));
		URL url = new URL("http://localhost:8983/solr/collection1/select?q="+query+"&start=30&row=5&wt=json&indent=true&mlt=true&mlt.fl=title,snippet&mlt.boost=true");
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String thisLine = null;
			StringBuilder output = new StringBuilder();
			while ((thisLine = br.readLine()) != null) {
				output.append(thisLine+"\n");
			}
			conn.disconnect();

			JSONObject jsonObject;

			jsonObject = new JSONObject(output.toString());
			JSONObject myResponse = jsonObject.getJSONObject("response");
			JSONArray tsmresponse = (JSONArray) myResponse.get("docs");
			PrintWriter writer = response.getWriter();
			for(int i=0; i<tsmresponse.length(); i++){
				writer.append("<h4>"+tsmresponse.getJSONObject(i).getJSONArray("title").getString(0)+"</h4>");
				String desc = tsmresponse.getJSONObject(i).getString("description");
				if(desc.length()>200){
					writer.append(desc.substring(0, 200));
					writer.append("<span style=\"display:none;\">"+ desc.substring(200));
					writer.append("</span> <a href=\"moreClick();\" class=\"more\">more</a>");
				} else {
					writer.append(desc);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
