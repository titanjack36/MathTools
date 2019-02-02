//---------------------------EXPONENTIAL CLASS--------------------------------//
//@author TitanJack
//@project MathTools
//Exponents take the form <base>^<exponent>, both the base and exponent can be
//formed by individual expressions. For example, (x+1)^(4x) is an exponent

package Functions;

import NumberFormats.Numbers;

public class Exponential extends Function{
    private double coeff;
    private Function baseFunc;
    private Function exponentFunc;

    @SuppressWarnings("WeakerAccess")
    public Exponential(Function baseFunc, Function exponentFunc) {
        this(baseFunc, exponentFunc, 1);
    }

    public Exponential(Function baseFunc, Function exponentFunc, double coeff) {
        this.baseFunc = baseFunc;
        this.exponentFunc = exponentFunc;
        this.coeff = coeff;
    }

    public Exponential(Function baseFunc, double power, double coeff) {
        this(baseFunc, new Constant(power), coeff);
    }

    public Exponential(Function baseFunc, double power) {
        this(baseFunc, power, 1);
    }

    public double compute(double x) {
        return coeff*Math.pow(baseFunc.compute(x), exponentFunc.compute(x));
    }

    public Function differentiate() {
        if (exponentFunc instanceof Constant) {
            //Derivative for power functions. d/dx(f(x))^a=a(f(x))^(a-1)(f'(x))
            double constVal = exponentFunc.compute(0);
            if (constVal == 0) return new Constant(0);
            else if (constVal == 1) return baseFunc.differentiate();
            else return new GeoFunction(new Function[]{exponentFunc, new
                        Exponential(baseFunc,exponentFunc.compute(0) -
                        1), baseFunc.differentiate()});
        } else {
            //Generalized derivative formula for exponential functions
            //d/dx(f(x)^g(x))=(f(x)^g(x))(g'(x)lnf(x)+g(x)/f(x)*f'(x))
            return new GeoFunction(new Function[]{
                    this,
                    new SumFunction(new Function[]{
                            new GeoFunction(new Function[]{
                                    exponentFunc.differentiate(),
                                    new Logarithmic(baseFunc)
                            }),
                            new GeoFunction(new Function[]{
                                    new Rational(exponentFunc, baseFunc),
                                    baseFunc.differentiate()
                            })
                    })
            });
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
        String funcStr = "";
        if (exponentFunc instanceof Constant) {
            //Special instances for <x^0>, <x^1>, and <x^(1/2)>
            double constVal = exponentFunc.compute(0);
            if (constVal == 0) funcStr = "1";
            else if (constVal == 1) funcStr = baseFunc.toString();
            else if (constVal == 0.5) funcStr = "sqrt(" + baseFunc.toString()
                    + ")";
            else funcStr = "(" + baseFunc.toString() + ")^" +
                        Numbers.formatNum(constVal);
        } else
            funcStr = "(" + baseFunc.toString() + ")^(" +
                    exponentFunc.toString() + ")";
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = Numbers.formatNum(coeff) + funcStr;
        return funcStr;
    }
}
