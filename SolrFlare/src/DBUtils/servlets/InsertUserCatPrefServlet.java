package DBUtils.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import util.DBUtilException;
import util.DBUtils;

@WebServlet("/InsertUserCatPrefServlet")
public class InsertUserCatPrefServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(InsertUserCatPrefServlet.class);  

	public InsertUserCatPrefServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Change the string inside getParameter according to your input field name
		
		User userMetadata = (User) request.getSession().getAttribute("user");
		String userId = userMetadata.getUserId();
		Map<String,List<String>> CatPrefMap= new HashMap<String, List<String>>();
		List<String> prefList=null;
		String name=null;
		String value=null;
		String[] preference=null;
		boolean successFlag=false;
		String scicategory="",lifCategory="",enCategory="",traCategory="";
		String BussCategory="",autCategory="",worCategory="",misCategory="";
		boolean sciCategory=false, lifeCategory=false,entCategory=false,travCategory=false;
		boolean busCategory=false,autoCategory=false,worldCategory=false,miscCategory=false;
		//Variant1-->It is based on the assumption that the field name of category will begin with c or C and preferences will be separated by ';' and coming from a single textbox .
		Enumeration<String> enumParams = request.getParameterNames();
		for (;enumParams.hasMoreElements();) {
			name = (String)enumParams.nextElement();
			value = request.getParameter(name);
		
			if(name.startsWith("c") || name.startsWith("C") && value!="" && value!=null)
			{
				if(name.contains("science") )
				{

					if(value.equals("true"))
					{
						sciCategory=true;
						scicategory="Science and Technology";
					}
				}
				if(name.contains("lifestyle"))
				{

					if(value.equals("true"))
					{
						lifeCategory=true;
						lifCategory="Lifestyle";
					}
				}
				if(name.contains("entertainment"))
				{

					if(value.equals("true"))
					{
						entCategory=true;
						enCategory="Entertainment";
					}
				}
				if(name.contains("travel"))
				{

					if(value.equals("true"))
					{
						travCategory=true;
						traCategory="Travel";
					}
				}
				if(name.contains("business"))
				{

					if(value.equals("true"))
					{
						busCategory=true;
						BussCategory="Business";
					}
				}
				if(name.contains("automobiles"))
				{

					if(value.equals("true"))
					{
						autoCategory=true;
						autCategory="Automobiles";
					}
				}
				if(name.contains("worldnews"))
				{

					if(value.equals("true"))
					{
						worldCategory=true;
						worCategory="World news";
					}
				}
				if(name.contains("misc"))
				{

					if(value.equals("true"))
					{
						miscCategory=true;
						misCategory="Miscellaneous";
					}
				}
			}
			else
			{
				if(name.equals("science_prefs") && sciCategory)
				{
					prefList=new ArrayList<String>();
					preference=value.split(";");
					for(int i=0;i<preference.length;i++)
					{
						prefList.add(preference[i]);
					}
					CatPrefMap.put(scicategory, prefList);
				}
				if(name.equals("lifestyle_prefs") && lifeCategory)
				{
					prefList=new ArrayList<String>();
					preference=value.split(";");
					for(int i=0;i<preference.length;i++)
					{
						prefList.add(preference[i]);
					}
					CatPrefMap.put(lifCategory, prefList);
				}
				if(name.equals("entertainment_prefs") && entCategory)
				{
					prefList=new ArrayList<String>();
					preference=value.split(";");
					for(int i=0;i<preference.length;i++)
					{
						prefList.add(preference[i]);
					}
					CatPrefMap.put(enCategory, prefList);
				}
				if(name.equals("travel_prefs") && travCategory)
				{
					prefList=new ArrayList<String>();
					preference=value.split(";");
					for(int i=0;i<preference.length;i++)
					{
						prefList.add(preference[i]);
					}
					CatPrefMap.put(traCategory, prefList);
				}
				if(name.equals("business_prefs") && busCategory)
				{
					prefList=new ArrayList<String>();
					preference=value.split(";");
					for(int i=0;i<preference.length;i++)
					{
						prefList.add(preference[i]);
					}
					CatPrefMap.put(BussCategory, prefList);
				}
				if(name.equals("automobiles_prefs") && autoCategory)
				{
					prefList=new ArrayList<String>();
					preference=value.split(";");
					for(int i=0;i<preference.length;i++)
					{
						prefList.add(preference[i]);
					}
					CatPrefMap.put(autCategory, prefList);
				}

				if(name.equals("worldnews_prefs") && worldCategory)
				{
					prefList=new ArrayList<String>();
					preference=value.split(";");
					for(int i=0;i<preference.length;i++)
					{
						prefList.add(preference[i]);
					}
					CatPrefMap.put(worCategory, prefList);
				}
				if(name.equals("misc_prefs") && miscCategory)
				{
					prefList=new ArrayList<String>();
					preference=value.split(";");
					for(int i=0;i<preference.length;i++)
					{
						prefList.add(preference[i]);
					}
					CatPrefMap.put(misCategory, prefList);
				}

			}
		}
		Connection con = (Connection) getServletContext().getAttribute("DBConnection");
		DBUtils dbUtils = null;
		try {
			dbUtils = new DBUtils(con);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try
		{
			if(dbUtils.isUserExists(userId))
			{
				try {
					//Method for inserting the CCategoryPrefernce Map into DB.
					if(CatPrefMap!=null)
						successFlag=dbUtils.insertPreCatMapIntoDB(userId, CatPrefMap);
					response.sendRedirect("Home.jsp");
				} 
				catch (DBUtilException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.err.println("User doesn't exist");
			}
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/ClustererServlet");
			rd.include(request, response);

		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Database connection problem");
			throw new ServletException("DB Connection problem.");
		}
	}
}