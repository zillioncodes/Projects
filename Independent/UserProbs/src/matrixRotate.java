
public class matrixRotate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] arr = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};		
		int m = arr.length;
		int n = 4;int tmp = n-2;
		int[][] arrnew = new int[n][m];
		for(int k=0;k<m;k++){
			for(int l=0;l<n;l++){
				System.out.print(arr[k][l]+" ");
			}
			System.out.println();
		}
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){
				//System.out.println("1"+arrnew[j][tmp]);
				arrnew[j][tmp] = arr[i][j];
				//System.out.println("1"+arr[i][j]);
			}
			tmp--;
		}
		for(int k=0;k<n;k++){
			for(int l=0;l<m;l++){
				System.out.print(arrnew[k][l]+" ");
			}
			System.out.println();
		}
	}

}
