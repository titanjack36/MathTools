package Functions;

public class GeoFunction extends Function{

    private Function geoArr[];
    private double coeff;

    public GeoFunction(Function geoArr[]) {
        this.geoArr = geoArr;
        coeff = 1;
    }

    public GeoFunction(Function geoArr[], double coeff) {
        this.geoArr = geoArr;
        this.coeff = coeff;
    }

    public double compute(double x) {
        double result = geoArr[0].compute(x);
        for (int i = 1; i < geoArr.length; i++)
            result *= geoArr[i].compute(x);
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
        for (int i = 0; i < geoArr.length; i++)
            funcStr += "(" + geoArr[i].toString() + ")";
        if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }
}
