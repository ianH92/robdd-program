import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 */
public class RobddProgram extends Application {
	private Stage primaryStage;
	private Scene primaryScene;
	private BorderPane primaryLayout;
	
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
	
	private Canvas placeholderCanvas;
	private Group canvasGroup;
	private ScrollPane canvasScrollPane;
	private Canvas nodeCanvas;
	
	private int WIDTH;
	private int HEIGHT;
	
	private String fileName;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		// Initialize the primaryStage
		initialize();
		// Set actions for all buttons/menu items
		setActions();
		
		primaryStage.show();
	}
	
	/** Method used to group code where GUI elements are initialized.
	 */
	private void initialize() {
		WIDTH = 400;
		HEIGHT = 300;
		
		// Calc widths and heights from base width and height
		int canvasScrollPaneWidth = 225;
		int canvasScrollPaneHeight = HEIGHT - 10;
		int promptLayoutWidth = (WIDTH - canvasScrollPaneWidth);
		int promptLayoutHeight = HEIGHT;
		
		// Initialize currently unused fields to null
		equation = null;
		order = null;
		varOrder = null;
		charEquation = null;
		robdd = null;
		ops = new Operators();
		fileName = null;
		
		// These are characters reserved for use by the program
		reservedChars = new char[]{'0', '1'};
		
		// Creating primary layout
		primaryLayout = new BorderPane();
		
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
		
		// Setting promptLayout sizing constraints
		promptLayout.setPrefWidth(promptLayoutWidth);
		promptLayout.setPrefHeight(promptLayoutHeight);
		
		// Add promptLayout to primaryLayout
		primaryLayout.setLeft(promptLayout);
		
		// Creating menu and adding to primaryLayout
		mainMenu = new MenuBar();
		fileMenu = new Menu("File");
		save = new MenuItem("Save Robdd Image");
		fileMenu.getItems().add(save);
		helpMenu = new Menu("Help");
		help = new MenuItem("Program Information");
		helpMenu.getItems().add(help);
		mainMenu.getMenus().addAll(fileMenu, helpMenu);
		
		// Adding mainMenu to primaryLayout
		primaryLayout.setTop(mainMenu);
		
		// Creating canvas layout, canvas group, and canvas scrollpane
		canvasGroup = new Group();
		canvasScrollPane = new ScrollPane();
		canvasScrollPane.setContent(canvasGroup);
		canvasScrollPane.setFitToHeight(true);
		canvasScrollPane.setFitToWidth(true);
		
		// Creating placeholder white canvas
		placeholderCanvas = DrawRobdd.getWhiteCanvas(canvasScrollPaneWidth, canvasScrollPaneHeight);
		canvasGroup.getChildren().add(placeholderCanvas);
		canvasScrollPane.setContent(canvasGroup);
		
		//Adding canvas to primaryLayout
		primaryLayout.setCenter(placeholderCanvas);
		
		// Setting primaryScene and Stage
		primaryScene = new Scene(primaryLayout, WIDTH, HEIGHT);
		primaryStage.setTitle("RobddProgram");
		primaryStage.setScene(primaryScene);
	}
	
	/** Method used t group sections of code where buttons' actions are defined.
	 */ 
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
				primaryLayout.setCenter(canvasScrollPane);
			} catch(ExpressionError err) {
				errorDisplay(err);
			}
		});
		
		clearButton.setOnAction(e -> {
				inputEqtn.clear();
				inputOrder.clear();
				canvasGroup.getChildren().clear();
				canvasGroup.getChildren().add(placeholderCanvas);
				canvasScrollPane.setContent(canvasGroup);
				primaryLayout.setCenter(canvasScrollPane);
		});
		
		save.setOnAction(e -> {
			savePromptDisplay();
		});
		
		help.setOnAction(e -> {
			readmeDisplay();
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
		errorMsg.setScene(new Scene(err, 200.0, 200.0));
		errorMsg.show();
	}
	
	/** Displays the README file for the program, or an error if the file is not found.
	 */
	private void readmeDisplay() {
		Stage helpStage = new Stage();
		helpStage.initModality(Modality.WINDOW_MODAL);
		ScrollPane s = new ScrollPane();
		VBox v = new VBox(2);
		
		try{
			FileReader f = new FileReader("README.txt");
			BufferedReader b = new BufferedReader(f);
			
			String reader = b.readLine();
			while(reader != null) {
				v.getChildren().add(new Label(reader));
				reader = b.readLine();
			}
			s.setContent(v);
		}catch(FileNotFoundException e){
			errorDisplay(new Exception("Error: README.txt not found."));
		}catch(IOException e){
			errorDisplay(new Exception("Error: File Read Error."));
		}
		
		helpStage.setScene(new Scene(s, 600, 600));
		helpStage.show();
	}
	
	
	/** Creates a save prompt dialog box for saving an image.
	 * @return the name to use when saving an image.
	 */
	private void savePromptDisplay() {
		Stage savePrompt = new Stage();
		savePrompt.initModality(Modality.WINDOW_MODAL);
		VBox saveBox = new VBox(10.0);
		saveBox.setAlignment(Pos.CENTER);
		Label l = new Label("Enter the Image name below:");
		TextField name = new TextField();
		name.setPromptText("Enter File Name");
		Button s = new Button("Save");
		s.setOnAction(n -> {
			fileName = name.getText();
			fileName += ".png";
			
			WritableImage image = new WritableImage((int)nodeCanvas.getWidth(), (int)nodeCanvas.getHeight());
			nodeCanvas.snapshot(null, image);
			BufferedImage im = SwingFXUtils.fromFXImage(image, null);
			
			try {
				File f = new File(fileName);
				ImageIO.write(im, "png", f);
			} catch(Exception err) {
				errorDisplay(err);
			}
			savePrompt.close();
		});
		saveBox.getChildren().addAll(l, name, s);
		saveBox.setMargin(name, new Insets(10.0));
		savePrompt.setScene(new Scene(saveBox, 200.0, 150.0));
		savePrompt.show();
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
}