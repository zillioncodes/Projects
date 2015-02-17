
public class exercise15 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "   a   b ";
		String result = reverseWords(s);
		System.out.println(result);
		

	}
	 public static String reverseWords(String s) {
	        //StringBuffer sb = new StringBuffer(s);
	        s = s.trim();
	        if(s.equals("")) return "";
	        StringBuffer res = new StringBuffer("");
	        String[] sbArr = s.split(" ");
	        for(int i=sbArr.length-1;i>=0;i--){
	        if(!(sbArr[i].equals(""))){	
	            StringBuffer sb = new StringBuffer(sbArr[i]);
	            if(i>0){
	            res.append(sb).append(" ");
	            }else{
	                res.append(sb);
	            }
	        }
	        }
	        return res.toString();
	    }

}
