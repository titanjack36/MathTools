//-----------------------------RATIONAL CLASS---------------------------------//
//@author TitanJack
//@project MathTools
//Any division which occurs within the expression can be expressed as a rational
//function with a numerator and a denominator. Eg. (x+1)/(x-1)

package Functions;

import NumberFormats.Numbers;

public class Rational extends Function {

    private Function numerator;
    private Function denominator;
    private double coeff;

    @SuppressWarnings("WeakerAccess")
    public Rational(Function n, Function d) {
        numerator = n;
        denominator = d;
        coeff = 1;
    }

    @SuppressWarnings("unused")
    public Rational(Function n, Function d, double coeff) {
        numerator = n;
        denominator = d;
        this.coeff = coeff;
    }

    public double compute(double x) {
        return coeff * numerator.compute(x) / denominator.compute(x);
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = "(" + numerator.toString()+ ")/(" +
                denominator.toString() + ")";
        if (coeff != 1) funcStr = Numbers.formatNum(coeff) + funcStr;
        return funcStr;
    }
}
