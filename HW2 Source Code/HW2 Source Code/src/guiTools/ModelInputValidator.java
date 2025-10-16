package guiTools;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*-********************************************************************************************

The Model for guiTools 

**********************************************************************************************/

/**********
 * <p> Title: ModelInputValidation Class</p>
 * 
 * <p> Description: This class is called upon whenever input validation is needed.
 * This class validates input for user username, password, email, first name, middle name, and last name. </p>
 *
 */

public class ModelInputValidator {
	
	// general input error display message
	protected static Alert updateInputInvalidAlert = new Alert(AlertType.INFORMATION);
	
	public static void displayUpdateInputInvalidAlert() {

		updateInputInvalidAlert.setTitle("Input is invalid!");
		updateInputInvalidAlert.setHeaderText("Input is invalid!");
		updateInputInvalidAlert.setContentText("Please review the requirements and try again.");
		updateInputInvalidAlert.showAndWait();
		
	}
	/*-********************************************************************************************

	This section contains methods for the validation of usernames

	**********************************************************************************************/

	public static String userNameRecognizerErrorMessage = "";	// The error message text
	public static String userNameRecognizerInput = "";			// The input being processed
	public static int userNameRecognizerIndexofError = -1;		// The index of error location
	private static int state = 0;							// The current state value
	private static int nextState = 0;						// The next state value
	private static boolean finalState = false;				// Is this state a final state?
	private static String inputLine = "";					// The input line
	private static char currentChar;						// The current character in the line
	private static int currentCharNdx;						// The index of the current character
	private static boolean running;							// The flag that specifies if the FSM is 
															// running
	private static int userNameSize = 0;					// A numeric value may not exceed 16 characters

	
	private static Alert usernameErrorAlert = new Alert(AlertType.INFORMATION);
	
	// Private method to display debugging data
	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
				((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
				((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
				nextState + "     " + userNameSize);
	}
	
	// Private method to move to the next character within the limits of the input line
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}
	

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if every things is okay or it is a String
	 * 						with a helpful description of the error
	 */
	public static String checkForValidUserName(String input) {
		// Check to ensure that there is input to process
		if(input.length() <= 0) {
			userNameRecognizerIndexofError = 0;	// Error at first character;
			return "\n*** ERROR *** The input is empty";
		}
		
		// The local variables used to perform the Finite State Machine simulation 
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		currentChar = input.charAt(0);		// The current character from above indexed position

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		userNameRecognizerInput = input;	// Save a copy of the input
		running = true;						// Start the loop
		nextState = -1;						// There is no next state
		System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");
		
		// This is the place where semantic actions for a transition to the initial state occur
		
		userNameSize = 0;					// Initialize the UserName size

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {

			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 0: 
				// State 0 has 1 valid transition that is addressed by an if statement.
				
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 1
				
				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
					(currentChar >= 'a' && currentChar <= 'z' )) {		// Check for a-z
					nextState = 1;
					
					// Count the character 
					userNameSize++;
					
					
					// This only occurs once, so there is no need to check for the size getting
					// too large.
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;
				
				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has two valid transitions, 
				//	1: a A-Z, a-z that transitions back to state 1 (modified this comment to reflect changes in FSM)
				//  2: a period that transitions to state 2 

				
				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) || 	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) { // (Check for 0-9
					nextState = 1;
					
					// Count the character
					userNameSize++;
				}
				// . or - or _ -> State 2
				else if (currentChar == '.' || currentChar == '-' || currentChar == '_') {	
					nextState = 2;
					userNameSize++;
				}				
				// If it is none of those characters, the FSM halts
				else
					running = false;
				
				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					running = false;
				break;			
				
			case 2: 
				// State 2 deals with a character after a special character in the name.
				
				// A-Z, a-z, 0-9 -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
						(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
					nextState = 1;
					
					// Count the odd digit
					userNameSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;

				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					running = false;
				break;

			}
			
			
			if (running) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar.  If there is no next character the currentChar is
				// set to a blank.
				moveToNextCharacter();

				// Move to the next state
				state = nextState;
				
				// Is the new state a final state?  If so, signal this fact.
				if (state == 1) finalState = true;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again
	
		}
		displayDebuggingInfo();
		
		System.out.println("The loop has ended.");
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		userNameRecognizerIndexofError = currentCharNdx;	// Set index of a possible error;
		userNameRecognizerErrorMessage = "\n*** ERROR *** ";
		//userNameRecognizerErrorMessage = "";
		
		// The following code is a slight variation to support just console output.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			userNameRecognizerErrorMessage += "A Username must start with A-Z or a-z.\n";
			return userNameRecognizerErrorMessage;

		case 1:
			// State 1 is a final state.  Check to see if the UserName length is valid.  If so we
			// we must ensure the whole string has been consumed.

