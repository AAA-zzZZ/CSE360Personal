package guiAdminHome;

import java.util.List;

import database.Database;
import guiTools.ModelInputValidator;


/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (!checkValidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress + ". Please inform user that the code is only a one-time use and will expire in 10 minutes.";
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Function allows an admin to set a temporary password for users who has requested to
	 * reset their password. Admin can select from a list of users, then set the OTP.  This is done 
	 * by invoking the OneTimePassword Page. There is no need to specify the home page for the return as this 
	 * can only be initiated by an Admin.
	 *  </p>
	 */
	protected static void setOnetimePassword () {
		
		guiOneTimePassword.ViewOneTimePassword.displayOneTimePassword(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to select from a list of users, and delete the user.
	 * Admin has to type "Yes" to confirm the deletion, and an admin cannot delete another admin. </p>
	 */
	protected static void deleteUser() {

	    List<String> usernames = theDatabase.getUserList();

	    if (usernames == null || usernames.isEmpty()) {
	        javafx.scene.control.Alert noUsers = new javafx.scene.control.Alert(
	                javafx.scene.control.Alert.AlertType.INFORMATION);
	        noUsers.setTitle("Delete User");
	        noUsers.setHeaderText("No users available");
	        noUsers.setContentText("There are no users to select.");
	        noUsers.showAndWait();
	        return;
	    }

	   
	    String preselect = usernames.get(0); //shows user in array position [0] by default

	    // user selection dialog 
	    javafx.scene.control.ChoiceDialog<String> picker =
	            new javafx.scene.control.ChoiceDialog<>(preselect, usernames);
	    picker.setTitle("Delete User");
	    picker.setHeaderText("Select a user to remove access");
	    picker.setContentText("User:");
	    java.util.Optional<String> chosen = picker.showAndWait();
	    if (!chosen.isPresent()) return;

	    String chosenName = chosen.get();
	    
	    
	    //check if selected user is admin, if it is, display a pop up window telling user 
	    // unable to delete admin
	     if(theDatabase.getUserAccountDetails(chosenName))
	     {
	    	if(theDatabase.getCurrentAdminRole())
	    	{
	    		System.out.println("\n***DEBUG: admin picked another admin role");
	    		ViewAdminHome.alertDeleteAdmin.setTitle("*** WARNING ***");
	    		ViewAdminHome.alertDeleteAdmin.setHeaderText("Cannot Delete Admin");
	    		ViewAdminHome.alertDeleteAdmin.setContentText("You may not remove another admin's access");
	    		ViewAdminHome.alertDeleteAdmin.showAndWait();
	    		return;
	    	}
	    	else {
	    		// action check, requests the admin to type out "yes"
	    		javafx.scene.control.TextInputDialog confirm = new javafx.scene.control.TextInputDialog();
	    		confirm.setTitle("Confirm Deletion");
	    		confirm.setHeaderText("Are you sure you want to remove access for \"" + chosenName + "\"?");
	    		confirm.setContentText("Type \"Yes\" to confirm:");
	    		java.util.Optional<String> input = confirm.showAndWait();
	    		
	    		// confirmation message
	    		javafx.scene.control.Alert done = new javafx.scene.control.Alert(
	    				javafx.scene.control.Alert.AlertType.INFORMATION);
	    		done.setTitle("Delete User");
	    		if (input.isPresent() && "Yes".equals(input.get().trim())) {
	    			theDatabase.deleteUser(chosenName);
	    			done.setHeaderText("User deleted");
	    			//done.setContentText("Deletion accepted for \"" + chosenName + "\".\n(Logic to be added.)");
	    		} else {
	    			done.setHeaderText("Cancelled");
	    			done.setContentText("No changes were made.");
	    		}
	    		done.showAndWait();
			}
	     }
	    
	}


	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Function lists the user's username,first name, middle name, and last name, 
	 * email, role so the admin can view all of the users and perform admin functions. This is done 
	 * by invoking the UserList Page. There is no need to specify the home page for the return as this 
	 * can only be initiated by an Admin. </p>
	 */
	protected static void listUsers() {
		guiUserList.ViewUserList.displayUserList(ViewAdminHome.theStage, 
								ViewAdminHome.theUser);		
	} 

	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean checkValidEmailAddress(String emailAddress) {
		boolean emailIsValid = ModelInputValidator.checkForValidEmail(emailAddress);
		if (emailIsValid) {
			return true;
		}
		ViewAdminHome.alertEmailError.setHeaderText("Email address is not valid!");
		ViewAdminHome.alertEmailError.setContentText(
				"Correct the email address and try again.");
		ViewAdminHome.alertEmailError.showAndWait();
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
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
