package guiUserUpdate;

import entityClasses.User;
import javafx.stage.Stage;

public class ControllerUserUpdate {
	/*-********************************************************************************************

	The Controller for ViewUserUpdate 
	
	**********************************************************************************************/

	/**********
	 * <p> Title: ControllerUserUpdate Class</p>
	 * 
	 * <p> Description: This static class supports the actions initiated by the ViewUserUpdate
	 * class. In this case, there is just one method, no constructors, and no attributes.</p>
	 *
	 */

	/*-********************************************************************************************

	The User Interface Actions for this page
	
	**********************************************************************************************/

	
	/**********
	 * <p> Method: public goToUserHomePage(Stage theStage, User theUser) </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the button to
	 * proceed to the user's home page.
	 * 
	 * @param theStage specifies the JavaFX Stage for next next GUI page and it's methods
	 * 
	 * @param theUser specifies the user so we go to the right page and so the right information
	 *
	 */

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static User theUser;				// The current logged in User

			
	
	/**
	 * Method: updatePassword()
	 * 
	 * Description: Protected method that allows an user to update their password, This is done by invoking the UpdatePassword Page. 
	 * There is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.
	 */
	protected static void updatePassword() {

		guiUpdatePassword.ViewUpdatePassword.displayUpdatePassword(ViewUserUpdate.theStage, 
						ViewUserUpdate.theUser);
		} 
				
	protected static void goToUserHomePage(Stage theStage, User theUser) {
		
		// Get the roles the user selected during login
		int theRole = applicationMain.FoundationsMain.activeHomePage;

		// Use that role to proceed to that role's home page
		switch (theRole) {
		case 1:
			guiAdminHome.ViewAdminHome.displayAdminHome(theStage, theUser);
			break;
		case 2:
			guiRole1.ViewRole1Home.displayRole1Home(theStage, theUser);
			break;
		case 3:
			guiRole2.ViewRole2Home.displayRole2Home(theStage, theUser);
			break;
		default: 
			System.out.println("*** ERROR *** UserUpdate goToUserHome has an invalid role: " + 
					theRole);
			System.exit(0);
		}
 	}
}