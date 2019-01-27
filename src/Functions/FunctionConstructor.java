//----------------------FUNCTION CONSTRUCTOR CLASS----------------------------//
//@author TitanJack
//@project MathTools
//An expression parser, converts an function expression given by the user in the
//form of a string into function objects using a recursive algorithm

package Functions;

import java.util.ArrayList;
import java.util.Stack;

public class FunctionConstructor {

    private boolean DEBUG = false;
    private boolean DEBUG_indices = true;

    private final String[] trigFuncs = {
            "sin", "cos", "tan", "csc", "sec", "cot", "arcsin", "arccos",
            "arctan", "arccsc", "arcsec", "arccot", "sinh", "cosh", "tanh",
            "csch", "sech", "coth", "arcsinh", "arccosh", "arctanh", "arccsch",
            "arcsech", "arccoth",
    };

    private final String[] mathConstants = {"pi", "e"};

    //---------------------------CORE FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public Function toFunction(String funcStr)
    //public Function toFunctionDebug(String funcStr)
    //private Function toFunction(String funcStr, String DEBUG_spacing)

    //Function: To Function (Side method)
    //@param funcStr        String function expression
    //@return               A function object
    //Calls the "main" toFunction method
    @SuppressWarnings ({"unused", "WeakerAccess"})
    public Function toFunction(String funcStr) {
        funcStr = removeOccurrences(funcStr, " ");
        return toFunction(funcStr, "");
    }

    //Function: To Function Debug
    //@param funcStr        String function expression
    //@return               A function object
    //Activates debug mode (prints out function at each step) and calls the
    //"main" toFunction method
    @SuppressWarnings ("unused")
    public Function toFunctionDebug(String funcStr) {
        DEBUG = true;
        DEBUG_indices = false;
        System.out.println("----------------------DEBUG----------------------");
        Function result = toFunction(funcStr);
        System.out.println("-------------------------------------------------");
        return result;
    }

