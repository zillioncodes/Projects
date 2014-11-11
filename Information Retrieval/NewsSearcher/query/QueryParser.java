/**
 * 
 */
package edu.buffalo.cse.irf14.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo
 * Static parser that converts raw text to Query objects
 */
public class QueryParser {
	public static String[] toQuery;
	/**
	 * MEthod to parse the given user query into a Query object
	 * @param userQuery : The query to parse
	 * @param defaultOperator : The default operator to use, one amongst (AND|OR)
	 * @return Query object if successfully parsed, null otherwise
	 */
	public static Query parse(String userQuery, String defaultOperator) {
		//TODO: YOU MUST IMPLEMENT THIS METHOD
		if(userQuery.contains("\"")){
		userQuery = userQuery.replaceAll("\"","");
		}
		if(userQuery.equals(" ")|| userQuery.isEmpty()|| userQuery==null){
			System.out.println("Is Empty Query or is Null!");
		}
		Query query = new Query();
		query.setUserString(userQuery);
		String str = userQuery; String strRep = null, retString = "";
		if(str!=null){
			String[] strHold;
			str = defaultOp(str, "OR");
			int index = 0, indexInc = 0, indexOf=0;
			if(str.contains("Category:")){
				str = stringProcess(str,"Category");
				
			}
 			if(str.contains("Author:")){
				str = stringProcess(str,"Author");
				
			}
			if(str.contains("Place:")){
				str = stringProcess(str,"Place");
				
			}
			str = str.replaceAll("[(]", "[ ");
			str = str.replaceAll("[)]", " ]");
		}
		strRep = regCorrect(str, "Term:");
		String[] strArr = strRep.split(" ");
		for(int i=0;i<strArr.length;i++){
			if(strArr[i].equals("NOT")){
				strArr[i] = "AND";
				strArr[i+1]= "<"+strArr[i+1]+">";
			}
			if(i==0){retString = strArr[i];}
			else{retString = retString+" "+strArr[i];}
		}
		//retString = "[ "+retString+" ]";
		query.setString(retString);
		return query;
	}
	
	private static String regCorrect(String str, String field) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(str);int index = 0; int indexInc = 0;
		String regex = "[a-zA-Z0-9:]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while(m.find()){
			if(!(m.group(0).equals(""))){
				if(!(m.group(0).contains("Category:")|| m.group(0).contains("Place:")||m.group(0).contains("Author:"))){
					if(!(m.group(0).equals("AND")||m.group(0).equals("OR")||m.group(0).equals("NOT"))){
						index = m.start();
						index = index + indexInc;
						sb.insert(index, field);
						indexInc = indexInc+field.length();
					}
				}
			}
		}
		str = sb.toString();
		return str;
	}

	private static String stringProcess(String str, String field) {
		// TODO Auto-generated method stub
		String[] strHold;int index = 0; int indexInc = 0;int j=1;
		//str = str.replace(field,""); 
		String regCorr="", string1="";
		field = field+":";
		strHold = str.split(field);
		strHold[0] = strHold[0]+field;
		while(j<strHold.length){
		if(strHold[j].trim().charAt(0)=='('){
				String reg = "\\((.*?)\\)";
				Pattern p = Pattern.compile(reg);
				Matcher m = p.matcher(strHold[j]);
				if(m.find()){
					regCorr = regCorrect(m.group(0), field);
					strHold[0] = strHold[0].replace(field, "");
				    strHold[j] = strHold[j].replace(m.group(0), regCorr);
					}
				}
		j++;
		}
		for(String s: strHold){
		string1 = string1+s;
		}
		return string1;
		
	}
	
	private static String defaultOp(String str, String string) {
		// TODO Auto-generated method stub
		String[] strHold = str.split(" ");
		String strWidDefOp = "";
		for(int i=0;i<strHold.length;i++){
			if(!(strHold[i].equals("AND")||strHold[i].equals("OR")||strHold[i].equals("NOT"))){
				if(i+1==strHold.length){
					strWidDefOp = strWidDefOp + strHold[i];
					continue;
				}else if(!(strHold[i+1].equals("AND")||strHold[i+1].equals("OR")||strHold[i+1].equals("NOT"))){
						if(i==0){strWidDefOp = strHold[i] + " OR ";}
						else{strWidDefOp = strWidDefOp + strHold[i]+" "+"OR"+" ";}
						continue;
				}
			}
			strWidDefOp = strWidDefOp + strHold[i]+" ";
		}
		return strWidDefOp;
	}
}
