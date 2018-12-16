public class Main {

    public static void main(String[]args) {
        double matrix1Arr[][] = {{1,1,1},{1,5,3}};

        Matrix matrix1 = new Matrix(matrix1Arr);

        System.out.println(matrix1.toString());
        matrix1.reducedRowEchelon();
        System.out.println(">>Perform reduced row echelon:");
        System.out.println(matrix1.toString());
    }
}
