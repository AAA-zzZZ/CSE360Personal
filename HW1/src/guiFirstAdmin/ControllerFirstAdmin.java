package guiFirstAdmin;

import java.sql.SQLException;
import database.Database;
import entityClasses.User;
import javafx.stage.Stage;
import guiTools.*;

public class ControllerFirstAdmin {
	/*-********************************************************************************************

	The controller attributes for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	private static String adminUsername = "";
	private static String adminPassword1 = "";
	private static String adminPassword2 = "";
	
	//The error messages being returned from each validator method.
	private static String userNameRecognizerErr = "";	
	private static String passwordEvaluationErr ="";
	
	protected static Database theDatabase = applicationMain.FoundationsMain.database;	
	
	

	/*-********************************************************************************************

	The User Interface Actions for this page
	
	*/
	
	
	/**********
	 * <p> Method: setAdminUsername() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the username field in the
	 * View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 * 
	 */
	protected static void setAdminUsername() {
		adminUsername = ViewFirstAdmin.text_AdminUsername.getText();
		ViewFirstAdmin.label_UsernameInvalidLabel.setText("");
	}
	
	
	/**********
	 * <p> Method: setAdminPassword1() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 1 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminPassword1() {
		adminPassword1 = ViewFirstAdmin.text_AdminPassword1.getText();
		ViewFirstAdmin.label_PasswordsDoNotMatch.setText("");
		ViewFirstAdmin.label_PasswordInvalidLabel.setText("");
	}
	
	
	/**********
	 * <p> Method: setAdminPassword2() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 2 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminPassword2() {
		adminPassword2 = ViewFirstAdmin.text_AdminPassword2.getText();		
		ViewFirstAdmin.label_PasswordsDoNotMatch.setText("");
		ViewFirstAdmin.label_PasswordInvalidLabel.setText("");
	}
	
	
	/**********
	 * <p> Method: doSetupAdmin() </p>
	 * 
	 * <p> Description: This method is called when the user presses the button to set up the Admin
	 * account.  It start by checking if the inputs are valid, then trying to establish a new user and placing that user into the
	 * database.  If that is successful, we proceed to the UserUpdate page.</p>
	 * 
	 */
	protected static void doSetupAdmin(Stage ps, int r) {
		
		//after the button is pressed, check if inputs for both username and password are valid
		//checking username first, then check adminPassword1. 
		//all evaluation will be done based on adminPassword1, adminPassword2 is only checked 
		//when checking if 2 password inputs are matching
		userNameRecognizerErr =ModelInputValidator.checkForValidUserName(adminUsername);
		passwordEvaluationErr = ModelInputValidator.evaluatePassword(adminPassword1);
		
		//checking username first
		//if username not valid, clear input and fail the setup of admin,
		if (userNameRecognizerErr.length()!=0) {
			ViewFirstAdmin.text_AdminUsername.setText("");
			ViewFirstAdmin.label_UsernameInvalidLabel.setText(userNameRecognizerErr);
		}
		
		//then check password1
		//if error message is not empty, clear both password1&2 input and fail the setup of admin
		//and display the error message to user
		else if(passwordEvaluationErr.length()!=0)
		{
			ViewFirstAdmin.text_AdminPassword1.setText("");
			ViewFirstAdmin.text_AdminPassword2.setText("");
			ViewFirstAdmin.label_PasswordInvalidLabel.setText(passwordEvaluationErr);
		}
		
		//else, input for both username and password are validated
		else {
			
			// Make sure the two passwords are the same and the input is valid
			if (adminPassword1.compareTo(adminPassword2) == 0) {
				// Create the passwords and proceed to the user home page
				User user = new User(adminUsername, adminPassword1, "", "", "", "", "", true, false, 
						false);
				try {
					// Create a new User object with admin role and register in the database
					theDatabase.register(user);
				}
				catch (SQLException e) {
					System.err.println("*** ERROR *** Database error trying to register a user: " + 
							e.getMessage());
					e.printStackTrace();
					System.exit(0);
				}
				
				// User was established in the database, so navigate to the User Update Page
				guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewFirstAdmin.theStage, user);
			}
			else {
				// The two passwords are NOT the same, so clear the passwords, explain the passwords
				// must be the same, and clear the message as soon as the first character is typed.
				ViewFirstAdmin.text_AdminPassword1.setText("");
				ViewFirstAdmin.text_AdminPassword2.setText("");
				ViewFirstAdmin.label_PasswordsDoNotMatch.setText(
						"The two passwords must match. Please try again!");
			}
		}
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}	
}


	

