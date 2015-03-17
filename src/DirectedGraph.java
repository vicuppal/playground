import java.util.ArrayList;
import java.util.List;

/*
 * Name   : DirectedGraph.java
 * Author : VicramU
 * Created: Mar 17, 2015
 */



/**
 * DOCUMENT ME!
 *
 * @author VicramU
 */
public class DirectedGraph<T> {

	/*
	 **************************************** PUBLIC FIELDS ************************************************************
	 */

	/*
	 **************************************** PRIVATE FIELDS ***********************************************************
	 */
	private List<Node<T>> nodeList = null;

	/*
	 **************************************** CONSTRUCTORS *************************************************************
	 */

	/**
	 * Creates a new instance of DirectedGraph.
	 */
	public DirectedGraph(List<T> unsortedList) {
		if(unsortedList != null && !unsortedList.isEmpty()){
			nodeList = new ArrayList<Node<T>>();
			for(T obj : unsortedList){
				nodeList.add(new Node<T>(obj));
			}
		}
	}

	/*
	 **************************************** PUBLIC METHODS ***********************************************************
	 */

	public boolean addNode(Node<T> node){
		
		return false;
	}
	
	public boolean addEdge(Node<T> nStart, Node<T> nDest){
		
		return false;
	}
	
	/*
	 **************************************** PRIVATE METHODS **********************************************************
	 */
}
