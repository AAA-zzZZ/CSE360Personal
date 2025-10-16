package guiUserLogin;


import database.Database;
import entityClasses.User;
import guiTools.ModelInputValidator;
import javafx.stage.Stage;

public class ControllerUserLogin { 
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	
	*/


	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	private static Stage theStage;

	
	//The error messages being returned from each validator method.
	private static String userNameRecognizerErr = "";	
	private static String passwordEvaluationErr ="";
	
	/**********
	 * <p> Method: public doLogin() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Login button. This
	 * method checks the username and password to see if they are valid.  If so, it then logs that
	 * user in my determining which role to use.
	 * 
	 * The method reaches batch to the view page and to fetch the information needed rather than
	 * passing that information as parameters.
	 * 
	 */	
	protected static void doLogin(Stage ts) {
		theStage = ts;
		String username = ViewUserLogin.text_Username.getText();
		String password = ViewUserLogin.text_Password.getText();
		
		ViewUserLogin.label_UsernameInvalidLabel.setText("");
		ViewUserLogin.label_PasswordInvalidLabel.setText("");
		
		//same login as in FirstAdmin
		//input validation is here
		//first, check the username input is valid or not
		//then check if the password is valid or not
		userNameRecognizerErr = ModelInputValidator.checkForValidUserName(username);
		
		passwordEvaluationErr = ModelInputValidator.checkForValidPassword(password);
		
		if(userNameRecognizerErr.length()!=0)
		{
			ViewUserLogin.text_Username.setText("");
			ViewUserLogin.label_UsernameInvalidLabel.setText(userNameRecognizerErr);
		}
		
		else if(passwordEvaluationErr.length()!=0)
		{
			ViewUserLogin.text_Password.setText("");
			ViewUserLogin.label_PasswordInvalidLabel.setText(passwordEvaluationErr);
		}
		
		//if both username and password are validated, then retrieve the information
		//from the username entered in the database.
		else {
			boolean loginResult = false;
			
			//check if user exists
			if(theDatabase.doesUserExist(username)==false) {
				ViewUserLogin.alertUserDoesntExistError.setContentText(
							"User does not exist. Try again!");
				ViewUserLogin.alertUserDoesntExistError.showAndWait();
				return;
			}
			else
			{
				// Fetch the user and verify the username
				if (theDatabase.getUserAccountDetails(username) == false) {
					// Don't provide too much information.  Don't say the username is invalid or the
					// password is invalid.  Just say the pair is invalid.
					ViewUserLogin.alertUsernamePasswordError.setContentText(
							"Incorrect username/password. Try again!");
					ViewUserLogin.alertUsernamePasswordError.showAndWait();
					return;
				}
				System.out.println("*** Username is valid");
				
				// TODO: check if user requested a onetime password, if yes, then
				// automatically direct user to reset password, invoke doResetPassword()
				
				// Check to see that the login password matches the account password
				String actualPassword = theDatabase.getCurrentPassword();
				
				if (password.compareTo(actualPassword) != 0) {
					ViewUserLogin.alertUsernamePasswordError.setContentText(
							"Incorrect username/password. Try again!");
					ViewUserLogin.alertUsernamePasswordError.showAndWait();
					return;
				}
				System.out.println("*** Password is valid for this user");
			}
			// Establish this user's details
			User user = new User(username, password, theDatabase.getCurrentFirstName(), 
					theDatabase.getCurrentMiddleName(), theDatabase.getCurrentLastName(), 
					theDatabase.getCurrentPreferredFirstName(), theDatabase.getCurrentEmailAddress(), 
					theDatabase.getCurrentAdminRole(), 
					theDatabase.getCurrentNewRole1(), theDatabase.getCurrentNewRole2());
			
			// See which home page dispatch to use
			int numberOfRoles = theDatabase.getNumberOfRoles(user);		
			System.out.println("*** The number of roles: "+ numberOfRoles);
			if (numberOfRoles == 1) {
				// Single Account Home Page - The user has no choice here
				
				// Admin role
				if (user.getAdminRole()) {
					loginResult = theDatabase.loginAdmin(user);
					if (loginResult) {
						guiAdminHome.ViewAdminHome.displayAdminHome(theStage, user);
					}
				} else if (user.getNewRole1()) {
					loginResult = theDatabase.loginRole1(user);
					if (loginResult) {
						guiRole1.ViewRole1Home.displayRole1Home(theStage, user);
					}
				} else if (user.getNewRole2()) {
					loginResult = theDatabase.loginRole2(user);
					if (loginResult) {
						guiRole2.ViewRole2Home.displayRole2Home(theStage, user);
					}
					// Other roles
				} else {
					System.out.println("***** UserLogin goToUserHome request has an invalid role");
				}
			} else if (numberOfRoles > 1) {
				// Multiple Account Home Page - The user chooses which role to play
				System.out.println("*** Going to displayMultipleRoleDispatch");
				guiMultipleRoleDispatch.ViewMultipleRoleDispatch.
				displayMultipleRoleDispatch(theStage, user);
			}
		}
	}
	
