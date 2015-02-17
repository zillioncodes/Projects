import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class bfs{
	
	public static void main(String[] args){
		Node nA=new Node('A');
		Node nB=new Node('B');
		Node nC=new Node('C');
		Node nD=new Node('D');
		Node nE=new Node('E');
		Node nF=new Node('F');
		Graph g = new Graph();
		
		g.addNode(nA);
		g.addNode(nB);
		g.addNode(nC);
		g.addNode(nD);
		g.addNode(nE);
		g.addNode(nF);
		g.setRootNode(nA);
		
		g.connectNode(nA,nB);
		g.connectNode(nA,nC);
		g.connectNode(nA,nD);		
		g.connectNode(nB,nE);
		g.connectNode(nB,nF);
		g.connectNode(nC,nF);
		
		//performBfs(g);
		performDfs(g);
		//getConnect(g);
	}
	
	private static void getConnect(Graph g) {
		// TODO Auto-generated method stub
		int count=0;
		for(int i=0;i<g.nodes.size();i++){
			Node node = (Node) g.nodes.get(i);
			if(node.color.equals("white")){
				performBfs(g);
				count++;
			}
		}
		System.out.println(count);
	}

	public static void performBfs(Graph g){
		Queue q = new LinkedList();
		q.add(g.rootNode);
		//System.out.println(g.rootNode.data);
		//System.out.println(g.rootNode.distance);
		g.rootNode.color = "grey";
		while(!(q.isEmpty())){
			Node n = (Node) q.remove();
			Node child=null;
			int startIndex = g.nodes.indexOf(n);
			for(int i=0;i<g.nodes.size();i++){
				if(g.adjMatrix[startIndex][i]==1){
					child = (Node) g.nodes.get(i);			
					if(child.color.equals("white")){
						//System.out.println(child.data);
						child.color = "grey";
						child.distance = n.distance + 1;
						//System.out.println(child.distance);
						child.parent = n;
						q.add(child);
					}
				}
			}
			n.color="black";
		}		
	}
	public static void performDfs(Graph g){
		Stack st = new Stack();
		for(int i=0;i<g.nodes.size();i++){
			Node n = (Node) g.nodes.get(i);
			if(n.color.equals("white")){
				performDfsVisit(g,n);
			}
			System.out.println(n.data);
			System.out.println(n.finish);
		}
		
	}

	private static void performDfsVisit(Graph g,Node n) {
		// TODO Auto-generated method stub=
		n.color = "grey"; int time=0;time+=1;
		n.distance = time;int startIndex=g.nodes.indexOf(n);
		for(int i=0;i<g.nodes.size();i++){
			if(g.adjMatrix[startIndex][i]==1){
				Node child = (Node) g.nodes.get(i);			
				if(child.color.equals("white")){
					//System.out.println(child.data);
					child.parent = n;
					performDfsVisit(g,child);
					//System.out.println(child.distan
				}
				child.color="black";
			}
		}
		n.finish = time + 1;
	}

}