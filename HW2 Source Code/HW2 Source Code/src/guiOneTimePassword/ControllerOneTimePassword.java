package guiOneTimePassword;

import java.util.List;

import database.*;
import entityClasses.User;
import guiAddRemoveRoles.ViewAddRemoveRoles;
import guiTools.ModelInputValidator;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ControllerOneTimePassword {
	

	// is needed

	private static String newPassword = "";
	
	// UPDATE FIRST ADMIN PASSWORD UI
	public static String passwordErrorMessage = "";		// The error message text
	public static String passwordInput = "";			// The input being processed

	//The error messages being returned from validator method.
	private static String passwordEvaluationErr ="";


	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static User theUser;				// The current logged in User

	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**
	 * Method: doSetupOnetimePassword()
	 * 
	 * Description: 
	 */
	protected static void updateOnetimePassword()
	{
		passwordInput = ViewOneTimePassword.text_SetOTP.getText();	// initial state and fetch the input
		
		// If the input is empty, clear the aspects of the user interface having to do with the
		// user input and tell the user that the input is empty.
		if (passwordInput.isEmpty()) {
			passwordErrorMessage ="No password input text found!";
			ViewOneTimePassword.label_noInputFound.setText(passwordErrorMessage);
		}
		else
		{
			// There is user input, so evaluate it to see if it satisfies the requirements
			String errMessage = ModelInputValidator.checkForValidPassword(passwordInput);
	
			// An empty string means there is no error message, which means the input is valid
			if (errMessage != "") {
				
				// Since the output is not empty, at least one requirement have not been satisfied.
				System.out.println(errMessage);			// Display the message to the console
				
				ViewOneTimePassword.label_noInputFound.setText("");			// There was input, so no error message
				
				// Tell the user that the password is not valid with a red message
				ViewOneTimePassword.label_validPassword.setTextFill(Color.RED);
				ViewOneTimePassword.label_validPassword.setText("Failure! The password is not valid.");
				
			}
			else {
				// All the requirements were satisfied - the password is valid
				System.out.println("Success! Password is valid.");
				// Tell the user that the password is valid with a green message
				ViewOneTimePassword.label_validPassword.setTextFill(Color.GREEN);
				ViewOneTimePassword.label_validPassword.setText("Success! Password is valid.");
			}
		}

	}
	
	
	/**********
	 * <p> Method: doSelectUser() </p>
	 * 
	 * <p> Description: This method uses the ComboBox widget, fetches which item in the ComboBox
	 * was selected (a user in this case), and establishes that user and the current user, setting
	 * easily accessible values without needing to do a query. </p>
	 * 
	 */
	protected static void doSelectUser() {
		ViewOneTimePassword.theSelectedUser = 
				(String) ViewOneTimePassword.comboBox_SelectUser.getValue();
		theDatabase.getUserAccountDetails(ViewOneTimePassword.theSelectedUser);
	}
	
	/**********
	 * <p> Method: setNewPassword1() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 1 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	public static void setNewPassword() {
		newPassword = ViewOneTimePassword.text_SetOTP.getText();
	}
	
	
	/**********
	 * <p> Method: doUpdatePassword() </p>
	 * 
	 * <p> Description: This method is called when the user presses the button to update password
	 * It start by checking if the inputs are valid, then trying to upadte the password in the user's record in
	 * database.  If that is successful, it displays an alert stating update was successful, 
	 * then we proceed to the UserLogin page.</p>
	 * 
	 */
	public static void doUpdatePassword()
	{
		setNewPassword();
		String newPassword = ViewOneTimePassword.text_SetOTP.getText();
		passwordEvaluationErr = ModelInputValidator.checkForValidPassword(newPassword);
		if(passwordEvaluationErr.length()!=0)
		{
			ModelInputValidator.displayPasswordErrorMessages(newPassword);
			ViewOneTimePassword.text_SetOTP.setText("");
		}
		else {
				//update the password in the user's database
				theDatabase.adminSetOneTimePassCode(newPassword,ViewOneTimePassword.theSelectedUser);
				System.out.println("*******DEBUG: updated user "+theDatabase.getCurrentUsername()
						+ "'s password to "+ newPassword+"\n");
				ViewOneTimePassword.alertUpdateSuccessful.showAndWait();
				//ask user to log in again
				guiUserLogin.ViewUserLogin.displayUserLogin(ViewOneTimePassword.theStage);
			}
		}
		
	/**********
	* <p> 
	* 
	* Title: performReturn() Method. </p>
	* 
	* <p> Description: Protected method that allows user to return to the previous page..
	* </p>
	*/
	protected static void performReturn() { 
		
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewOneTimePassword.theStage,
				ViewOneTimePassword.getTheUser());
	}
	
	/**********
	* <p> 
	* 
	* Title: performLogout() Method. </p>
	* 
	* <p> Description: Protected method that allows user to logout.
	* </p>
	*/
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewOneTimePassword.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}