    //Function: To Function (Main method)
    //@param funcStr        String function expression
    //@param DEBUG_spacing  Used in debug mode to indent the output to a tree
    //                      structure
    //@return               A function object
    //Uses a recursive divide and conquer approach to create the function object
    //For example, the expression <e^((x+1)/(x-1))> can be described as:
    //1. An exponential function with base: <e> and exponent: <(x+1)/(x-1)>
    //   so insert <e> and <(x+1)/(x-1)> back into the toFunction method
    //   to be processed individually
    //2. Inside the base function we find a math constant e, so we're done
    //3. Inside the exponent function, we find a rational function with
    //   numerator <x+1> and denominator <x-1>
    //4. For <x+1>, we find a sum function with terms <x> and <1>
    //5. <x> is a variable, so done, <1> is a constant, so done
    //6. Similar approach is taken with <x-1>
    private Function toFunction(String funcStr, String DEBUG_spacing) {

        //Debug output
        if (DEBUG) {
            System.out.println(DEBUG_spacing + funcStr);
            DEBUG_spacing += "  | ";
        }

        if (funcStr.length() == 0) {
            System.err.println("Expression has invalid form");
            System.exit(0);
        }

        if (funcStr.length() == 1 && !eqlsAny(funcStr.charAt(0),
                "1234567890abcdefghijklmnopqrstuvwxyz")) {
            System.err.println("Expression has invalid form");
            System.exit(0);
        }

        //Check for sum function, eg. x^2+2x+1
        ArrayList<Integer> sumIndex = findOutsideOfBrackets(funcStr, "sum",
                null);
        if (sumIndex.size() != 0) {
            return new SumFunction(getSubFunctions(funcStr, sumIndex,
                    DEBUG_spacing));
        }

        //Check for geometric function, eg. (x+1)(x-1)
        ArrayList<Integer> geoIndex = findOutsideOfBrackets(funcStr, "geo",
                null);
        if (geoIndex.size() != 0) {
            return new GeoFunction(getSubFunctions(funcStr, geoIndex,
                    DEBUG_spacing));
        }

        //Check for rational function, eg. (x+1)/(x-1)
        ArrayList<Integer> divideIndex = findOutsideOfBrackets(funcStr, "/");
        if (divideIndex.size() != 0) {
            return new Rational(toFunction(funcStr.substring(0, divideIndex.get
                  (0)), DEBUG_spacing), toFunction(funcStr.substring
                    (divideIndex.get(0) + 1), DEBUG_spacing));
        }

        //Check for exponents, eg. (x+1)^(x-1)
        ArrayList<Integer> exponentIndex = findOutsideOfBrackets(funcStr, "^");
        if (exponentIndex.size() != 0) {
            return new Exponential(toFunction(funcStr.substring(0,
                    exponentIndex.get(0)), DEBUG_spacing), toFunction(funcStr.
                    substring(exponentIndex.get(0) + 1), DEBUG_spacing));
        }

        //Remove brackets, eg. (x+5) -> x+5
        if (eqlsAny(funcStr.charAt(0), "([{<")) {
            assertHasMatchingBrackets(funcStr);
            if (eqlsAny(funcStr.charAt(funcStr.length() - 1),
                    ">}])"))
                return toFunction(funcStr.substring(1, funcStr.length()
                        - 1), DEBUG_spacing);

        }

        //Check for leading negative sign, eg. -(x+1)
        if (funcStr.length() >= 2 && funcStr.charAt(0) == '-') {
            return toFunction(funcStr.substring(1), DEBUG_spacing).setCoeff(-1);
        }

        //Check for constant value
        if (isNum(funcStr.charAt(0))) {
            int numEndIndex = getNumEndIndex(funcStr);
            double num = toNumberDouble(funcStr.substring(0, numEndIndex));
            if (numEndIndex == funcStr.length()) {
                return new Constant(num);
            }
        }

        //Check for special functions: trigonometric, logarithmic, square root
        if (isLetter(funcStr.charAt(0))) {
            int wordEndIndex = getWordEndIndex(funcStr);
            String word = funcStr.substring(0, wordEndIndex);
            if (funcStr.length() >= word.length() + 3 && eqlsOpenBrackets(
                funcStr.charAt(word.length())) && eqlsClosedBrackets(funcStr.
                charAt(funcStr.length() - 1))) {
                String innerFunc = funcStr.substring(word.length() + 1,
                        funcStr.length() - 1);
                if (strEqlsAny(word, trigFuncs)) {
                    return new Trigonometric(word, toFunction(innerFunc, ""));
                }
                if (word.equals("sqrt")) {
                    return new Exponential(toFunction(innerFunc, DEBUG_spacing),
                            new Constant(0.5));
                }
                if (word.equals("log")) {
                    return new Logarithmic(toFunction(innerFunc,
                            DEBUG_spacing));
                }
            }
        }

        //Check for variable "x"
        if(funcStr.equals("x")) return new Variable(1);

        //Check for math constants: pi, e
        if (strEqlsAny(funcStr, mathConstants))
            return new MathConstant(funcStr);

        //Check for abrivated form: sinx instead of sin(x)
        if (strEqlsAny(funcStr, trigFuncs, new String[]{"x"}))
            return new Trigonometric(funcStr.substring(0, funcStr.length() - 1),
                    new Variable());
        if (funcStr.equals("logx"))
            return new Logarithmic(new Variable());
        if (funcStr.equals("sqrtx")) {
            return new Exponential(new Variable(), new Constant(0.5));
        }

        System.err.println("Sorry, the parser is not equipped to" +
                " handle this expression");
        System.exit(0);

        return null;
    }

    //--------------------------UTILITY FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //private Function[] getSubFunctions(String funcStr, ArrayList<Integer>
    //                                     splitIndicies, String DEBUG_spacing)
    //private ArrayList<Integer> findOutsideOfBrackets(String str,
    //                                                        String searchStr)
    //private ArrayList<Integer> findOutsideOfBrackets(String str, String type,
    //                                                      String[] searchStr)

