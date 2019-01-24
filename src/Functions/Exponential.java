package Functions;

public class Exponential extends Function{
    double coeff;
    private Function baseFunc;
    private Function exponentFunc;

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
        String funcStr = "((" + baseFunc.toString() + ")^(" +
                exponentFunc.toString() + "))";
        if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }
}
