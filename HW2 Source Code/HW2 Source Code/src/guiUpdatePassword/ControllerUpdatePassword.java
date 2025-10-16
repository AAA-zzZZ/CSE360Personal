package guiUpdatePassword;

import database.Database;

import guiTools.*;
import javafx.scene.paint.Color;

public class ControllerUpdatePassword {
	
	/*********************************************************************************************
	*
	* The controller attributes for this page
	*
	* This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	* static methods that can be called by the View (which is a singleton instantiated object) and
	* the Model is often just a stub, or will be a singleton instantiated object.
	*/

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	private static String newPassword1 = "";
	private static String newPassword2 = "";
	
	// UPDATE FIRST ADMIN PASSWORD UI
	public static String passwordErrorMessage = "";		// The error message text
	public static String passwordInput = "";			// The input being processed

	//The error messages being returned from validator method.
	private static String passwordEvaluationErr ="";
	
	
	
	/**********
	 * <p> Method: setNewPassword1() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 1 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setNewPassword1() {
		newPassword1 = ViewUpdatePassword.text_Password1.getText();
	}
	
	/**********
	 * <p> Method: setNewPassword2() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 1 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setNewPassword2() {
		newPassword2 = ViewUpdatePassword.text_Password2.getText();
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
		setNewPassword1();
		setNewPassword2();
		String newPassword = ViewUpdatePassword.text_Password1.getText();
		passwordEvaluationErr = ModelInputValidator.checkForValidPassword(newPassword);
		if(passwordEvaluationErr.length()!=0)
		{
			ModelInputValidator.displayPasswordErrorMessages(newPassword);
			ViewUpdatePassword.text_Password1.setText("");
			ViewUpdatePassword.text_Password2.setText("");
		}
		else {
			// Make sure the two passwords are the same and the input is valid
			if (newPassword1.compareTo(newPassword2) == 0) {
				//update the password in the user's database
				theDatabase.updatePassword(newPassword, theDatabase.getCurrentUsername());
				System.out.println("*******DEBUG: updated user "+theDatabase.getCurrentUsername()
						+ "'s password to "+ newPassword+"\n");
				ViewUpdatePassword.alertUpdateSuccessful.showAndWait();
				//ask user to log in again
				guiUserLogin.ViewUserLogin.displayUserLogin(ViewUpdatePassword.theStage);
			}
			else {
				// The two passwords are NOT the same, so clear the passwords, explain the passwords
				// must be the same, and clear the message as soon as the first character is typed.
				ModelInputValidator.displayPasswordDoesNotMatchErrorMessage();
				ViewUpdatePassword.text_Password1.setText("");
				ViewUpdatePassword.text_Password2.setText("");
			}
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
			guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewUpdatePassword.getTheStage(),
					ViewUpdatePassword.getTheUser());
		}
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that allows the user to logout.
	 * </p>
	 */	
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewUpdatePassword.theStage);
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

	
	/*******
	 * <p> Title: updatePassword Method. </p>
	 * 
	 * <p> Description: This method is triggered everytime there is a change in the
	 * password input field. If the input is empty, it tells the user that there is no input text found.
	 * Otherwise, it will check whether the password input is valid and make corresponding UI updates
	 * based on the validity of the input. </p>

	 */
	 protected static String  updateNewPassword() {
			 passwordInput = ViewUpdatePassword.text_Password1.getText();	// initial state and fetch the input
				
				// If the input is empty, clear the aspects of the user interface having to do with the
				// user input and tell the user that the input is empty.
				if (passwordInput.isEmpty()) {
					passwordErrorMessage ="No password input text found!";
					ViewUpdatePassword.label_noInputFound.setText(passwordErrorMessage);
				}
				else
				{
					// There is user input, so evaluate it to see if it satisfies the requirements
					String errMessage = ModelInputValidator.checkForValidPassword(passwordInput);
			
					// An empty string means there is no error message, which means the input is valid
					if (errMessage != "") {
						
						// Since the output is not empty, at least one requirement have not been satisfied.
						System.out.println(errMessage);			// Display the message to the console
						
						ViewUpdatePassword.label_noInputFound.setText("");			// There was input, so no error message
						
						// Tell the user that the password is not valid with a red message
						ViewUpdatePassword.label_validPassword.setTextFill(Color.RED);
						ViewUpdatePassword.label_validPassword.setText("Failure! The password is not valid.");
						
					}
					else {
						// All the requirements were satisfied - the password is valid
						System.out.println("Success! Password is valid.");
						// Tell the user that the password is valid with a green message
						ViewUpdatePassword.label_validPassword.setTextFill(Color.GREEN);
						ViewUpdatePassword.label_validPassword.setText("Success! Password is valid.");
					}
		}
		return passwordErrorMessage;
	}

	

}