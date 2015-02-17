import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class lexicoSort {

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(in.readLine());
        for(int i=0;i<t;i++){
        	String input = in.readLine();
        	char[] arrayChar = input.toCharArray();
        	int prev = arrayChar[arrayChar.length-1];
        	for(int j=arrayChar.length-2;j>=0;j--){
        		int curr = arrayChar[j];
        		if(prev>curr){
        			char temp=arrayChar[j+1];
        			arrayChar[j+1]=arrayChar[j];
        			arrayChar[j] = temp;
        			break;
        		}else{
        			prev = arrayChar[j];
        		}
        	}
        	System.out.println(String.valueOf(arrayChar));
        }

	}

}
