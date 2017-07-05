import java.util.NoSuchElementException;

/** Implements Dijkstra's shunting yard algorithm - converts infix expressions to postfix.
 * The infixToPostfix method will convert any valid infix expression to a valid postfix expression
 * and return it as a char array.
 *
 * @author Ian (ianH92)
 * @version 2.0
 * @since June 11th, 2017
 */
public class ShuntingYardAlgorithm {
	
	/** The method that carries out the algorithm.
	 * @param input The infix expression to be converted.
	 * @param variables The valid variables of the expression.
	 * @param ops An Operators object containing the valid operators and operator precedence.
	 * @return The postfix expression.
	 */
	public static char[] infixToPostfix(String input, char[] variables, Operators ops) throws ExpressionError {
		// Reject empty expressions.
		int inputLength = input.length();
		if(inputLength == 0) {
			throw new ExpressionError("Error: Input an equation.");
		}
		if(variables.length == 0) {
			throw new ExpressionError("Error: Input a variable ordering.");
		}
		
		// Arrays which define the parens used in the expression.
		final char[] parens = {'(', '[', '{', ')', ']', '}'};
		final char[] leftParens = {'(', '[', '{'};
		final char[] rightParens = {')', ']', '}'};
		
		NodeStack<Character> stack = new NodeStack<>();
		
		char[] output = new char[inputLength];
		int outputIndex = 0;
		int inputIndex = 0;
		
		// Initialize to random chars.
		char currChar = 'a';
		char tempChar  = 'a';
		
		// While there is input to be read
		while(inputIndex < inputLength) {
			try {
				// Pull a char from the input string.
				currChar = input.charAt(inputIndex);
			} catch(IndexOutOfBoundsException e) {
				System.err.println("charAt() method error." + e.getMessage());
			}
			
			if(currChar == ' ') {
				// Skip whitespace
			} else {
				if(checkArray(currChar, variables)) {
					output[outputIndex] = currChar;
					outputIndex++;
				} else if(checkArray(currChar, ops.operators)) {
					try {
						tempChar = stack.peek().charValue();
						
						/* While the top of the stack is an operator, and the precedence of the
						 * input token is less than or equal to the precedence of the top operator.
						 */
						while(ops.getPrecedence(currChar) <= ops.getPrecedence(tempChar)) {
							// Pop the top of the stack and append to the output string.
							output[outputIndex] = stack.pop().charValue();
							outputIndex++;
							tempChar = stack.peek().charValue();
						}
					} catch(IllegalArgumentException e) {
						// This is fine, tempChar was not an operator
					} catch(NoSuchElementException e) {
						// This is fine - stack is empty; while loop is done.
					}
					
					stack.push(new Character(currChar));
				} else if(checkArray(currChar, leftParens)) {
					// Push left parentheses onto stack.
					stack.push(new Character(currChar));
				} else if(checkArray(currChar, rightParens)) {
					// If a right parenthesis.
					try {
						tempChar = stack.peek().charValue();
						
						// While the top of the stack is not a left parentheses.
						while(!checkArray(tempChar, leftParens)) {
							output[outputIndex] = stack.pop().charValue();
							outputIndex++;
							tempChar = stack.peek().charValue();
						}
					} catch(NoSuchElementException e) {
						// Stack empty before a left parenthesis found; there are mismatched parens.
						throw new ExpressionError("Mismatched parentheses in expression"
													+ ", expected a left parenthesis.");
					}
					
					// Pop left parenthesis; do not append to output
					stack.pop();
				} else {
					String msg = "Error: " + currChar + "is not a variable, operator, or parenthesis.";
					throw new ExpressionError(msg);
				}
			}
			inputIndex++;
		}
		
		// While there are still tokens on the stack.
		while(!stack.isEmpty()) {
			tempChar = stack.peek().charValue();
			
			// If left parenthesis, there are mismatched parentheses.
			if(checkArray(tempChar, leftParens)) {
				throw new ExpressionError("Mismatched parentheses in expression" +
											", there are too many left parentheses.");
			}
			
			// Append the top of the stack to the outpu string.
			output[outputIndex] = stack.pop().charValue();
			outputIndex++;
		}
		return output;
	}
	
	/** Checks if a char is in a char array. Method used to check for valid variables.
	 * @param a The char to check for.
	 * @param array The char array to search.
	 * @return True if found, false otherwise.
	 */
	public static boolean checkArray(char a, char[] array) {
		// Traverse array.
		for(char c: array) {
			// If a is equal, return true.
			if(a == c) {
				return true;
			}
		}
		return false;
	}
}