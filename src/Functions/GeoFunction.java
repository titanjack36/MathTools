//---------------------------GEOFUNCTION CLASS--------------------------------//
//@author TitanJack
//@project MathTools
//Any multiplication that exists in the expression can be characterized as a
//geometric function. Eg. <(x+1)(x-1)>, <3x>, <pi*x>, etc

package Functions;

import NumberFormats.Numbers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeoFunction extends Function{

    private Function[] geoArr;
    private double coeff;

    @SuppressWarnings("WeakerAccess")
    public GeoFunction(Function[] geoArr) {
        this(geoArr, 1);
    }

    @SuppressWarnings("unused")
    public GeoFunction(Function[] geoArr, double coeff) {
        //To push all functions in geoArr into a hash map so that duplicate
        //functions can get stacked. Individual numbers being multiplied are
        //recorded separately using variable numMultiply.
        int numMultiply = 1;
        HashMap<Function, Integer> subFuncs = new HashMap<>();
        for (int i = 0; i < geoArr.length && numMultiply != 0; i++) {
            if (geoArr[i] instanceof Constant) {
                if (geoArr[i].compute(0) == 0)
                    numMultiply = 0;
                if (numMultiply != 0 && geoArr[i].compute(0) != 1)
                    numMultiply *= geoArr[i].compute(0);
            } else {
                boolean isMatched = false;
                for (Map.Entry subFunc : subFuncs.entrySet()) {
                    if (!isMatched && subFunc.getKey().toString().equals
                            (geoArr[i].toString())) {
                        subFuncs.put((Function)subFunc.getKey(),
                                (int)subFunc.getValue() + 1);
                        isMatched = true;
                    }
                }
                if (!isMatched) subFuncs.put(geoArr[i], 1);
            }
        }
        //Zero multiplied by anything is zero
        if (numMultiply == 0)
            this.geoArr = new Function[]{new Constant(0)};
        else {
            //If number being multiplied is a "1", do not include it in the
            //array
            this.geoArr = new Function[numMultiply == 1 ? subFuncs.size() :
                    subFuncs.size() + 1];
            if (numMultiply != 1) this.geoArr[0] = new Constant(numMultiply);
            int count = numMultiply != 1 ? 1 : 0;
            for (Map.Entry subFunc : subFuncs.entrySet()) {
                if ((int)subFunc.getValue() == 1)
                    this.geoArr[count] = (Function)subFunc.getKey();
                //The expression <x*x> is converted into <x^2>
                else this.geoArr[count] = new Exponential((Function)subFunc.
                        getKey(), (int)subFunc.getValue());
                count++;
            }
        }
        this.coeff = coeff;
    }

    public GeoFunction(Function coeffNum,  Function func, double coeff) {
        this(new Function[]{coeffNum, func}, coeff);
    }

    public GeoFunction(Function coeffNum, Function func) {
        this(coeffNum, func, 1);
    }

    public GeoFunction(double coeffNum, Function func) {
        this(new Constant(coeffNum), func, 1);
    }

    public double compute(double x) {
        double result = geoArr[0].compute(x);
        for (int i = 1; i < geoArr.length; i++)
            result *= geoArr[i].compute(x);
        return coeff * result;
    }

    public Function differentiate() {
        //Using power rule.
        //d/dx(f(x)g(x))=f'(x)g(x)+f(x)g'(x)
        Function[] diffSumArr = new Function[geoArr.length];
        for (int i = 0; i < geoArr.length; i++) {
            Function[] diffSubGeoArr = new Function[geoArr.length];
            for (int j = 0; j < geoArr.length; j++)
                if (j != i)  diffSubGeoArr[j] = geoArr[j];
                else diffSubGeoArr[j] = geoArr[j].differentiate();
            diffSumArr[i] = new GeoFunction(diffSubGeoArr);
        }
        return new SumFunction(diffSumArr, coeff);
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
        int startIndex = 0;
        //A leading constant or variable does not need a bracket surrounding
        //itself. Eg. <3(x+1)(x+2)>, <x(x-1)>, etc.
        if (geoArr.length >= 1 && (geoArr[0] instanceof Constant || geoArr[0]
                instanceof Variable)) {
            funcStr = geoArr[0].toString();
            startIndex = 1;
        }
        //If the geo function is only made up of a constant multiplied by a
        //variable, then no brackets are necessary. Eg. <3x>
        if (geoArr.length == 2 && geoArr[1] instanceof Variable) {
            funcStr += geoArr[1].toString();
            startIndex = 2;
        }
        for (int i = startIndex; i < geoArr.length; i++)
            funcStr += "(" + geoArr[i].toString() + ")";
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = Numbers.formatNum(coeff) + funcStr;
        return funcStr;
    }

    private Function[] combineFuncArrs(Function[] arr1, Function[] arr2) {
        Function[] combinedArr = new Function[arr1.length + arr2.length];
        for (int i = 0; i < arr1.length; i++)
            combinedArr[i] = arr1[i];
        for (int i = arr1.length; i < arr1.length + arr2.length; i++) {
            combinedArr[i] = arr2[i - arr1.length];
        }
        return combinedArr;
    }
}
