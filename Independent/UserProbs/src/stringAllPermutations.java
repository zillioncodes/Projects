import java.util.ArrayList;
import java.util.Vector;


public class stringAllPermutations {
	static int count = 0;
	static ArrayList<String> listOfPermutations = new ArrayList<String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String s= "cdcdcdcdeeeef";
		
		//permutation("",s);
		//for(String string:listOfPermutations){
			//count++;
			//System.out.println(string+count);
		 for (String str : permStr(s)){
		        System.out.println(str);
		 }
	}

	private static void permutation(String prefix, String s) {
		// TODO Auto-generated method stub
		int n = s.length();
		if(n==0){
			if(!(listOfPermutations.contains(prefix))){
				listOfPermutations.add(prefix);
			}
			//count++;System.out.println(prefix+count);}
		}
		else{
			for(int i=0;i<n;i++){
				permutation(prefix+s.charAt(i),s.substring(0,i)+s.substring(i+1,n));
				
			}
		}
	}
	static Vector<String> permStr(String str){

	    if (str.length() == 1){
	        Vector<String> ret = new Vector<String>();
	        ret.add(str);
	        return ret;
	    }

	    char start = str.charAt(0);
	    Vector<String> endStrs = permStr(str.substring(1));
	    Vector<String> newEndStrs = new Vector<String>();
	    for (String endStr : endStrs){
	        for (int j = 0; j <= endStr.length(); j++){
	            if (endStr.substring(0, j).endsWith(String.valueOf(start)))
	                break;
	            newEndStrs.add(endStr.substring(0, j) + String.valueOf(start) + endStr.substring(j));
	        }
	    }
	    return newEndStrs;
	}

}
