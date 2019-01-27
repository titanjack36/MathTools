//---------------------------SUMFUNCTION CLASS--------------------------------//
//@author TitanJack
//@project MathTools
//Addition and subtraction that occur within the expression is represented using
//the sum function. Eg. <x^2+2x+1>, <(x+1)-(x-1)/(x+2)>

package Functions;

import NumberFormats.Numbers;

public class SumFunction extends Function {

    private Function[] sumArr;
    private double coeff;

    @SuppressWarnings("WeakerAccess")
    public SumFunction(Function[] sumArr) {
        this.sumArr = sumArr;
        coeff = 1;
    }

    @SuppressWarnings("unused")
    public SumFunction(Function[] sumArr, double coeff) {
        this.sumArr = sumArr;
        this.coeff = coeff;
    }

    public double compute(double x) {
        double result = 0;
        for (Function function : sumArr) result += function.compute(x);
        return coeff * result;
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = "";
        for (int i = 0; i < sumArr.length; i++) {
            if (sumArr[i].getCoeff() > 0 && i != 0) funcStr += "+";
            funcStr += sumArr[i].toString();
        }
        if (coeff == -1) funcStr = "-(" + funcStr + ")";
        else if (coeff != 1) funcStr = Numbers.formatNum(coeff) + "(" + funcStr + ")";
        return funcStr;
    }
}
