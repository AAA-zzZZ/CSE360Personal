package guiFirstAdmin;

import javafx.geometry.Pos;
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
import javafx.scene.Scene;


/*******
 * <p> Title: ViewFirstAdmin Class</p>
 * 
 * <p> Description: The FirstAdmin Page View.  This class is used to require the very first user of
 * the system to specify an Admin Username and Password when there is no database for the
 * application.  This avoids the common weakness practice of hard coding credentials into the 
 * application.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ViewFirstAdmin {

	/*-********************************************************************************************

	Attributes

	 */

	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// These are the widget attributes for the GUI
	
	// The GUI informs the user about the purpose of this page, provides three text inputs fields
	// for the user to specify a username for this account and two copies of the password to be
	// used (they must match), a button to request that the account be established, and a quit
	// but to abort the action and stop the application.
	private static Label label_ApplicationTitle = new Label("Foundation Application Startup Page");
	private static Label label_TitleLine1 = 
			new Label(" You are the first user.  You must be an administrator.");
	
	private static Label label_TitleLine2 = 
			new Label("Enter the Admin's Username, the Password twice, and then click on " + 
					"Setup Admin Account.");
	
//	protected static Label label_PasswordsDoNotMatch = new Label();
	protected static TextField text_AdminUsername = new TextField();
	protected static PasswordField text_AdminPassword1 = new PasswordField();
	protected static PasswordField text_AdminPassword2 = new PasswordField();
	private static Button button_AdminSetup = new Button("Setup Admin Account");
	
	
	// ERROR MESSAGES FOR USER FOR USERNAME
    static protected Label label_errUsername = new Label();	// There error labels change based on the
	static protected Label noUsernameInputFound = new Label();			// user's input
	
	static protected Label validUsername = new Label();		// This only appears with a valid username
	static protected Label label_UsernameRequirements = new Label();
			
    // ERROR MESSASGES FOR USER FOR PASSWORD
    // Code from Password: View. This code is initializing the boolean flags to check if the password is valid.
	static protected Label label_errPassword = new Label();	// There error labels change based on the
	static protected Label noInputFound = new Label();			// user's input

    /* 
	 * Feedback labels with text and color to specify which of the requirements have been satisfied
	 * (using both text and the color green) and which have not (both with text and the color red).
	 * 
	 */
	static protected Label validPassword = new Label();		// This only appears with a valid password
	static protected Label label_PasswordRequirements = new Label();
	


	// This alert is used should the user enter two passwords that do not match
	protected static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);

	// This button allow the user to abort creating the first admin account and terminate
	private static Button button_Quit = new Button("Quit");

	// These attributes are used to configure the page and populate it with this user's information
	protected static Stage theStage;	
	private static Pane theRootPane;
	private static Scene theFirstAdminScene = null;
	private static final int theRole = 1;		// Admin: 1; Role1: 2; Role2: 3
		
	
	/*-********************************************************************************************

	Constructor

	 */
	
	/**********
	 * <p> Method: public displayFirstAdmin(Stage ps) </p>
	 * 
	 * <p> Description: This method is called when the application first starts. It create an
	 * an instance of the View class.  
	 * 
	 * NOTE: As described below, this code does not implement MVC using the singleton pattern used
	 * by most of the pages as the code is written this way because we know with certainty that it
	 * will only be called once.  For this reason, we directly call the private class constructor.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 */
	public static void displayFirstAdmin(Stage ps) {
		
		// Establish the references to the GUI.  There is no user yet.
		theStage = ps;			// Establish a reference to the JavaFX Stage
		
		// This page is only called once so there is no need to save the reference to it
		new ViewFirstAdmin();	// Create an instance of the class 
		text_AdminUsername.setText("");	// Clear the input fields so previously entered values do not
		text_AdminPassword1.setText("");	// appear for a new user
		text_AdminPassword2.setText("");
		
		// LISTING OUT USERNAME REQUIREMENTS
		setupLabelWidget(label_UsernameRequirements, 50, 360, "Arial", 14, width-10, Pos.BASELINE_LEFT);	
		label_UsernameRequirements.setText("Valid USERNAMES must meet these requirements:\n"
											+ "Be between 4-16 characters in length\n"
											+ "Consist only of letters, digits, and/or _ . -\n"
											+ "If a . - _ is used, it must be between letters or digits.\n"
											+ "The username must begin with a letter.");	// This changes based on input, but we 
		label_UsernameRequirements.setTextFill(Color.BLACK);								// want this to be shown at startup	
		
		
		// DISPLAY FOR CHECKING USERNAME REQUIREMENTS
		text_AdminUsername.textProperty().addListener((observable, oldValue, newValue) 
				-> {ModelFirstAdmin.updateFirstAdminUsername(); });
		
		// Establish an error message for when there is no input
		setupLabelWidget(noUsernameInputFound, 50, 140, "Arial", 14, width-10, Pos.BASELINE_LEFT);	
		noUsernameInputFound.setText("No username input text found!");	// This changes based on input, but we 
		noUsernameInputFound.setTextFill(Color.RED);			// want this to be shown at startup	
		
		
		// Setup the valid Username message, which is used when all the requirements have been met
		validUsername.setTextFill(Color.GREEN);
		validUsername.setAlignment(Pos.BASELINE_RIGHT);
		setupLabelWidget(validUsername, 50, 330, "Arial", 18, width-10, Pos.BASELINE_LEFT);
		
		
		// LISTING OUT PASSWORD REQUIREMENTS
		setupLabelWidget(label_PasswordRequirements, 420, 360, "Arial", 14, width-10, Pos.BASELINE_LEFT);	
		label_PasswordRequirements.setText("Valid PASSWORDS must meet these requirements:\n"
											+ "Be between 8-32 characters in length\n"
											+ "Consist of at least one of each of the following:\n"
											+ "An uppercase letter\nA lowercase letter\n"
											+ "A special character\nA digit\n");	// This changes based on input, but we 
		label_PasswordRequirements.setTextFill(Color.BLACK);			
		
		
		// DISPLAY FOR CHECKING PASSWORD REQUIREMENTS
		text_AdminPassword1.textProperty().addListener((observable, oldValue, newValue) 
				-> {ModelFirstAdmin.updateFirstAdminPassword(); });
		
		// Establish an error message for when there is no input
		setupLabelWidget(noInputFound, 50, 300, "Arial", 14, width-10, Pos.BASELINE_LEFT);	
		noInputFound.setText("No password input text found!");	// This changes based on input, but we 
		noInputFound.setTextFill(Color.RED);			// want this to be shown at startup	
		

		// Setup the valid Password message, which is used when all the requirements have been met
		validPassword.setTextFill(Color.GREEN);
		validPassword.setAlignment(Pos.BASELINE_LEFT);
		setupLabelWidget(validPassword, 420, 330, "Arial", 18, width-10, Pos.BASELINE_LEFT);
		
		
    	// Place all of the established GUI elements into the pane
    	theRootPane.getChildren().clear();
    	
    	theRootPane.getChildren().addAll(label_ApplicationTitle, label_TitleLine1, text_AdminUsername, text_AdminPassword1,
    			text_AdminPassword2, button_AdminSetup, button_Quit, noInputFound, 
				 validPassword,label_PasswordRequirements, 
				noUsernameInputFound, validUsername, label_UsernameRequirements); 
		    	
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		applicationMain.FoundationsMain.activeHomePage = theRole;	// 1: Admin; 2: Role1; 3 Roles2

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundation Code: First User Account Setup");	
		theStage.setScene(theFirstAdminScene);
		theStage.show();
	}

	
	
	/**********
	 * <p> Constructor: ViewFirstAdmin() </p>
	 * 
	 * <p> Description: This constructor is called just once, the first time a new admin needs to
	 * be created.  It establishes all of the common GUI widgets for the page so they are only
	 * created once and reused when needed.
	 * 		
	 */
	private ViewFirstAdmin() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theFirstAdminScene = new Scene(theRootPane, width, height);

		// Label theScene with the name of the system startup screen
		setupLabelUI(label_ApplicationTitle, "Arial", 32, width, Pos.CENTER, 0, 10);

		// Label to display the welcome message for the first user
		setupLabelUI(label_TitleLine1, "Arial", 24, width, Pos.CENTER, 0, 70);

		// Label to display the welcome message for the first user
		setupLabelUI(label_TitleLine2, "Arial", 18, width, Pos.CENTER, 0, 130);


		// USERNAME VERIFICATION
		// Establish the text input operand field for the Admin username
		setupTextUI(text_AdminUsername, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 160, 
				true);
		text_AdminUsername.setPromptText("Enter Admin Username");
		text_AdminUsername.textProperty().addListener((observable, oldValue, newValue) 
				-> {ControllerFirstAdmin.setAdminUsername(); });
		text_AdminUsername.textProperty().addListener((observable, oldValue, newValue) 
				-> {ModelFirstAdmin.updateFirstAdminUsername(); });
			
		
		// PASSWORD VERIFICATION
		// Establish the text input operand field for the password
		setupTextUI(text_AdminPassword1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, 
				true);
		text_AdminPassword1.setPromptText("Enter Admin Password");
		text_AdminPassword1.textProperty().addListener((observable, oldValue, newValue)
				-> {ControllerFirstAdmin.setAdminPassword1(); });
		
		text_AdminPassword1.textProperty().addListener((observable, oldValue, newValue)
					-> {ModelFirstAdmin.updateFirstAdminPassword(); });
		// Establish the text input operand field for the password
		setupTextUI(text_AdminPassword2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 260, 
				true);
		text_AdminPassword2.setPromptText("Enter Admin Password Again");
		text_AdminPassword2.textProperty().addListener((observable, oldValue, newValue) 
				-> {ControllerFirstAdmin.setAdminPassword2(); });

		// Set up the Log In button
		setupButtonUI(button_AdminSetup, "Dialog", 18, 200, Pos.CENTER, 475, 210);
		button_AdminSetup.setOnAction((event) -> {
			ControllerFirstAdmin.doSetupAdmin(theStage,1); 
			});
		button_AdminSetup.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");


		// Label to display the Passwords do not match error message
		//setupLabelUI(label_PasswordsDoNotMatch, "Arial", 18, width, Pos.CENTER, 0, 300);

		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 520);
		button_Quit.setOnAction((event) -> {ControllerFirstAdmin.performQuit(); });
		button_Quit.setStyle("-fx-font: 22 arial; -fx-base: #FFC627;");

		// Place all of the just-initialized GUI elements into the pane
		theRootPane.getChildren().addAll(label_ApplicationTitle, label_TitleLine1,
				label_TitleLine2, text_AdminUsername, text_AdminPassword1, 
				text_AdminPassword2, button_AdminSetup, 
				button_Quit);

	}
	
	
	/*-********************************************************************************************

	Helper methods to reduce code length

	 */
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Label
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

	
	/**********
	 * Private local method to initialize the standard fields for a text field
	 * 
	 * @param t		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		The flag (Boolean) that specifies if this field is editable
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, 
			boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}	
	
	static private void setupLabelWidget(Label l, double x, double y, String ff, double f, double w, 
			Pos p){
		l.setLayoutX(x);
		l.setLayoutY(y);		
		l.setFont(Font.font(ff, f));	// The font face and the font size
		l.setMinWidth(w);
		l.setAlignment(p);
	}

}