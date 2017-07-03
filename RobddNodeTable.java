/** RobddNodeTable holds the RobddNodes as the Robdd is constructed.
 *
 * Table supports adding a node, getting a node, and increasing the table size.
 * @author Ian (ianH92)
 * @version 2.0
 * @since June 22nd, 2017
 */
public class RobddNodeTable {
	RobddNode[] table;
	int nextSpot;
	int tableSize;
	
	/** Constructs an RobddNodeTable and adds the zero and one terminal nodes to the table.
	 */
	public RobddNodeTable(int startSize) {
		this.table = new RobddNode[startSize];
		this.tableSize = startSize;
		
		// Zero Node
		this.table[0] = RobddNode.RobddNode(-2, -1, -1, this.nextSpot);
		this.nextSpot++;
		// One Node
		this.table[1] = RobddNode.RobddNode(-1, -1, -1, this.nextSpot);
		this.nextSpot++;
	}
	
	/** Adds a node to the table as mapped by its variable, low path, and high path.
	 * @param i The variable of the node.
	 * @param l The low path of the node.
	 * @param h The high path of the node.
	 * @return The index the node was stored at.
	 */
	public int add(int i, int l, int h) {
		int u = this.nextSpot;
		RobddNode tmp = RobddNode.RobddNode(i, l, h, u);
		
		if((this.tableSize - 2) > u) {
			this.table[u] = tmp;
			this.nextSpot++;
		} else {
			increaseTable(u);
			this.table[u] = tmp;
			this.nextSpot++;
		}
		return u;
	}
	
	/** Increases the table size when necessary to ensure nodes can fit.
	 * @param u The index currently needing to be accessed - ensures that the index is present.
	 */
	private void increaseTable(int u) {
		RobddNode[] newTable = new RobddNode[2 * tableSize + u];
		
		for(int i = 0; i < this.table.length; i++) {
			newTable[i] = this.table[i];
		}
		
		this.table = newTable;
		this.tableSize = 2 * tableSize + u;
	}
	
	/** Gets the robdd node stored at index.
	 * @param u The index to be accessed.
	 * @return The robdd node stored at the index.
	 */
	public RobddNode get(int u) {
		return this.table[u];
	}
}