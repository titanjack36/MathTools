//-----------------------------MATRIX CLASS-----------------------------------//
//The object class stores matrix objects using 2x2 double floating point arrays
//and allows for operations to be performed on matrices.
//TODO: add functionality for augmented matrices
//TODO: calculate determinant and eigenvalues of matricies

public class Matrix {
    private double matrix[][];

    public Matrix(double [][] matrix) {
        this.matrix = matrix;
    }

    //---------------------------CORE FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public void rowEchelon()
    //public void reducedRowEchelon()
    //public void addMatrix(Matrix other)
    //public void multiplyMatrix(Matrix other)

    //Function: Row Echelon
    //Performs row echelon using gaussian elimination on an nxn matrix array
    public void rowEchelon() {
        int pivotRow = 0;
        for (int col = 0; col < matrix[0].length; col++) {
            if (hasPivot(col, pivotRow, matrix.length)) {
                setPivot(col, pivotRow, matrix.length);
                for (int row = pivotRow + 1; row < matrix.length; row++) {
                    //the coordinates [pivotRow][col] marks the pivot of the
                    //row, which will be used to "zero out" other rows
                    if (matrix[row][col] != 0) {
                        add(pivotRow, -matrix[row][col]
                                / matrix[pivotRow][col], row);
                    }
                }
                pivotRow++;
            }
        }
    }

    //Function: Reduced Row Echelon
    //Puts the matrix in RREF form
    public void reducedRowEchelon() {
        rowEchelon();
        int pivotRow = 0;
        for (int col = 0; col < matrix.length; col++) {
            if (hasPivot(col, pivotRow, matrix.length)) {
                for (int row = pivotRow - 1; row >= 0; row--) {
                    if (matrix[row][col] != 0) {
                        //"Zero out" values above the pivot value
                        add(pivotRow, -matrix[row][col]
                                / matrix[pivotRow][col], row);
                    }
                }
                //Set pivot values to be 1 by scaling row
                multiply(1 / matrix[pivotRow][col], pivotRow);
                pivotRow++;
            }
        }
    }

    //Function: Add Matrix
    //@param other      the other matrix that will be added to this matrix
    //Performs matrix addition
    public void addMatrix(Matrix other) {
        double otherMatrix[][] = other.get_matrix();

        if (matrix.length == otherMatrix.length && matrix[0].length
            == otherMatrix[0].length) {
            for (int row = 0; row < matrix.length; row++) {
                for (int col = 0; col < matrix[0].length; col++) {
                    matrix[row][col] += otherMatrix[row][col];
                }
            }
        }
    }

