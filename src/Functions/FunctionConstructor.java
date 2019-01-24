package Functions;

import sun.nio.cs.ext.DoubleByte;

import java.util.ArrayList;
import java.util.Stack;

public class FunctionConstructor {

    private boolean DEBUG = false;
    private boolean DEBUG_indices = false;

    /*
    (x+1)x^(3x+2)(x+3)
    (x+1)(x+2)^(3x+1)(2x+1)
    3x^x
    3x(x+1)(x+4)
    */

    public Function toFunction(String funcStr) {
        return toFunction(funcStr, "");
    }

    public Function toFunctionDebug(String funcStr) {
        DEBUG = true;
        DEBUG_indices = false;
        System.out.println("----------------------DEBUG----------------------");
        Function result = toFunction(funcStr, "");
        System.out.println("-------------------------------------------------");
        return result;
    }

    public Function toFunction(String funcStr, String DEBUG_spacing) {

        System.out.println(DEBUG_spacing + funcStr);
        DEBUG_spacing += "  | ";

        if (funcStr.length() == 0) {
            System.err.println("Expression has invalid form");
            System.exit(0);
        }

        if (funcStr.length() == 1 && !strHasAny(funcStr.charAt(0) + "",
                "1234567890x")) {
            System.err.println("Expression has invalid form");
            System.exit(0);
        }

        //Check for sum function
        ArrayList<Integer> sumIndex = findOutsideOfBrackets(funcStr, "sum", null);
        if (sumIndex.size() != 0) {
            return new SumFunction(getSubFunctions(funcStr, sumIndex,
                    DEBUG_spacing));
        }

        //Check for geometric function
        ArrayList<Integer> geoIndex = findOutsideOfBrackets(funcStr, "geo", null);
        if (geoIndex.size() != 0) {
            return new GeoFunction(getSubFunctions(funcStr, geoIndex,
                    DEBUG_spacing));
        }

        //Check for rational function
        ArrayList<Integer> divideIndex = findOutsideOfBrackets(funcStr, "/");
        if (divideIndex.size() != 0) {
            return new Rational(toFunction(funcStr.substring(0, divideIndex.get
                  (0)), DEBUG_spacing), toFunction(funcStr.substring
                    (divideIndex.get(0) + 1), DEBUG_spacing));
        }

        //Check for exponents
        ArrayList<Integer> exponentIndex = findOutsideOfBrackets(funcStr, "^");
        if (exponentIndex.size() != 0) {
            return new Exponential(toFunction(funcStr.substring(0,
                    exponentIndex.get(0)), DEBUG_spacing), toFunction(funcStr.
                    substring(exponentIndex.get(0) + 1), DEBUG_spacing));
        }

        //Remove brackets (x+5) -> x+5
        if (strHasAny(funcStr.charAt(0) + "", "([{<")) {
            assertHasMatchingBrackets(funcStr);
            if (strHasAny(funcStr.charAt(funcStr.length() - 1) + "",
                    ">}])"))
                return toFunction(funcStr.substring(1, funcStr.length()
                        - 1), DEBUG_spacing);

        }

        //Check for coefficient in front or constant value
        if (isNum(funcStr.charAt(0))) {
            int numEndIndex = getNumEndIndex(funcStr);
            double num = toNumberDouble(funcStr.substring(0, numEndIndex));
            if (numEndIndex == funcStr.length()) {
                return new Constant(num);
            } else {
                /*if (funcStr.charAt(numEndIndex) == '(') {
                    return toFunction(funcStr.substring(numEndIndex))
                            .setCoeff(num);
                } else if (funcStr.charAt(numEndIndex) == 'x') {
                    return new Variable(num);
                }else {
                    System.err.println("Sorry, the parser is not equipped to" +
                            "handle this expression");
                    System.exit(0);
                }*/
            }
        }

        if(funcStr.equals("x")) return new Variable(1);

        System.err.println("Sorry, the parser is not equipped to" +
                "handle this expression");
        System.exit(0);

        return null;
    }

    //--------------------------UTILITY FUNCTIONS-----------------------------//

    public Function[] getSubFunctions(String funcStr, ArrayList<Integer>
            splitIndicies, String DEBUG_spacing) {
        int geoOffset = 0;
        int subFuncsLength = splitIndicies.size() + 1;
        Function subFunctions[] = new Function[subFuncsLength];
        if (!eqlsAny(funcStr.charAt(splitIndicies.get(0)), "+-/^*"))
            geoOffset = 1;
        subFunctions[0] = toFunction(funcStr.substring(0,
                splitIndicies.get(0) + geoOffset), DEBUG_spacing);
        for (int i = 1; i < subFuncsLength - 1; i++) {
            if (!eqlsAny(funcStr.charAt(splitIndicies.get(i)), "+-/^*"))
                geoOffset = 1;
            else geoOffset = 0;
            subFunctions[i] = toFunction(funcStr.substring(
                    splitIndicies.get(i - 1) + 1, splitIndicies.get(i)
                            + geoOffset), DEBUG_spacing);
        }
        if (!eqlsAny(funcStr.charAt(splitIndicies.get(subFuncsLength - 2)),
                "+-/^*"))
            geoOffset = 1;
        subFunctions[subFuncsLength - 1] = toFunction(funcStr.substring(
                splitIndicies.get(subFuncsLength - 2) + 1), DEBUG_spacing);
        return subFunctions;
    }

