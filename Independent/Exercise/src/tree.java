import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class tree {
	tree leftNode;
	tree parent;
	tree rightNode;
	int data;
	
	tree(int value){
		data = value;
	}
	
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int value; final int count=0;
		System.out.println("Start a binary tree, Enter the root Integer:");
		value = Integer.parseInt(in.readLine());
		tree root = new tree(value);int op;
		do{
			System.out.println("Enter:1.Insert 2.Delete 3.Inorder 4.PostOrder 5.PreOrder 6.Exit");
			op = Integer.parseInt(in.readLine());
			switch(op){
			case 1: System.out.println("Enter the data to be inserted:");
					int val = Integer.parseInt(in.readLine());
					root.insert(val);
					break;
			case 2: System.out.println("Enter the data to be deleted:");
					int findNum = Integer.parseInt(in.readLine());
					root.delete(findNum,count);
			case 3: root.inOrder(root);
					break;
			case 4: root.postOrder(root);
					break;
			case 5: root.preOrder(root);
					break;
		}
		}while(op!=6);
	}


	private void delete(int findNum,int count) {
		// TODO Auto-generated method stub
		int tmp=0;
		int countr=0;
		if(this!=null){
			if(this.data==findNum){
				System.out.println("Data Found at tree level "+ count);
				if(this.leftNode==null && this.rightNode==null){ 
					if(parent.leftNode.data==findNum) parent.leftNode=null;
				    else parent.rightNode=null;
				}
				if(this.leftNode!=null && this.rightNode==null){ parent = this.leftNode;}
				if(this.leftNode==null && this.rightNode!=null){ parent = this.rightNode;}
				if(this.leftNode!=null && this.rightNode!=null){ 
					Object flag=null;
					int ret = inOrder(this,tmp);
					delete(ret,countr);
					this.data = ret;
				}
			}
			else if(this.data<findNum){
						count++;
						this.rightNode.parent = this;
						this.rightNode.delete(findNum, count);
						}
						//if(tmp==findNum){this.rightNode = null; return tmp; }
			else{count++;
				this.leftNode.parent = this;
				this.leftNode.delete(findNum, count);
			}
				//if(tmp==findNum){this.leftNode = null; return tmp;
		}
	}
	


	private int inOrder(tree root, int tmp) {
		// TODO Auto-generated method stub
		
		if(root!=null){			
			tmp= inOrder(root.leftNode,tmp);
			tmp = root.data;
			tmp= inOrder(root.rightNode,tmp);
		}
		return tmp;
	}


	private void preOrder(tree root) {
		// TODO Auto-generated method stub
		if(root!=null){
			System.out.println("At Node with Value:"+root.data);
			preOrder(root.leftNode);
			preOrder(root.rightNode);
		}
	}


	private void postOrder(tree root) {
		// TODO Auto-generated method stub
		if(root!=null){
			postOrder(root.leftNode);
			postOrder(root.rightNode);
			System.out.println("At Node with Value:"+root.data);
			
		}
	}
	


	private void inOrder(tree root) {
		// TODO Auto-generated method stub
		if(root!=null){
			inOrder(root.leftNode);
			System.out.println("At Node with Value:"+root.data);
			inOrder(root.rightNode);
		}
		
	}


	private void insert(int val) {
		// TODO Auto-generated method stub
		if(val<this.data){
			if(this.leftNode==null){
				System.out.println("Inserted the given data to the left of the node:"+this.data);
				this.leftNode = new tree(val);
			}else{
					leftNode.insert(val);
			}
		}else if(val>this.data){
			if(this.rightNode==null){
				System.out.println("Inserted the iven data to the right of the node:"+this.data);
				this.rightNode = new tree(val);
			}else{
				rightNode.insert(val);
			}			
		}
	}
	
	

}
