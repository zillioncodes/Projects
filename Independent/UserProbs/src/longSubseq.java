import java.io.BufferedReader;
import java.io.InputStreamReader;


public class longSubseq {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String one = in.readLine();
		String two = in.readLine();
		getSequence(one,two);

	}

	private static void getSequence(String one, String two) {
		// TODO Auto-generated method stub
		char[] row = one.toCharArray();
		char[] col = two.toCharArray();
		int[][] recArray = new int[row.length+1][col.length+1];
		for(int i=0;i<row.length+1;i++){
			recArray[i][0]=0;
		}
		for(int j=0;j<col.length+1;j++){
			recArray[0][j]=0;
		}
		for(int p=1;p<row.length+1;p++){
			for(int q=1;q<col.length+1;q++){
				if(row[p-1]==col[q-1]){
					recArray[p][q]=recArray[p-1][q-1]+1;
				}else{
					recArray[p][q] = Math.max(recArray[p][q-1], recArray[p-1][q]);
				}
			}
		}
		for(int[]rows: recArray){
			for(int cols:rows){
				System.out.format("%5d",cols);
			}
			System.out.println();
		}
		
	}

}
