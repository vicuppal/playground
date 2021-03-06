import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/*
 * Name   : DirectedGraph.java
 * Author : VicramU
 * Created: Mar 17, 2015
 */

/**
 * DirectedGraph class is used to generate a Directed Graph based on a List of objects of Type T.
 * Dependencies between objects are created based on compareTo so Class of Type T must implement Comparable<T>
 * Topological Sorting will only correctly work if the directed graph generated is a Directed Acyclic Graph.
 *
 * @author VicramU
 */
public class DirectedGraph<T extends Comparable<T>> {

	private List<Node<T>> nodeList = null;
	
	private final Map<Node<T>, LinkedList<Node<T>>> graph = new HashMap<Node<T>, LinkedList<Node<T>>>();

	/**
	 * Creates a new instance of DirectedGraph.
	 */
	public DirectedGraph(List<T> unsortedList) {
		
		initialzeGraph(unsortedList);
		buildGraph(nodeList);
	}

	public boolean addNode(T obj){
		Node<T> node = new Node<T>(obj);
		graph.put(node, new LinkedList<Node<T>>());
		return nodeList.add(node);
	}
	
	public boolean addEdge(Node<T> start, Node<T> dest){
		if(start == null || dest == null || !graph.containsKey(start) || !graph.containsKey(dest)){
			return false;
		}
		
		start.increaseOutgoingEdgeCount();
		dest.increaseIncomingEdgeCount();
		
		return graph.get(start).add(dest);
	}
	
	public List<T> topologicalSort(){
		
		Node<T> tempNode = null;
		Map<Node<T>, LinkedList<Node<T>>> tempGraph = null;
		Iterator<Map.Entry<Node<T>, LinkedList<Node<T>>>> iterator = null;
		
		List<T> sortedList = new ArrayList<T>();
		Queue<Node<T>> zeroIncomingEdgeQueue = new LinkedList<Node<T>>();
		
		if(graph == null || nodeList == null || graph.isEmpty() || nodeList.isEmpty() || graph.size() != nodeList.size()){
			return sortedList;
		}
		
		tempGraph = new HashMap<Node<T>, LinkedList<Node<T>>>(graph);
		
		//put any node with no edges on the sorted list first
		iterator = tempGraph.entrySet().iterator();
		
		
		while (iterator.hasNext()) {
			tempNode = iterator.next().getKey();
			
			if(tempNode != null && tempNode.hasNoEdges()){
				sortedList.add(tempNode.getObject());
				iterator.remove();
			}
		}
		
		int i = tempGraph.size();
		while(tempGraph.size() > 0 && i > 0){

			//put 0 incomingEdge nodes in queue
			iterator = tempGraph.entrySet().iterator();
			while (iterator.hasNext()) {
				tempNode = iterator.next().getKey();
				
				if(tempNode != null && tempNode.getIncomingEdgeCount() == 0){
					//System.out.println("ADDED TO QUEUE: " + tempNode.getObject().toString());
					zeroIncomingEdgeQueue.add(tempNode);
					iterator.remove();
				}
			}
			
			//dequeue
			tempNode = zeroIncomingEdgeQueue.poll();
			//System.out.println("TAKEN FROM QUEUE: " + tempNode.getObject().toString());
			if(tempNode != null){
				/*
				if(graph.containsKey(tempNode)){
					System.out.println("CONTAINED: " + tempNode.getObject().toString());
				}
				else{
					System.out.println("NOT CONTAINED: " + tempNode.getObject().toString());
				}*/
				//decrease incoming edge count from all dependent nodes
				for(Node<T> dependentNode : graph.get(tempNode)){
					if(dependentNode != null){
						dependentNode.decreaseIncomingEdgeCount();
					}
				}
				sortedList.add(tempNode.getObject());
				tempGraph.remove(tempNode);
				//System.out.println("REMOVED FROM GRAPH: " + tempNode.getObject().toString());
			}
			else if(tempNode == null && tempGraph.size() > 0){
				//there is a cyclical dependency...return null or throw exception?
				//LOG something
				return null;
			}
			i--;
		}
	
		return sortedList;
	}
	
	private boolean initialzeGraph(List<T> list){

		if(list == null || list.isEmpty()){
			return false;
		}

		nodeList = new ArrayList<Node<T>>();
		for(T obj : list){
			addNode(obj);
		}
		return true;
	}
	
	/**
	 * 
	 * @param nodeList
	 */
	private boolean buildGraph(List<Node<T>> nodeList) {
		if(graph == null || nodeList == null || graph.isEmpty() || nodeList.isEmpty() || graph.size() != nodeList.size()){
			return false;
		}
		
		for(Node<T> key : graph.keySet()){
			for(Node<T> node : nodeList){
				if(key != null && node != null && key.getObject() != null && node.getObject() != null && !node.getObject().equals(key.getObject())){
					//if key depends on node add it to key's
					if(key.compareTo(node) < 0){
						addEdge(key, node);
					}
				}
			}
		}
		return true;
	}	
	

	
	/**
	 * Inner class Node contains the object to compare along with counters for incoming 
	 * and outgoing edges for itself.
	 *
	 * @author VicramU
	 */
	private final class Node<E extends Comparable<E>> implements Comparable<Node<E>> {
		
		private E object = null;

		private int incomingEdgeCount = 0;

		private int outgoingEdgeCount = 0;
		
		/**
		 * Creates a new instance of Node.
		 */
		public Node(E e) {
			this.object = e;
		}

		public boolean hasNoEdges(){
			return ((incomingEdgeCount == 0) && (outgoingEdgeCount == 0));
		}

		/**
		 * @return the object
		 */
		public E getObject() {
			return this.object;
		}
		
		/**
		 * @return the incomingEdgeCount
		 */
		public int getIncomingEdgeCount() {
			return incomingEdgeCount;
		}
		
		/**
		 * @return the outgoingEdgeCount
		 */
		public int getOutgoingEdgeCount() {
			return outgoingEdgeCount;
		}
		
		public void decreaseIncomingEdgeCount(){
			if(this.incomingEdgeCount > 0){
				this.incomingEdgeCount--;	
			}
		}
		
		public void increaseIncomingEdgeCount(){
			this.incomingEdgeCount++;
		}
		
		public void decreaseOutgoingEdgeCount(){
			if(this.outgoingEdgeCount > 0){
				this.outgoingEdgeCount--;	
			}
		}
		
		public void increaseOutgoingEdgeCount(){
			this.outgoingEdgeCount++;
		}		

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Node<E> o) {
			return this.object.compareTo(o.getObject());
		}		
		
	}
	
	
}
