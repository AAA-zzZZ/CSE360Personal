package guiOneTimePassword;

import java.util.ArrayList;
import java.util.List;

import database.Database;
import entityClasses.User;
import guiAddRemoveRoles.ControllerAddRemoveRoles;
import guiUpdatePassword.ControllerUpdatePassword;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ViewOneTimePassword {

	
	
	/*-*******************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings.
	protected static Label label_UserDetails = new Label();
	protected static Label label_PageTitle = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	// GUI Area 2: It contains the basic function of this page. The combobox that lets the admin select a user from 
	// the userlist who has requested to reset their password, a textfield input for admin to enter their onetime password,
	// and the button to trigger the update funciton.
	protected static Label label_SelectUser = new Label("Select User to reset their password:");
	protected static Label lable_EnterOTP = new Label("Create One-Time Password:");
	protected static ComboBox <String> comboBox_SelectUser = new ComboBox <String>();
	protected static List<String> usernames = new ArrayList<String>();
	protected static TextField text_SetOTP = new TextField("One-Time Password");	
	protected static Button button_SendOTP = new Button("Send One Time Password");
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
		
	// GUI Area 3: It is used for quitting the application, logging
	// out, and on other pages a return is provided so the user can return to a previous page when
	// the actions on that page are complete.  Be advised that in most cases in this code, the 
	// return is to a fixed page as opposed to the actual page that invoked the pages.
	
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");
	
	// GUI Area 4: Feedback for passwords in the OTP text field
	// Feedback labels with text and color to specify which of the requirements have been satisfied
	// (using both text and the color green) and which have not (both with text and the color red).
	
	// ERROR MESSASGES FOR USER FOR PASSWORD
    // Code from Password: View. This code is initializing the boolean flags to check if the password is valid.
	static protected Label label_errPassword = new Label();	// There error labels change based on the
	static protected Label label_noInputFound = new Label();			// user's input
	static protected Label label_validPassword = new Label();		// This only appears with a valid password
	static protected Label label_PasswordRequirements = new Label();
	//This alert is used when the update is successful.
	protected static Alert alertUpdateSuccessful = new Alert(AlertType.INFORMATION);
	
	
	// This is the end of the GUI objects for the page.
	
	
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewOneTimePassword theView;		// Used to determine if instantiation of the class
		// is needed
	
	protected static String theSelectedUser = "";	// The user whose roles are being updated
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User
	
	private static Scene theOneTimePasswordScene;		// The shared Scene each invocation populates
	
	public static Scene theOneTimePassword = null;	
	
	
	
	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayOneTimePassword(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the AddRevove page to be displayed.
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
	 * @param user specifies the User whose roles will be updated
	 *
	 */
	public static void displayOneTimePassword(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI by creating the 
		// singleton instance of this class
		if (theView == null) theView = new ViewOneTimePassword();// Instantiate singleton if needed

		text_SetOTP.setText("");
		
		// DISPLAY FOR CHECKING PASSWORD REQUIREMENTS
		text_SetOTP.textProperty().addListener((observable, oldValue, newValue) 
				-> {ControllerOneTimePassword.doUpdatePassword();});
		
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
	
		theStage.setTitle("CSE 360 Foundation Code: Update Password");
		theStage.setScene(theOneTimePasswordScene);
		theStage.show();										
	
	}
	
	/**********
	 * <p> Method: ViewOnetimePassword() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object. </p>
	 * 
	 * This is a singleton, so this is performed just one.  Subsequent uses fill in the changeable
	 * fields using the displayAddRempoveRoles method.</p>
	 * 
	 */
	private ViewOneTimePassword() {
	
		theRootPane = new Pane();
		theOneTimePasswordScene = new Scene(theRootPane, width, height);
	
		// GUI Area 1
		label_PageTitle.setText("Add/Removed Roles Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + getTheUser().getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((event) -> 
			{guiUserUpdate.ViewUserUpdate.displayUserUpdate(getTheStage(), getTheUser()); });
		button_UpdateThisUser.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
	
		
		// GUI Area 2:
		
		setupComboBoxUI(comboBox_SelectUser,  "Arial", 18, 380, 400, 190);
		List<String> requestList = theDatabase.getRequestOnetimePasswordList();	
		comboBox_SelectUser.setItems(FXCollections.observableArrayList(requestList));
		comboBox_SelectUser.getSelectionModel().select(0);
		
		
		setupLabelUI(label_SelectUser, "Arial", 22, width, Pos.BASELINE_LEFT, 41, 194);
		setupLabelUI(lable_EnterOTP, "Arial", 22, width, Pos.BASELINE_LEFT, 110, 294);
		setupTextUI(text_SetOTP, "Arial", 18, 380, Pos.BASELINE_LEFT,400, 290, true);
		setupButtonUI(button_SendOTP, "Dialog", 18, 210, Pos.CENTER, 250, 400);
		button_SendOTP.setOnAction((event) -> {ControllerOneTimePassword.doUpdatePassword() ;});
		button_SendOTP.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
		
		
		// GUI Area 3:
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 50, 540);
		button_Return.setOnAction((event) -> {ControllerOneTimePassword.performReturn(); });
		button_Return.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
	
		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((event) -> {ControllerOneTimePassword.performLogout(); });
		button_Logout.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
	
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((event) -> {ControllerOneTimePassword.performQuit(); });
		button_Quit.setStyle("-fx-font: 22 arial; -fx-base: #8C1D40;");
		
		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
		
			//label_EnterUserEmail, button_SendOTP,
			label_PageTitle,
			label_SelectUser,
			text_SetOTP,
			lable_EnterOTP,
			comboBox_SelectUser,
			button_SendOTP, 
			button_Return,
			button_Logout,
			button_Quit
			);
		
		// With theRootPane set up with the common widgets, it is up to displayAdminHome to show
		// that Pane to the user after the dynamic elements of the widgets have been updated.
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
	 * Private local method to initialize the standard fields for a text input field
	 * 
	 * @param b		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		Is this TextField user editable?
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
	
	
	/**********
	 * Private local method to initialize the standard fields for a ComboBox
	 * 
	 * @param c		The ComboBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the ComboBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w, double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
	
	public static Stage getTheStage() {
		return theStage;
	}
	
	public static User getTheUser() {
		return theUser;
	}
	
}



	