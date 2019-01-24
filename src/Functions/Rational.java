package Functions;

public class Rational extends Function {

    private Function numerator;
    private Function denominator;
    private double coeff;

    public Rational(Function n, Function d) {
        numerator = n;
        denominator = d;
        coeff = 1;
    }

    public Rational(Function n, Function d, double coeff) {
        numerator = n;
        denominator = d;
        this.coeff = coeff;
    }

    public double compute(double x) {
        return coeff * numerator.compute(x) / denominator.compute(x);
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
        if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }
}
