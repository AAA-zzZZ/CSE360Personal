package guiDeleteUser;


import database.Database;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
public class ControllerDeleteUser {
	
	/*-********************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;		

	
	/**********
	 * <p> Method: doSelectUser() </p>
	 * 
	 * <p> Description: This method uses the ComboBox widget, fetches which item in the ComboBox
	 * was selected (a user in this case), and establishes that user and the current user, setting
	 * easily accessible values without needing to do a query. </p>
	 * 
	 */
	
	protected static void doSelectUser() {
		
		ViewDeleteUser.theSelectedUser = 
				(String) ViewDeleteUser.combobox_SelectUser.getValue();
		theDatabase.getUserAccountDetails(ViewDeleteUser.theSelectedUser);

		setupSelectedUser();
	}
	
	
	
	/**********
	 * <p> Method: repaintTheWindow() </p>
	 * 
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 * 
	 */
	protected static void repaintTheWindow() {
		// Clear what had been displayed
		ViewDeleteUser.theRootPane.getChildren().clear();
		
		
		if( ViewDeleteUser.theSelectedUser.compareTo("<Select a User>")==0)
		{
		// Show the fields as there is not a selected user (as opposed to the prompt)
			ViewDeleteUser.theRootPane.getChildren().addAll(
					ViewDeleteUser.label_PageTitle, ViewDeleteUser.label_UserDetails,
					ViewDeleteUser.button_UpdateThisUser, ViewDeleteUser.line_Separator1,
					ViewDeleteUser.label_SelectUser,
					ViewDeleteUser.combobox_SelectUser, 
					ViewDeleteUser.line_Separator4, 
					ViewDeleteUser.button_Return,
					ViewDeleteUser.button_Logout,
					ViewDeleteUser.button_Quit);
		}
		
		else {
		// Show all the fields as there is a selected user (as opposed to the prompt)
			ViewDeleteUser.theRootPane.getChildren().addAll(
					ViewDeleteUser.label_PageTitle, ViewDeleteUser.label_UserDetails,
					ViewDeleteUser.button_UpdateThisUser, ViewDeleteUser.line_Separator1,
					ViewDeleteUser.label_SelectUser,
					ViewDeleteUser.combobox_SelectUser, 
					ViewDeleteUser.button_Delete,
					ViewDeleteUser.line_Separator4, 
					ViewDeleteUser.button_Return,
					ViewDeleteUser.button_Logout,
					ViewDeleteUser.button_Quit);
		}
		// Add the list of widgets to the stage and show it
		
		// Set the title for the window
		ViewDeleteUser.theStage.setTitle("CSE 360 Foundation Code: Admin Opertaions Page");
		ViewDeleteUser.theStage.setScene(ViewDeleteUser.theDeleteUserScene);
		ViewDeleteUser.theStage.show();
	}
	
	
	/**********
	 * <p> Method: setupSelectedUser() </p>
	 * 
	 * <p> Description: This method fetches the current values based on which user is selected
	 *  </p>
	 * 
	 */
	private static void setupSelectedUser() {
		System.out.println("*** Entering setupSelectedUser");
		
//		ViewDeleteUser.setupButtonUI(ViewDeleteUser.button_Delete,"Dialog", 16, 150, 
//	Pos.CENTER, 200, 205);
//		ViewDeleteUser.combobox_SelectUser.setItems(FXCollections.
//				observableArrayList(ViewDeleteUser.userList));
		//ViewDeleteUser.combobox_SelectUser.getSelectionModel().clearAndSelect(0);
		
		
		repaintTheWindow();
	}
	
	/**********
	 * <p> Method: performDelete() </p>
	 * 
	 * <p> Description: This method Deletes the selected user
	 * list. </p>
	 * 
	 */
	protected static void performDeleteUser() {
		// Determine the username in the ComboBox list was selected
		ViewDeleteUser.theSelectedUser =
				(String) ViewDeleteUser.combobox_SelectUser.getValue();
		
		// If the selection is the list header (e.g., "<Select a user>") don't do anything
		
		if (ViewDeleteUser.theSelectedUser.compareTo("<Select a user>") != 0) {
			// If an actual user is selected,
			// determine if that user is an admin
			// if is, shows a pop up window
			if (theDatabase.getCurrentAdminRole()) {				
				ViewDeleteUser.alertDeleteAdmin.setTitle("*** WARNING ***");
				ViewDeleteUser.alertDeleteAdmin.setHeaderText("User deletion Issue");
				ViewDeleteUser.alertDeleteAdmin.setContentText("Admin cannot delete another admin");
				ViewDeleteUser.alertDeleteAdmin.showAndWait();
			}
			else 
			{
				//delete that user record from database
				if (theDatabase.deleteUser(ViewDeleteUser.theSelectedUser) ) {
					ViewDeleteUser.combobox_SelectUser = new ComboBox<String>();
					ViewDeleteUser.userList =theDatabase.getUserList();
					ViewDeleteUser.combobox_SelectUser.setItems(FXCollections.
							observableArrayList(ViewDeleteUser.userList));
					ViewDeleteUser.combobox_SelectUser.getSelectionModel().
							clearAndSelect(0);			
					System.out.println("Delete successful");
					setupSelectedUser();
				}
			}
		}
		
	}
	
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 * 
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewDeleteUser.theStage,
				ViewDeleteUser.theUser);
	}
	
	
	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewDeleteUser.theStage);
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
		System.exit(0);
	}
	
	


}
