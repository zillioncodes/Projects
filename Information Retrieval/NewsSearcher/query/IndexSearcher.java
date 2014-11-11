package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.irf14.SearchRunner.ScoringModel;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;

public class IndexSearcher {
    ScoringModel mdl;String indexDir = null;
	int count=0,j=0;
	public IndexSearcher(ScoringModel model,String indxDir){
		//indexDir = "C:/Users/manish/workspace/newsIndexerSearch/Indexes"; 
		indexDir = indxDir;
	    mdl = model;
	}

	public ArrayList<String> consumeQuery(Query query) {
		// TODO Auto-generated method stub
		String[] splitOfQuery=null; String[] splits; int k=0,j=0, startIndex=0, endIndex=0;char[] array=null;
		char[] charArray = query.toString().toCharArray(); String substr;
		String queryString = query.toString();
		String userQuery = query.getUserString();
		ArrayList<String> ordArray  =  orderString(queryString);
		ArrayList<String> result = processQuery(ordArray,userQuery);
		return result;
	}

	public ArrayList<String> processQuery(ArrayList<String> ordArray, String queryStr) {
		// TODO Auto-generated method stub
		int j=0; String[] pair; ArrayList<String> postFreq; String[] query = new String[100] ;int k=0,t=0;
		HashMap<String,ArrayList<String>> stackFiles = new HashMap<String, ArrayList<String>>();int nOp=0;
		if(ordArray.size()==1){
			String[] ordArr = {ordArray.get(j)};
			stackFiles = splitPosting(ordArr,stackFiles);
			String[] str = ordArray.get(j).split(":");
			stackFiles.put("Query", stackFiles.get(str[1]));
		}else{
			while(ordArray.size()>1){
				if(ordArray.get(j).equals("AND")||ordArray.get(j).equals("OR")||ordArray.get(j).equals("NOT")){
					if(ordArray.get(j+1).contains("<")){
						String str = ordArray.get(j+1).substring(1, ordArray.get(j+1).length()-1);
						ordArray.set(j+1, str);
						ordArray.set(j, "NOT");
					}
					String[] ordArr = {ordArray.get(j-1),ordArray.get(j+1)};
					stackFiles = splitPosting(ordArr,stackFiles);
					postFreq = boolOp(stackFiles,ordArray.get(j),ordArr);
					stackFiles.put(ordArray.get(j), postFreq);//(ordArray.get(j+2)!=null)&& 
					if((ordArray.size()>3)&&(ordArray.get(j+2).equals("y1")||ordArray.get(j+2).equals("y2")||ordArray.get(j+2).equals("y3"))){
						String lasFile = ordArray.get(j+2);
						stackFiles.put(lasFile, postFreq);
						ordArray.remove(j-1);
						ordArray.remove(j-1);
						ordArray.remove(j-1);
						ordArray.remove(j-1);
						if(ordArray.size()==0){ 
							postFreq = stackFiles.get(lasFile); stackFiles.put("Query", postFreq);
							stackFiles.remove(lasFile);
						}
						j--;
					}else{
					ordArray.remove(j-1);
					ordArray.remove(j);
					j--;
					if(ordArray.size()==1){
						postFreq = stackFiles.get(ordArray.get(0)); stackFiles.put("Query", postFreq);
						stackFiles.remove(ordArray.get(0));
					}
					}
				}
				j++;
			}
		}
	
		//for(Entry<String, ArrayList<String>> etr:stackFiles.entrySet()){
			//System.out.println(etr.getKey()+":"+etr.getValue());
			//System.out.println("Size:"+etr.getValue().size());
		//}
		Scorer scr = new Scorer(mdl,indexDir);
		ArrayList<String> resultForQuery = scr.rankedDocRetrieve(stackFiles,queryStr);
		
			
		return resultForQuery;
	}

