import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class exercise {
	HashMap<String,String> hm;
    public exercise(){
        hm=new HashMap<String,String>();

        hm.put("1","A");
        hm.put("2","B");
        hm.put("3","C");

        method1(hm);

    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new exercise();
		//exercise1();
		//exercise2();
		exercise4();
		

	}
	
	private static void exercise4() {
		// TODO Auto-generated method stub
		String line; String[] terms; HashMap<String,Integer> dict = new HashMap<String,Integer>();
		String s = "";
		try{
			BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\manish\\workspace\\newsIndexerSearch"+File.separator+"dictionary.txt"), "UTF-8"));
			while ((line = rdr.readLine()) != null){
				terms = line.split(",");
				for(String s1:terms){
					s = s1.trim();
					dict.put(s, 1);
				}
			}
			for(Entry<String,Integer> etr:dict.entrySet()){
				System.out.println(etr.getKey()+":"+etr.getValue());
				
			}
			System.out.println(dict.size());
			}catch(Exception e){}
		System.out.println("Enter search term:");
		Scanner sc = new Scanner(System.in);
		String sin = sc.nextLine();
		if(sin.contains("*")){
			String regex = sin.replace("*", ".*");
			Pattern p = Pattern.compile(regex);
			for(Entry<String,Integer> etr:dict.entrySet()){
			Matcher m = p.matcher(etr.getKey());
			if(m.find()){
				s = s + m.group(0).trim();
			}
			}
		}
		if(sin.contains("?")){
				String regex1 = sin.replace("?", ".");
				Pattern p1 = Pattern.compile(regex1);
				for(Entry<String,Integer> etr:dict.entrySet()){
				Matcher m = p1.matcher(etr.getKey());
				if(m.find()){
					s = s + m.group(0).trim();
				}
				}
		}
	}
		

	private static void exercise3() {
		// TODO Auto-generated method stub
		Scanner s = new Scanner(System.in);
		String str1 = s.nextLine();
		String string = str1.replaceAll("\"", "");
		System.out.println(string);
		String str = "Q_6V87S:{Category:oil AND place:Dubai AND ( price OR cost )}";String query=null;
		String regex = "\\{(.*)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if(m.find()){
			query = m.group(1);
		}
		System.out.println(query);
		query = query.replace("( ","(");
		query = query.replace(" )",")");
		System.out.println(query);
	}

	private static void exercise2() {
		// TODO Auto-generated method stub
		TreeMap<String, Integer> fileDocMap = new TreeMap<String, Integer>(); String[] files; 
		File dir = new File("C:/Users/manish/workspace/newsIndexerSearch/FlatCorpus");
		files = dir.list(); int count = 0;
		for(String s:files){
			count++;
			System.out.println(s+"\t "+count);
		}
		System.out.println("\n Final Count "+count);
	}

	private static void exercise1() {
		// TODO Auto-generated method stub
		
		//line = line.replaceAll("[(]","[");
		//line = line.replaceAll("[)]","]");
		//lineArray = line.split(" AND | OR | NOT ");
		//for(String s:lineArray){
		//System.out.println(s);
		//}
		String retString="";
		String str = "Hello NOT World NOT me";
		String[] strArr = str.split("NOT");
		for(int i=0;i<strArr.length;i++){
			if(i==0){ retString = strArr[i].trim();}
			else{retString = retString+" AND "+"<"+strArr[i].trim()+">";}
		}
		System.out.println(retString);
		//String refineStr = refineStr(str);
		//System.out.println(refineStr);
		
	}
	
	private static String refineStr(String str) {
		// TODO Auto-generated method stub
		String[] strHold;
		str = defaultOperator(str, "OR");
		int index = 0, indexInc = 0, indexOf=0;
		if(str.contains("Category:")){
			str = stringProcess(str,"Category");
			
		}else if(str.contains("Author:")){
			str = stringProcess(str,"Author");
			
		}else if(str.contains("Place:")){
			str = stringProcess(str,"Place");
			
		}
		str = str.replaceAll("[(]", "[");
		str = str.replaceAll("[)]", "]");
		StringBuffer sb = new StringBuffer(str);
		String regex = "[a-zA-Z0-9:]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while(m.find()){
			if(!(m.group(0).equals(""))){
				if(!(m.group(0).contains("Category:")|| m.group(0).contains("Place:")||m.group(0).contains("Author:"))){
					if(!(m.group(0).equals("AND")||m.group(0).equals("OR")||m.group(0).equals("NOT"))){
						index = m.start();
						index = index + indexInc;
						sb.insert(index, "Term:");
						indexInc = indexInc+5;
					}
				}
			}
		}
		str = sb.toString();
		return str;
	}

	private static String defaultOperator(String str, String string) {
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

	private static String stringProcess(String str, String field) {
		// TODO Auto-generated method stub
		String[] strHold;int index = 0; int indexInc = 0;
		str = str.replace(field,"");
		field = field+":";
		strHold = str.split(":");
		if(strHold[1].trim().contains("(")){
				StringBuffer sb = new StringBuffer(strHold[1]);
				String regex = "[a-zA-Z0-9:]*";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(strHold[1]);
				while(m.find()){
					if(!(m.group(0).equals(""))){
							if(!(m.group(0).equals("AND")||m.group(0).equals("OR")||m.group(0).equals("NOT"))){
								index = m.start();
								index = index + indexInc;
								sb.insert(index, field);
								indexInc = indexInc+field.length();
							}
				
					}
				}
				str = strHold[0]+sb.toString();
		}
		return str;
	}

	public void regexCheck(){
		String[] lineArray; int index; int indexInc = 0; String str = "";
		System.out.println("enter the string:");
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		StringBuffer str1 = new StringBuffer(line);
		String regex = "(([a-z])+)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);
		while(m.find()){
			index = m.start();
			index = index + indexInc;
			str1.insert(index, "Term:");
			indexInc = indexInc+5;
			
		}
		
	}

	public void method1(HashMap<String,String> map){
	    //write to file : "fileone"
	    try{
	    File fileOne=new File("fileone");
	    FileOutputStream fos=new FileOutputStream(fileOne);
	        ObjectOutputStream oos=new ObjectOutputStream(fos);

	        oos.writeObject(map);
	        oos.flush();
	        oos.close();
	        fos.close();
	    }catch(Exception e){}

	    //read from file 
	    try{
	        File toRead=new File("fileone");
	        FileInputStream fis=new FileInputStream(toRead);
	        ObjectInputStream ois=new ObjectInputStream(fis);

	        HashMap<String,String> mapInFile=(HashMap<String,String>)ois.readObject();

	        ois.close();
	        fis.close();
	        //print All data in MAP
	        for(Map.Entry<String,String> m :mapInFile.entrySet()){
	            System.out.println(m.getKey()+" : "+m.getValue());
	        }
	    }catch(Exception e){}
	  }
}

