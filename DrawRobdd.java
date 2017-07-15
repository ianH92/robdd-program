import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Group;
import javafx.scene.text.*;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class DrawRobdd {

	/** Method used to draw the Robdd
	 * @param r the robdd to be drawn.
	 * @param variables the variables of the robdd in order.
	 */
	public static Canvas drawRobddNodes(Robdd r, char[] variables) {
		// Array of Colors used in drawing the nodes.
		Color[] cols = {Color.DEEPSKYBLUE, Color.MEDIUMSEAGREEN, Color.LIGHTCORAL, 
										Color.GOLD, Color.LIGHTSKYBLUE, Color.LIGHTGREEN, 
										Color.LIGHTPINK};
		
		// Find the maximum number of nodes which occur at a level
		int maxNodes = getMax(r.levelsCount);
		// And the number of levels is
		int numOfLevels = r.levelsCount.length;
		
		int xBuffer = 10;
		int yBuffer = 20;
		int nodeWidth = 20;
		int nodeSpace = 80;
		int levelSeparation = 60;
		int drawWidth = nodeSpace * maxNodes;
		int drawHeight = ((numOfLevels - 1) * levelSeparation) + (2 * yBuffer);
		
		// Calculate canvas's true height/width
		int height = drawHeight + (2 * xBuffer);
		int width = drawWidth + (2 * yBuffer);
		
		// Calculate the middle of the canvas
		int middle = (width / 2);
		
		int[] levelStartX = new int[numOfLevels];
		int totalLevelWidth = 0;
		// Calculate the position at which each level will begin drawing.
		for(int i = 0; i < numOfLevels; i++) {
			totalLevelWidth = (nodeSpace * r.levelsCount[i]);
			levelStartX[i] = middle - (totalLevelWidth / 2);
		}
		
		// Create the Canvas and GraphicsContext
		Canvas canvas = DrawRobdd.getWhiteCanvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		// Calculate the x and y coordinates for each node
		int distUsed;
		int x;
		int y;
		for(int i = 0; i < numOfLevels; i++) {
			for(int j = 0; j < r.levelsCount[i]; j++) {
				distUsed = nodeSpace * j;
				x = levelStartX[i] + distUsed + (nodeSpace / 2);
				y = yBuffer + (i * levelSeparation);
				
				// Set the new x and y coordinates of each nodeSpace
				r.nodes[i][j].setX(x);
				r.nodes[i][j].setY(y);
			}
		}
		
		// Draw the edges and the nodes
		double[] lineDashArray = {5.0};
		for(int i = 0; i < numOfLevels; i++) {
			
			for(int j = 0; j < r.levelsCount[i]; j++) {
				RobddNode n =  r.nodes[i][j];
				x = n.getX();
				y = n.getY();
				
				// Draw the left edge
				RobddNode leftChild = n.getLeftChild();
				if(leftChild != null) {
					int tmpX = x;
					int tmpY = y;
					int leftX = leftChild.getX();
					int leftY= leftChild.getY();
				
					if((n.getLevel() + 1) == leftChild.getLevel()) {
						// If child is only one level below then no possible intersection, just draw
						gc.setLineDashes(lineDashArray);
						gc.strokeLine(x, y, leftX, leftY);
					} else {
						drawEdge(r.nodes, n, leftChild, nodeWidth, (nodeSpace / 3), gc);
					}
				}
				
				// Draw the right edge
				RobddNode rightChild = n.getRightChild();
				if(rightChild != null) {
					
					if((n.getLevel() + 1) == rightChild.getLevel()) {
						// If child is only one level below then no possible intersection, just draw
						int rightX = rightChild.getX();
						int rightY = rightChild.getY();
						gc.setLineDashes(null);
						gc.strokeLine(x, y, rightX, rightY);
					} else {
						drawEdge(r.nodes, n, rightChild, nodeWidth, (nodeSpace / 3), gc);
					}
				}
				
				// Draw the node
				gc.setFill(cols[i % cols.length]);
				gc.fillOval(x - (nodeWidth / 2), y - (nodeWidth / 2), nodeWidth, nodeWidth);
				gc.setFill(Color.BLACK);
					
				// Draw the node labels
				String txt = "tmp";
				if(r.nodes[i][j].getVar() == -1) {
					txt = "1";
				} else if(r.nodes[i][j].getVar() == -2) {
					txt = "0";
				} else {
					txt = Character.toString(variables[r.nodes[i][j].getVar()]);
				}
				gc.fillText(txt, (x - 3), (y + 3));
			}
		}
		return canvas;		
	}
	
	/** Method draws edges between a parent node and a child node. The method draws edges arround
	 * nodes to avoid intersecting with nodes in between the parent and child node.
	 *
	 * The program calculates whether a proposed egde from parent to child will intersect any of the
	 * nodes in betewen the parent and the child. If it doesn't, the edge is drawn. If the edge does
	 * intersect, then the edge's terminal point is shifted left or right to avoid the node it
	 * intersects, and the program then trys to draw from this new point to the child. If necessary
	 * further shifts for new intersections will be added until the edge connects to the parent node.
	 * @param nodes double array holding all nodes in the robdd.
	 * @param parent the parent node where the edge will start.
	 * @param child the child node where the edge will end.
	 * @param nodeWidth constant representing the node size; used to determine intersection.
	 * @param nodeShift constant used to determine degree to which intersecting nodes are shifted.
	 * @param gc GraphicsContext to which the edges are drawn.
	 */
	private static void drawEdge(RobddNode[][] nodes, RobddNode parent, RobddNode child, int nodeWidth,
								int nodeShift, GraphicsContext gc) {
		int numLevels = nodes.length;
		int lowestLevel = (parent.getLevel() + 1);
		int highestLevel = (child.getLevel() - 1);
		
		int startX = parent.getX();
		int startY = parent.getY();
		int finalX = child.getX();
		int finalY = child.getY();
		 
		// Array used to hold points which make up the edge.
		int[][] xyPoints = new int[numLevels][2];
		int numOfPoints = 0;
		
		// Add the starting coordinates.
		xyPoints[0][0] = startX;
		xyPoints[0][1] = startY;
		numOfPoints++;
		
		// For nodes one level below parent to one level above child, check for intersection.
		for(int i = lowestLevel; i <= highestLevel; i++) {
			for(int j = 0; j < nodes[i].length; j++) {
				RobddNode tmp = nodes[i][j];
				int tmpX = tmp.getX();
				int tmpY = tmp.getY();
				double minDistance = minDist(startX, startY, finalX, finalY, tmpX, tmpY);
				
				if(minDistance > (nodeWidth / 2)) {
					// The min distance between edge and node is large enough, no intersection
				} else {
					// Shift point left or right based on location of parent and intersecting node.
					if(startX < tmpX) {
						xyPoints[numOfPoints][0] = tmp.getX() - (nodeShift + (5 * numOfPoints));
						xyPoints[numOfPoints][1] = tmp.getY();
					} else if (startX > tmpX) {
						xyPoints[numOfPoints][0] = tmp.getX() + (nodeShift + (5 * numOfPoints));
						xyPoints[numOfPoints][1] = tmp.getY();
					} else {
						// Use modulus to avoid egde clustering by dividing between left and right.
						if(lowestLevel % 2 == 0) {
							xyPoints[numOfPoints][0] = tmp.getX() - (nodeShift + (5 * numOfPoints));
							xyPoints[numOfPoints][1] = tmp.getY();
						} else {
							xyPoints[numOfPoints][0] = tmp.getX() + (nodeShift + (5 * numOfPoints));
							xyPoints[numOfPoints][1] = tmp.getY();
						}
					}
					startX = xyPoints[numOfPoints][0];
					startY = xyPoints[numOfPoints][1];
					numOfPoints++;
				}
			}
		}
		
		// Add the final coordinates.
		xyPoints[numOfPoints][0] = finalX;
		xyPoints[numOfPoints][1] = finalY;
		numOfPoints++;
		
		// Now draw edges between the collected points.
		for(int k = 0; k < (numOfPoints - 1); k++) {
			gc.setLineDashes(null);
			gc.strokeLine(xyPoints[k][0], xyPoints[k][1], xyPoints[k + 1][0], xyPoints[k + 1][1]);
		}
	}
	
	/** Method which computes the minimum distance from a line to a point. Used to determine if and
	 * edge intersects with a node.
	 * 
	 * The method works by describing a line as a segment between two points (x1, y1) and (x2,y2).
	 * The method then calculates the minimum distance between this line and the point (p1, p2).
	 * @param x1 the x-coordinate of the first point in the line segment.
	 * @param y1 the y-coordinate of the first point in the line segment.
	 * @param x2 the x-coordinate of the second point in the line segment.
	 * @param y2 the y-coordinate of the second point in the line segment.
	 * @param p1 the x-coordinate of the point.
	 * @param p2 the y-coordinate of the point.
	 * @return the minimum distance between the line and the point.
	 */
	private static double minDist(double x1, double y1, double x2, double y2, double p1, double p2) {
		double minDistNum = ((y2 - y1) * p1) - ((x2 - x1) * p2) + ((x2 * y1) - (y2 * x1));
		minDistNum = Math.abs(minDistNum);
		double minDistDenom = Math.pow((y2-y1), 2) + Math.pow((x2-x1), 2);
		minDistDenom = Math.sqrt(minDistDenom);
		return (minDistNum / minDistDenom);
	}
		
		
	/** Method returns a painted white Canvas of the specified size.
	 * @param width the width of the Canvas.
	 * @param height the height of the Canvas.
	 * @return the white Canvas.
	 */
	private static Canvas getWhiteCanvas(int width, int height) {
		Canvas c = new Canvas(width, height);
		GraphicsContext g = c.getGraphicsContext2D();
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, width, height);
		return c;
	}
	
	/** Helper method. Returns the max element of an int array.
	 * @return the max element of the array.
	 */
	private static int getMax(int[] a) {
		int max = 0;
		for(int i = 0; i < a.length; i++) {
			if(a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}
}