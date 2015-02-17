import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class exercise9 {

	public static void main(String[] args) throws Exception {
	        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
	        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	        long n = Long.parseLong(in.readLine()); long min = Long.MAX_VALUE; long diff;
	        long[] nums = new long[(int)n]; StringBuffer sb= new StringBuffer();
	        HashMap<Long,String> map = new HashMap<Long,String>();
	        ArrayList<Long> res = new ArrayList<Long>();
	        String[] tmp = in.readLine().split(" ");
	        for(int i=0;i<n;i++){
	            nums[i] = Long.parseLong(tmp[i]);
	        }
	        java.util.Arrays.sort(nums);
	        for(int p=0;p<n;p++){
	            //System.out.println(nums[p]);
	        }
	        for(int j=1;j<n;j++){
	            diff = nums[j] - nums[j-1];
	            diff = Math.abs(diff);
	            if(diff<=min){
	                min = diff; 
	                if(map.containsKey(min)){
	                   String tmp1 = map.get(min); 
	                   sb.append(tmp1).append(" ").append(nums[j-1]).append(" ").append(nums[j]);
	                    map.put(min,sb.toString());
	                    sb.setLength(0);
	                  
	                }else{                   
	                    sb.append(nums[j-1]).append(" ").append(nums[j]);
	                    map.put(min,sb.toString());
	                    sb.setLength(0);
	                }
	            }             
	        }
	        System.out.println(min);
	        System.out.println(map.get(min));      
	        
	    }

}
