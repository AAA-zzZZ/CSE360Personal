package inputValidationTestbedMain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InputValidationTestingAutomation
 * Aditya's Branch
 * Semi-automated console testbed for input validators:
 *  - Username
 *  - Password
 *  - Email
 *  - First / Middle / Last / Preferred First Name
 *
 * Contract:
 *   - Validator returns "" when input is VALID.
 *   - Validator returns a non-empty error message when INVALID.
 *
 * Output:
 *   - Each test prints Result: VALID or Result: INVALID
 *   - At the end, summary of passed/failed tests.
 */
public class InputValidationTestingAutomation {

    enum InputType {
        USERNAME, PASSWORD, EMAIL,
        FIRST_NAME, MIDDLE_NAME, LAST_NAME, PREFERRED_FIRST_NAME
    }

    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        // -------- Report header --------
        System.out.println("______________________________________");
        System.out.println("\nInput Validation Testing Automation\n");

        // ===== USERNAME tests =====
        run(1,  InputType.USERNAME, "alex", true);  // valid
        run(2,  InputType.USERNAME, "1alex", false);  // invalid: starts with digit
        run(3,  InputType.USERNAME, "a", false);  // invalid: too short
        run(4,  InputType.USERNAME, "alex.smith", true);
        run(5,  InputType.USERNAME, "alex__smith", false); //consecutive specials not allowed
        run(6,  InputType.USERNAME, "alex-", false);  //invalid: trailing special
        run(7,  InputType.USERNAME, "Alex-42", true);
        run(8,  InputType.USERNAME, "veryVeryLongUserName", false); // invalid: >16

        // ===== PASSWORD tests =====
        run(20, InputType.PASSWORD, "Aa1!aaaa", true);  // valid
        run(21, InputType.PASSWORD, "aaaaaaa", false);  // too short, missing rules
        run(22, InputType.PASSWORD, "AAAAAAAA1!", false);  // no lowercase
        run(23, InputType.PASSWORD, "aaaaaaa1!", false); // no uppercase
        run(24, InputType.PASSWORD, "Aaaaaaaa!", false);  // no digit
        run(25, InputType.PASSWORD, "Aaaaaaaa1", false);  // no special
        run(26, InputType.PASSWORD, "A1!aaaaaA1!aaaaaA1!aaaaaA1!aaa", true);  // valid near max length


        // ===== EMAIL tests =====
        run(40, InputType.EMAIL, "a@b.co", true);  // valid
        run(41, InputType.EMAIL, ".a@b.com", false);  // invalid: starts with special
        run(42, InputType.EMAIL, "a..b@c.com", false);  // invalid: consecutive dots
        run(43, InputType.EMAIL, "a@b", false);  // invalid: no dot in domain
        run(44, InputType.EMAIL, "a@-b.com", false);  // invalid: label starts with hyphen
        run(45, InputType.EMAIL, "First.Last+tag@sub-domain.example.com", true);  // valid complex

        // ===== NAME tests =====
        run(60, InputType.FIRST_NAME, "Alex", true);  // valid
        run(61, InputType.FIRST_NAME, "", false);  // invalid: first name required
        run(62, InputType.MIDDLE_NAME, "", true);  // valid: middle name optional
        run(63, InputType.MIDDLE_NAME, "Christopher", true); // valid
        run(64, InputType.LAST_NAME, "Ng", true);
        run(65, InputType.LAST_NAME, "O'Neil", false);  // invalid: only letters allowed
        run(66, InputType.PREFERRED_FIRST_NAME, "", true); 
        run(67, InputType.PREFERRED_FIRST_NAME, "ANameThatIsOverThirtyTwoCharactersLong", false);  // invalid: too long

        // -------- Report footer --------
        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    private static void run(int caseNo, InputType type, String input, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + caseNo);
        System.out.println("Type: " + type);
        System.out.println("Input: \"" + input + "\"");
        System.out.println("______________");

        // Call validator
        String raw;
        switch (type) {
            case USERNAME:
                raw = Validators.evaluateUsername(input);
                break;
            case PASSWORD:
                raw = Validators.evaluatePassword(input);
                break;
            case EMAIL:
                raw = Validators.evaluateEmail(input);
                break;
            case FIRST_NAME:
                raw = Validators.evaluateFirstName(input);
                break;
            case MIDDLE_NAME:
                raw = Validators.evaluateMiddleName(input);
                break;
            case LAST_NAME:
                raw = Validators.evaluateLastName(input);
                break;
            case PREFERRED_FIRST_NAME:
                raw = Validators.evaluatePreferredFirstName(input);
                break;
            default:
                raw = "Unknown input type";
                break;
        }

        String result = (raw != null) ? raw : "";