	protected static void doRequestOnetimePass()
	{
		String username = ViewUserLogin.text_ForgetPasswordUsername.getText();
		
		//check if the username entered is in the database
		if (!theDatabase.doesUserExist(username)) {
			ViewUserLogin.alertUserDoesntExistError.setContentText(
					"User does not exist. Try again!");
			ViewUserLogin.alertUserDoesntExistError.showAndWait();
			return;
		}
		else {
			//if the user is not inside the ResetPasswordUsers table(not yet requested updates)
			if(!theDatabase.doesUserRequestedResetPassword(username))
			{			
				ViewUserLogin.text_ForgetPasswordUsername.setText("");
				theDatabase.requestResetPassword(username);
				ViewUserLogin.alertOnetimePasswordRequested.setContentText("You have successfully requested to reset your password."
						+ " Waiting on admin to set a onetime password");
				ViewUserLogin.alertOnetimePasswordRequested.showAndWait();
				//TODO: clear input field
				System.out.println("*****DEBUG: Requested Onetime Password");
				return;
			}
			
			//if the user has already requested resetting their password, but admin has not yet set an onetime passcode
			//display an alert to inform the user
			else {
				if(!theDatabase.getAdminUpdatedUsingUsername(username))
				{
					ViewUserLogin.text_ForgetPasswordUsername.setText("");
					ViewUserLogin.alertOnetimePasswordNotSet.setContentText("You have already requested resetting password, please "
							+ "do not request multiple times and wait for admin to reset your password.");
					ViewUserLogin.alertOnetimePasswordNotSet.showAndWait();
					System.out.println("*****DEBUG: pending on admin action to set onetime password for user");	
					return;
				}
				else {
					//TODO: if the user is inside resetpasswordUsers table and admin has already reset their password
					//inform the user to login with the password provided
					ViewUserLogin.text_ForgetPasswordUsername.setText("");
					ViewUserLogin.alertPasswordReset.setContentText("Your password has already reset by the admin, please login with"
							+ "your onetime password, and proceed to reset your password.");
					ViewUserLogin.alertPasswordReset.showAndWait();
					System.out.println("*****DEBUG: informing user to login with new password");	
					return;
				}
			}
		}
		
	}
	
	//TODO: update password after onetime password is set by admin
	protected static void doResetPassword(Stage theStage)
	{
		
	}
		
	/**********
	 * <p> Method: setup() </p>
	 * 
	 * <p> Description: This method is called to reset the page and then populate it with new
	 * content.</p>
	 * 
	 */
	protected static void doSetupAccount(Stage theStage, String invitationCode) {
		guiNewAccount.ViewNewAccount.displayNewAccount(theStage, invitationCode);
	}
	
	/**********
	 * <p> Method: public performQuit() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.  All important data must be stored in the
	 * database, so there is no cleanup required.  (This is important so we can minimize the impact
	 * of crashed.)
	 * 
	 */	
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}
	
	
	
	
}
