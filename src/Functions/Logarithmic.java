//---------------------------LOGARITHMIC CLASS--------------------------------//
//@author TitanJack
//@project MathTools
//The natural log (base e) function which has the form log(x)

package Functions;

public class Logarithmic extends Function {

    private Function subFunc;
    private double coeff;

    public Logarithmic(Function subFunc) {
        this.subFunc = subFunc;
        coeff = 1;
    }

    public Logarithmic(Function subFunc, double coeff) {
        this.subFunc = subFunc;
        this.coeff = coeff;
    }

    public double compute(double x) {
        return Math.log(subFunc.compute(x));
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = "log(" + subFunc.toString() + ")";
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }

}
