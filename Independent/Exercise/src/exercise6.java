import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class exercise6 {
	
	   public static void main(String[] args) throws NumberFormatException, IOException {
			  BufferedReader in = new BufferedReader(new InputStreamReader(
				         System.in));
			      long numPackets = Long.parseLong(in.readLine());
			      long numKids = Long.parseLong(in.readLine());
			      long[] packets = new long[(int)numPackets];
			      
			      for(int i = 0; i < numPackets; i ++)
			      {
			         packets[i] = Long.parseLong(in.readLine());
			      }
			      long min = Long.MAX_VALUE;long max=0;
			      java.util.Arrays.sort(packets);
			      for(int j=0;j<numKids;j++){
			          if(packets[j]>max){max = packets[j];}
			          if(packets[j]<min){min = packets[j];}
			      }
			      long unfairness = max-min;
			      
			      // Write your code here, to process numPackets N, numKids K, and the packets of candies
			      // Compute the ideal value for unfairness over here
			      System.out.println(unfairness);
			       
			       
			   }
	}

