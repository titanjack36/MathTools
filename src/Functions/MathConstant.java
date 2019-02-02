//---------------------------MATHCONSTANT CLASS-------------------------------//
//@author TitanJack
//@project MathTools
//A mathematical constant which represents a fixed number. Eg. pi=3.1415926

package Functions;

public class MathConstant extends Function{

    private String constant;
    private double coeff;

    public MathConstant(String constant) {
        this(constant, 1);
    }

    public MathConstant(String constant, double coeff) {
        this.constant = constant;
        this.coeff = coeff;
    }

    public double compute(double x) {
        switch(constant) {
            case "pi": return Math.PI;
            case "e": return Math.E;
        }
        System.exit(0);
        return 0;
    }

    public Function differentiate() {
        return new Constant(0);
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = constant;
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }
}
