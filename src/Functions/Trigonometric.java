//--------------------------TRIGONOMETRIC CLASS-------------------------------//
//@author TitanJack
//@project MathTools
//The class contains most trigonometric functions including primary, reciprocal,
//and hyperbolic trig functions as well as their respective inverses

package Functions;

public class Trigonometric extends Function {

    private String type;
    private double coeff;
    private Function subFunc;

    public Trigonometric(String type, Function subFunc) {
        this.type = type;
        this.subFunc = subFunc;
        coeff = 1;
    }

    public Trigonometric(String type, Function subFunc, double coeff) {
        this.type = type;
        this.subFunc = subFunc;
        this.coeff = coeff;
    }

    public double compute(double x) {
        double y = subFunc.compute(x);
        switch (type) {
            case "sin": return Math.sin(y);
            case "cos": return Math.cos(y);
            case "tan": return Math.tan(y);
            case "csc": return 1/Math.sin(y);
            case "sec": return 1/Math.cos(y);
            case "cot": return 1/Math.tan(y);
            case "arcsin": return Math.asin(y);
            case "arccos": return Math.acos(y);
            case "arctan": return Math.atan(y);
            case "arccsc": return Math.asin(1/y);
            case "arcsec": return Math.acos(1/y);
            case "arccot": return Math.atan(1/y);
            case "sinh": return Math.sinh(y);
            case "cosh": return Math.cosh(y);
            case "tanh": return Math.tanh(y);
            case "csch": return 1/Math.sinh(y);
            case "sech": return 1/Math.cosh(y);
            case "coth": return 1/Math.tanh(y);
            case "arcsinh": return Math.log(y + Math.sqrt(y*y + 1.0));
            case "arccosh": return Math.log(y + Math.sqrt(y*y - 1.0));
            case "arctanh": return 0.5 * Math.log((1.0 + y)/(1.0 - y));
            case "arccsch": return Math.log(1.0/y + Math.sqrt(1.0/(y*y) + 1.0));
            case "arcsech": return Math.log(1.0/y + Math.sqrt(1.0/(y*y) - 1.0));
            case "arccoth": return 0.5 * Math.log((y + 1.0)/(y - 1.0));
        }
        System.exit(0);
        return 0;
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = type + "(" + subFunc.toString() + ")";
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }
}