    public ArrayList<Integer> findOutsideOfBrackets(String str,
                                                           String searchStr) {
        return findOutsideOfBrackets(str, "", new String[]{searchStr});
    }

    public ArrayList<Integer> findOutsideOfBrackets(String str, String type,
                                                    String[] searchStr) {
        ArrayList<Integer> indices = new ArrayList<>();
        assertHasMatchingBrackets(str);

        Stack<Character> brackets = new Stack<>();
        int index = 0;
        int buffer = 0;
        while (index < str.length()) {
            if (strHasAny(str.charAt(index) + "", ">}])"))
                brackets.pop();
            if (strHasAny(str.charAt(index) + "", "([{<"))
                brackets.push(str.charAt(index));
            if (brackets.empty() && buffer == 0) {
                if (type.equals("sum")) {
                    if (index != 0 && (str.charAt(index) == '+'
                            || str.charAt(index) == '-'))
                        indices.add(index);
                } else if (type.equals("geo")) {
                    if (str.charAt(index) == '*') indices.add(index);
                    if (index < str.length() - 1) {
                        char nextChr = str.charAt(index + 1);
                        if (isNum(str.charAt(index))) {
                            if (eqlsOpenBrackets(nextChr) || nextChr == 'x')
                                indices.add(index);
                        }
                        if (str.charAt(index) == 'x' || eqlsClosedBrackets(
                                str.charAt(index))) {
                            if (eqlsOpenBrackets(nextChr) || nextChr == 'x' ||
                            isNum(nextChr))
                                indices.add(index);
                        }
                    }
                } else
                    for (int i = 0; i < searchStr.length; i++) {
                        if (index + searchStr[i].length() < str.length() &&
                                str.substring(index, index + searchStr[i]
                                        .length()).equals(searchStr[i])) {
                            indices.add(index);
                            //buffer = searchStr[i].length();
                        }
                    }
            }
            if (buffer != 0) buffer--;
            index++;
        }

        if (DEBUG_indices)
            if (indices.size() != 0) {
                System.out.print("Indices: ");
                for (int i = 0; i < indices.size(); i++)
                    System.out.print(indices.get(i) + " ");
                System.out.println();
            }

        return indices;
    }

    //--------------------------ASSIST FUNCTIONS------------------------------//

    public boolean hasMatchingBrackets(String str) {
        Stack<Character> brackets = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            if (strHasAny(str.charAt(i) + "", ">}])")) {
                if (brackets.isEmpty() || getOpposite(brackets.peek())
                        != str.charAt(i))
                    return false;
                else
                    brackets.pop();
            }
            if (strHasAny(str.charAt(i) + "", "([{<"))
                brackets.push(str.charAt(i));
        }
        if (!brackets.isEmpty()) return false;
        else return true;
    }

    public void assertHasMatchingBrackets(String str) {
        if (!hasMatchingBrackets(str)) {
            System.err.print("Expression does not have matching brackets");
            System.exit(0);
        }
    }

    private boolean isNum(char chr) {
        return (chr >= 48 && chr <= 57);
    }

    private int getNumEndIndex(String numStr) {
        for (int i = 0; i < numStr.length(); i++) {
            if (!isNum(numStr.charAt(i)) && strHasAny(numStr.charAt(i) + "",
                    ".+-")) return i;
        }
        return numStr.length();
    }

    private int getNumStartIndex(String numStr) {
        for (int i = numStr.length() - 1; i >= 0; i--) {
            if (!isNum(numStr.charAt(i)) && strHasAny(numStr.charAt(i) + "",
                    ".+-")) return i;
        }
        return 0;
    }

    private double toNumberDouble(String str) {
        double num = 0;
        try {
            num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            System.err.println("A number in the expression is invalid");
            e.printStackTrace();
            System.exit(0);
        }
        return num;
    }

    private boolean strHasAny(String str, String charsToCompare) {
        for (int i = 0; i < str.length(); i++) {
            for (int j = 0; j < charsToCompare.length(); j++) {
                if (str.charAt(i) == charsToCompare.charAt(j)) return true;
            }
        }
        return false;
    }

    private boolean eqlsOpenBrackets(char chr) {
        return strHasAny(chr + "", "([{<");
    }

    private boolean eqlsClosedBrackets(char chr) {
        return strHasAny(chr + "", ">}])");
    }

    private boolean eqlsAny(char chr, String charsToCompare) {
        return strHasAny(chr + "", charsToCompare);
    }

    private char getOpposite(char chr) {
        if (strHasAny(chr + "", "([{<>}])"))
            return new String("([{<>}])").charAt(
                    new String(")]}><{[(").indexOf(chr));
        else return 0;
    }
}
