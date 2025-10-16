package guiUserList;

import java.util.ArrayList;
import java.util.List;
import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;




public class ViewUserList {
	
	

private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
		
	

	
	
private static ViewUserList theView;// Used to determine if instantiation of the class is needed




protected static Stage theStage;			// The Stage that JavaFX has established for us
protected static Pane theRootPane;			// The Pane that holds all the GUI widgets 
protected static User theUser;				// The current logged in User
public static Scene theUserListScene;      // The shared Scene each invocation populates


//List Instantiation
//protected static List<String> userList = new ArrayList<String>();
protected static List<String> roleList = new ArrayList<String>();

//labels and buttons for the user list Page



protected static Label label_UserListTitle = new Label("List of Users");
protected static Label label_NumberOfUsers = new Label("Number of Users:");


//protected static Button button_ReturnAdminHomepage = new Button("Admin Home Page");
protected static Button button_Return = new Button("Return");
protected static Button button_Logout = new Button("Logout");
protected static Button button_Quit = new Button("Quit");



public static void displayUserList(Stage ps, User user) {
	
	// Establish the references to the GUI and the current user
	setTheStage(ps);
	setTheUser(user);
	
	// If not yet established, populate the static aspects of the GUI
	if (theView == null) theView = new ViewUserList();		// Instantiate singleton if needed
	// Set the title for the window, display the page, and wait for the Admin to do something
	theStage.setTitle("CSE 360 Foundation Code: View User Detail Lists");
    theStage.setScene(theUserListScene);
	theStage.show();
	
	ControllerUserList.repaintTheWindow();
	
}


	private ViewUserList() {
		theRootPane = new Pane();
		theUserListScene = new Scene(theRootPane, width, height);

		// GUI Area 1
		label_UserListTitle.setText("User List");
		setupLabelUI(label_UserListTitle, "Arial", 28, width, Pos.CENTER, 0, 5); 
	
		
		// GUI Area 3
//		setupButtonUI(button_ReturnAdminHomepage, "Diaglog", 18, 210, Pos.CENTER, 50, 200);
//		button_ReturnAdminHomepage.setOnAction((event) -> {ControllerUserList.performAdminHomePage(); });
//		button_ReturnAdminHomepage.setStyle("-fx-font: 18 arial; -fx-base: #8C1D40;");
		
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 50, 540);
		button_Return.setOnAction((event) -> {ControllerUserList.performReturn(); });
		button_Return.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
	
		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((event) -> {ControllerUserList.performLogout(); });
		button_Logout.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
	
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((event) -> {ControllerUserList.performQuit(); });
		button_Quit.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
		
		
		
	
		
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll
			(button_Return, button_Logout,button_Quit);
		
		// With theRootPane set up with the common widgets, it is up to displayAdminHome to show
		// that Pane to the user after the dynamic elements of the widgets have been updated.
		ControllerUserList.repaintTheWindow();
	}




	
	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	
	
	public static User getTheUser() {
		return theUser;
	}


	public static void setTheUser(User theUser) {
		ViewUserList.theUser = theUser;
	}


	public static Stage getTheStage() {
		return theStage;
	}


	public static void setTheStage(Stage theStage) {
		ViewUserList.theStage = theStage;
	}
	
}