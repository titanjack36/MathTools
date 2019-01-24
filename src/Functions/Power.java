package Functions;

public class Power extends Function {

    private double coeff;
    private int power;
    private Function innerFunc;

    public Power(double coeff, int power, Function innerFunc) {
        this.coeff= coeff;
        this.power = power;
        this.innerFunc = innerFunc;
    }

    public double compute(double x) {
        return coeff * Math.pow(innerFunc.compute(x), power);
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = "((" + innerFunc.toString() + ")^" + power + ")";
        if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }

}
