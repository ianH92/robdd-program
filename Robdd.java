/** Holds an ROBDD. The RobddFactory method builds an Robdd and then gathers information about it
 * to allow for drawing.
 *
 * @author Ian (ianH92)
 * @version 2.0
 * @since June 22nd, 2017
 */
public class Robdd {
	public RobddNode root;
	public int[] levelsCount;
	public RobddNode[][] nodes;
	
	/** Constructs an Robdd
	 */
	public Robdd() {
		this.root = null;
		this.levelsCount = null;
		this.nodes = null;
	}
	
	/** Factory method that constructs and returns an ROBDD.
	 * The method creates a new Robdd, gets information about its levels, and then adds its
	 * nodes to an array in level order to allow for drawing the Robdd.
	 * @param postfixExpression The postfixExpression the ROBDD will represent.
	 * @param variableOrder The variable Ordering being used.
	 * @return The Robdd
	 */
	public static Robdd RobddFactory(char[] postfixExpression, char[] variableOrder, Operators ops) 
	throws ExpressionError {
		Robdd newRobdd = new Robdd();
		
		newRobdd.root = RobddBuilder.build(postfixExpression, variableOrder, ops);
		newRobdd.levelsCount = new int[variableOrder.length + 1];
		
		newRobdd.setLevelsRobdd(newRobdd.root, (newRobdd.root.getCount() + 1), 0);
		
		newRobdd.nodes = new RobddNode[newRobdd.levelsCount.length][];
		// Construct the levelOrder array to hold the nodes.
		for(int i = 0; i < newRobdd.levelsCount.length; i++) {
			newRobdd.nodes[i] = new RobddNode[newRobdd.levelsCount[i]];
		}
		
		newRobdd.getLevels(newRobdd.root, (newRobdd.root.getCount() + 1));
		
		return newRobdd;
	}
	
	////ADDD INFO ABOUT COUNT
	/** Method counts the number of nodes at each level; adds that info to an array.
	 * @param t The RobddNode to be added.
	 * @param levelsInfo The array to be filled with the number of nodes at each level.
	 * @param count The value for which a node should be counted. Indicates if the node
	 * has been visited during traversal.
	 */
	private void setLevelsRobdd(RobddNode n, int count, int level) {
		if(n == null) {
			return;
		}
		if(n.getCount() == (count - 1)) {
			// Node hasn't been visited yet
			n.incCount();
			n.setLevel(level);
			this.levelsCount[level]++;
		} else {
			/* Node has been visited.
			 * If the current level is higher than node's level, set node's level to current level.
			 * If lower, leave unchanged.
			 */
			int nodeLevel = n.getLevel();
			if(nodeLevel < level) {
				this.levelsCount[nodeLevel]--;
				n.setLevel(level);
				this.levelsCount[level]++;
			}
		}
		
		setLevelsRobdd(n.getLeftChild(), count, (level + 1));
		setLevelsRobdd(n.getRightChild(), count, (level + 1));
	}
	
	private void getLevels(RobddNode n, int count) {
		if(n == null) {
			return;
		}
		
		if(n.getCount() == (count - 1)) {
			// Node hasn't been visited yet
			n.incCount();
			
			for(int i = 0; i < this.nodes[n.getLevel()].length; i++) {
				if(this.nodes[n.getLevel()][i] == null) {
					this.nodes[n.getLevel()][i] = n;
					break;
				}
			}
				
		}
		
		getLevels(n.getLeftChild(), count);
		getLevels(n.getRightChild(), count);
	}
}



















