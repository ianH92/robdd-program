/** Holds an ROBDD. The RobddFactory method builds an Robdd and then gathers information about it
 * to allow for drawing.
 *
 * @author Ian (ianH92)
 * @version 2.0
 * @since June 22nd, 2017
 */
public class Robdd {
	RobddNode root;
	RobddNodeTable table;
	int[] levelsCount;
	
	/** Constructs an Robdd
	 */
	public Robdd() {
		this.root = null;
		this.table = null;
		this.levelsCount = null;
	}
	
	/** Factory method that constructs and returns an ROBDD.
	 * The method creates a new Robdd, gets information about its levels, and then adds its
	 * nodes to an array in level order to allow for drawing the Robdd.
	 * @param postfixExpression The postfixExpression the ROBDD will represent.
	 * @param variableOrder The variable Ordering being used.
	 * @return The Robdd
	 */
	public static Robdd RobddFactory(char[] postfixExpression, char[] variableOrder, Operators ops) {
		Robdd newRobdd = new Robdd();
		
		newRobdd.table =  new RobddNodeTable(200);
		newRobdd.root = RobddBuilder.build(postfixExpression, variableOrder, ops, table);
		
		newRobdd.levelsCount = new int[variableOrder.length + 1];
		
		
		return newRobdd;
	}
	
	/** Method counts the number of nodes at each level; adds that info to an array.
	 * @param t The RobddNode to be added.
	 * @param levelsInfo The array to be filled with the number of nodes at each level.
	 * @param count The value for which a node should be counted. Indicates if the node
	 * has been visited during traversal.
	 */
	private static void setLevels(Robdd r, RobddNode n, int count, int level) {
		if(n.getCount() < count) {
			// Node hasn't been visited yet
			n.incCount();
			n.setLevel(level);
			r.levelsCount[level]++;
		}else {
			// Has been visited; adjust level count and set node level to current level.
			n.incCount();
			r.levelsCount[n.getLevel()]--;
			n.setLevel(level);
			r.levelsCount[level]++;
		}
		
		// Visit child nodes
		if(n.getLeftLink() >= 0) {
			
		}
	}
}



















