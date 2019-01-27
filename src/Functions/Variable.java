//---------------------------EXPONENTIAL CLASS--------------------------------//
//@author TitanJack
//@project MathTools
//A basic variable which represented as a single <x>.

package Functions;

import NumberFormats.Numbers;

public class Variable extends Function {

    private double coeff;

    @SuppressWarnings("unused")
    public Variable() {
        coeff = 1;
    }

    @SuppressWarnings("WeakerAccess")
    public Variable(double coeff) {
        this.coeff = coeff;
    }

    public double compute(double x) {
        return coeff * x;
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = "x";
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = Numbers.formatNum(coeff) + "x";
        return funcStr;
    }
}
