package guiUpdatePassword;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;

/*******
 * <p> Title: ViewUpdatePassword Class. </p>
 * 
 * <p> Description: The ViewUpdatePassword Page is used to enable a potential User with the ability 
 * to update their password. The user is able to enter a new password and return, quit, and logout. 
 * Once the password is updated, the user is required to log in again with their new password.
 *  </p>
 * 
 */



public class ViewUpdatePassword {
	
	/*-********************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	//Label, Buttons, Alerts
	private static Label label_ApplicationTitle = new Label("Update Password");
	private static Label label_TitleLine1 = 
			new Label(" Update your password here.");
	
	private static Label label_TitleLine2 = 
			new Label("Enter the the new Password twice, and then click on " + 
					"Update.");
	
	
	
	//protected static Label label_PasswordUpdate = new Label("Update Your Password");
	//protected static Label label_Enterapasswordline = new Label("Please enter the new password");
	protected static PasswordField text_Password1 = new PasswordField();
	protected static PasswordField text_Password2 = new PasswordField();
	protected static Button button_Update = new Button("Update");
	
	
	//protected static Label label_PasswordsDoNotMatch = new Label();
	
	
	// This alert is used when the 2 passwords don't match
	protected static Alert alertPasswordError = new Alert(AlertType.INFORMATION);
	
	//This alert is used when the update is successful.
	protected static Alert alertUpdateSuccessful = new Alert(AlertType.INFORMATION);
	
	
	/* 
	 * Feedback labels with text and color to specify which of the requirements have been satisfied
	 * (using both text and the color green) and which have not (both with text and the color red).
	 * 
	 */
	
	// ERROR MESSASGES FOR USER FOR PASSWORD
    // Code from Password: View. This code is initializing the boolean flags to check if the password is valid.
	static protected Label label_errPassword = new Label();	// There error labels change based on the
	static protected Label label_noInputFound = new Label();			// user's input
	static protected Label label_validPassword = new Label();		// This only appears with a valid password
	static protected Label label_PasswordRequirements = new Label();
	
	// buttons on the GUI page
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");
	
	
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewUpdatePassword theView;
	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User
	
	public static Scene theUpdatePasswordScene = null;	// Access to the password update page's GUI widgets 
	


	/*-********************************************************************************************
	
	Constructors
	
	*/
	
	/**********
	 * <p> Method: displayUpdatePassword(Stage ps, String user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the NewAccount page to be displayed.
	 * 
	 * It first sets up very shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User whose password will be updated
	 * 
	 */
	public static void displayUpdatePassword(Stage ps, User user) {
	
		// Establish the references to the GUI and the current user
		theUser = user;
		theStage = ps;
		
		// If not yet established, populate the static aspects of the GUI by creating the 
		if(theView == null) theView = new ViewUpdatePassword();// singleton instance of this class
			
		text_Password1.setText("");
		text_Password2.setText("");
		
		// DISPLAY FOR CHECKING PASSWORD REQUIREMENTS
		text_Password1.textProperty().addListener((observable, oldValue, newValue) 
				-> {ControllerUpdatePassword.updateNewPassword();});
		
		// Establish an error message for when there is no input
		setupLabelWidget(label_noInputFound, 50, 300, "Arial", 14, width-10, Pos.BASELINE_LEFT);	
		label_noInputFound.setText("No password input text found!");	// This changes based on input, but we 
		label_noInputFound.setTextFill(Color.RED);			// want this to be shown at startup	
		

		// Setup the valid Password message, which is used when all the requirements have been met
		label_validPassword.setTextFill(Color.RED);
		label_validPassword.setText("Failure! The password is not valid.");
		label_validPassword.setAlignment(Pos.BASELINE_LEFT);
		setupLabelWidget(label_validPassword, 420, 330, "Arial", 18, width-10, Pos.BASELINE_LEFT);
		
		// LISTING OUT PASSWORD REQUIREMENTS
		setupLabelWidget(label_PasswordRequirements, 420, 360, "Arial", 14, width-10, Pos.BASELINE_LEFT);	
		label_PasswordRequirements.setText("Valid PASSWORDS must meet these requirements:\n"
											+ "Be between 8-32 characters in length\n"
											+ "Consist of at least one of each of the following:\n"
											+ "An uppercase letter\nA lowercase letter\n"
											+ "A special character\nA digit\n");	// This changes based on input, but we 
		label_PasswordRequirements.setTextFill(Color.BLACK);			
				
		
		
		// Place all of the established GUI elements into the pane
		theRootPane.getChildren().clear();
		theRootPane.getChildren().addAll(label_ApplicationTitle,label_TitleLine1,label_TitleLine2,
				 text_Password1, text_Password2, button_Update, button_Return, button_Logout, 
				button_Quit, label_errPassword, label_PasswordRequirements, label_noInputFound,
				label_validPassword);
		
		theStage.setTitle("CSE 360 Foundation Code: Update Password");
			theStage.setScene(theUpdatePasswordScene);
			theStage.show();
	}
	
	
	
