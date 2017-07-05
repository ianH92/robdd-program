/** UniqueTable class maps a node with a variable i, a low path l, and a high path h to an index.
 * Table resolves collisions by linking. Supports searching for a node, retreiving a node, and
 * inserting. Holds the uNode class which is used to store information in the table.
 *
 * @author ian (ianH92)
 * @version 1.0
 * @since March 3rd, 2017
 */
public class UniqueTable {
	private uNode[] table;
	private int numberOfNodes;
	private int tableSize;
	
	/** Constructor for the table.
	 * @param startSize The size the table should be initialized to. Recommend power of 2.
	 */
	public UniqueTable(int startSize) {
		this.table = new uNode[startSize];
		this.tableSize = startSize;
		this.numberOfNodes =0;
	}

	/** Checks if a node is in the table.
	 * @param i The variable of the node.
	 * @param l The low path of the node.
	 * @param h The high path of the node.
	 * @return True if a member, false otherwise.
	 */
	public boolean isMember(int i, int l, int h) {
		int hash = hashCode(i, l, h);
		int index = hash % this.tableSize;
		uNode tmp = this.table[index];
		
		while(tmp != null) {
			if(tmp.equals(i, l, h)) {
				return true;
			}
			tmp = tmp.getNext();			
		}
		return false;
	}
	
	/** Returns the index in the RobddNodeTable of the node.
	 * @param i The variable of the node.
	 * @param l The low path of the node.
	 * @param h The high path of the node.
	 * @return The index in the RobddNodeTable for the node.
	 */
	public int findMember(int i, int l, int h) {
		int hash = hashCode(i, l, h);
		int index = hash % this.tableSize;
		uNode tmp = this.table[index];
		
		while(tmp != null) {
			if(tmp.equals(i, l, h)) {
				return tmp.getU();
			}
			tmp = tmp.getNext();			
		}
		return -1;
	}
	
	/** Inserts the node into the table.
	 * @param u The index of the node in the RobddNodeTable.
	 * @param i The variable of the node.
	 * @param l The low path of the node.
	 * @param h The high path of the node.
	 */
	public void insert(int u, int i, int l, int h) {
		uNode newNode = new uNode(u, i, l, h);
		int hash = hashCode(i, l, h);
		int index = hash % this.tableSize;
		uNode tmp = this.table[index];
		
		if(tmp == null) {
			this.table[index] = newNode;
		} else {
			while(tmp.getNext() != null) {
				tmp = tmp.getNext();
			}
			tmp.setNext(newNode);
		}
	}
	
	/** Returns the hashCode for a node.
	 * @param i The variable of the node.
	 * @param l The low path of the node.
	 * @param h The high path of the node.
	 * @return The node's hashCode.
	 */
	private int hashCode(int i, int l, int h) {
		return 37 * ((int)(i + Math.pow(l, 2) + Math.pow(h, 3)));
	}
	
	/** Class used to store node information.
	 * 
	 * @author Ian Drummond
	 * @version 1.0
	 * @since March 3rd, 2017
	 */
	public class uNode {
		private int u;
		private int i;
		private int l;
		private int h;
		private uNode next;
		
		/** Constructs the node.
		 * @param u The index of the node in the RobddNodeTable.
		 * @param i The variable of the node.
		 * @param l The low path of the node.
		 * @param h The high path of the node.
		 */
		public uNode(int u, int i, int l, int h) {
			this.u = u;
			this.i = i;
			this.l = l;
			this.h = h;
			this.next = null;
		}
		
		/** Checks if two nodes are equal.
		 * @param i The variable of the node.
		 * @param l The low path of the node.
		 * @param h The high path of the node.
		 * @return True if equal, false otherwise.
		 */
		public boolean equals(int i, int l, int h) {
			return (this.i == i && this.l == l && this.h == h);
		}
		
		/** Returns the RobddNodeTable index.
		 * @return the RobddNodeTable index.
		 */
		public int getU() {
			return this.u;
		}
		
		/** Returns the variable.
		 * @return the variable.
		 */
		public int getI() {
			return this.i;
		}
		
		/** Returns the low path.
		 * @return the RobddNodeTable index.
		 */
		public int getL() {
			return this.l;
		}
		
		/** Returns the high path.
		 * @return the high path.
		 */
		public int getH() {
			return this.h;
		}
		
		/** Returns the next link.
		 * @return the next link.
		 */
		public uNode getNext() {
			return this.next;
		}
		
		/** Sets the next link.
		 * @param next The next link.
		 */
		public void setNext(uNode next) {
			this.next = next;
		}
	}
}