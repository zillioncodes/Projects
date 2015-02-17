import java.io.BufferedReader;
import java.io.InputStreamReader;


public class exercise2 {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the strings:");
		String str = in.readLine();
		//boolean res = isuniqechars(str);
		//if(res==true) System.out.println("all are unique");
		//else System.out.println("not unique");
		String str1 = in.readLine();
		//boolean res1 = ispermutationOne(str1,str);
		boolean res1 = ispermutationTwo(str1,str);
		if(res1==true) System.out.println("Strings are equal");
		else System.out.println("not equal");
		

	}

	private static boolean ispermutationTwo(String str1, String str) {
		// TODO Auto-generated method stub
		if(str.length()!=str1.length())	return false;
		int[] ltrs = new int[256];
		char[] s_array = str.toCharArray();
		for(char c:s_array){
			ltrs[c]++;
		}
		for(int i=0;i<str1.length();i++){
			int c = (int) str1.charAt(i);
			if(--ltrs[c]<0) return false;
		}
		return true;
	}

	private static boolean ispermutationOne(String str1, String str) {
		// TODO Auto-generated method stub
		if(str1.length()!=str.length()) return false;
		return sort(str).equals(sort(str1));
	}

	private static Object sort(String str1) {
		// TODO Auto-generated method stub
		char[] con = str1.toCharArray();
		java.util.Arrays.sort(con);
		return new String(con);
	}

	private static boolean isuniqechars(String str) {
		// TODO Auto-generated method stub
		if(str.length()>128) return false;
		boolean[] char_set = new boolean[256];
		for(int i=0;i<str.length();i++){
			int val = str.charAt(i);
			if(char_set[val]) return false;
			System.out.println(char_set[val]);
			char_set[val] = true;
		}
		return true;
	}
	
	

}