	private HashMap<String, ArrayList<String>> splitPosting(String[] ordArray, HashMap<String, ArrayList<String>> stackFiles) {
		// TODO Auto-generated method stub
		String[] pair;ArrayList<String> postFreq; int j = 0;
		while(j<ordArray.length){
		if(ordArray[j].contains("Term")||ordArray[j].contains("Category")||ordArray[j].contains("Place")||ordArray[j].contains("Author")){
			
			pair = ordArray[j].split(":");
			postFreq = getIdxValues(pair[1],pair[0].charAt(0));
			stackFiles.put(pair[1],postFreq);
		}
		j++;
		}
		return stackFiles;
	}

	private ArrayList<String> boolOp(HashMap<String, ArrayList<String>> stackFiles, String boolOp, String[] ordArr) {
		// TODO Auto-generated method stub
		ArrayList<String> files;String[] pair1;
		String[] pair2; String term1, term2;
		if(ordArr[0].contains(":")){pair1= ordArr[0].split(":"); term1 = pair1[1];}else{ term1 = ordArr[0];}
		if(ordArr[1].contains(":")){pair2= ordArr[1].split(":"); term2 = pair2[1];}else{ term2 = ordArr[1];}
		ArrayList<String> list1 = stackFiles.get(term1); 
		ArrayList<String> list2 = stackFiles.get(term2);
		if(boolOp.equals("AND")){
			files = boolAnd(list1,list2);
		}else if(boolOp.equals("OR")){
			files = boolOr(list1,list2);
		}else{
			files = boolNot(list1,list2);
		}
		
		return files;
	}

	private ArrayList<String> boolNot(ArrayList<String> list1,
			ArrayList<String> list2) {
		// TODO Auto-generated method stub
		String[] pair1,pair2; int check=0;
		ArrayList<String> fileFreq = new ArrayList<String>();
		String file = "";
		for(int i=0; i<list1.size();i++){
			pair1 = list1.get(i).split(":");
			for(int r=0;r<list2.size();r++){
				pair2 = list2.get(r).split(":");
				if(pair1[0].equals(pair2[0])){
					check++;
				}
			}
			if(check==0){
				file = pair1[0]+":"+pair1[1];
				fileFreq.add(file);
				check=0;
			}
		}
		return fileFreq;
		
	}

	private ArrayList<String> boolOr(ArrayList<String> list1, ArrayList<String> list2) {
		String[] pair1,pair2; String file; ArrayList<String> tmp;
		ArrayList<String> fileFreq = new ArrayList<String>();
		if(list1.isEmpty()){ tmp = list1; list1 = list2; list2 = tmp;}
		for(int i=0; i<list1.size();i++){
			pair1 = list1.get(i).split(":");
			for(int r=0;r<list2.size();r++){
				pair2 = list2.get(r).split(":");
				if(pair1[0].equals(pair2[0])){
				file = pair1[0]+":"+pair1[1]+":"+pair2[1];
				fileFreq.add(file);
				}else{
					if(!(fileFreq.contains(list2.get(r)))){
						file = pair2[0]+":"+pair2[1];
						fileFreq.add(file);
					}
				}
			}
			if(!(fileFreq.contains(list1.get(i)))){
				file = pair1[0]+":"+pair1[1];
				fileFreq.add(file);
			
			}
		}
		return fileFreq;
	}

	private ArrayList<String> boolAnd(ArrayList<String> list1, ArrayList<String> list2) {
		// TODO Auto-generated method stub
		String[] pair1,pair2;
		ArrayList<String> fileFreq = new ArrayList<String>();
		String file = "";
		for(int i=0; i<list1.size();i++){
			pair1 = list1.get(i).split(":");
			for(int r=0;r<list2.size();r++){
				pair2 = list2.get(r).split(":");
				if(pair1[0].equals(pair2[0])){
					file = pair1[0]+":"+pair1[1]+pair2[1];
					fileFreq.add(file);
				}
			}
		}
		return fileFreq;
	}

