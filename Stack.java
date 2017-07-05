/** Generic Interface for a Stack.
 *
 * @author ian (ianH92)
 * @version 1.0
 * @since 2017-2-25
 */
public interface Stack<E> {
	
	/** Returns true if empty, false otherwise.
	 * @return true if empty, false otherwise.
	 */
	boolean isEmpty();
	
	/** Returns the top of the stack, but does not remove it.
	 * @return the top of the stack.
	 * @exception NoSuchElementException if the stack is empty.
	 */
	E peek();
	
	/** Returns and removes the top of the stack.
	 * @return the top of the stack.
	 * @exception NoSuchElementException if the stack is empty.
	 */
	E pop();
	
	/** Adds the element to the top of the stack.
	 * @param element the item to be added to the top of the stack.
	 */
	void push(E element);
	
	/** Returns the number of elements in the stack.
	 * @return the number of elements.
	 */
	int size();
}
