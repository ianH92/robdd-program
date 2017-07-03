/** RobddNode class stores information for a node in Reduced Ordered Binary Decision Diagram(ROBDD). 
 * Each node has a unique integer nodeNumber which idenitifies it from the other nodes.
 *
 * @author Ian (ianH92)
 * @version 2.0
 * @since June 22nd, 2017
 */
public class RobddNode {
	// Basic Node attributes.
	private final int nodeNumber;
	private int level;
	private int variable;
	
	// Links to the child RobddNodes.
	private int leftLink;
	private int rightLink;
	
	// Identifies if the Node has been counted during a traversal.
	private int count;
	
	/** Constructor for RobddNode
	 * Constructor takes a node number, a variable, and a level. All other values initialized
	 * to null or zero and must be set later using accessor methods.
	 * @param ndeNum The unique number of the node.
	 * @param var The variable the node holds.
	 * @param lev The level of the node, determined by the variable (user should check the varible 
	 * ordering and assign the value based on that.)
	 */
	public RobddNode(int i, int l, int h, int nodeNumber) {
		this.nodeNumber = nodeNumber;
		this.variable = i;
		this.leftLink = l;
		this.rightLink = h;
		this.count = 0;
		this.level = -1;
	}
	
	/** Increases number of times node has been counted.
	 * @return the number of times the node has been counted.
	 */
	 public void incCount() {
		 this.count++;
	 }
	
	/** Returns number of times node counted as determined by incCounted() method.
	 *
	 * Checks if the node has been counted during traversal. 0 means it has not been counted,
	 * anything higher means it has. "Counted" means any operation that is only meant to affect the 
	 * node once. The number indicates the number of times the node has been
	 * counted. Intended to be used to track if a node has been accessed during traversal to prevent
	 * a node from being counted multiple times.
	 * @return The number of times the node has been counted.
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 *
	 */
	public int getLevel() {
		return this.level;
	}
	
	/**
	 *
	 */
	public int getLeftLink() {
		return this.leftLink;
	}
	
	/** Returns unique node number.
	 * @return the node number.
	 */
	public int getNodeNum() {
		return this.nodeNumber;
	}
	
	/**
	 *
	 */
	public int getRightLink() {
		return this.rightLink;
	}
	
	/**
	 *
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/** Returns node variable.
	 * @return The node variable.
	 */
	public int getVar() {
		return this.variable;
	}
}