import java.util.*;


public class Graph {
	public Node rootNode;
	public ArrayList nodes=new ArrayList();
	public int[][] adjMatrix;//Edges will be represented as adjacency Matrix
	int size;	
	public void setRootNode(Node n)
	{
		this.rootNode=n;
	}	
	
	public Node getRootNode()
	{
		return this.rootNode;
	}	
	
	public void addNode(Node n)
	{
		nodes.add(n);
	}
	
	//This method will be called to make connect two nodes
	public void connectNode(Node start,Node end)
	{
		if(adjMatrix==null)
		{
			size=nodes.size();
			adjMatrix=new int[size][size];
		}

		int startIndex=nodes.indexOf(start);
		int endIndex=nodes.indexOf(end);
		adjMatrix[startIndex][endIndex]=1;
		adjMatrix[endIndex][startIndex]=1;
		
	}
}

