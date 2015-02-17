import java.io.BufferedReader;
import java.io.InputStreamReader;


public class exercise10 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the String:");
		String str = in.readLine();int j=0;
		char[] carr = str.toCharArray();
		char[] carrcopy = new char[str.length()];
		for(int i=str.length()-1;i>-1;i--){
			//System.out.println(carr[i]);
			carrcopy[j] = carr[i];
			//System.out.println(carrcopy[j]);
			j++;
		}
		System.out.println(String.valueOf(carrcopy));
		StringBuffer s =  new StringBuffer(str).reverse();
		System.out.println(s.toString());
	}

}
