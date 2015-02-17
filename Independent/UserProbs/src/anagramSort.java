import java.util.LinkedList;


public class anagramSort {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] arr = {"abcbca","defsd","bcabca","kefkef","efkkfe","asdefr","artifd","mannam","nnmmaa"};
		LinkedList<String> lis = new LinkedList<String>();
		lis.add("abcbca");lis.add("defsd");lis.add("bcabca");lis.add("kefkef");lis.add("efkkfe");lis.add("asdefr");
		lis.add("artifd");lis.add("mannam");lis.add("nnmmaa");
		StringBuffer sbAna = new StringBuffer("");int flag=0;
		StringBuffer sbNonAna = new StringBuffer("");
		for(int i=0;i<lis.size();i++){
			String temp1 = lis.get(i);
			for(int j=i+1;j<lis.size();j++){	
			String temp2 = lis.get(j);
			boolean bool = checkAnagram(temp1,temp2);
			if(bool==true){
					flag++;
					sbAna.append(temp1).append(" ").append(temp2).append(" ");
					lis.remove(j);
			}
			}
			if(flag==0){
				sbNonAna.append(temp1).append(" ");
			}
			flag=0;
		}
		sbAna.append(" ").append(sbNonAna);
		String s = sbAna.toString();
		System.out.println(s);
		
	}

	private static boolean checkAnagram(String string1, String string2) {
		// TODO Auto-generated method stub
		if(string1.length()!=string2.length()){
			return false;
		}
		int[] arr = new int[256];
		char[] s1_arr = string1.toCharArray();
		for(char c:s1_arr){
			arr[c]++;
		}
		for(int i=0;i<string2.length();i++){
			int c = (int) string2.charAt(i);
			if(--arr[c]<0){
				return false;
			}
		}
		return true;
	}

}