    //Function: getSubFunctions
    //@param funcStr        string function expression
    //       splitIndices   the indices where the function string should be
    //                      split eg. for <x+1>, split it at index of '+'
    //       DEBUG_spacing  Used in debug mode to indent the output to a tree
    //                      structure
    //@return               The sub functions in object form
    //Splits the initial function expression and constructs individual function
    //objects for each expression
    private Function[] getSubFunctions(String funcStr, ArrayList<Integer>
                                          splitIndices, String DEBUG_spacing) {
        //Special case where the substring must be taken differently
        int substrOffset = 0;
        double coeff = 1;
        int subFuncsLength = splitIndices.size() + 1;
        Function[] subFunctions = new Function[subFuncsLength];
        //When splitting functions, we must consider <(x+1)(x-1)> and
        //<(x+1)*(x-1)> which needs to be split differently and that is what the
        //offset is for
        if (!eqlsAny(funcStr.charAt(splitIndices.get(0)), "+-/^*"))
            substrOffset = 1;
        subFunctions[0] = toFunction(funcStr.substring(0,
                splitIndices.get(0) + substrOffset), DEBUG_spacing);
        for (int i = 1; i < subFuncsLength - 1; i++) {
            if (!eqlsAny(funcStr.charAt(splitIndices.get(i)), "+-/^*"))
                substrOffset = 1;
            else substrOffset = 0;
            if (funcStr.charAt(splitIndices.get(i - 1)) == '-') coeff = -1;
            else coeff = 1;
            subFunctions[i] = toFunction(funcStr.substring(
                    splitIndices.get(i - 1) + 1, splitIndices.get(i)
                            + substrOffset), DEBUG_spacing).setCoeff(coeff);
        }
        if (funcStr.charAt(splitIndices.get(subFuncsLength - 2)) == '-')
            coeff = -1;
        subFunctions[subFuncsLength - 1] = toFunction(funcStr.substring(
                splitIndices.get(subFuncsLength - 2) + 1), DEBUG_spacing).
                setCoeff(coeff);
        return subFunctions;
    }


    //Function: Find Outside of Brackets
    //@param str            The string that will be searched
    //       searchStr      The string that contains the search term
    //@return               An array of indices where the term has been found
    //Finds all occurrences of the term from <searchStr> outside of brackets
    //and records them in an array list
    private ArrayList<Integer> findOutsideOfBrackets(String str,
                                                             String searchStr) {
        return findOutsideOfBrackets(str, "", new String[]{searchStr});
    }

    //Function: Find Outside of Brackets
    //@param str            The string that will be searched
    //       type           Special search request for "sum", "geo", or leave
    //                      blank for general search with <searchStr>
    //       searchStr      The string array that contains the search terms
    //@return               An array of indices where the term has been found\
    //If "sum", finds all occurrences of addition or subtraction outside of
    //brackets. If "geo", finds all occurrences of multiplication, eg. 3x or
    //(x+1)(x-1) or pi*x. Else find occurrences of terms specified in
    //<searchStr>. All indices are added to an array list and returned
    private ArrayList<Integer> findOutsideOfBrackets(String str, String type,
                                                           String[] searchStr) {
        ArrayList<Integer> indices = new ArrayList<>();
        assertHasMatchingBrackets(str);
        //Uses stack algorithm similar to method <hasMatchingBrackets>
        Stack<Character> brackets = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            if (eqlsClosedBrackets(str.charAt(i))) brackets.pop();
            if (eqlsOpenBrackets(str.charAt(i))) brackets.push(str.charAt(i));
            //Empty stack means current index is not surrounded by brackets
            if (brackets.empty()) {
                if (type.equals("sum")) {
                    //Corner cases: <(x+1)/-(x-1)>, '-' should not trigger
                    if (i != 0 && (str.charAt(i) == '+'
                            || (str.charAt(i) == '-' && !eqlsAny(str.charAt
                            (i - 1), "^/"))))
                        indices.add(i);
                } else if (type.equals("geo")) {
                    //Detect cases like: <x(x+1)>, <3x>, <x*x>, <xx>, etc
                    if (str.charAt(i) == '*') indices.add(i);
                    if (i < str.length() - 1) {
                        char nextChr = str.charAt(i + 1);
                        if (isNum(str.charAt(i))) {
                            if (eqlsOpenBrackets(nextChr) || nextChr == 'x')
                                indices.add(i);
                        }
                        if (str.charAt(i) == 'x' || eqlsClosedBrackets(
                                str.charAt(i))) {
                            if (eqlsOpenBrackets(nextChr) || nextChr == 'x' ||
                                    isNum(nextChr))
                                indices.add(i);
                        }
                    }
                } else
                    for (String search : searchStr) {
                        if (i + search.length() < str.length() &&
                                str.substring(i, i + search
                                        .length()).equals(search)) {
                            indices.add(i);
                        }
                    }
            }
        }

