/** RobddBuilder Class builds the robbd. Implemented using methods adapted from "An Introduction to 
 * Binary Decision Diagrams", author Henrik Reif Andersen. Citation follows:
 *
 * The Build and Make methods were adapted from methods described in the lecture notes cited below:
 *
 * H. R. Andersen, Lecture Notes, Topic: "An Introduction to Binary Decision Diagrams."
 *		The IT University of Copenhagen, Copenhagen. Fall 1999.
 *
 * @author Ian (ianH92)
 * @version 2.0
 * @since June 22nd, 2017
 */
public class RobddBuilder {
	
	/** Builds the robdd using Shannon Expansion and the variable order array.
	 * Implemented using the method described in the lecture notes above.
	 * @param expression The postfix boolean expression to be converted to an ROBDD.
	 * @param variableOrder The char array containing the variable order.
	 * @return The RobddNode that holds the root of the ROBDD.
	 */
	public static RobddNode build(char[] expression, char[] variableOrder, Operators ops, RobddNodeTable t) throws ExpressionError {
		UniqueTable h = new UniqueTable(500);
		int root = buildHelper(expression, 0, variableOrder, t, h);
		return t.get(root);
	}
	
	/** Helper Class for build.
	 * @param expression The postfix boolean expression to be converted to an ROBDD.
	 * @param variableOrder The char array containing the variable order.
	 * @param i The current variable being expanded; i refers to its index in the variable array.
	 * @param t The table holding the ROBDD nodes.
	 * @param h The unique table used to look up the ROBDD nodes.
	 * @return The int value for table index that holds the root of the ROBDD.
	 */
	private static int buildHelper(char[] exp, int i, char[]varOrder, RobddNodeTable t, 
									UniqueTable h, Operators ops) throws ExpressionError {
		if(i < varOrder.length) {
			int v0 = buildHelper(shannonExpansion(exp, varOrder[i], '0'), (i + 1), varOrder, t, h);
			int v1 = buildHelper(shannonExpansion(exp, varOrder[i], '1'), (i + 1), varOrder, t, h);
			return make(i, v0, v1, t, h);
		} else {
			return postfixEvaluator(exp, ops);
		}
	}
	
	/** Make class implemented from the notes above. Makes the ROBDD nodes.
	 * @param i The variable number.
	 * @param l The index for the low path.
	 * @param h The index for the high path.
	 * @param t The table holding the ROBDD nodes.
	 * @param h The unique table used to look up the ROBDD nodes.
	 * @return The int value for table index that holds the root of the ROBDD.
	 */
	private static int make(int i, int l, int h, RobddNodeTable t, UniqueTable hTable) {
		if(l == h) {
			return l;
		} else if(hTable.isMember(i, l, h)) {
			return hTable.findMember(i, l, h);
		} else {
			int u = t.add(i, l, h);
			hTable.insert(u, i, l, h);
			return u;
		}
	}
	
	/** Evaluates a postfix expression when the variables have been replaced with either 1 or 0.
	 * @param exp The boolean expression to be evaluated.
	 * @return The result of the evaluation.
	 */
	public static int postfixEvaluator(char[] exp, Operators ops) throws ExpressionError {
		NodeStack<Character> stack = new NodeStack<>();
		int index = 0;
		char currChar = 'a';
		
		while(index < exp.length) {
			currChar = exp[index]
			
			if(currChar == '0' || currChar =='1') {
				// It is a variable.
				stack.push(new Character(currChar));
				
			} else if(ShuntingYardAlgorithm.checkArray(currChar, ops.operators) {
				// It is an operator.
				int operatorArgs = ops.getArity(currChar);
				
				if(operatorArgs > stack.size()) {
					throw new ExpressionError("Insufficient number of variables in expression.");
				}
				
				if(operatorArgs == 1) {
					stack.push(new Character(ops.performOperation
										(currChar, stack.pop().charValue(), stack.pop().charValue()));
				} else {
					stack.push(new Character(ops.performOperation(currChar, stack.pop().charValue()));
				}
			} else {
				throw new ExpressionError("Not all variables have been initialized to values.");
			}
			index++;
		}
		
		if(stack.size() == 1) {
			return stack.pop().getNumericValue();
		} else {
			throw new ExpressionError("There are too many variables in the Expression.");
		}
	}
	
	/** Performs Shannon Expansion on expression by replacing variables with replacement.
	 * Replacement should be either 0 or 1 to correctly expand the expression.
	 * @param exp The boolean expression to be expanded.
	 * @param variable The variable to be replaced.
	 * @param replacement The value to take the variable's place.
	 * @return The result of the expansion.
	 */
	private static char[] shannonExpansion(char[] exp, char variable, char replacement) {
		int index = 0;
		char currChar = 'a';
		
		while(index < exp.length()) {
			currChar = exp[index];
			
			if(currChar == variable) {
				exp[index] = replacement;
			}
			index ++;
		}
		return exp;
	}
}