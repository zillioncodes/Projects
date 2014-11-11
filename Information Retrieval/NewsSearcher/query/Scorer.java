package edu.buffalo.cse.irf14.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import edu.buffalo.cse.irf14.SearchRunner.ScoringModel;
import edu.buffalo.cse.irf14.index.IndexReader;

public class Scorer {
	ScoringModel mdl; String indexDir = null;	
	public Scorer(ScoringModel model, String indxDir){
		mdl = model;
		indexDir = indxDir;
	}
	public ArrayList<String> rankedDocRetrieve(HashMap<String,ArrayList<String>> stackFiles,String queryStr) {
		// TODO Auto-generated method stub
		ArrayList<String> searchResult = null;
		switch(mdl){
		case TFIDF:
			searchResult = tfidfScore(stackFiles, queryStr);
			break;
		case OKAPI:
			searchResult = okapiScore(stackFiles, queryStr);
			break;
		}
		return searchResult;
	}
	private ArrayList<String> okapiScore(
			HashMap<String, ArrayList<String>> stackFiles, String queryStr) {
		// TODO Auto-generated method stub
		IndexReader indx = new IndexReader(indexDir);
		Map<String, Double> fileScoreMap = new HashMap<String, Double>();
		Map<String, Integer> fileLengthsMap  = new HashMap<String, Integer>();
		String[] queryTerms = queryStr.split(" "); ArrayList<String> queryFiles = null;
		String[] file; String[] tf = null; String key;int N;
		double weight=0, score=0;int resultDocCtr=0;double lenSum=0;Double lenAvg;
		ArrayList<String> results = new ArrayList<String>();
		fileLengthsMap = indx.docLength(); N = fileLengthsMap.size();
		for(Entry<String, Integer> etr:fileLengthsMap.entrySet()){
			lenSum = lenSum + etr.getValue();
		}
		lenAvg = lenSum / fileLengthsMap.size();
		for(Entry<String, ArrayList<String>> mapEntry:stackFiles.entrySet()){
			if(mapEntry.getKey().equals("Query")){
				  queryFiles = mapEntry.getValue();
			}
		}
		for(int r=0;r<queryTerms.length;r++){
			if(!(queryTerms[r].isEmpty())){
				String term = queryTerms[r].trim();
				List<String> termFiles = stackFiles.get(term);
				for(int i=0;i<queryFiles.size();i++){
					file = queryFiles.get(i).split(":");
					for(String s:termFiles){
						if(s.contains(file[0])){
							tf = s.split("#");
							int termfreq = Integer.parseInt(tf[1]);
							double term1 = (Math.log10(N-queryFiles.size()+0.5)/(queryFiles.size()+0.5));
							double lenNormalize = (fileLengthsMap.get(file[0])) / lenAvg;
							double term2 = (2.3 * termfreq)/(termfreq+(1.3*lenNormalize)); 
							weight = term1*term2;
							if(fileScoreMap.containsKey(file[0])){
								score = fileScoreMap.get(file[0]);
								score = score + weight;
							}else{score = weight;}
						}
					}
					score = (double)Math.round(score * 100000) / 100000;
					fileScoreMap.put(file[0], score);
				}
			}
		}
		TreeMap<String, Double> sortedMap = SortByValue(fileScoreMap);
		for(Entry<String, Double> etr:sortedMap.entrySet()){
			String res = etr.getKey()+"#"+etr.getValue();
			results.add(res); resultDocCtr++;
			if(resultDocCtr==15){
				break;
			}
		}
		
		return results;
		
	}
	
	
	
	private ArrayList<String> tfidfScore(HashMap<String,ArrayList<String>> stackFiles,String queryStr) {
		// TODO Auto-generated method stub
		Map<String, Double> fileScoreMap = new HashMap<String, Double>();
		Map<String, Double> fileLenMap = new HashMap<String, Double>();
		ArrayList<String> queryFiles = null; int resultDocCtr = 0, K=10;
		String[] queryTerms = queryStr.split(" "); 
		ArrayList<String> results = new ArrayList<String>();;
		IndexReader indx = new IndexReader(indexDir);
		double idf; int N;
		fileLenMap = indx.docNormalizedLength();
		N = fileLenMap.size();
		double[] docScore = new double[N]; String[] file; String[] tf = null; String key;
		double[] docLength = new double[N]; double weight=0, score=0, val;
			
		for(Entry<String, ArrayList<String>> mapEntry:stackFiles.entrySet()){
			if(mapEntry.getKey().equals("Query")){
				  queryFiles = mapEntry.getValue();
			}
		}
		for(int r=0;r<queryTerms.length;r++){
			if(!(queryTerms[r].isEmpty())){
				String term = queryTerms[r].trim();
				List<String> termFiles = stackFiles.get(term);
				for(int i=0;i<queryFiles.size();i++){
					file = queryFiles.get(i).split(":");
					for(String s:termFiles){
						if(s.contains(file[0])){
							tf = s.split("#");
							int termfreq = Integer.parseInt(tf[1]);
							double term1 = (Math.log10(N/queryFiles.size()));
							double term2 = (1+ Math.log10(termfreq)); 
							weight = term1*term2;
							if(fileScoreMap.containsKey(file[0])){
								score = fileScoreMap.get(file[0]);
								score = score + weight;
							}else{score = weight;}
						}
					}
					score = (double)Math.round(score * 100000) / 100000;
					fileScoreMap.put(file[0], score);
				}
			}
		}
			
		//System.out.println(fileLenMap.size());			
		for(Entry<String, Double> etr:fileScoreMap.entrySet()){
				key = etr.getKey(); double fileLen = fileLenMap.get(key);
				score = etr.getValue()/fileLen;
				fileScoreMap.put(etr.getKey(), score);
		}
		TreeMap<String, Double> sortedMap = SortByValue(fileScoreMap);
		for(Entry<String, Double> etr:sortedMap.entrySet()){
			String res = etr.getKey()+"#"+etr.getValue();
			//System.out.println(res);
			results.add(res); resultDocCtr++;
			if(resultDocCtr==20){
				break;
			}
		}
		return results;
	}
	
	public static TreeMap<String, Double> SortByValue(Map<String, Double> fileScoreMap) {
		// TODO Auto-generated method stub
		ValueComparator vc =  new ValueComparator(fileScoreMap);
		TreeMap<String,Double> sortedMap = new TreeMap<String,Double>(vc);
		sortedMap.putAll(fileScoreMap);
		return sortedMap;
		
	}
	
}


class ValueComparator implements Comparator<String> {
	 
    Map<String,Double> map;
 
    public ValueComparator(Map<String, Double> fileScoreMap) {
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