			if (userNameSize < 4) {
				// UserName is too small
				userNameRecognizerErrorMessage += "A Username must have at least 4 characters.\n";
				return userNameRecognizerErrorMessage;
			}
			else if (userNameSize > 16) {
				// UserName is too long
				userNameRecognizerErrorMessage += 
					"A Username must have no more than 16 characters.\n";
				return userNameRecognizerErrorMessage;
			}
			else if (currentCharNdx < input.length()) {
				// There are characters remaining in the input, so the input is not valid
				userNameRecognizerErrorMessage += 
					"A Username character may only contain the characters \nA-Z, a-z, 0-9, or special characters . _ -.\n";
				return userNameRecognizerErrorMessage;
			}
			else {
					// UserName is valid
					userNameRecognizerIndexofError = -1;
					userNameRecognizerErrorMessage = "";
					return userNameRecognizerErrorMessage;
			}

		case 2:
			// State 2 is not a final state, so we can return a very specific error message
			userNameRecognizerErrorMessage +=
				"A Username character after a period, minus, or \nunderscore must be A-Z, a-z, 0-9.\n";
			return userNameRecognizerErrorMessage;
			
		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return "idk what to put here ngl";
		}
	}
	
	public static void displayUsernameErrorMessages() {
	
		usernameErrorAlert.setTitle("Username is invalid");
		usernameErrorAlert.setHeaderText("Username does not satisfy following requirement(s):");
		usernameErrorAlert.setContentText(userNameRecognizerErrorMessage);
		usernameErrorAlert.showAndWait();

	}
	
	
	/*-********************************************************************************************

	This section contains methods for the validation of passwords

	**********************************************************************************************/
	
	public static String passwordErrorMessage = "";		// The error message text
	public static String passwordInput = "";			// The input being processed
	public static int passwordIndexofError = -1;		// The index where the error was located
	
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	public static boolean foundShortEnough = true;		
	
	private static Alert passwordErrorAlert = new Alert(AlertType.INFORMATION);
	public static String passwordErrorAlertMessage = "";
	
	private static Alert passwordDoesNotMatchErrorAlert = new Alert(AlertType.INFORMATION);
	public static String passwordDoesNotMatchErrorAlertMessage = "";

	/*
	 * This private method displays the input line and then on a line under it displays the input
	 * up to the point of the error.  At that point, a question mark is place and the rest of the 
	 * input is ignored. This method is designed to be used to display information to make it clear
	 * to the user where the error in the input can be found, and show that on the console 
	 * terminal.
	 * 
	 */

	private static void displayInputState() {
		// Display the entire input line
		System.out.println(inputLine);
		System.out.println(inputLine.substring(0,currentCharNdx) + "?");
		System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " + 
				currentCharNdx + "  |  The currentChar: \"" + currentChar + "\"");
	}
	
	
	/**********
	 * <p> Title: checkForValidPassword - Public Method </p>
	 * 
	 * <p> Description: This method is a mechanical transformation of a Directed Graph diagram 
	 * into a Java method. This method is used by both the GUI version of the application as well
	 * as the testing automation version.
	 * 
	 * @param input		The input string evaluated by the directed graph processing
	 * @return			An output string that is empty if every things is okay or it will be
	 * 						a string with a helpful description of the error follow by two lines
	 * 						that shows the input line follow by a line with an up arrow at the
	 *						point where the error was found.
	 */
	
	public static String checkForValidPassword(String input) {
		// The following are the local variable used to perform the Directed Graph simulation
		passwordErrorMessage = "";
		passwordIndexofError = 0;			// Initialize the IndexofError
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		
		if(input.length() <= 0) {
			return "*** Error *** The password is empty!";
		}
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		// The current character from the above indexed position

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state.  This
		// local variable is a working copy of the input.
		passwordInput = input;				// Save a copy of the input
		
		// The following are the attributes associated with each of the requirements
		foundUpperCase = false;				// Reset the Boolean flag
		foundLowerCase = false;				// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundSpecialChar = false;			// Reset the Boolean flag
		foundNumericDigit = false;			// Reset the Boolean flag
		foundLongEnough = false;			// Reset the Boolean flag
		foundShortEnough = true;			// Reset the Boolean flag
		
		// This flag determines whether the directed graph (FSM) loop is operating or not
		running = true;						// Start the loop

		// The Directed Graph simulation continues until the end of the input is reached or at some
		// state the current character does not match any valid transition
		while (running) {
			displayInputState();
			// The cascading if statement sequentially tries the current character against all of
			// the valid transitions, each associated with one of the requirements
			if (currentChar >= 'A' && currentChar <= 'Z') {
				System.out.println("Upper case letter found");
				foundUpperCase = true;
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				System.out.println("Lower case letter found");
				foundLowerCase = true;
			} else if (currentChar >= '0' && currentChar <= '9') {
				System.out.println("Digit found");
				foundNumericDigit = true;
			} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
				System.out.println("Special character found");
				foundSpecialChar = true;
			} else {
				passwordIndexofError = currentCharNdx;
				return "*** Error *** An invalid character has been found!";
			}
			if (currentCharNdx >= 7) {
				System.out.println("At least 8 characters found");
				foundLongEnough = true;
			}
			if (currentCharNdx > 32) {
				System.out.println("More than 32 characters found");
				foundShortEnough = false;
			}
			
			// Go to the next character if there is one
			currentCharNdx++;
			if (currentCharNdx >= inputLine.length())
				running = false;
			else
				currentChar = input.charAt(currentCharNdx);
			
			System.out.println();
		}
		
		// Construct a String with a list of the requirement elements that were found.
		String errMessage = "";
		if (!foundUpperCase)
			errMessage += "Upper case; ";
		
		if (!foundLowerCase)
			errMessage += "Lower case; ";
		
		if (!foundNumericDigit)
			errMessage += "Numeric digits; ";
			
		if (!foundSpecialChar)
			errMessage += "Special character; ";
			
		if (!foundLongEnough)
			errMessage += "Long Enough; ";
		
		if (!foundShortEnough)
			errMessage += "Short Enough; ";
		
		if (errMessage == "")
			return "";
		
		// If it gets here, there something was not found, so return an appropriate message
		passwordIndexofError = currentCharNdx;
		return errMessage + "conditions were not satisfied";
	}
	
	public static void displayPasswordErrorMessages(String password) {
		passwordErrorAlertMessage = "";

		if (!foundUpperCase) { passwordErrorAlertMessage += "Password must include an uppercase letter.\n"; }
		if (!foundLowerCase) { passwordErrorAlertMessage += "Password must include a lowercase letter.\n"; }
		if (!foundNumericDigit) { passwordErrorAlertMessage += "Password must include a numeric digit.\n"; }
		if (!foundSpecialChar) { passwordErrorAlertMessage += "Password must include a special character.\n"; }
		if (!foundLongEnough) { passwordErrorAlertMessage += "Password must be at least 8 characters.\n"; }
		if (!foundShortEnough) { passwordErrorAlertMessage += "Password must no more than 32 characters.\n"; }
		
		
		passwordErrorAlert.setTitle("Password is invalid");
		passwordErrorAlert.setHeaderText("Password does not satisfy following requirement(s):");
		passwordErrorAlert.setContentText(passwordErrorAlertMessage);
		passwordErrorAlert.showAndWait();
	}
	
	public static void displayPasswordDoesNotMatchErrorMessage() {
		passwordDoesNotMatchErrorAlertMessage = "";
		
		passwordDoesNotMatchErrorAlert.setTitle("Passwords do not match!");
		passwordDoesNotMatchErrorAlert.setHeaderText("The passwords must be identical to be valid");
		passwordDoesNotMatchErrorAlert.showAndWait();
		
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	/*-********************************************************************************************

	This section contains methods for the validation of emails

	**********************************************************************************************/
	
	/**********
	 * <p> Title: checkForValidEmail - Public Method </p>
	 * 
	 * <p> Description: This method validates the email input by first splitting the email input into two seconds
	 * (the user and the domain) using the '@' as the delimiter. Then, it verifies each section independently.
	 * If and only if both portions are valid, the email is deemed as valid and the method returns a true value.
	 * 
	 * @param input		The input string to be evaluated
	 * @return			Boolean value true for if the email input is found to be valid; false otherwise
	 */
	public static boolean checkForValidEmail(String emailInput) {
		String emailUserString = "";
		String emailDomainString = "";
		boolean emailUserIsValid = false;
		boolean emailDomainIsValid = false;
		
		// split the emailInput into the user part and the domain part
		// using @ as the delimiter
		// if emailInput does not have exactly one @,
		// the input will be invalid
		String[] emailSplit = emailInput.split("@");
		if(emailSplit.length != 2) {
			return false;
		}
		
		// assign each string to their respective values
		emailUserString = emailSplit[0];
		emailDomainString = emailSplit[1];
		
		// validate each independently
		emailUserIsValid = validateEmailUser(emailUserString);
		emailDomainIsValid = validateEmailDomain(emailDomainString);
		
		if(emailUserIsValid && emailDomainIsValid)
			return true;
		
		return false;
	}
	
	public static boolean validateEmailUser(String emailUserInput) {
		// the email user part has the same validation rules as the username validation
		// so the email user part is validated through the validateUsername method
		String errEmailUser = "";
		errEmailUser = checkForValidUserName(emailUserInput);
		
		if(errEmailUser == "" ) 
			return true;
					
		return false;
	}
	
	public static boolean validateEmailDomain(String emailDomainInput) {
		String validEmailDomainSyntax = "^[A-Za-z.]*$";
		//String validEmailDomainSyntax = "^[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$";
		
		if(emailDomainInput.matches(validEmailDomainSyntax)) 
			return true;
		
		return false;
	}
	
	
}