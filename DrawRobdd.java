// Canvas imports
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.Group;
import javafx.scene.text.*;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class DrawRobdd {

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
			/* Keep track of the number of shifts used to avoid edge-node intersection.
			 * The two ints below help increase the shift slightly each time it is required to help
			 * prevent two edges from overlapping significantly.
			 */
			int numOfRightShifts = 0;
			int numOfLeftShifts = 0;
			
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
						/* For each node below the left of the parent and above the level of the child,
						 * determine if the edge will intersect another node. If so, draw arround that
						 * node.
						 * Use double int array to store points to between.
						 */
						int[][] xyPoints = new int[numOfLevels + 1][2];
						int numOfPoints = 0;
						
						// Add the starting coordinates.
						xyPoints[0][0] = tmpX;
						xyPoints[0][1] = tmpY;
						numOfPoints++;
						boolean done = false;
						
						for(int k = (n.getLevel() + 1); (k <= leftChild.getLevel() && done == false); k++) {
							for(int l = 0; l < r.nodes[k].length; l++) {
								RobddNode tmp = r.nodes[k][l];
								double minDistance = minDist(tmpX, tmpY, leftX, leftY, tmp.getX(), tmp.getY());
								
								if(minDistance > (nodeWidth / 2) || k == leftChild.getLevel()) {
									// The min distance between edge and node is large enough, no intersection
									if(k == leftChild.getLevel()) {
										xyPoints[numOfPoints][0] = leftX;
										xyPoints[numOfPoints][1] = leftY;
										numOfPoints++;
										done = true;
										break;
									}
								} else {
									
									if(n.getLevel() % 2 == 0) {
										xyPoints[numOfPoints][0] = tmp.getX() + (nodeWidth + (4 * numOfRightShifts));
										xyPoints[numOfPoints][1] = tmp.getY() + nodeWidth;
										numOfRightShifts++;
									} else {
										xyPoints[numOfPoints][0] = tmp.getX() - (nodeWidth + (4 * numOfLeftShifts));
										xyPoints[numOfPoints][1] = tmp.getY() + nodeWidth;
										numOfLeftShifts++;
									}
									tmpX = xyPoints[numOfPoints][0];
									tmpY = xyPoints[numOfPoints][1];
									numOfPoints++;
								}
							}
						}
						
						// Now draw edges between the collected points.
						for(int k = 0; k < (numOfPoints - 1); k++) {
							gc.setLineDashes(lineDashArray);
							gc.strokeLine(xyPoints[k][0], xyPoints[k][1], xyPoints[k + 1][0], xyPoints[k + 1][1]);
						}
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
						/* For each node below the left of the parent and above the level of the child,
						 * determine if the edge will intersect another node. If so, draw arround that
						 * node.
						 * Use double int array to store points to between.
						 *
						int[][] xyPoints = new int[numOfLevels][2];
						int numOfPoints = 0;
						
						// Add the starting coordinates.
						xyPoints[0][0] = tmpX;
						xyPoints[0][1] = tmpY;
						numOfPoints++;
						boolean done = false;
						
						for(int k = (n.getLevel() + 1); (k <= rightChild.getLevel() && done == false); k++) {
							for(int l = 0; l < r.nodes[k].length; l++) {
								RobddNode tmp = r.nodes[k][l];
								double minDistance = minDist(tmpX, tmpY, rightX, rightY, tmp.getX(), tmp.getY());
								
								if(minDistance > (nodeWidth / 2) || k == rightChild.getLevel()) {
									// The min distance between edge and node is large enough, no intersection
									if(k == rightChild.getLevel()) {
										xyPoints[numOfPoints][0] = rightX;
										xyPoints[numOfPoints][1] = rightY;
										numOfPoints++;
										done = true;
										break;
									}
								} else {
									
									if(n.getLevel() % 2 == 0) {
										xyPoints[numOfPoints][0] = tmp.getX() + (nodeWidth + (4 * numOfRightShifts));
										xyPoints[numOfPoints][1] = tmp.getY() + nodeWidth;
										numOfRightShifts++;
									} else {
										xyPoints[numOfPoints][0] = tmp.getX() - (nodeWidth + (4 * numOfLeftShifts));
										xyPoints[numOfPoints][1] = tmp.getY() + nodeWidth;
										numOfLeftShifts++;
									}
									tmpX = xyPoints[numOfPoints][0];
									tmpY = xyPoints[numOfPoints][1];
									numOfPoints++;
								}
							}
						}
						
						// Now draw edges between the collected points.
						for(int k = 0; k < (numOfPoints - 1); k++) {
							gc.setLineDashes(null);
							gc.strokeLine(xyPoints[k][0], xyPoints[k][1], xyPoints[k + 1][0], xyPoints[k + 1][1]);
						}
						*/
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
	
	/**
	 *
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
		 
		int[][] xyPoints = new int[numLevels][2];
		int numOfPoints = 0;
		
		// Add the starting coordinates.
		xyPoints[0][0] = startX;
		xyPoints[0][1] = startY;
		numOfPoints++;
		
		for(int i = lowestLevel; i <= highestLevel; i++) {
			for(int j = 0; j < nodes[i].length; j++) {
				RobddNode tmp = nodes[i][j];
				int tmpX = tmp.getX();
				int tmpY = tmp.getY();
				double minDistance = minDist(startX, startY, finalX, finalY, tmpX, tmpY);
				
				if(minDistance > (nodeWidth / 2)) {
					// The min distance between edge and node is large enough, no intersection
				} else {
					if(startX < tmpX) {
						xyPoints[numOfPoints][0] = tmp.getX() - (nodeShift + (5 * numOfPoints));
						xyPoints[numOfPoints][1] = tmp.getY();
					} else if (startX > tmpX) {
						xyPoints[numOfPoints][0] = tmp.getX() + (nodeShift + (5 * numOfPoints));
						xyPoints[numOfPoints][1] = tmp.getY();
					} else {
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
		
		
	
	public static Canvas getWhiteCanvas(int width, int height) {
		Canvas c = new Canvas(width, height);
		GraphicsContext g = c.getGraphicsContext2D();
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, width, height);
		return c;
	}
	
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