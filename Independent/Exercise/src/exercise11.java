import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;


public class exercise11 {
	public static void main(String[] args) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(in.readLine()); int count=0;
        HashMap<Long,Integer> map = new HashMap<Long,Integer>(); int res =1;
        for(int i=0;i<t;i++){
            int n = Integer.parseInt(in.readLine());
            String[] nums = in.readLine().split(" ");
            for(int j=0;j<n;j++){
                long tmp = Long.parseLong(nums[j]);
                if(map.containsKey(tmp)){
                    int ind = map.get(tmp);ind++;
                    for(int k=ind;k>0;k--){
                    	res = res  * k;
                    } 
                    count = res;
                    map.put(tmp,ind);
                    res = 1;
                }else{
                    map.put(tmp,1);
                }
            }
            System.out.println(count);
            count=0;
            map.clear();
         }
}
}
