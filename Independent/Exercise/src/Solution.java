import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Solution {
    Solution leftNode;
	Solution parent;
	Solution rightNode;
	long data;
	
	Solution(long value){
		data = value;
	}
    private Solution insert(long val) {
		// TODO Auto-generated method stub
        long diff;
    	diff = val - this.data;
		if(diff==1){
            this.leftNode = new Solution(val);
            return this.leftNode;
        }else{
            this.rightNode = new Solution(val);
        }   return this.rightNode;
    }
	
    public static void main(String[] args) throws IOException {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));boolean bool=true;
        int t = Integer.parseInt(in.readLine());ArrayList<Long> arr = new ArrayList<Long>();
        ArrayList<Long> temp = new ArrayList<Long>(); 
        Map<Long,Long> map = new HashMap<Long,Long>();
        while(t!=-1){
            String[] n = in.readLine().split(" ");
            for(String s:n){
                long tmp = Integer.parseInt(s);
                arr.add(tmp);
            }
            Collections.sort(arr);
            Solution s = new Solution(arr.get(0));
            Solution tempo = s;
            for(int j=1;j<arr.size();j++){
                tempo = tempo.insert(arr.get(j));
            }
            displayRes(s,temp);
            
            t--;
        }
        
       
    }
	private static void displayRes(Solution s, ArrayList<Long> temp) {
		// TODO Auto-generated method stub
		if(s.leftNode!=null){
			temp.add(s.data);
			displayRes(s.leftNode,temp);
			
		}
	}

}
