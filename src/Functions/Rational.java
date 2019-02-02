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
        this(n, d, 1);
    }

    @SuppressWarnings("unused")
    public Rational(Function n, Function d, double coeff) {
        numerator = n;
        denominator = d;
        this.coeff = coeff;
    }

    public Rational(double n, Function d, double coeff) {
        this(new Constant(n), d, coeff);
    }

    public Rational(double n, Function d) {
        this(n, d, 1);
    }

    public double compute(double x) {
        return coeff * numerator.compute(x) / denominator.compute(x);
    }

    public Function differentiate() {
        //Uses rational differentiation formula.
        //d/dx(f(x)/g(x))=(f'(x)g(x)-f(x)g'(x))/(g(x))^2
        return new Rational(new SumFunction(new Function[] {
                new GeoFunction(numerator.differentiate(), denominator),
                new GeoFunction(numerator, denominator.differentiate(), -1)
        }), new Exponential(denominator, 2), coeff);
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
