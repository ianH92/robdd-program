RobddProgram.java
Author: Ian D. (ianH92 on GitHub)


	RobddProgram is a program designed to allow a user to input a Boolean function to a graphical
interface. The program will then synthesize and display an Robdd (Reduced Ordered Binary Decision
Diagram) for that Boolean function. The program is designed to accept a Boolean expression in infix 
notation and a variable ordering from the user. Using the variable ordering, the program draws a 
ROBDD for the Boolean expression. The variables and terminal nodes are represented in the drawing as 
circular nodes. The zero paths (or low paths) between nodes are represented as dashed lines and the 
one paths (high paths) are represented as solid lines. The program is designed to reject invalid 
expressions as well as invalid variable orderings. The program supports saving an Robdd image to
the current folder in the PNG format.

Compiling and running the program:
Compile all .java files in the program directory and then run: java RobddProgram

Using the program:
1. Enter a Boolean expression in infix notation into the text box with prompt “Input Boolean 
Expression:”. The Boolean expression must follow the following rules:
a. The expression can contain any amount of whitespace separating the operators, parentheses, 
and variables. 
b. The expression can contain any number of parentheses, but the parentheses must be balanced.
The program will reject expressions with unbalanced parentheses. Parentheses, braces, or 
brackets – ( ), { }, or [ ] – are all accepted as parenthesis and  the program does not 
distinguish among them.
c. The characters '0' and '1' are reserved for program use and the program will reject 
expressions containing them.
d. The characters '+', '&', '^', and ' ' ' are reserved for the OR, AND, XOR, and NEGATION
operators. The expression can contain these as operators but not as variables. 
e. Expressions where all operators have the same precedence will be evaluated left-to-right. 
The first three operators are placed between the two variables or subexpressions they 
operate on. The negation operator is placed to the right of the operator it operates on.
f. All other 16-byte Java characters are acceptable as variables.
							       
Operator Precedence:
Operator        Precedence
'               1st
&, ^, +         2nd

2. Enter a variable ordering for the ROBDD to use into the text box under the text “Input Variable 
Ordering”. The variable ordering must follow the following rules.
a. The variables are entered without any spaces between them.  
b. The variables are ordered from left-to right, with the leftmost variable being the top 
variable in the ROBDD and the rightmost being the bottom variable in the ROBDD.
c. All variables in the Boolean expression must be in the variable ordering. 
The program will reject expressions which contain variables that are not in the variable 
ordering.
3. Once the Boolean expression and the variable input have been entered, click the “Build ROBDD” 
button to build the ROBDD.
4. To save the ROBDD image to the current folder navigate the the top menubar and click file, then
click Save RobddImage. The program will prompt the user for a name for the file, then save it.
NOTE: If the folder contains a file already possessing that name the file will be deleted.

The program follows this basic workflow:
1. The GUI prompts the user for input, and then passes that input to an implementation of Edsgar 
Dijkstra’s Shunting Yard Algorithm. The Shunting Yard Algorithm converts the infix expression to a 
postfix version, and also passes an error back to the GUI if the expression is invalid. The Shunting 
Yard Algorithm was used because it is much easier to evaluate the Boolean expression when it is in 
postfix form.
2. The GUI then passes the postfix expression to RobddBuilder.java. This program uses the Build 
and Make methods to build the ROBDD following the method described in “An Introduction to Binary 
Decision Diagrams” by Henrik Reif Andersen (Andersen 15).
3. The Build method recursively performs Shannon Expansion on the expression using the variable 
ordering. The Make method builds the nodes; first checking a unique table of nodes to use that node 
if it exists, and building the node if it does not. It also inserts every created node into a table 
of RobddNodes, which is where the ROBDD is stored. The unique table and the node table follow the 
methods described in Andersen’s paper and in 
“Efficient Implementation of a BDD Package” (Andersen 16; Brace, Bryant, and Rudell 41).
4. The Robdd is then returned to the GUI and then to DrawRobdd.java, which traverses the Robdd and 
draws circles for nodes and lines for paths between them using JavaFX’s Canvas.

Below are several Boolean expression test cases and variable orderings which can be used for testing. 
#	Boolean Expression                  Variable Ordering        Alternate Ordering
1	a + b + c                                abc	
2	{{[a) & (b)} & c) & d & (e & f)        abcdef	
3	a*b*c*d*e*f                            abcdef	
4	a&b+a'&c+b&c '&d                        abcd                         acdb
5	a&d + b&e + c&f                        abcdef                       adbecf
6	A + b + c +d                            abcd	
7	a&b&c&d&e&f&g&h&i&j&k+l&m&n&o&p    abcdefghijklmnop	

Works Cited
1.	Andersen, Henrik Reif. "An introduction to binary decision diagrams." Lecture notes, available online, IT University of Copenhagen (1997).

2.	 Brace, Karl S., Richard L. Rudell, and Randal E. Bryant. "Efficient implementation of a 
BDD package." Proceedings of the 27th ACM/IEEE design automation conference. ACM, 1991.