//----------------------------FUNCTION CLASS----------------------------------//
//@author TitanJack
//@project MathTools
//A blueprint class for all types of functions, includes core methods

package Functions;

public abstract class Function {

    public abstract double compute(double x);

    public abstract double getCoeff();

    public abstract Function setCoeff(double c);

    public abstract String toString();
}
