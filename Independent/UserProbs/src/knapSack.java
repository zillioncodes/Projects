
public class knapSack {

		public static void main(String[] args) {
		// TODO Auto-generated method stub
			int[] val = {10,40,30,50};
			int[] wt = {5,4,6,3};
			int w = 10;
			System.out.println(knapSack(val,wt,w));

		}

		private static int knapSack(int[] val, int[] wt, int w) {
			// TODO Auto-generated method stub
			int N = val.length;
			int[][] knapArrVal = new int[N+1][w+1];
			for(int col=0;col<=w;col++){
				knapArrVal[0][col] = 0;
			}
			for(int row=0;row<=N;row++){
				knapArrVal[row][0] = 0;
			}
			for(int item = 1;item<=N;item++){
				for(int weight = 1;weight<=w;weight++){
					if(wt[item-1]<=weight){
						knapArrVal[item][weight]=Math.max(knapArrVal[item-1][weight], val[item-1]+knapArrVal[item-1][weight-wt[item-1]]);						
					}else{
						knapArrVal[item][weight] = knapArrVal[item-1][weight];
					}
				}
			}
			for(int[]rows: knapArrVal){
				for(int col:rows){
					System.out.format("%5d",col);
				}
				System.out.println();
			}
			
			return knapArrVal[N][w];
					
		}

}
