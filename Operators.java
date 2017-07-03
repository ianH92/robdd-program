/** Operators class. Used to pass a set of operators; method getPrecedence allows for checking valid
 * operators and returning their precedence.
 *
 * @author Ian (ianH92)
 * @version 1.0 
 * @since June 11th, 2017
 */
public class Operators {
	public char[] operators;
	private int[] precedence;
	private int[] arity;
	
	/** Constructs an empty operators object, private method.
	 */
	private Operators() {
		this.operators = {'+', '&', '^', '\''};
		this.precedence = {2, 2, 2, 1};
		this.arity = {2, 2, 2, 1};
	}
	
	
	/** Method returns the precedence of an operator. Also allows for checking for valid operators.
	 * The method will throw an illegalArgumentException if passed a operator not contained in the
	 * array of valid operators.
	 * @param c The operator for which to validate and determine precedence.
	 * @return The precedence of the operator.
	 */
	public int getPrecedence(char c) throws illegalArgumentException {
		int index = 0;
		for(char o: operators) {
			if(o == c) {
				return precedence[index];
			}
			index++;
		}
		// If code reaches here, the passed argument was invalid.
		throw new illegalArgumentException("Passed character is not a valid operator.");
	}
	
	/**
	 */
	public int getArity(char c) throws illegalArgumentException {
		int index = 0;
		for(char o: operators) {
			if(o == c) {
				return arity[index];
			}
			index++;
		}
		// If code reaches here, the passed argument was invalid.
		throw new illegalArgumentException("Passed character is not a valid operator.");
	}
	
	/**
	 *
	 */
	public char performOperatation(char operator, char... args) throws ExpressionError{
		switch (operator) {
			case '+': (args[0] == '1' || args[1] == '1') ? return '1':return '0';
			case '&': (args[0] == '1' && args[1] == '1') ? return '1':return '0';
			case '^': (args[0] != args[1]) ? return '1':return '0';
			case '\'': (args[0] == '1') ? return '1':return '0';
			default: throw new ExpressionError("Unsupported Operator");
		}
	}
}