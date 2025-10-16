package guiUserList;


import java.util.List;

import database.Database;
import entityClasses.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;



public class ControllerUserList {

	
protected static User theUser;	// The current logged in User
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;		


	protected static void repaintTheWindow()
	{
		// Clear what had been displayed
		ViewUserList.theRootPane.getChildren().clear();
		
		
		//creating the TableView with ObservableList
		TableView <ObservableList<String>> table = new TableView<>();
		
		//ObservableList for the rows 
		ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
		
		List<String> usernameList = theDatabase.getUserList();
		List<String> fnameslist = theDatabase.getFirstNames();
		List<String> mnamesList = theDatabase.getMiddleNames();
		List<String> lnamesList = theDatabase.getLastNames();
		List<String> emailList = theDatabase.getEmails();
		List<Boolean> adminRoleList = theDatabase.getAdminRoles();
		List<Boolean> studentRoleList = theDatabase.getStudentRoles();
		List<Boolean> staffRoleList = theDatabase.getStaffRoles();
		
	
		// sets the number or rows based on the size of the usernameList
		usernameList.remove(0);
		int rcount = usernameList.size();

		// the first item in the usernamelist is <Select a User>, do not include <Select a User>
		for(int i = 0; i < rcount; i++) { //iterate through rcount
			ObservableList <String> row =FXCollections.observableArrayList();
			row.add(usernameList.get(i));
			row.add(fnameslist.get(i));
			row.add(mnamesList.get(i));
			row.add(lnamesList.get(i));
			row.add(emailList.get(i));
			row.add(adminRoleList.get(i).toString());
			row.add(studentRoleList.get(i).toString());
			row.add(staffRoleList.get(i).toString());
			
			rows.add(row);
			
		}
	
		table.setItems(rows);
		
		TableColumn <ObservableList<String>, String> userNameColumn = new TableColumn<>("Username");
		userNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
		
		TableColumn <ObservableList<String> , String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
		
		TableColumn <ObservableList<String> ,String> middleNameColumn = new TableColumn<>("Middle Name");
		middleNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
		
		TableColumn <ObservableList<String> , String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
		
		TableColumn <ObservableList<String> , String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4))); 
		
		TableColumn <ObservableList<String>, String> adminRolesColumn = new TableColumn<>("Is Admin?");
		adminRolesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
		
		TableColumn <ObservableList<String>, String> studentRolesColumn = new TableColumn<>("Is Student?");
		studentRolesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(6)));
	
		TableColumn <ObservableList<String>, String> staffRolesColumn = new TableColumn<>("Is Staff?");
		staffRolesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(7)));
	
	
		table.getColumns().addAll(userNameColumn,firstNameColumn, middleNameColumn, lastNameColumn, emailColumn, adminRolesColumn,studentRolesColumn,staffRolesColumn); 
		
		
		ViewUserList.theRootPane.getChildren().addAll(
				table,ViewUserList.button_Return,ViewUserList. button_Logout,ViewUserList.button_Quit);
		
		ViewUserList.getTheStage().setTitle("CSE 360 Foundation Code: View User Detail Lists");
		ViewUserList.getTheStage().setScene(ViewUserList.theUserListScene);
		ViewUserList.getTheStage().show();
	}



	/**********
	 * <p> 
	 * 
	 * Title: performAdminHomePage() Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully takes the user to the Admin Homepage.
	 * </p>
	 */
	protected static void performAdminHomePage() {
		
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewUserList.theStage,
				ViewUserList.getTheUser());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performReturn() Method. </p>
	 * 
	 * <p> Description: Protected method that allows user to return to the previous page.
	 * </p>
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewUserList.theStage,
				ViewUserList.getTheUser());
	}
	/**********
	 * <p> 
	 * 
	 * Title: performLogout() Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully logs the user out of the account and takes them to the login page.
	 * </p>
	 */
	
	
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewUserList.theStage);
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
