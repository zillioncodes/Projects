
public class arrayHopper {

	public static int arrayRecHopper(int[] arr, int l, int h){
		if(h==l){
			return 0;
		}
		if(arr[l]==0){
			return Integer.MAX_VALUE;
		}
		int min = Integer.MAX_VALUE;
		
		for(int i=l+1;i<=h && i<=l+arr[l];i++){
			int jumps = arrayRecHopper(arr,i,h);
			if(jumps!=Integer.MAX_VALUE && jumps+1<min){
				min = jumps+1;
			}
		}
		return min;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] array = {1, 2, 5, 8, 9, 2, 6, 7, 6, 8, 9};
		//System.out.println(arrayRecHopper(array,0,array.length));
		System.out.println(arrayDpHopper(array));
	}

	public static int getMin(int x,int y){return (x<y)?x:y;}
	private static int arrayDpHopper(int[] array) {
		// TODO Auto-generated method stub
		int[] jumps = new int[array.length];
		if(array[0]==0||array.length==0){
			return Integer.MAX_VALUE;
		}
		jumps[0]=0;
		for(int i=1;i<array.length;i++){
			jumps[i]=Integer.MAX_VALUE;
			for(int j=0;j<i;j++){
				if(i<=j + array[j] && jumps[j]!=Integer.MAX_VALUE){
					jumps[i] = arrayHopper.getMin(jumps[i],jumps[j]+1);
					break;
				}
			}
		}
		
		return jumps[array.length-1];
	}

}
