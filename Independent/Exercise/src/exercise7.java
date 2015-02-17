import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;


public class exercise7 {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the operation:1.Push 2.Pop 3.Display");
		int op = Integer.parseInt(in.readLine());
		
		Queue<Integer> q1 = new LinkedList<Integer>();
		Queue<Integer> q2 = new LinkedList<Integer>();
		do{
			switch(op){
			case 1:
				System.out.println("Enter the integer:");
				int val = Integer.parseInt(in.readLine());
				push(val,q1,q2);
				break;
			case 2:
				q1 = pop(q1,q2);
				System.out.println(q1);
				//System.out.println(q1);
				break;
			case 3:
				System.out.println(q1);
				break;
			}
			System.out.println("Enter the operation:1.Push 2.Pop 3.Display");
			op = Integer.parseInt(in.readLine());
		}while(op!=4);
		
		
	}

	private static Queue<Integer> pop(Queue<Integer> q1, Queue<Integer> q2) {
		// TODO Auto-generated method stub
		q2.clear();
		int head=0;int sz=0;
		System.out.println(q1);
		while(sz<=q1.size()){
			head = q1.remove();
			q2.add(head);
			sz++;
		}
		head = q1.remove();
		q1=q2;
		System.out.println("item popped:"+head);
		//System.out.println(q2);
		//System.out.println(q1);
		return q1;
	}

	private static void push(int val, Queue<Integer> q1, Queue<Integer> q2) {
		// TODO Auto-generated method stub
		q1.add(val);
		
	}

}
