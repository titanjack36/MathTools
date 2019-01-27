//----------------------------FRACTION CLASS----------------------------------//
//@author TitanJack
//@project MathTools
//Useful functions for operations with numbers. For now, its main functionality
//is to format the function so that it removes imperfections in the number
//created by precision limitations

package NumberFormats;

public class Numbers {

    //Function: Format Num
    //@param num    the number to be formatted into string
    //@return       the formatted string representation of the number
    //If number is whole, return string integer
    //If rational, return string fraction approximation
    public static String formatNum(double num) {
        //Check for errors like: 1.0000000000001 and get rid of decimal place
        if (abs(round(num) - num) < 0.0000001) num = round(num);
        if (num == round(num)) return (int)num + ""; //return integer string
        else {
            Fraction fracValue = new Fraction(num, 100);
            return fracValue.toString(); //return fraction string
        }
    }

    public static double abs(double num) {
        return num >= 0 ? num : -num;
    }

    public static int round(double num) {
        return num % 1 < 0.5 ? (int)num : (int)num + 1;
    }
}
