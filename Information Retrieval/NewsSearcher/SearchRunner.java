package edu.buffalo.cse.irf14;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import edu.buffalo.cse.irf14.query.IndexSearcher;
import edu.buffalo.cse.irf14.query.Query;
import edu.buffalo.cse.irf14.query.QueryParser;

/**
 * Main class to run the searcher.
 * As before implement all TODO methods unless marked for bonus
 * @author nikhillo
 *
 */
public class SearchRunner {
	//static String indexDir = "";
	//static String corpusDir = "";
	String userQuery;//String idxDir, corpusDir;
	public enum ScoringModel {TFIDF, OKAPI};
	File queryFile = null;
	long start;
	/**
	 * Default (and only public) constuctor
	 * @param indexDir : The directory where the index resides
	 * @param corpusDir : Directory where the (flattened) corpus resides
	 * @param mode : Mode, one of Q or E
	 * @param stream: Stream to write output to
	 */
	public SearchRunner(String indexDir, String corpusDir, 
			char mode, PrintStream stream) {
		//String idxDir  = indexDir; String cpsDir = corpusDir;
		switch(mode){
		case 'q':
			while(true){
			System.out.println("Enter your search Query: ");
			Scanner queryIn = new Scanner(System.in);
			userQuery = queryIn.nextLine();
			System.out.println("Select Scoring Model: Press 1. for OKAPI 2. for TFIDF ");
			Scanner modelIn = new Scanner(System.in);
			userQuery = userQuery.replaceAll("\"", "");
			userQuery = userQuery.replaceAll("-", " ");
			int model = modelIn.nextInt();
			start = System.currentTimeMillis();
			switch(model){
			case 1:
				this.query(userQuery, ScoringModel.OKAPI,stream,corpusDir,indexDir);
				break;
			case 2:
				this.query(userQuery, ScoringModel.TFIDF,stream,corpusDir,indexDir);
				break;
			}}
		case 'e':
			//File queryFile = new File("C:\\Users\\manish\\workspace\\newsIndexerSearch\\evalMode\\query.txt");
			this.query(queryFile, indexDir, stream);
			break;
		}
		//TODO: IMPLEMENT THIS METHOD
		
	}
	/*public static void main(String[] args){
				
				Scanner sc = new Scanner(System.in);
				char mode = sc.next().charAt(0);
				PrintStream results = new PrintStream(System.out);
				SearchRunner sr = new SearchRunner("C:\\Users\\manish\\workspace\\newsIndexerSearch\\Indexes", "C:\\Users\\manish\\workspace\\newsIndexerSearch\\FlatCorpus", mode, results);
	}*/
	
	/**
	 * Method to execute given query in the Q mode
	 * @param userQuery : Query to be parsed and executed
	 * @param model : Scoring Model to use for ranking results
	 * @param stream 
	 * @param indexDir 
	 * @param indexDir 
	 */
	public void query(String userQuery, ScoringModel model, PrintStream stream, String corpusDir, String indexDir) {
		//TODO: IMPLEMENT THIS METHOD
		if(userQuery==null){
			stream.print("User Query is null!!");
		}else{
		ScoringModel mdl = model;
		String defaultOperator = "OR";
		QueryParser parseQuery = new QueryParser();
		Query query = parseQuery.parse(userQuery, defaultOperator);
		System.out.println(query.toString());ArrayList<String> searchResults = null;
		switch(model){
		case TFIDF:
			IndexSearcher iStfidf = new IndexSearcher(ScoringModel.TFIDF,indexDir);
			searchResults = iStfidf.consumeQuery(query);
			break;
		case OKAPI:
			IndexSearcher iSokapi = new IndexSearcher(ScoringModel.OKAPI,indexDir);
			searchResults = iSokapi.consumeQuery(query);
			break;
		}
		
		String userString = query.getUserString(); 
		//Map<String, ArrayList<String>> snippetMap = genSnippet(searchResults, userString);
		displaySnippet(searchResults,stream,userString,corpusDir);
		System.out.println("\n"+"Total time = " + (System.currentTimeMillis() - start) + "milliSeconds");	
		}
	}
	
