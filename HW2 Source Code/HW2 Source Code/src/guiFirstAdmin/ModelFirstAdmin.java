package guiFirstAdmin;

import guiTools.ModelInputValidator;
import javafx.scene.paint.Color;

/*******
 * <p> Title: ModelFirstAdmin Class. </p>
 * 
 * <p> Description: The First Admin Page Model.  This class validates the username and password for 
 * first admin account creation. </p>

 */
public class ModelFirstAdmin {
	
	// UPDATE FIRST ADMIN USERNAME UI
		public static String userNameRecognizerErrorMessage = "";	// The error message text
		public static String userNameRecognizerInput = "";			// The input being processed
		public static int userNameRecognizerIndexofError = -1;		// The index of error location
		


		/*******
		 * <p> Title: updateFirstAdminUsername Method. </p>
		 * 
		 * <p> Description: This method is triggered everytime there is a change in the
		 * username input field. If the input is empty, it tells the user that there is no input text found.
		 * Otherwise, it will check whether the username input is valid and make corresponding UI updates
		 * based on the validity of the input. </p>

		 */
		protected static void updateFirstAdminUsername() {
			
		String username = ViewFirstAdmin.text_AdminUsername.getText();
		
		// If the input is empty, clear the aspects of the user interface having to do with the
		// user input and tell the user that the input is empty.
		if (username.isEmpty()) {
			ViewFirstAdmin.noUsernameInputFound.setText("No username input text found!");
		}
		else
		{
			// There is user input, so evaluate it to see if it satisfies the requirements
			String errMessage = ModelInputValidator.checkForValidUserName(username);

			
			// An empty string means there is no error message, which means the input is valid
			if (errMessage != "") {
				
				// Since the output is not empty, at least one requirement have not been satisfied.
				System.out.println(userNameRecognizerErrorMessage);			// Display the message to the console
				ViewFirstAdmin.noUsernameInputFound.setText("");			// There was input, so no error message
				
				
				// Tell the user that the password is not valid with a red message
				ViewFirstAdmin.validUsername.setTextFill(Color.RED);
				ViewFirstAdmin.validUsername.setText("Failure! The username is not valid.");
				
			}
			else {
				// All the requirements were satisfied - the password is valid
				System.out.println("Success! Username is valid.");

				// Tell the user that the username is valid with a green message
				ViewFirstAdmin.validUsername.setTextFill(Color.GREEN);
				ViewFirstAdmin.validUsername.setText("Success! Username is valid.");
				
			} 
		}
	}
		
		
		
		/*******
		 * <p> Title: updateFirstAdminPassword Method. </p>
		 * 
		 * <p> Description: This method is triggered everytime there is a change in the
		 * password input field. If the input is empty, it tells the user that there is no input text found.
		 * Otherwise, it will check whether the password input is valid and make corresponding UI updates
		 * based on the validity of the input. </p>

		 */
		// UPDATE FIRST ADMIN PASSWORD UI
		public static String passwordErrorMessage = "";		// The error message text
		public static String passwordInput = "";			// The input being processed
		public static int passwordIndexofError = -1;		// The index where the error was located

		
		protected static void updateFirstAdminPassword() {
			
			String password = ViewFirstAdmin.text_AdminPassword1.getText();	// initial state and fetch the input
			
			// If the input is empty, clear the aspects of the user interface having to do with the
			// user input and tell the user that the input is empty.
			if (password.isEmpty()) {
				ViewFirstAdmin.noInputFound.setText("No password input text found!");
			}
			else
			{
				// There is user input, so evaluate it to see if it satisfies the requirements
				String errMessage = ModelInputValidator.checkForValidPassword(password);

				
				// An empty string means there is no error message, which means the input is valid
				if (errMessage != "") {
					
					// Since the output is not empty, at least one requirement have not been satisfied.
					System.out.println(errMessage);			// Display the message to the console
					
					ViewFirstAdmin.noInputFound.setText("");			// There was input, so no error message
					
					// Tell the user that the password is not valid with a red message
					ViewFirstAdmin.validPassword.setTextFill(Color.RED);
					ViewFirstAdmin.validPassword.setText("Failure! The password is not valid.");
					
				}
				else {
					// All the requirements were satisfied - the password is valid
					System.out.println("Success! Password is valid.");
					
					// Tell the user that the password is valid with a green message
					ViewFirstAdmin.validPassword.setTextFill(Color.GREEN);
					ViewFirstAdmin.validPassword.setText("Success! Password is valid.");
					
				} 
			}
		}
		

}