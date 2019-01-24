package Functions;

public class Constant extends Function{

    double constVal;

    public Constant(double constVal) {
        this.constVal = constVal;
    }

    public double compute(double x) {
        return constVal;
    }

    public double getCoeff() {
        return 1;
    }

    public Function setCoeff(double c) {
        //Do nothing
        return this;
    }

    public String toString() {
        return constVal + "";
    }
}
