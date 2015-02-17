import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class stackAndqueue {
			
		Node headNode = null;
		Node tailNode = null;
		Node ltailNode = null;
	
	private class Node{
		Node next;
		Object value;
		public Node(){
			this.value = null;
			this.next = null;
		}
		public Node(Object val){
			this.value = val;
			this.next = null;
		}
		public Object getData(){
			return value;
		}
		public void setData(Object data){
			this.value = data;
		}
		public void setNext(Node node){
			this.next = node;
		}
		public Node getNext(){
			return next;
		}
	}
	public void push(Object value){
		Node node = new Node(value);
		if(this.headNode==null){
			headNode = node;
			tailNode = node;
		}else{
			this.ltailNode = tailNode;
			this.tailNode.setNext(node);
			this.tailNode = node;
		}
		
	}
	public void enqueue(Object value){
		Node node = new Node(value);
		if(this.headNode==null){
			headNode = node;
			tailNode = node;
		}else{
			this.tailNode.setNext(node);
			this.tailNode = node;
		}
		
	}
	public Object dequeue(){
		Node currentNode=headNode;
		Node temp = tailNode;
		Object ret=null;
		while(!(currentNode.getNext().equals(temp))){
			currentNode = currentNode.getNext();
		}
		ret = tailNode.getData();
		currentNode.setNext(null);
		tailNode = currentNode;
		return ret;
	}
	
	public void display(){
		Node currentNode=headNode;
		Node temp = tailNode;
		Object ret=null;
		do{
			System.out.println(currentNode.getData());
			currentNode = currentNode.getNext();
		}while(!(currentNode.getNext().equals(temp)));
		System.out.println(currentNode.getData());
		System.out.println(tailNode.getData());
	}
	
	
	 
	public Object pop(){
		Node currentNode=tailNode;
		Node temp =ltailNode;
		Object ret = currentNode.getData();
		currentNode.setData(null);
		temp.setNext(null);
		tailNode = temp;		
		return ret;
	}
	public void peep(){
		System.out.println(tailNode.getData());
	}
		
	
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		stackAndqueue stq = new stackAndqueue();int op;
		//stq.stack();
		stq.queue();
	}
	private void queue() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		stackAndqueue q = new stackAndqueue();int op;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		do{
			System.out.println("Enter Operation Type:1.enqueue 2.dequeue 3.display 4.Exit");
			op = Integer.parseInt(in.readLine());
			switch(op){
			case 1: System.out.println("enter the value:");
					Object val = in.readLine();
					q.enqueue(val);
					break;
			case 2: Object ret = q.dequeue();
					System.out.println(ret);
					break;
			case 3: q.display();
					break;
			}
		}while(op!=4);
		
	}
	private void stack() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		stackAndqueue st = new stackAndqueue();int op;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		do{
			System.out.println("Enter Operation Type:1.push 2.pop 3.peep 4.Exit");
			op = Integer.parseInt(in.readLine());
			switch(op){
			case 1: System.out.println("enter the value:");
					Object val = in.readLine();
					st.push(val);
					break;
			case 2: Object ret = st.pop();
					System.out.println(ret);
					break;
			case 3: st.peep();
					break;
			}
		}while(op!=4);
		
	}
}

