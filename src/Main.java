//------------------------------MATH TOOLS------------------------------------//
//@author TitanJack
//@version 1.2 (2019-02-02)
//MathTools is a simple maths API coded in Java which provides computation for
//certain mathematical concepts. The project is currently in early development
//stages and only features matrix arithmetic operations. More elements from
//linear algebra and calculus will be added as time progresses. Disclaimer:
//There are already mathematical computation programs such as Wolfram Alpha and
//Apache API which are more efficient and powerful than this program. This
//project was developed as a hobby and not meant to be used professionally.

import Functions.*;
import NumberFormats.Numbers;

import java.util.Scanner;

public class Main {

    public static void main(String[]args) {
        /*double matrix1Arr[][];
        double matrix2Arr[][];
        double vector1[];
        Matrix matrix1;
        Matrix matrix2;

        System.out.println("------------------Row Echelon--------------------");
        matrix1Arr = new double[][]{{2,1,5},{-1,3,2},{3,0,-2}};
        matrix1 = new Matrix(matrix1Arr);
        System.out.println("Matrix:\n" + matrix1.toString());
        matrix1.rowEchelon();
        System.out.println(">>Row Echelon\n" + matrix1.toString());
        System.out.println();

        System.out.println("---------------Reduced Row Echelon---------------");
        matrix1Arr = new double[][]{{2,1,5,3},{-1,3,2,1}};
        vector1 = new double[]{4,2};
        matrix1 = new Matrix(matrix1Arr, vector1);
        System.out.println("Matrix:\n" + matrix1.toString());
        matrix1.reducedRowEchelon();
        System.out.println(">>Reduced Row Echelon\n" + matrix1.toString());
        System.out.println();

        System.out.println("-------------------Matrix Rank-------------------");
        System.out.println("Matrix:\n" + matrix1.toString());
        System.out.println(">>Rank: " + matrix1.getRank());
        System.out.println();

        System.out.println("-----------------Matrix Solution-----------------");
        matrix1Arr = new double[][]{{1,0,5,1},{0,1,2,0},{0,0,0,3}};
        vector1 = new double[]{-1,2,-4};
        matrix1 = new Matrix(matrix1Arr, vector1);
        System.out.println("Matrix:\n" + matrix1.toString());
        System.out.println(">>Matrix solution: ");
        matrix1.printSolution();
        System.out.println();

        System.out.println("-----------------Matrix Addition-----------------");
        matrix1Arr = new double[][]{{1,9,3},{2,3,1},{-5,2,-1}};
        matrix2Arr = new double[][]{{5,3,-3},{-2,-1,-1},{4,2,1}};
        matrix1 = new Matrix(matrix1Arr);
        matrix2 = new Matrix(matrix2Arr);
        System.out.println("Matrix A:\n" + matrix1.toString());
        System.out.println("Matrix B:\n" + matrix2.toString());
        matrix1.addMatrix(matrix2);
        System.out.println("=> A = A + B\n" + matrix1.toString());

        System.out.println("--------------Matrix Multiplication--------------");
        matrix1Arr = new double[][]{{1,9,3},{2,3,1}};
        matrix2Arr = new double[][]{{-3,4},{2,-5},{8,3}};
        matrix1 = new Matrix(matrix1Arr);
        matrix2 = new Matrix(matrix2Arr);
        System.out.println("Matrix A:\n" + matrix1.toString());
        System.out.println("Matrix B:\n" + matrix2.toString());
        matrix1.multiplyMatrix(matrix2);
        System.out.println("=> A = A * B\n" + matrix1.toString());

        System.out.println("---------------Matrix Determinant----------------");
        matrix1Arr = new double[][]{{1,9,3},{2,3,1},{-5,2,-1}};
        matrix1 = new Matrix(matrix1Arr);
        System.out.println("Matrix:\n" + matrix1.toString());
        System.out.println(">>Determinant: " + matrix1.getDeterminant());
        System.out.println();

        System.out.println("----------------Matrix Transpose-----------------");
        matrix1Arr = new double[][]{{1,9},{2,3},{-5,2}};
        matrix1 = new Matrix(matrix1Arr);
        System.out.println("Matrix:\n" + matrix1.toString());
        matrix1.transpose();
        System.out.println(">>Transpose\n" + matrix1.toString());
        System.out.println();

        System.out.println("-----------------Matrix Inverse------------------");
        matrix1Arr = new double[][]{{1,9,3},{2,3,1},{-5,2,-1}};
        matrix1 = new Matrix(matrix1Arr);
        System.out.println("Matrix:\n" + matrix1.toString());
        matrix1.inverse();
        System.out.println(">>Inverse\n" + matrix1.toString());*/

        FunctionConstructor fc = new FunctionConstructor();
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Enter an expression:");
            Function func = fc.toFunctionDebug(input.nextLine());
            System.out.println("f(x) = " + func.toString());
            System.out.println("f'(x) = " + func.differentiate().toString());
        }
    }
}
