package guiUserUpdate;

import guiTools.ModelInputValidator;

//Validates input for user updating account information


public class ModelUserUpdate {
	/*-********************************************************************************************

	This section contains methods for the validation of first, middle, and last names

	**********************************************************************************************/
	public static String nameErrorMessage = "";		// The error message text
	public static String firstNameInput = "";			// The input being processed
	public static String middleNameInput = "";
	public static String lastNameInput = "";
	public static int nameIndexofError = -1;		// The index where the error was located
	
	// flags for names
	public static boolean nameFoundLongEnough = false;
	public static boolean nameFoundShortEnough = false;
	public static boolean nameAlphaOnly = false;
	
	// Validate Name
	// nameTypeFlag indicates what type of name is being validated
	// 0 = middle name or preferred first name (min length of 0)
	// 1 = first name or last name (min length of 1)
	public static boolean validateName(String nameInput, int nameTypeFlag) {
		String validNameSyntax = "^[A-Za-z]*$";
		boolean isValid = false;
		
		// If name does not consist of only letters, return name is not valid
		if(!nameInput.matches(validNameSyntax)) {
			nameAlphaOnly = false;
			return false;
		}

		nameAlphaOnly = true;
		// validates length of input
		int length = nameInput.length();
		
		switch(nameTypeFlag) {
			// middle name and preferred first name should be between 0-16 characters
			case 0:
				if((length >= 0 && length <= 16) || (nameInput == null))
			    	isValid = true;
				return isValid;
			
			// first name & last name should be between 1-16 characters
			case 1: 
				if(length >= 1 && length <= 16) 
					isValid = true;
				return isValid;

			default:
				return false;	
		
		}
		
	}
	
	public static String nameErrorMessages(boolean alphaOnly, boolean shortEnough, boolean longEnough) {
		return "";
	}
	
	public static String caseCorrectName(String nameInput) {
		String caseCorrectedName = "";
		
		if((nameInput != null) && (nameInput.length() != 0)) {
			nameInput = nameInput.toLowerCase();
			caseCorrectedName = nameInput.substring(0, 1).toUpperCase() + nameInput.substring(1);
		}
		
		return caseCorrectedName;
	}
	
	public static boolean validateEmail(String emailInput) {
		boolean emailIsValid = false;
		emailIsValid = ModelInputValidator.checkForValidEmail(emailInput);
		return emailIsValid;
	}
	
}