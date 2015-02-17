
public class exercise8 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		node root = new node(5);
		insert(root,8);
		insert(root,4);
		insert(root,10);
		insert(root,3);
		System.out.println("ye sahi tha");
		printInOrder(root);
		
	}
	public static void insert(node root, int value){
		if(value<root.value){
			if(root.left!=null){
				insert(root.left,value);
			}
			else{
				System.out.println("Inserted"+value+"to the left of"+root.value);
				root.left = new node(value);
			}
		}else if(value>root.value){
			if(root.right!=null){
				insert(root.right,value);
			}else{
				System.out.println("Inserted"+value+"to the right of"+root.value);
				root.right=new node(value);
				
			}
		}
	}
	public static void printInOrder(node root){
		if(root!=null){
			printInOrder(root.left);
			System.out.println("Traversed"+root.value);
			printInOrder(root.right);
		}
	}

}
class node{
	node left;
	node right;
	int value;
	public node(int value){
		this.value = value;
	}
}