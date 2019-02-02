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
        this(type, subFunc, 1);
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

    public Function differentiate() {
        Function diff = null;
        switch (type) {
            case "sin": diff = new Trigonometric("cos", subFunc, 1);
                break;
            case "cos": diff = new Trigonometric("sin", subFunc, -1);
                break;
            case "tan": diff = new Exponential(new Trigonometric("sec",
                    subFunc), 2, 1);
                break;
            case "csc": diff = new GeoFunction(new Trigonometric("csc",
                    subFunc), new Trigonometric("cot", subFunc), -1);
                break;
            case "sec": diff = new GeoFunction(new Trigonometric("sec",
                    subFunc), new Trigonometric("tan", subFunc), 1);
                break;
            case "cot": diff = new Exponential(new Trigonometric("csc",
                    subFunc), 2, -1);
                break;
            case "arcsin": diff = new Rational(1, new Exponential(new
                    SumFunction(new Function[]{new Constant(1), new
                    Exponential(new Variable(), 2, -1)}), 0.5),
                    1);
                break;
            case "arccos": diff = new Rational(1, new Exponential(new
                    SumFunction(new Function[]{new Constant(1), new
                    Exponential(new Variable(), 2, -1)}), 0.5),
                    -1);
                break;
            case "arctan": diff = new Rational(1, new SumFunction(new
                    Function[]{new Constant(1), new  Exponential(new
                    Variable(), 2)}), 1);
                break;
            case "arccsc": diff = new Rational(1, new GeoFunction(new
                    Exponential(new SumFunction(new Function[]{new Constant(
                    1), new Rational(1, new Exponential(new
                    Variable(), 2), -1)}), 0.5), new
                    Exponential(new Variable(), 2)), -1);
                break;
            case "arcsec": diff = new Rational(1, new GeoFunction(new
                    Exponential(new SumFunction(new Function[]{new Constant(
                    1), new Rational(1, new Exponential(new
                    Variable(), 2), -1)}), 0.5), new
                    Exponential(new Variable(), 2)), 1);
                break;
            case "arccot": diff = new Rational(new Constant(1),
                    new SumFunction (new Function[]{new Constant(1),
                    new Exponential(new Variable(), 2)}), -1);
                break;
            case "sinh": diff = new Trigonometric("cosh", subFunc, 1);
                break;
            case "cosh": diff = new Trigonometric("sinh", subFunc, 1);
                break;
            case "tanh": diff = new Exponential(new Trigonometric("sech",
                    subFunc), 2, 1);
            case "csch": diff = new GeoFunction(new Trigonometric("csch",
                    subFunc), new Trigonometric("coth", subFunc), -1);
                break;
            case "sech": diff = new GeoFunction(new Trigonometric("sech",
                    subFunc), new Trigonometric("tanh", subFunc), 1);
                break;
            case "coth": diff = new Exponential(new Trigonometric("csc",
                    subFunc), 2, -1);
                break;
            case "arcsinh": diff = new Rational(1, new Exponential(
                    new SumFunction(new Function[]{new Exponential(new
                    Variable(), 2), new Constant(1)}), 0.5),
                    1);
                break;
            case "arccosh": diff = new Rational(1, new Exponential(
                    new SumFunction(new Function[]{new Exponential(new
                    Variable(), 2), new Constant(-1)}), 0.5),
                    1);
                break;
            case "arctanh": diff = new Rational(1, new SumFunction(new
                    Function[]{new Constant(1), new  Exponential(new
                    Variable(), 2, -1)}), 1);
                break;
            case "arccsch": diff = new Rational(1, new GeoFunction(new
                    Exponential(new SumFunction(new Function[]{new Rational(1,
                    new Exponential(new Variable(), 2)), new Constant(
                    1)}), 0.5), new Exponential(new Variable(),
                    2)), -1);
                break;
            case "arcsech": diff = new Rational(1, new GeoFunction(new
                    Exponential(new SumFunction(new Function[]{new Rational(1,
                    new Exponential(new Variable(), 2)), new Constant(
                    1)}), 0.5), new Exponential(new Variable(),
                    2)), 1);
                break;
            case "arccoth": diff = new Rational(new Constant(1),
                    new SumFunction (new Function[]{new Exponential(new
                    Variable(), 2, -1), new Constant(1), }),
                    -1);
                break;
        }
        if (diff == null) {
            System.exit(0);
            return null;
        } else {
            return new GeoFunction(new Function[]{diff,
                    subFunc.differentiate()}, coeff);
        }
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
