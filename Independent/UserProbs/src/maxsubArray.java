import java.io.BufferedReader;
import java.io.InputStreamReader;

public class maxsubArray {

		    public static void main(String[] args)throws Exception {
		        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
		        
		        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		        int t = Integer.parseInt(in.readLine());
		        for(int r=0;r<t;r++){
		            int n = Integer.parseInt(in.readLine());
		            int startIndex=0,endIndex=0;int max_current = -1*(Integer.MAX_VALUE);int max_current_sum=-1;
		            int[] arr = new int[n];
		            int[] maxsub = new int[n];int nonContmax=-1*(Integer.MAX_VALUE),anothersum=0;
		            String[] str = in.readLine().split(" ");
		            for(int k=0;k<n;k++){
		                arr[k] = Integer.parseInt(str[k]);
		            }
		            for(int i=0;i<n;i++){
		                if(arr[i]>0){anothersum+=arr[i];}
		                else{
		                	if(arr[i]>nonContmax){nonContmax=arr[i];}
		                }
		                if(max_current_sum<0){
		                    max_current_sum = arr[i];
		                    startIndex = i;
		                    endIndex = i;
		                }else{
		                    max_current_sum+=arr[i];
		                    endIndex = i;
		                }
		                if(max_current_sum>max_current){
		                    max_current = max_current_sum;
		                }
		                maxsub[i] = max_current;
		            }
		            if(anothersum==0){anothersum = nonContmax;}
		            System.out.println(maxsub[n-1]+" "+anothersum);
		        }
		    }

}
