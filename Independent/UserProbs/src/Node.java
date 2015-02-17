
public class Node 
{
	public char data;
	public int distance=0;
	public int finish;	
	public String color;
	public Node parent;
	public Node(char l)
	{
		this.data=l;
		this.color="white";
	}
}