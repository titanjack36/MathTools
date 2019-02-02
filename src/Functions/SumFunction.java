//---------------------------SUMFUNCTION CLASS--------------------------------//
//@author TitanJack
//@project MathTools
//Addition and subtraction that occur within the expression is represented using
//the sum function. Eg. <x^2+2x+1>, <(x+1)-(x-1)/(x+2)>

package Functions;

import NumberFormats.Numbers;

import java.util.HashMap;
import java.util.Map;

public class SumFunction extends Function {

    private Function[] sumArr;
    private double coeff;

    @SuppressWarnings("WeakerAccess")
    public SumFunction(Function[] sumArr) {
        this(sumArr, 1);
    }

    @SuppressWarnings("unused")
    public SumFunction(Function[] sumArr, double coeff) {
        //To push all functions in sumArr into a hash map so that duplicate
        //functions can get stacked. Individual numbers being added are
        //recorded separately using variable constant.
        int constant = 0;
        HashMap<Function, Integer> subFuncs = new HashMap<>();
        for (int i = 0; i < sumArr.length; i++) {
            if (sumArr[i] instanceof Constant)
                constant += sumArr[i].compute(0);
            else {
                boolean isMatched = false;
                for (Map.Entry subFunc : subFuncs.entrySet()) {
                    if (!isMatched && subFunc.getKey().toString().equals
                            (sumArr[i].toString())) {
                        subFuncs.put((Function)subFunc.getKey(),
                                (int)subFunc.getValue() + 1);
                        isMatched = true;
                    }
                }
                if (!isMatched) subFuncs.put(sumArr[i], 1);
            }
        }

        //If constant = 0, it means there are no constants to be included in
        //the sum array
        this.sumArr = new Function[constant == 0 ? subFuncs.size() :
                subFuncs.size() + 1];
        int count = 0;
        for (Map.Entry subFunc : subFuncs.entrySet()) {
            if ((int)subFunc.getValue() == 1)
                this.sumArr[count] = (Function)subFunc.getKey();
            //The expression <x+x> is converted into <2x>. Unfortunately,
            //expressions such as <2x+3x> will not get combined
            else this.sumArr[count] = new GeoFunction((int)subFunc.getValue(),
                    (Function)subFunc.getKey());
            count++;
        }
        if (constant != 0) this.sumArr[this.sumArr.length - 1] = new Constant
                (constant);
        this.coeff = coeff;
    }

    public double compute(double x) {
        double result = 0;
        for (Function function : sumArr) result += function.compute(x);
        return coeff * result;
    }

    public Function differentiate() {
        Function[] diffSumArr = new Function[sumArr.length];
        for (int i = 0; i < sumArr.length; i++) {
            diffSumArr[i] = sumArr[i].differentiate();
        }
        return new SumFunction(diffSumArr);
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
        if (coeff == -1) funcStr = "-(" + funcStr + ")";
        else if (coeff != 1) funcStr = Numbers.formatNum(coeff) + "(" + funcStr + ")";
        return funcStr;
    }
}
