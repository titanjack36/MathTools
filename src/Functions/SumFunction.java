package Functions;

public class SumFunction extends Function {

    private Function sumArr[];
    private double coeff;

    public SumFunction(Function sumArr[]) {
        this.sumArr = sumArr;
        coeff = 1;
    }

    public SumFunction(Function sumArr[], double coeff) {
        this.sumArr = sumArr;
        this.coeff = coeff;
    }

    public double compute(double x) {
        double result = 0;
        for (int i = 0; i < sumArr.length; i++)
            result += sumArr[i].compute(x);
        return coeff * result;
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
        for (int i = 0; i < sumArr.length; i++) {
            if (sumArr[i].getCoeff() > 0 && i != 0) funcStr += "+";
            funcStr += sumArr[i].toString();
        }
        if (coeff != 1) funcStr = coeff + "(" + funcStr + ")";
        return funcStr;
    }
}