        if (DEBUG_indices)
            if (indices.size() != 0) {
                System.out.print("Indices: ");
                for (Integer index : indices) System.out.print(index + " ");
                System.out.println();
            }

        return indices;
    }

    //--------------------------ASSIST FUNCTIONS------------------------------//
    //FUNCTION LIST:
    //private boolean hasMatchingBrackets(String str)
    //private void assertHasMatchingBrackets(String str)
    //private boolean isNum(char chr)
    //private int getNumEndIndex(String numStr)
    //private double toNumberDouble(String str)
    //private boolean isLetter(char chr)
    //private int getWordEndIndex(String wordStr)
    //private boolean strEqlsAny(String str, String[] strsToCompare)
    //private boolean strEqlsAny(String str, String[] strsToCompare,
    //                                                      String[] extraStrs)
    //private boolean strHasAny(String str, String charsToCompare)
    //private boolean eqlsOpenBrackets(char chr)
    //private boolean eqlsClosedBrackets(char chr)
    //private boolean eqlsAny(char chr, String charsToCompare)
    //private char getOpposite(char chr)
    //private String removeOccurrences(String str, String sub)

    //Function: Has Matching Brackets
    //@param str            The string to be searched for matching brackets
    //@return               Whether or not <str> has matching brackets
    //Implements stack algorithm:
    //If open bracket, add bracket to stack
    //If closed bracket and top of stack is open bracket, pop open bracket
    //from stack
    //If stack is not empty by the end, brackets do not match
    //Other example found at:
    //www.geeksforgeeks.org/check-for-balanced-parentheses-in-an-expression/
    private boolean hasMatchingBrackets(String str) {
        Stack<Character> brackets = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            if (eqlsClosedBrackets(str.charAt(i))) {
                if (brackets.isEmpty() || getOpposite(brackets.peek())
                        != str.charAt(i))
                    return false;
                else
                    brackets.pop();
            }
            if (eqlsOpenBrackets(str.charAt(i))) brackets.push(str.charAt(i));
        }
        return brackets.isEmpty();
    }

    //Function: Assert Has Matching Brackets
    //@param str            String to be searched for matching brackets
    //Stops the program if <str> does not have matching brackets
    private void assertHasMatchingBrackets(String str) {
        if (!hasMatchingBrackets(str)) {
            System.err.print("Expression does not have matching brackets");
            System.exit(0);
        }
    }

    //Function: Is Number
    //@param chr            The character to be checked for number
    //@return               Whether the character is a number
    //Check if ASCII value of character is within number range
    private boolean isNum(char chr) {
        return (chr >= 48 && chr <= 57);
    }

    //Function: Get Number End Index
    //@param numStr         The string containing the number
    //@return               The ending digit of the number in the string
    //Given that the first index of the string must be a number, the method
    //finds how many digits there are in the number and returns the last digit
    private int getNumEndIndex(String numStr) {
        for (int i = 0; i < numStr.length(); i++) {
            if (!isNum(numStr.charAt(i)) && eqlsAny(numStr.charAt(i),
                    ".+-")) return i;
        }
        return numStr.length();
    }

    /*private int getNumStartIndex(String numStr) {
        for (int i = numStr.length() - 1; i >= 0; i--) {
            if (!isNum(numStr.charAt(i)) && eqlsAny(numStr.charAt(i),
                    ".+-")) return i;
        }
        return 0;
    }*/

    //Function: To Number Double
    //@param str            The number in the form of a string
    //@return               Double number which is parsed from string
    //Parses string to double, if it cannot be converted, stop the program
    private double toNumberDouble(String str) {
        double num = 0;
        try {
            num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            System.err.println("The number in the string is invalid");
            e.printStackTrace();
            System.exit(0);
        }
        return num;
    }

    //Function: Is Letter
    //@param chr            The character to be check for letter
    //@return               Whether the character is a letter
    //Check if ASCII value of character is within the lowercase letter range
    private boolean isLetter(char chr) {
        return (chr >= 97 && chr <= 122);
    }

    private int getWordEndIndex(String wordStr) {
        for (int i = 0; i < wordStr.length(); i++) {
            if (!isLetter(wordStr.charAt(i))) return i;
        }
        return wordStr.length();
    }

    //Function: String Equals Any
    //@param str            Check if this string equals any in <strsToCompare>
    //       strsToCompare  Strings to be compared
    //@return               Whether <str> equals any in <strsToCompare>
    private boolean strEqlsAny(String str, String[] strsToCompare) {
        return strEqlsAny(str, strsToCompare, new String[]{""});
    }

    //Function: String Equals Any
    //@param str            Check if this string equals any in <strsToCompare>
    //       strsToCompare  Strings to be compared
    //       extraStrs      To be added behind each string in <strsToCompare>
    //@return               Whether <str> equals any in <strsToCompare>
    private boolean strEqlsAny(String str, String[] strsToCompare,
                                                           String[] extraStrs) {
        for (String strCompare : strsToCompare)
            for (String extraStr : extraStrs)
                if (str.equals(strCompare + extraStr)) return true;
        return false;
    }

    //Function: String Has Any
    //@param str            String where the characters will be checked
    //       charsToCompare Chacracters to be compared to characters in <str>
    //@return               If any character in <charsToCompare> equals any
    //                      character in <str>
    private boolean strHasAny(String str, String charsToCompare) {
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < charsToCompare.length(); j++) {
                if (str.charAt(i) == charsToCompare.charAt(j)) return true;
            }
        }
        return false;
    }

    //Function: Equals Open Brackets
    //@param chr            The character to be checked for an open bracket
    //@return               If character match an open bracket
    private boolean eqlsOpenBrackets(char chr) {
        return strHasAny(chr + "", "([{<");
    }

    //Function: Equals Closed Brackets
    //@param chr            The character to be checked for a closed bracket
    //@return               If character match a closed bracket
    private boolean eqlsClosedBrackets(char chr) {
        return strHasAny(chr + "", ">}])");
    }

    //Function: Equals Any
    //@param chr            The character to be checked
    //       charsToCompare Characters to be compared to the character in <chr>
    //@return               Whether <chr> equals any in <charsToCompare>
    private boolean eqlsAny(char chr, String charsToCompare) {
        return strHasAny(chr + "", charsToCompare);
    }

    //Function: Get Opposite
    //@param chr            The character which contains a bracket
    //@return               The opposite bracket
    //If character is "(", then the method will return ")"
    private char getOpposite(char chr) {
        if (eqlsAny(chr, "([{<>}])"))
            return "([{<>}])".charAt(
                    ")]}><{[(".indexOf(chr));
        else return 0;
    }

    //Function: Remove Occurrences
    //@param str            The string where occurrences of <sub> are to be
    //                      removed
    //       sub            The snippet of string to be remove from <str>
    //@return               The string with all occurrences of <sub> removed
    private String removeOccurrences(String str, String sub) {
        if (str.length() < sub.length()) return str;
        if (str.substring(0, sub.length()).equals(sub))
            return removeOccurrences(str.substring(sub.length()), sub);
        return str.charAt(0) + removeOccurrences(str.substring(1), sub);
    }
}
