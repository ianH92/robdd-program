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
	
	public static final Color[] cols = {Color.DEEPSKYBLUE, Color.MEDIUMSEAGREEN, Color.LIGHTCORAL, 
										Color.GOLD, Color.LIGHTSKYBLUE, Color.LIGHTGREEN, 
										Color.LIGHTPINK};

	public static Canvas drawRobddNodes(Robdd r, char[] variables) {
		// Find the maximum number of nodes which occur at a level
		int maxNodes = getMax(r.levelsCount);
		// And the number of levels is
		int numOfLevels = r.levelsCount.length;
		
		int nodeWidth = 20;
		int nodeSpace = 50;
		int levelSeparation = 40;
		int drawWidth = nodeSpace * maxNodes;
		int drawHeight = numOfLevels * levelSeparation;
		int xBuffer = 5;
		int yBuffer = 5;
		
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
		Canvas canvas = new Canvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		int distUsed;
		int x;
		int y;
		for(int i = 0; i < numOfLevels; i++) {
			for(int j = 0; j < r.levelsCount[i]; j++) {
				distUsed = nodeSpace * j;
				x = levelStartX[i] + distUsed + (nodeSpace / 2);
				y = yBuffer + (i * levelSeparation);
				
				gc.setFill(cols[i % cols.length]);
				gc.fillOval(x, y, nodeWidth, nodeWidth);
				gc.setFill(Color.BLACK);
					
				String txt = "tmp";
				if(r.nodes[i][j].getVar() == -1) {
					txt = "1";
				} else if(r.nodes[i][j].getVar() == -2) {
					txt = "0";
				} else {
					txt = Character.toString(variables[r.nodes[i][j].getVar()]);
				}
				gc.fillText(txt, (x + ((nodeWidth / 2) - 3)), (y + ((nodeWidth / 2) + 3)));
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