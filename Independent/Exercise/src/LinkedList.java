import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LinkedList {
	Node headNode = null;
	Node tailNode = null;
	int size = 0;
	private class Node{
		Node next;
		Object data;
		
		public Node(){
			this.next = null;
			this.data = null;
		}
		
		public Node(Object value){
			this.data = value;
			this.next = null;
		}
		public Object getData(){
			return data;
		}
		public void setData(Object value){
			this.data = value;
		}
		public Node getNext(){
			return next;
		}
		public void setNext(Node next){
			this.next = next;
		}
	}
	public void add(Object val){
		Node node = new Node(val);
		if(this.headNode==null){
			this.tailNode = node;
			this.headNode=node;
		}else{
			this.tailNode.setNext(node);
			this.tailNode = node;
		}
		this.size++;
		System.out.println("The size of the list is:"+size);
	}
		
	public Object delete(Object val){
		Node currentNode = headNode;
		Node temp = currentNode;;
		if(headNode.data==null){
			return "List is empty";
		}else{
		do{
			if(currentNode.getData().equals(val)){
				currentNode.setData(null);
				temp.setNext(currentNode.getNext());
				return val+" has been deleted";
			}else{
				temp = currentNode;
				currentNode = currentNode.getNext();
				 
			}
		}while(currentNode!=null);
		}
		return "Entry not found in the linked list";
	}
	
	public void display(){
		Node currentNode = headNode;
		do{
			System.out.println(currentNode.getData());
			currentNode = currentNode.getNext();
		}while(currentNode!=null);
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LinkedList lobj = new LinkedList();int op;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		do{
			System.out.println("Enter Operation Type:1.Add 2.Delete 3.Display 4.Exit");
			op = Integer.parseInt(in.readLine());
			switch(op){
			case 1: System.out.println("enter the value:");
					Object val = in.readLine();
					lobj.add(val);
					break;
			case 2: System.out.println("Enter the value to be deleted:");
					Object val1 = in.readLine();
					Object ret = lobj.delete(val1);
					System.out.println(ret);
					break;
			case 3: lobj.display();
					break;
			}
		}while(op!=4);

	}

}
