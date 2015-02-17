import java.io.BufferedReader;
import java.io.InputStreamReader;


public class exercise4 {


		public static void main(String[] args) throws Exception{
	        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
	        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	        int n = Integer.parseInt(in.readLine()); int count=0;
	        String[] numbers = new String[n];
	        for(int i=0;i<n;i++){
	            numbers[i] = in.readLine();
	        }
	        for(int j=0;j<n;j++){
	            int len = numbers[j].length();
	            int num = Integer.parseInt(numbers[j]);
	            int[] p = new int[len];
	            for(int k=0;k<len;k++){
	                               
	                p[k] = Integer.parseInt(numbers[j].substring(k,k+1));
	                
	            }            
	            for(int r=0;r<p.length;r++){
	                if(p[r]!=0){
	                    int div = num % p[r];
	                    if(div==0){ count++;}
	                }
	            }
	            System.out.println(count);
	            count = 0;
	        }
	       
	        
	    }

	}
