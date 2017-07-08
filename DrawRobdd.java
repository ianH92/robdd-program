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
		
		int nodeWidth = 20;
		int nodeSpace = 55;
		int levelSeparation = 55;
		int drawWidth = nodeSpace * maxNodes;
		int drawHeight = numOfLevels * levelSeparation;
		int xBuffer = 10;
		int yBuffer = 10;
		
		// Calculate canvas's true height/width
		int height = drawHeight + (2 * xBuffer);
		int width = drawWidth + (2 * yBuffer);
		
		// Calculate the middle of the canvas
		int middle = (drawWidth / 2) + xBuffer;
		
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
		int leftX;
		int leftY;
		int rightX;
		int rightY;
		double[] lineDashArray = {10.0};
		for(int i = 0; i < numOfLevels; i++) {
			for(int j = 0; j < r.levelsCount[i]; j++) {
				RobddNode n =  r.nodes[i][j];
				x = n.getX();
				y = n.getY();
				
				// Draw the left edges
				RobddNode leftChild = n.getLeftChild();
				if(leftChild != null) {
					leftX = leftChild.getX();
					leftY= leftChild.getY();
					
					gc.setLineDashes(lineDashArray);
					gc.strokeLine(x, y, leftX, leftY);
				}
				
				// Draw the right edges
				RobddNode rightChild = n.getRightChild();
				if(rightChild != null) {
					rightX = rightChild.getX();
					rightY = rightChild.getY();
					
					gc.setLineDashes(null);
					gc.strokeLine(x, y, rightX, rightY);
				}
				
				// Draw the node
				gc.setFill(cols[i % cols.length]);
				gc.fillOval(x - (nodeWidth / 2), y - (nodeWidth / 2), nodeWidth, nodeWidth);
				gc.setFill(Color.BLACK);
				
				// Set the new x and y coordinates of each nodeSpace
				r.nodes[i][j].setX(x);
				r.nodes[i][j].setY(y);
					
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