	private ArrayList<String> orderString(String queryString) {
		// TODO Auto-generated method stub
		int j=0, k=0, count = 0,startIndex=0, endIndex=0; char[] charArray=null; 
		String[] name = {"y1","y2","y3","y4","y5"};int t=0;
		ArrayList<String> queArr = new ArrayList<String>();
		String[] splits; String substr; String grpStr = null;
		String[] splitOfQuery= new String[50000];
		charArray = queryString.toCharArray();
		for(int i=0; i<charArray.length;i++){
			if(charArray[i]=='['){
				count++;
			}
		}
		if(count>0){queryString = "[ "+queryString+" ]";}
		while(j<count){
		String regex = "\\[\\s.*\\[(.*)\\]\\s\\]|\\[\\s\\[(.*)\\].*\\s\\]|\\[\\s(.*)\\s\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(queryString);
			if(m.find()){
				if(m.group(1)!=null){
					grpStr = m.group(1);
					startIndex = m.start(1);
					endIndex = m.end(1);
				}else if(m.group(2)!=null){
					grpStr = m.group(2);
					startIndex = m.start(2);
					endIndex = m.end(2);
				}else if(m.group(3)!=null){
					grpStr = m.group(3);
					startIndex = m.start(3);
					endIndex = m.end(3);
				} 
				if(grpStr!=null){
					splits = grpStr.split(" ");
					for(int r=0;r<splits.length;r++){
						splitOfQuery[k] = splits[r];
						k++;
					}
					substr = queryString.substring(startIndex-1, endIndex+1);
					queryString = queryString.replace(substr,name[t]);
					splitOfQuery[k]= name[t]; k++;t++;
				}
			}
			j++;
		}
		splits = queryString.split(" ");
		for(int r=0;r<splits.length;r++){
			if(!(splits[r].isEmpty())){
			splitOfQuery[k] = splits[r];
			k++;}
		}
		for(int r=0;r<splitOfQuery.length;r++){
			try{
			if(!((splitOfQuery[r].contains("["))||splitOfQuery[r].contains("]")||splitOfQuery[r].isEmpty())){
			queArr.add(splitOfQuery[r]);
			}}catch(NullPointerException e){}	
		}
		return queArr;
	}

	private ArrayList<String> getIdxValues(String term, char itype) {
		// TODO Auto-generated method stub
		String[] termVar = getTermVariants(term);
		ArrayList<String> postFreq = new ArrayList<String>(); String fileFreq = "";
		IndexReader iRead = new IndexReader(indexDir,itype);
		Map<String, Integer> getQtPostingstmp = new HashMap<String, Integer>();
		Map<String, Integer> getQtPostings = new HashMap<String, Integer>();
		for(String termVars:termVar){
		getQtPostingstmp = iRead.getPostings(termVars);
		if(getQtPostingstmp!=null){
		for(Entry<String, Integer> etr:getQtPostingstmp.entrySet()){
			if(getQtPostings.containsKey(etr.getKey())){
				int tf = etr.getValue() + getQtPostings.get((etr.getKey()));
				getQtPostings.put(etr.getKey(), tf);
			}else{
				getQtPostings.put(etr.getKey(), etr.getValue());
			}
		}
		}
		}
		try{
			for(Map.Entry<String, Integer> entry: getQtPostings.entrySet()){
				fileFreq = entry.getKey() + ":" + term+"#"+entry.getValue();
				postFreq.add(fileFreq);
				//System.out.println("FileId="+entry.getKey()+", TermFrequency="+ entry.getValue());
		}}catch(NullPointerException e){} //System.out.println("Query not Found!");}
		
	return postFreq;
	}

	private String[] getTermVariants(String term) {
		// TODO Auto-generated method stub
		String[] termVars = new String[3];
		termVars[0] = term.toUpperCase();
		termVars[1] = term.toLowerCase();
		termVars[2] = termVars[1].substring(0, 1).toUpperCase()+termVars[1].substring(1,termVars[1].length());
		return termVars;
	}
}
