package DBUtils.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class semAnalysis
 */
@WebServlet("/SemanticAnalysis")
public class SemanticAnalysis extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SemanticAnalysis() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		String phrase1 = String.valueOf(session.getAttribute("query"));
		Map<String,Float> pMap=new HashMap<String,Float>();	
		String[] prefs ={ "technology","engineering","computers","zurich","travel" };
		for(String s:prefs){
		URL url = new URL("http://swoogle.umbc.edu/StsService/GetStsSim?operation=api&"+"phrase1="+phrase1+"&"+"phrase2="+s);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output=br.readLine();
		conn.disconnect();
		pMap.put(s,Float.parseFloat(output));
		}
		
		//response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String,Float> sortedmap = new HashMap<String,Float>();
		sortedmap = SortByValue(pMap);int i=0;
		String[] preferences = new String[3];
		StringBuilder stringB = new StringBuilder();
		for(Entry<String,Float> etr:sortedmap.entrySet()){
			preferences[i] = etr.getKey();
			stringB.append(preferences[i]+" ");
			i++;
			if(i>2){
				break;
			}
		}
		PrintWriter writer = response.getWriter();
		writer.write(stringB.toString());
	}
	
	public static Map<String, Float> SortByValue(Map<String, Float> pMap) {
		// TODO Auto-generated method stub
		ValueComparator vc =  new ValueComparator(pMap);
		TreeMap<String,Float> sortedMap = new TreeMap<String,Float>(vc);
		sortedMap.putAll(pMap);
		return sortedMap;
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	

}
class ValueComparator implements Comparator<String> {
	 
    Map<String,Float> map;
 
    public ValueComparator(Map<String, Float> fileScoreMap) {
        this.map = fileScoreMap;
    }
 
    public int compare(String a, String b) {
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys 
    }
}


