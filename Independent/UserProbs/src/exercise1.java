import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class exercise1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = {1,2,3,4,5,};int temp1,temp2;
		Map<Integer,String> map = new HashMap<Integer,String>();
		for(int i=0;i<arr.length;i++){
			for(int j=1;j<arr.length;j++){
			temp1 = arr[i]*arr[i] + arr[j]*arr[j];
			String str = arr[i]+" "+arr[j];
			map.put(temp1, str);
			}
		}
		for(int k=0;k<arr.length;k++){
			arr[k] = arr[k]*arr[k];
		}
		for(int s=0;s<arr.length;s++){
			if(map.containsKey(arr[s])){
				temp2 = (int) Math.sqrt((double)arr[s]);
				System.out.println(map.get(arr[s])+" "+temp2);
				
			}
		}
	}

}
