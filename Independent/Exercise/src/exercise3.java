import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;


public class exercise3 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int value=0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter anything except q:");
		char op = in.readLine().charAt(0);
		Stack<Integer> s1 = new Stack<Integer>();
		Stack<Integer> s2 = new Stack<Integer>();
		do{
			System.out.println("enter the integer to enqueue:");
			value = Integer.parseInt(in.readLine());
			s1.push(value);
			System.out.println("press q to exit OR press d to dequeue:");
			if(in.readLine().charAt(0)=='d')
			{	
				while(!s1.isEmpty()){
					int pop = s1.pop();
					s2.push(pop);
				}
				int deq = s2.pop();
				System.out.println(s2);
				System.out.println("dequeued:"+deq);
				s1=s2;
				System.out.println(s1);
				s2.clear();
				System.out.println(s2);
			}
		}while(in.readLine().charAt(0)!='q');			
		//System.out.println(value);
		
	}

}
