import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class exercise14 {

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		   BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	        long n = Long.parseLong(in.readLine());
	        String[] a = in.readLine().split(" ");
	        long m = Long.parseLong(in.readLine());
	        String[] b = in.readLine().split(" ");
	        Map<Long,Long> map = new HashMap<Long,Long>();
	        Map<Long,Long> chkmap = new HashMap<Long,Long>();
	        for(int i=0;i<m;i++){
	            long num = Long.parseLong(b[i]); 
	            if(map.containsKey(num)){
	                long tmp = map.get(num);
	                tmp++;
	                map.put(num,tmp);
	            }else{
	                long tmp=1;
	                map.put(num,tmp);
	            }
	        }
	        for(int j=0;j<n;j++){
	            long checknum = Long.parseLong(a[j]);
	            if(chkmap.containsKey(checknum)){
	                long tmp = chkmap.get(checknum);
	                tmp++;
	                chkmap.put(checknum,tmp);
	            }else{
	                long tmpr=1;
	                chkmap.put(checknum,tmpr);
	            }
	        }
	        ArrayList<Long> arr = new ArrayList<Long>();
	        for(Map.Entry<Long,Long> etr:map.entrySet()){
	            long key = etr.getKey();
	            if(chkmap.containsKey(key)){
	                long tmpval = etr.getValue();
	                long val = chkmap.get(key);
	                if(val!=tmpval){arr.add(key);}
	                
	            }else{
	                 arr.add(key);
	            }
	        }
	        Collections.sort(arr);
	        for(long value:arr){
	            System.out.print(value+" ");
	        }
	            
	     }

}
