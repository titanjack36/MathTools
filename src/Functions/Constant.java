//----------------------------CONSTANT CLASS----------------------------------//
//@author TitanJack
//@project MathTools
//Constants are standalone numbers within expressions. For example, in the
//expression: x^2+3x+4, "4" is the constant.

package Functions;

import NumberFormats.Numbers;

public class Constant extends Function{

    private double constVal;

    @SuppressWarnings("WeakerAccess")
    public Constant(double constVal) {
        this.constVal = constVal;
    }

    public double compute(double x) {
        return constVal;
    }

    public double getCoeff() {
        return constVal >= 0 ? 1 : -1;
    }

    public Function setCoeff(double c) {
        if (c == -1) constVal = -constVal;

        return this;
    }

    public String toString() {
        return Numbers.formatNum(constVal) + "";
    }
}