    //Function: Multiply Matrix
    //@param other      the other matrix that will be multiplied to this matrix
    //Performs matrix multiplication
    public void multiplyMatrix(Matrix other) {
        double otherMatrix[][] = other.get_matrix();

        if (matrix[0].length == otherMatrix.length) {
            double product[][]
                    = new double[matrix.length][otherMatrix[0].length];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < otherMatrix[0].length; j++) {
                    for (int k = 0; k < matrix[0].length; k++) {
                        product[i][j] += matrix[i][k] * otherMatrix[k][j];
                    }
                }
            }
            matrix = product;
        } else
            System.err.println("Error occurred while executing method " +
                    "'Multiply Matrix', matrix dimensions do not allow for " +
                    "multiplication");
    }

    //--------------------------UTILITY FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //public void add(int row1, double scalar, int row2)
    //public void multiply(int scalar, int row)
    //public void swap(int row1, int row2)
    //public double[] createArr(int row)
    //public void replace(int row, double [] arr)
    //public void replace(int row1, int row2)
    //public void sortRows(int col, int bgn, int end)

    //Function: Add
    //@param row1       index of specified row
    //       scalar     value to be multiplied to row
    //       row2       index of specified row
    //Adds row1 multiplied by scalar to row2 and places result in row2
    //row1 * scalar + row2 => row2
    public void add(int row1, double scalar, int row2) {
        if (inBounds(row1, row2, matrix.length)) {
            for (int col = 0; col < matrix[row1].length; col++) {
                matrix[row2][col] = matrix[row2][col]
                                    + scalar * matrix[row1][col];
            }
        } else error("add", new String[] {"row1", "row2"});
    }

    //Function: Multiply
    //@param scalar     value to be multiplied to row
    //       row        index of row to be multiplied by scalar
    //Multiplies every value in the horizontal row by a constant scalar
    public void multiply(double scalar, int row) {
        if (inBounds(row, matrix.length)) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] *= scalar;
            }
        } else error("multiply", new String[] {"scalar", "row"});
    }

    //Function: Swap
    //@param row1       indices of rows to be swapped
    //       row2
    //Swaps rows by interchanging values for the corresponding columns of the
    //rows
    public void swap(int row1, int row2) {
        if (inBounds(row1, row2, matrix.length)) {
            double[] temp = createArr(row1);
            replace(row1, createArr(row2));
            replace(row2, temp);
        } else error("swap", new String[] {"row1", "row2"});
    }

    //Function: Create Array
    //@param row        the specified row to be converted into array
    //@return           the created array
    //Creates an array of doubles from the values of the specified row
    public double[] createArr(int row) {
        double arr[] = new double[matrix[row].length];
        if (inBounds(row, matrix.length)) {
            for (int col = 0; col < matrix[row].length; col++) {
                arr[col] = matrix[row][col];
            }
        }
        return arr;
    }

    //Function: Replace (1)
    //@param row        a specified index of row to be replaced
    //       arr        the array for which to replace the row
    //Replaces values in the row with corresponding values in the array
    public void replace(int row, double [] arr) {
        if (inBounds(row, matrix.length) && matrix[row].length == arr.length) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = arr[col];
            }
        }
    }

    /*public void replace(int row1, int row2) {
        if (inBounds(row1, row2, matrix.length)) {
            double[] temp = createArr(row1);
            replace(row1, createArr(row2));
        } else error("replace", new String[] {"row1", "row2"});
    }*/

    /*public void sortRows(int col, int bgn, int end) {
        for (int i = end - 1; i > bgn; i--) {
            int maxRow = bgn;
            for (int row = bgn + 1; row <= i; row++)
                if (matrix[row][col] > matrix[maxRow][col])
                    maxRow = row;
            if (maxRow != i && matrix[maxRow][col] != matrix[i][col])
                swap(maxRow, i);
        }
    }*/

    //Function: Set Pivot
    //@param col        the column where the pivot is to be selected
    //       bgn        the begin interval to search the interval (inclusive)
    //       end        the end interval (exclusive)
    //Rules for finding the pivot:
    //1) If pivot of "1" is found, use it as pivot
    //2) If no "1" is found, use lowest whole number that is not "0"
    //3) If no whole value is found, use any number that is not 0
    public void setPivot(int col, int bgn, int end) {
        int pivotRow = end;
        for (int row = bgn; row < end; row++) {
            //Find pivot with leading 1
            if (matrix[row][col] == 1) {
                pivotRow = row;
                break;
            }
            //Find pivot with the smallest whole number
            if (matrix[row][col] != 0 && matrix[row][col] % 1 == 0) {
                if (pivotRow != end) {
                    if (matrix[row][col] < matrix[pivotRow][col])
                        pivotRow = row;
                    else
                        pivotRow = row;
                }
            }
        }
        //Find any pivot that is not 0
        if (pivotRow == end)
            for (int i = bgn; i < end; i++)
                if (matrix[i][col] != 0) {
                    pivotRow = i;
                    break;
                }
        if (pivotRow != end)
            swap(pivotRow, bgn);
    }

    //Function: Has Pivot
    //@return           whether there is a pivot (non zero number) in the
    //                  specified column
    boolean hasPivot(int col, int bgn, int end) {
        for (int row = bgn; row < end; row++)
            if (matrix[row][col] != 0) return true;
        return false;
    }

    //Function: Error
    //@param function   the name of the function the error occurred in
    //       var        the names of the parameters of that function
    //Displays an error message if values for parameters of a function are out
    //of bounds of the matrix
    public void error(String function, String[] var) {
        System.err.print("Error occurred while executing method '" + function
            + "', ");
        for (int i = 0; i < var.length; i++) {
            if (i != 0) System.err.print(" or ");
            System.err.print(var[i]);
        }
        System.err.println(" may be out of bounds of the matrix");
    }

    //--------------------------ASSIST FUNCTIONS------------------------------//
    //FUNCTION LIST:
    //public boolean inBounds(int a, int b, int upBound)
    //public boolean inBounds(int a, int upBound)

    public boolean inBounds(int a, int b, int upBound) {
        return a >= 0 && b >= 0 && a < upBound && b < upBound;
    }

    public boolean inBounds(int a, int upBound) {
        return inBounds(a, 0, upBound);
    }

    //---------------------------MISC FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public double [][] get_matrix()
    //public String toString()

    public double [][] get_matrix() {
        return matrix;
    }

    public String toString() {
        String matrixStr = "";
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrixStr += matrix[row][col] + "\t";
            }
            matrixStr += "\n";
        }
        return matrixStr;
    }

    //------------------------------------------------------------------------//

}
