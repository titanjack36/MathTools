//----------------------------FRACTION CLASS----------------------------------//
//@author TitanJack
//@project MathTools
//The fraction class is able to convert decimal expressions into fraction
//representation using rational approximation. It stores fractions as objects
// and can perform operations with fractions.

package NumberFormats;

public class Fraction {

    private int numerator;
    private int denominator;

    public Fraction(int num, int denom) {
        numerator = num;
        denominator = denom;
    }

    //Implements rational approximation using continued fractions
    public Fraction(double startNum, int maxDenom) {
        int contFrac[] = new int[100];
        double num = startNum;
        contFrac[0] = (int)num; //Continued fraction array
        int size = 1; //Size of continued fraction
        int n = 0, d = 0; //Nominator, denominator
        do {
            //Algorithm:
            //https://en.wikipedia.org/wiki/Continued_fraction#Best_rational_
            //approximations
            num = 1 / (num - contFrac[size - 1]);
            contFrac[size] = (int)num;
            //Converts continued fraction into standard fraction form
            if (size == 1) {
                n = contFrac[0];
                d = 1;
            } else {
                n = 1;
                d = contFrac[size - 1];
                for (int i = size - 2; i >= 0; i--) {
                    n = contFrac[i] * d + n;
                    if (i != 0) {
                        int temp = n;
                        n = d;
                        d = temp;
                    }
                }
            }
            size++;
        } while (abs(n/(double)d - startNum) > 0.0000001 && d < maxDenom &&
                size < contFrac.length);
        numerator = startNum >= 0 ? (int)abs(n) : -(int)abs(n);
        denominator = (int)abs(d);
    }

    //---------------------------CORE FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public void addBy(NumberFormats.Fraction other)
    //public void subtractBy(NumberFormats.Fraction other)
    //public void multiplyBy(NumberFormats.Fraction other)
    //public void divideBy(NumberFormats.Fraction other)
    //public void negate()
    //private void simplify()

    //Function: Add By
    //@param other      the other fraction object to be added
    //Adds other fraction onto this fraction
    public void addBy(Fraction other) {
        int newDenom = lcm(denominator, other.getDenominator());
        numerator = numerator * newDenom / denominator
                + other.getNumerator() * newDenom / other.getDenominator();
        denominator = newDenom;
    }

    //Function: Subtract By
    //@param other      the other fraction to be subtracted
    //subtracts other function from this function
    public void subtractBy(Fraction other) {
        other.negate();
        addBy(other);
        other.negate();
    }

    //Function: Multiply By
    //@param other      the other fraction to be multiplied
    //multiplies other function to this function
    public void multiplyBy(Fraction other) {
        numerator *= other.getNumerator();
        denominator *= other.getDenominator();
    }

    //Function: Divide By
    //@param other      the other fraction to be divided
    //divides other function onto this function
    public void divideBy(Fraction other) {
        if (other.numerator != 0) {
            numerator *= other.getDenominator();
            denominator *= other.getNumerator();
        } else System.err.println("Error: Division by zero");
    }

    //Function: Negate
    //Switches the sign of this function
    public void negate() {
        numerator = -numerator;
    }

    //Function: Simplify
    //Reduces the fraction to simplest terms
    public void simplify() {
        int gcd = gcd(numerator, denominator);
        if (gcd != 1) {
            numerator /= gcd;
            denominator /= gcd;
        }
    }

    //--------------------------ASSIST FUNCTIONS------------------------------//
    //FUNCTION LIST:
    //private int lcm(int a, int b)
    //private int gcd(int a, int b)
    //private double abs(double num)

    //Function: Least Common Multiple
    //@param a      numbers for LCM calculation
    //       b
    //@return       the LCM between the two numbers
    private int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    //Function: Greatest Common Divisor
    //@param a      numbers for GCD calculation
    //       b
    //@return       the GCD between the two numbers
    //Uses Euclid algorithm for GCD
    private int gcd(int a, int b) {
        if (a == 0) return b;
        if (b == 0 || a == b) return a;
        if (a > b) return gcd(a - b, b);
        return gcd(a, b - a);
    }

    //Function: Absolute Value
    //@param num    the number to be evaluated
    //@return       the absolute value of the number
    private double abs(double num) {
        return num >= 0 ? num : -num;
    }

    //---------------------------MISC FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public int getNumerator()
    //public int getDenominator()
    //public String toString()

    //Function: Get Numerator
    //@return       the numerator of the fraction
    public int getNumerator() {
        return numerator;
    }

    //Function: Get Denominator
    //@return       the denominator of the fraction
    public int getDenominator() {
        return denominator;
    }

    //Function: To String
    //@return       the string representation of the fraction "n/d"
    public String toString() {
        return numerator + "/" + denominator;
    }
}
