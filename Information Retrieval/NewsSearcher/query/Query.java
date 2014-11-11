package edu.buffalo.cse.irf14.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that represents a parsed query
 * @author nikhillo
 *
 */
public class Query {
	/**
	 * Method to convert given parsed query into string
	 */
	String stringQuery = null;String userStringQuery = null;
	public String toString() {
		//TODO: YOU MUST IMPLEMENT THIS
		return stringQuery;
	}

	public void setString(String strRep) {
		// TODO Auto-generated method stub
		stringQuery = strRep;
	}

	public void setUserString(String userQuery) {
		// TODO Auto-generated method stub
		String str = userQuery; String finalUserString="";
		String regex = "[a-zA-Z0-9:]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while(m.find()){
			if(!(m.group(0).equals(""))){
				if(!(m.group(0).contains("Category:")|| m.group(0).contains("Place:")||m.group(0).contains("Author:"))){
					if(!(m.group(0).equals("AND")||m.group(0).equals("OR")||m.group(0).equals("NOT"))){
						finalUserString = finalUserString + " "+ m.group(0);
					}
				}else if(m.group(0).contains("Category:")|| m.group(0).contains("Place:")||m.group(0).contains("Author:")||m.group(0).contains("Term:")){
				String[] pair = m.group(0).split(":");
				finalUserString = finalUserString + " " +pair[1];
				}
			}
			
		}
		userStringQuery = finalUserString;
		
	}
	public String getUserString() {
		// TODO Auto-generated method stub
		return userStringQuery;
	}
}