	/**********
	 * <p> Method: ViewUpdatePassword() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object. </p>
	 * 
	 * This is a singleton, so this is performed just one.  Subsequent uses fill in the changeable
	 * fields using the displayAddRempoveRoles method.</p>
	 * 
	 */
	public ViewUpdatePassword() {
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane(); 
		theUpdatePasswordScene = new Scene(theRootPane, width, height);
		
		
		// Label theScene with the name of the system startup screen
		setupLabelUI(label_ApplicationTitle, "Arial", 32, width, Pos.CENTER, 0, 10);

		// Label to display the welcome message for the first user
		setupLabelUI(label_TitleLine1, "Arial", 24, width, Pos.CENTER, 0, 70);

		// Label to display the welcome message for the first user
		setupLabelUI(label_TitleLine2, "Arial", 18, width, Pos.CENTER, 0, 130);
		

		//Button, Label, Text, and Label Widget configurations
		// setupLabelUI(label_PasswordUpdate, "Arial", 32, width, Pos.CENTER, 0, 10);
		
		// setupLabelUI(label_Enterapasswordline, "Arial", 24, width, Pos.CENTER, 0, 70);
		
		setupTextUI(text_Password1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, true);
		text_Password1.setPromptText("Enter the Password");
		
		setupTextUI(text_Password2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 260, true);
		text_Password2.setPromptText("Enter the Password Again");
		
		
		alertPasswordError.setTitle("Passwords Do Not Match");
		alertPasswordError.setHeaderText("The two passwords must be identical.");
		alertPasswordError.setContentText("Correct the passwords and try again.");
		
		alertUpdateSuccessful.setTitle("Passwords Update Successful");
		alertUpdateSuccessful.setHeaderText("You have successfully updated your password.");
		alertUpdateSuccessful.setContentText("Please Log in again with your new password.");
		
		
		setupButtonUI(button_Update, "Dialog", 18, 200, Pos.CENTER, 475, 230);
		button_Update.setOnAction((event) -> {ControllerUpdatePassword.doUpdatePassword();});
	    button_Update.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
	       
        
    	setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 50, 540);
    	button_Return.setOnAction((event) -> {ControllerUpdatePassword.performReturn(); });
    	button_Return.setStyle("-fx-font: 22 arial; -fx-base: #FFC627;");


    	setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
    	button_Logout.setOnAction((event) -> {ControllerUpdatePassword.performLogout(); });
    	button_Logout.setStyle("-fx-font: 22 arial; -fx-base: #FFC627;");


    	setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
    	button_Quit.setOnAction((event) -> {ControllerUpdatePassword.performQuit(); });
    	button_Quit.setStyle("-fx-font: 22 arial; -fx-base: #FFC627;");

    	
    	
		label_PasswordRequirements.setTextFill(Color.BLACK);		

		// Place all of the established GUI elements into the pane
    	theRootPane.getChildren().clear();
    	
    	theRootPane.getChildren().addAll(label_ApplicationTitle, label_TitleLine1, label_TitleLine2,
    									text_Password1, text_Password2,button_Update,
    									button_Return, button_Logout,button_Quit);
	}
	

	public static Stage getTheStage() {
		return theStage;
	}

	
	/*-********************************************************************************************

	Helper methods to reduce code length

	 */
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 */
	
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	static private void setupLabelWidget(Label l, double x, double y, String ff, double f, double w, 
			Pos p){
		l.setLayoutX(x);
		l.setLayoutY(y);		
		l.setFont(Font.font(ff, f));	// The font face and the font size
		l.setMinWidth(w);
		l.setAlignment(p);
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

	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}

	public static User getTheUser() {
		// TODO Auto-generated method stub
		return theUser;
	}	


}