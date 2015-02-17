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
 * Servlet implementation class Suggestor
 */
@WebServlet("/Suggestor")
public class Suggestor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Suggestor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String query = "int";//String.valueOf(session.getAttribute("query"));
		URL url = new URL("http://localhost:8983/solr/collection1/suggest?q="+query+"&wt=json&indent=true");
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
			JSONObject myResponse = jsonObject.getJSONObject("spellcheck");
			JSONObject tsmresponse1 = (JSONObject)myResponse.getJSONArray("suggestions").get(1);
			JSONArray tsmresponse = (JSONArray)tsmresponse1.getJSONArray("suggestion");
			PrintWriter writer = response.getWriter();
			for(int i=0; i<tsmresponse.length(); i++){
				writer.append(String.valueOf(tsmresponse.get(i))+" ");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
