import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ediDistance {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String one = in.readLine();char[] row = one.toCharArray();
		String two = in.readLine();char[] col = two.toCharArray();
		int[][] dp = new int[one.length()+1][two.length()+1];
		for(int i=0;i<row.length+1;i++){
			dp[i][0] = 0;
		}
		for(int j=0;j<col.length+1;j++){
			dp[0][j] = 0;
		}
		for(int p=1;p<row.length+1;p++){
			for(int q=1;q<col.length+1;q++){
				char c1 = row[p-1];
				char c2 = col[q-1];
				if(c1==c2){dp[p][q] = dp[p-1][q-1];}
				else{
					int replace = dp[p-1][q-1] + 1;
					int insert = dp[p-1][q]+1;
					int delete = dp[p][q-1]+1;
					int min = replace>delete ? delete:replace;
					min = insert>min ? min:insert;
					dp[p][q] = min;
				}
			}
		}
		System.out.println(dp[row.length][col.length]);
	}

}
