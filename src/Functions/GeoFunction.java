//---------------------------GEOFUNCTION CLASS--------------------------------//
//@author TitanJack
//@project MathTools
//Any multiplication that exists in the expression can be characterized as a
//geometric function. Eg. <(x+1)(x-1)>, <3x>, <pi*x>, etc

package Functions;

import NumberFormats.Numbers;

public class GeoFunction extends Function{

    private Function[] geoArr;
    private double coeff;

    @SuppressWarnings("WeakerAccess")
    public GeoFunction(Function[] geoArr) {
        this.geoArr = geoArr;
        coeff = 1;
    }

    @SuppressWarnings("unused")
    public GeoFunction(Function[] geoArr, double coeff) {
        this.geoArr = geoArr;
        this.coeff = coeff;
    }

    public double compute(double x) {
        double result = geoArr[0].compute(x);
        for (int i = 1; i < geoArr.length; i++)
            result *= geoArr[i].compute(x);
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
        for (Function subFunc : geoArr)
            funcStr += "(" + subFunc.toString() + ")";
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = Numbers.formatNum(coeff) + funcStr;
        return funcStr;
    }
}
