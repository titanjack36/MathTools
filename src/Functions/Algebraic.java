package Functions;

public class Algebraic extends Function{

    private int root;
    private double coeff;
    private Function innerFunc;

    public Algebraic(int root, Function innerFunc) {
        this.root = root;
        coeff = 1;
        this.innerFunc = innerFunc;
    }

    public Algebraic(int root, double coeff, Function innerFunc) {
        this.root = root;
        this.coeff = coeff;
        this.innerFunc = innerFunc;
    }

    public double compute(double x) {
        return coeff * Math.pow(innerFunc.compute(x), 1.0 / root);
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = innerFunc.toString();
        if (root == 2) funcStr = "(sqrt(" + funcStr + "))";
        else funcStr = "((" + funcStr + ")^(1/" + root + "))";
        if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }
}
