import java.io.BufferedReader;
import java.io.InputStreamReader;


public class exercise12 {

	public static void main(String[] args) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(in.readLine());int j=0,k=0; int res1=0, res2=0;       
        for(int p=0;p<t;p++){
            int m = Integer.parseInt(in.readLine());
            int n = Integer.parseInt(in.readLine());
            String[] str=in.readLine().split(" ");
            int[] num = new int[n];
            for(int i=0;i<n;i++){
                num[i] = Integer.parseInt(str[i]);
            }
            for(j=0;j<n;j++){
                for(k=j+1;k<n;k++){
                	int tmp = num[j]+num[k];
                    if(m==tmp){res1=j+1;res2=k+1; break;}
                }
            }
            System.out.println(res1+" "+res2);
            j=0;k=0;
        }
    }
}