        // Print result
        if (!result.isEmpty()) { // INVALID
            System.out.println("Result: INVALID");
            System.out.println("Error message: " + result);
            if (expectedPass) numFailed++;
            else numPassed++;
        } else { // VALID
            System.out.println("Result: VALID");
            if (expectedPass) numPassed++;
            else numFailed++;
        }
    }

    // ================= VALIDATORS =================
    public static final class Validators {

        // ===== USERNAME =====
        private static final Pattern USERNAME_PATTERN =
                Pattern.compile("^[A-Za-z][A-Za-z0-9]*(?:[._-][A-Za-z0-9]+)*$");

        public static String evaluateUsername(String s) {
            if (s == null) return "Username is required";
            int len = s.length();
            if (len < 4 || len > 16) return "Username must be 4–16 characters";
            if (!USERNAME_PATTERN.matcher(s).matches())
                return "Username must start with a letter; specials . _ - allowed only between letters/digits";
            return "";
        }

        // ===== PASSWORD =====
        private static final String SPECIALS = "~`!@#$%^&*()_\\-+{}\\[\\]|:,.?/";
        private static final Pattern PASSWORD_ALLOWED =
                Pattern.compile("^[A-Za-z0-9" + SPECIALS + "]{8,32}$");
        private static final Pattern HAS_UPPER = Pattern.compile("[A-Z]");
        private static final Pattern HAS_LOWER = Pattern.compile("[a-z]");
        private static final Pattern HAS_DIGIT = Pattern.compile("[0-9]");
        private static final Pattern HAS_SPECIAL = Pattern.compile("[" + SPECIALS + "]");

        public static String evaluatePassword(String s) {
            if (s == null) return "Password is required";
            int len = s.length();
            if (len < 8 || len > 32) return "Password must be 8–32 characters";
            if (!PASSWORD_ALLOWED.matcher(s).matches())
                return "Password contains an invalid character";
            if (!HAS_UPPER.matcher(s).find())
                return "Password must include at least one uppercase letter";
            if (!HAS_LOWER.matcher(s).find())
                return "Password must include at least one lowercase letter";
            if (!HAS_DIGIT.matcher(s).find())
                return "Password must include at least one digit";
            if (!HAS_SPECIAL.matcher(s).find())
                return "Password must include at least one special character";
            return "";
        }

        // ===== EMAIL =====
        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^([A-Za-z0-9]+(?:[._+-][A-Za-z0-9]+)*)@([A-Za-z0-9]+(?:-[A-Za-z0-9]+)*(?:\\.[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*)+)$");

        public static String evaluateEmail(String s) {
            if (s == null) return "Email is required";
            int len = s.length();
            if (len < 5 || len > 100) return "Email must be 5–100 characters";

            int atIdx = s.indexOf('@');
            if (atIdx <= 0 || atIdx != s.lastIndexOf('@'))
                return "Email must contain exactly one '@'";

            String local = s.substring(0, atIdx);
            String domain = s.substring(atIdx + 1);

            if (local.length() < 1 || local.length() > 50)
                return "Email address (before @) must be 1–50 characters";

            Matcher m = EMAIL_PATTERN.matcher(s);
            if (!m.matches()) {
                if (!local.matches("^[A-Za-z0-9].*[A-Za-z0-9]$"))
                    return "Email address must begin and end with a letter or digit";
                if (!local.matches("^[A-Za-z0-9]+(?:[._+-][A-Za-z0-9]+)*$"))
                    return "Email address may use . _ - + only between letters/digits";
                if (!domain.contains("."))
                    return "Email domain must contain at least one '.'";
                if (!domain.matches("^[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*(?:\\.[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*)+$"))
                    return "Email domain labels may not start or end with '-'";
                return "Email format is invalid";
            }
            return "";
        }

        // ===== NAMES =====
        private static final Pattern LETTERS_ONLY = Pattern.compile("^[A-Za-z]+$");

        public static String evaluateFirstName(String s) {
            if (s == null) return "First name is required";
            int len = s.length();
            if (len < 1 || len > 32) return "First name must be 1–32 characters";
            if (!LETTERS_ONLY.matcher(s).matches()) return "First name may contain only letters A–Z";
            return "";
        }

        public static String evaluateMiddleName(String s) {
            if (s == null) return "Middle name is required (use empty string if none)";
            int len = s.length();
            if (len == 0) return ""; // allowed
            if (len > 32) return "Middle name must be 0–32 characters";
            if (!LETTERS_ONLY.matcher(s).matches()) return "Middle name may contain only letters A–Z";
            return "";
        }

        public static String evaluateLastName(String s) {
            if (s == null) return "Last name is required";
            int len = s.length();
            if (len < 1 || len > 32) return "Last name must be 1–32 characters";
            if (!LETTERS_ONLY.matcher(s).matches()) return "Last name may contain only letters A–Z";
            return "";
        }

        public static String evaluatePreferredFirstName(String s) {
            if (s == null) return "Preferred first name is required (use empty string if none)";
            int len = s.length();
            if (len == 0) return ""; // allowed
            if (len > 32) return "Preferred first name must be 0–32 characters";
            if (!LETTERS_ONLY.matcher(s).matches()) return "Preferred first name may contain only letters A–Z";
            return "";
        }
    }
}