	private void displaySnippet(ArrayList<String> searchResults, PrintStream stream, String userString, String corpusDir){
		String linesOfSnippet = "";
		String[] userStringArr = userString.split(" "); String[] checkLines;
		Map<String, ArrayList<String>> snippetMap = genSnippet(searchResults, userString,corpusDir);
		for(Entry<String,ArrayList<String>> etr: snippetMap.entrySet()){
			
			String[] file = etr.getKey().split("#");
			ArrayList<String> lines = etr.getValue();
			//System.out.println(file[0]+"\n");
			for(String str: userStringArr){
				//System.out.println(file[0]+str+"\n");
				if((!str.isEmpty())){
				String[] termVars = new String[3];
				termVars[0] = str.toUpperCase();
				termVars[1] = str.toLowerCase();
				termVars[2] = termVars[1].substring(0, 1).toUpperCase()+termVars[1].substring(1,termVars[1].length());
				
				for(String s: lines){
					if(s.contains(termVars[0])||s.contains(termVars[1])||s.contains(termVars[2])){
						//checkLines = s.split(str);
						linesOfSnippet = ".."+linesOfSnippet +s+"..."+"\n";
					}
				}
				}
			}
			stream.print("File Name:"+file[0]+"\t"+"Relevance Score:"+file[1]+"\n");
			stream.print(linesOfSnippet+"\n");
			linesOfSnippet = "";
			}
	}
	private Map<String, ArrayList<String>> genSnippet(List<String> results, String UserString, String corpusDir) {
		// TODO Auto-generated method stub
		String[] row; String[] queryTerms = UserString.split(" ");
		String line,filename,lineCase,qtCase; ArrayList<String> fileLines;
		Map<String, ArrayList<String>> snippetMap = new HashMap<String, ArrayList<String>>();
		for(int i=0;i<results.size();i++){
			fileLines = new ArrayList<String>();
			row = results.get(i).split("#");
			filename = row[0];int lineCtr = 0;
			//System.out.println(filename+"\n");
			for(int j=0;j<queryTerms.length;j++){
				if(!(queryTerms[j].equals(""))){
				try{
					BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(corpusDir+File.separator+filename), "UTF-8"));
					while ((line = rdr.readLine()) != null){
						lineCase = line.toLowerCase();
						qtCase =queryTerms[j].trim().toLowerCase();
						queryTerms[j] = queryTerms[j];
						if(lineCase.contains(qtCase+" ")||lineCase.contains(queryTerms[j]+" ")){
							line = line.trim();
							fileLines.add(line);lineCtr++;
							if(lineCtr>4){
								break;
							}
						}
					}
					rdr.close();
				}catch(IOException e){System.out.println("File not found");}
				}
			}
			for(String s:fileLines){
				//System.out.println(s);
			}
			//System.out.println("this snippet above put under this file:"+results.get(i));
			if(!(fileLines.isEmpty())){
			snippetMap.put(results.get(i),fileLines);
			}
		}
		return snippetMap;
		
	}
	/**
	 * Method to execute queries in E mode
	 * @param queryFile : The file from which queries are to be read and executed
	 * @param indexDir 
	 * @param stream 
	 */
	public void query(File queryFile, String indexDir, PrintStream stream) {
		//TODO: IMPLEMENT THIS METHOD
		if(queryFile==null){
			stream.append("Query file is null");
		}else{
		String line, numOfQueries=null;String[] row; String query = null;
		HashMap<String,String> queryMap = new HashMap<String, String>();
		try{
			BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(queryFile), "UTF-8"));
			while ((line = rdr.readLine()) != null){
				if(line.contains("numQueries"))
					{ numOfQueries = line;}
				else{
					row = line.split(":");
					String regex = "\\{(.*)\\}";
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(line);
					if(m.find()){
						query = m.group(1);
					}
					query = query.replace("( ","(");
					query = query.replace(" )",")");
				    queryMap.put(row[0],query);
				}
			}	
		}catch(IOException e){}	
		try{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexDir+File.separator+"queryOutput.idx"), "UTF-8"));
		writer.write(numOfQueries);
		stream.append(numOfQueries);
		writer.newLine();
		writeSearchOutput(writer,queryMap,stream,indexDir);
		}catch(IOException e){}
		}
	}
		
	private void writeSearchOutput(BufferedWriter writer,
			HashMap<String, String> queryMap, PrintStream stream,String indexDir) throws IOException {
		StringBuilder bldr = new StringBuilder();
		//bldr.append("numQueries")
		ArrayList<String> searchResults = null;int j=1;
		// TODO Auto-generated method stub
		for(Entry<String,String> etr:queryMap.entrySet()){
			searchResults = evalModeSearch(etr.getValue(),indexDir);
			String str = searchResults.toString(); str = str.replace("[", "{"); str = str.replace("]","}");
			bldr.append(etr.getKey()).append(":").append(str);
			stream.append(bldr.toString());
			writer.write(bldr.toString());
			writer.newLine();
			bldr.delete(0, bldr.length());
			writer.flush();
			}
		System.out.println("Writing to disk Over!!");
	}
	
	private ArrayList<String> evalModeSearch(String fileQuery,String indexDir) {
		// TODO Auto-generated method stub
		ScoringModel mdl;ArrayList<String> searchResults = null;
		String defaultOperator = "OR";
		QueryParser parseQuery = new QueryParser();
		Query query = parseQuery.parse(fileQuery, defaultOperator);
		IndexSearcher iSokapi = new IndexSearcher(ScoringModel.OKAPI,indexDir);
		searchResults = iSokapi.consumeQuery(query);
		
		return searchResults;
	}
	/**
	 * General cleanup method
	 */
	public void close() {
		//TODO : IMPLEMENT THIS METHOD
	}
	
	/**
 	 * Method to indicate if wildcard queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean wildcardSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF WILDCARD BONUS ATTEMPTED
		return false;
	}
	
	/**
	 * Method to get substituted query terms for a given term with wildcards
	 * @return A Map containing the original query term as key and list of
	 * possible expansions as values if exist, null otherwise
	 */
	public Map<String, List<String>> getQueryTerms() {
		//TODO:IMPLEMENT THIS METHOD IFF WILDCARD BONUS ATTEMPTED
		return null;
		
	}
	
	/**
	 * Method to indicate if speel correct queries are supported
	 * @return true if supported, false otherwise
	 */
	public static boolean spellCorrectSupported() {
		//TODO: CHANGE THIS TO TRUE ONLY IF SPELLCHECK BONUS ATTEMPTED
		return false;
	}
	
	/**
	 * Method to get ordered "full query" substitutions for a given misspelt query
	 * @return : Ordered list of full corrections (null if none present) for the given query
	 */
	public List<String> getCorrections() {
		//TODO: IMPLEMENT THIS METHOD IFF SPELLCHECK EXECUTED
		return null;
	}
}
