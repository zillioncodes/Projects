import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class exercise5 {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));int tmp=0;
        ArrayList<Long> ar = new ArrayList<Long>(); long fib1 = 0,fib2 = 1; long fib = fib1+fib2;ar.add(fib);
        long n = Long.parseLong(in.readLine());
        long[] numbers = new long[(int) n];
        for(int i=0;i<n;i++){
            numbers[i] = Long.parseLong(in.readLine());
        }
        for(int j=0;j<n;j++){
        	if(ar.contains(numbers[j])){System.out.println("IsFibo");tmp++;}
        	else{
        		while(fib<=numbers[j]){
        			if(fib==numbers[j] || ar.contains(numbers[j])) {System.out.println("IsFibo");tmp++;break;}
        			fib1 = fib2; fib2 = fib; fib = fib1+fib2; ar.add(fib);
        		}
            }
        	if(tmp==0){System.out.println("IsNotFibo");}
        	tmp=0;
        }

	}

}
