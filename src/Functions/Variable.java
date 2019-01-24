package Functions;

public class Variable extends Function {

    private double coeff;

    public Variable() {
        coeff = 1;
    }

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
        if (coeff != 1) funcStr = coeff + "x";
        return funcStr;
    }
}
