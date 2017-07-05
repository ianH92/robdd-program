import java.util.NoSuchElementException;

/** NodeStack impelements the Stack Interface using a generic Node class. Each Node holds a value
 *  and a link to the next Node; these linked Nodes form the stack.
 *
 * @author Ian (ianH92)
 * @version 2.0
 * @since June 10th, 2017
 */
public class NodeStack<T> implements Stack<T> {
	private Node<T> top;
	private int size;
	
	/** Creates a NodeStack with no elements.
	 */
	public NodeStack() {
		this.top = null;
		this.size = 0;
	}
	
	/** Returns true if stack is empty, false otherwise.
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return (this.size == 0);
	}
	
	/** Returns the top of the stack, but does not remove it.
	 * @return the top of the stack.
	 * @exception NoSuchElementException if the stack is empty.
	 */
	public T peek() {
		if(size > 0) {
			return this.top.getValue();
		} else {
			throw new NoSuchElementException("Stack is Empty.");
		}
	}
	
	/** Returns and removes the top of the stack, and sets top to be the next node.
	 * @return the top of the stack.
	 * @exception NoSuchElementException if the stack is empty.
	 */
	public T pop() {
		if(size > 0) {
			// Temp variables to hold top values and list
			Node<T> temp = top.getNext();
			T value = top.getValue();
			
			// Delete the top, set new top and return
			this.top.setNext(null);
			this.top = temp;
			this.size--;
			return value;
		} else {
			throw new NoSuchElementException("Stack is Empty.");
		}
	}
	
	/** Adds the value to the top of the stack.
	 * @param value the item to be added to the top of the stack.
	 */
	public void push(T value) {
		if(this.size == 0) {
			this.top = new Node<>(value, null);
		} else {
			this.top = new Node<>(value, this.top);
		}
		this.size++;
	}
	
	/** Returns the number of elements in the stack.
	 * @return the number of elements.
	 */
	public int size() {
		return this.size;
	}
}