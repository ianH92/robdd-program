// Basic FX imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
// Group imports
import javafx.scene.Group;

// VBox imports
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

// GridPane imports
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

// TextField imports
import javafx.scene.control.TextField;

// Button imports
import javafx.scene.control.Button;

// MenuBar imports
import javafx.scene.control.MenuBar;

// Menu imports
import javafx.scene.control.Menu;

// MenuItem imports
import javafx.scene.control.MenuItem;

// Modality imports
import javafx.stage.Modality;

// Label imports
import javafx.scene.control.Label;

// Canvas imports
import javafx.scene.canvas.Canvas;

// ScrollPane imports
import javafx.scene.control.ScrollPane;

/**
 *
 */
public class RobddProgram extends Application {
	private Stage primaryStage;
	private Scene primaryScene;
	private GridPane primaryLayout;
	
	private GridPane promptLayout;
	private Button drawButton;
	private Button clearButton;
	private TextField inputEqtn;
	private TextField inputOrder;
	
	private MenuBar mainMenu;
	private Menu fileMenu;
	private Menu helpMenu;
	private MenuItem save;
	private MenuItem help;
	
	private String equation;
	private String order;
	private char[] charEquation;
	private char[] varOrder;
	private char[] reservedChars;
	private Operators ops;
	private Robdd robdd;
	
	private Group canvasGroup;
	private ScrollPane canvasScrollPane;
	private Canvas nodeCanvas;
	private Canvas egdeCanvas;
	
	private int WIDTH;
	private int HEIGHT;
	
	
	/**
	 *
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 *
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		// Initialize the primaryStage
		initialize();
		// Set actions for all buttons/menu items
		setActions();
		
		primaryStage.show();
	}
	
	private void setActions() {
		drawButton.setOnAction(e -> {
			equation = inputEqtn.getText();
			order = inputOrder.getText();
			
			
			try {
				checkString(equation, reservedChars);
				checkString(order, reservedChars);
				varOrder = order.toCharArray();
				charEquation = ShuntingYardAlgorithm.infixToPostfix(equation, varOrder, ops);
				robdd = Robdd.RobddFactory(charEquation, varOrder, ops);
				nodeCanvas = DrawRobdd.drawRobddNodes(robdd, varOrder);
				
				canvasGroup.getChildren().clear();
				canvasGroup.getChildren().add(nodeCanvas);
				canvasScrollPane.setContent(canvasGroup);
			} catch(ExpressionError err) {
				errorDisplay(err);
			}
		});
		
		clearButton.setOnAction(e -> {
				inputEqtn.clear();
				inputOrder.clear();
		});
	}
	
	/** Creates a graphical display for a passed error.
	 * @param e The exception whose message will be displayed.
	 */
	private void errorDisplay(Exception e) {
		Stage errorMsg = new Stage();
		errorMsg.initModality(Modality.WINDOW_MODAL);
		VBox err = new VBox(10.0);
		err.setAlignment(Pos.CENTER);
		Button b = new Button("Close");
		b.setOnAction(n -> {
			errorMsg.close();
		});
		Label errMsg = new Label(e.getMessage());
		err.getChildren().addAll(errMsg, b);
		errorMsg.setScene(new Scene(err, 300.0, 300.0));
		errorMsg.show();
	}
	
	/** Method that checks if a string contains reserved characters; throws an error if it does.
	 * @param s The String to check.
	 * @param reserved An array of reserved characters.
	 */
	private void checkString(String s, char[] reserved) throws ExpressionError {
		for(int i = 0; i < s.length(); i++) {
			for(int j = 0; j < reserved.length; j++) {
				if(reserved[j] == s.charAt(i)) {
					throw new ExpressionError("Error: Character " + reserved[j] + " is reserved" +
												" for program use and cannot be used in an equation.");
				}
			}
		}
		return;
	}
	
	private void initialize() {
		this.WIDTH = 250;
		this.HEIGHT = 200;
		
		this.equation = null;
		this.order = null;
		this.varOrder = null;
		this.charEquation = null;
		this.robdd = null;
		this.ops = new Operators();
		this.reservedChars = new char[]{'0', '1'};
		
		// Creating primary layout
		primaryLayout = new GridPane();
		
		// Creating canvas layout
		canvasGroup = new Group();
		canvasScrollPane = new ScrollPane();
		canvasScrollPane.setContent(canvasGroup);
		canvasScrollPane.setFitToHeight(true);
		canvasScrollPane.setFitToWidth(true);
		
		// Creating prompt sublayout
		promptLayout = new GridPane();
		promptLayout.setPadding(new Insets(5, 5, 5, 5));
		promptLayout.setVgap(10);
		promptLayout.setHgap(10);
		
		// Creating user input textfields and draw button
		inputEqtn = new TextField();
		inputEqtn.setPromptText("Enter Boolean Equation");
		inputOrder = new TextField();
		inputOrder.setPromptText("Enter Variable Order");
		drawButton = new Button("Draw ROBDD");
		clearButton = new Button("Clear Input");
		
		// Adding to promptLayout
		promptLayout.setConstraints(inputEqtn, 1, 1);
		promptLayout.setConstraints(inputOrder, 1, 2);
		promptLayout.setConstraints(drawButton, 1, 3);
		promptLayout.setConstraints(clearButton, 1, 5);
		promptLayout.getChildren().addAll(inputEqtn, inputOrder, drawButton, clearButton);
		
		// Add promptLayout to primaryLayout
		primaryLayout.setConstraints(promptLayout, 1, 2);
		primaryLayout.getChildren().addAll(promptLayout);
		
		// Creating menu and adding to primaryLayout
		mainMenu = new MenuBar();
		fileMenu = new Menu("File");
		save = new MenuItem("Save RobddImage");
		fileMenu.getItems().add(save);
		helpMenu = new Menu("Help");
		help = new MenuItem("Program Information");
		helpMenu.getItems().add(help);
		mainMenu.getMenus().addAll(fileMenu, helpMenu);
		
		// Adding mainMenu to primaryLayout
		primaryLayout.setConstraints(mainMenu, 1, 1);
		primaryLayout.getChildren().add(mainMenu);
		
		//Adding canvas to primaryLayout
		primaryLayout.setConstraints(canvasScrollPane, 2, 2);
		primaryLayout.getChildren().add(canvasScrollPane);
		
		// Setting primaryScene and Stage
		primaryScene = new Scene(primaryLayout, WIDTH, HEIGHT);
		primaryStage.setTitle("RobddProgram");
		primaryStage.setScene(primaryScene);
	}
}





















