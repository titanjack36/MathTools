//---------------------------EXPONENTIAL CLASS--------------------------------//
//@author TitanJack
//@project MathTools
//Exponents take the form <base>^<exponent>, both the base and exponent can be
//formed by individual expressions. For example, (x+1)^(4x) is an exponent

package Functions;

import NumberFormats.Numbers;

public class Exponential extends Function{
    private double coeff;
    private Function baseFunc;
    private Function exponentFunc;

    @SuppressWarnings("WeakerAccess")
    public Exponential(Function baseFunc, Function exponentFunc) {
        this.baseFunc = baseFunc;
        this.exponentFunc = exponentFunc;
        coeff = 1;
    }

    public Exponential(Function baseFunc, Function exponentFunc, double coeff) {
        this.baseFunc = baseFunc;
        this.exponentFunc = exponentFunc;
        this.coeff = coeff;
    }

    public double compute(double x) {
        return coeff*Math.pow(baseFunc.compute(x), exponentFunc.compute(x));
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = "(" + baseFunc.toString() + ")^(" +
                exponentFunc.toString() + ")";
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = Numbers.formatNum(coeff) + funcStr;
        return funcStr;
    }
}
