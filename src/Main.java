public class Main {

    public static void main(String[]args) {
        double matrix1Arr[][] = {{1,1,1},{1,5,3}};
        double matrix2Arr[][] = {{2,1},{-1,1},{-3,4}};

        Matrix matrix1 = new Matrix(matrix1Arr);
        Matrix matrix2 = new Matrix(matrix2Arr);

        System.out.println(matrix1.toString());
        matrix1.reducedRowEchelon();
        //matrix1.multiplyMatrix(matrix2);
        System.out.println(matrix1.toString());
    }
}
