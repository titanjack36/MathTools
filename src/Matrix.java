//-----------------------------MATRIX CLASS-----------------------------------//
//@author Jack Guo
//@version 0.2 (2018-12-18)
//The matrix class stores matrix objects using 2x2 double floating point arrays
//and allows for operations to be performed on matrices.
//TODO: calculate determinant and eigenvalues of matrices (difficult)
//TODO: fix toString method to print matrix in better form
//TODO: add rounding and fractions (?)
//TODO: add functionality for changing matrices

public class Matrix {
    private double matrix[][];
    private double backupMatrix[][];
    private boolean backedup;
    private boolean augmented;

    //Constructor for non-augmented matrix
    public Matrix(double [][] matrix) {
        this.matrix = copyArray(matrix);
        augmented = false;
        backedup = false;
    }

    //Constructor for augmented matrix
    public Matrix(double [][] matrix, double [] vector) {
        if (matrix.length == vector.length) {
            this.matrix = new double[matrix.length][matrix[0].length + 1];
            for (int row = 0; row < matrix.length; row++) {
                for (int col = 0; col < matrix[0].length; col++) {
                    this.matrix[row][col] = matrix[row][col];
                }
            }
            for (int row = 0; row < matrix.length; row++) {
                this.matrix[row][matrix[0].length] = vector[row];
            }
            augmented = true;
            backedup = false;
        } else
            System.err.println("Error occurred while creating augmented " +
                    "matrix, dimensions of matrix and vector do not match");
    }

    //---------------------------CORE FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public void rowEchelon()
    //public void reducedRowEchelon()
    //public int getRank()
    //public void printSolution()
    //public void addMatrix(Matrix other)
    //public void multiplyMatrix(Matrix other)
    //public double getDeterminant()
    //public void transpose()
    //public void inverse()

    //Function: Row Echelon
    //@return       the number of row swaps performed (used for calculating
    //              determinant)
    //Performs row echelon using gaussian elimination on an nxn matrix array
    public int rowEchelon() {
        int pivotRow = 0;
        int swaps = 0;
        for (int col = 0; col < matrix[0].length; col++) {
            if (hasPivot(col, pivotRow, matrix.length)) {
                if (setPivot(col, pivotRow, matrix.length)) swaps++;
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
        return swaps;
    }

    //Function: Reduced Row Echelon
    //Changes the matrix to reduced row echelon form
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

    //Function: Get Rank
    //@return       the rank of the matrix
    //Gets the rank of the matrix by counting number of pivots
    public int getRank() {
        backupMatrix();
        rowEchelon();
        int pivotColCoords[] = getPivotColCoords();
        int rank = 0;
        for (int i = 0; i < pivotColCoords.length; i++) {
            if (pivotColCoords[i] != -1) rank++;
        }
        restoreMatrix();
        return rank;
    }

    //Function: Get Solution
    //Prints out the solutions to the matrix using the Row Reduced Echelon Form
    //of the matrix
    public void printSolution() {
        //pivotCol stores if there is a pivot in that column and which row the
        //pivot is in
        if (augmented) {
            backupMatrix();
            reducedRowEchelon();
            if (hasSolution()) {
                String [] solution = new String[matrix[0].length - 1];
                int pivotColCoords[] = getPivotColCoords();
                //The variable "i" also represents the column location of
                //the pivot (if there is one)
                //Note: [pivotCol[i], i] is the coordinate of the pivot
                for (int i = 0; i < solution.length; i++) {
                    //If no pivot is found at that column, set free variable
                    if (pivotColCoords[i] == -1) {
                        solution[i] = "x[" + (i + 1) + "]";
                    } else {
                        //First add the constant from the augmented section of
                        //the matrix
                        double constant = matrix[pivotColCoords[i]]
                                [matrix[0].length - 1];
                        solution[i] = constant + "";
                        //Add free variables to solution, along with respective
                        //coefficients for those variables
                        //Look for free variables that occur after the pivot
                        for (int col = i + 1; col < matrix[0].length - 1; col++)
                            //If a matrix location is not 0 and is not a pivot,
                            //that column most have a free variable
                            //Add the free variable into the solution
                            if (matrix[pivotColCoords[i]][col] != 0) {
                                if (constant == 0)
                                    solution [i] = "";
                                double coeff = -matrix[pivotColCoords[i]][col];
                                if (coeff > 0 &&
                                        constant != 0) solution[i] += "+";
                                solution[i] += coeff + "x[" + (col + 1) + "]";
                            }
                    }
                }
                for (int i = 0; i < solution.length; i++) {
                    System.out.println("x[" + (i + 1) + "]: " + solution[i]);
                }
            } else
                System.out.println("System is inconsistent, no solution found");
            restoreMatrix();
        } else
            error("getSolution", "the current matrix is not " +
                    "augmented");

    }

    //Function: Add Matrix
    //@param other      the other matrix that will be added to this matrix
    //Performs matrix addition
    public void addMatrix(Matrix other) {
        if (!augmented) {
            double otherMatrix[][] = other.getMatrixCopy();
            if (matrix.length == otherMatrix.length && matrix[0].length
                    == otherMatrix[0].length) {
                for (int row = 0; row < matrix.length; row++) {
                    for (int col = 0; col < matrix[0].length; col++) {
                        matrix[row][col] += otherMatrix[row][col];
                    }
                }
            }else
                error("addMatrix", "dimension");
        } else
            error("addMatrix", "augment");
    }

    //Function: Multiply Matrix
    //@param other      the other matrix that will be multiplied to this matrix
    //Performs matrix multiplication
    public void multiplyMatrix(Matrix other) {
        if (!augmented) {
            double otherMatrix[][] = other.getMatrixCopy();

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
                error("multiplyMatrix", "dimension");
        } else
            error("multiplyMatrix", "augment");
    }

    //Function: Get Determinant
    //@return       the determinant of the matrix
    //Finds determinant of the matrix by putting matrix into REF through
    //determinant row operations and then multiply values along the diagonal
    public double getDeterminant() {
        double determinant = 0;
        boolean negate = false;
        if (!augmented) {
            if (matrix.length == matrix[0].length) {
                backupMatrix();
                if (rowEchelon() % 2 != 0) negate = true;
                determinant = matrix[0][0];
                for (int i = 1; i < matrix.length; i++) {
                    determinant *= matrix[i][i];
                }
                if (negate) determinant = -determinant;
                restoreMatrix();
            } else
                error("getDeterminant", "square");
        } else
            error("getDeterminant", "augment");
        return determinant;
    }

    //Function: Transpose
    //Switches coordinates of the matrix: matrix[i][j] <=> matrix[j][i]
    public void transpose() {
        double transposedMatrix[][] = new double[matrix[0].length]
                [matrix.length];
        if (!augmented) {
            for (int i = 0; i < matrix.length; i++)
                for (int j = 0; j < matrix[0].length; j++)
                    transposedMatrix[j][i] = matrix[i][j];
            matrix = transposedMatrix;
        } else error("transpose", "augment");

    }

    //Function: Inverse
    //Finds inverse of the matrix using the inverse formula: RREF[A|I] where
    //A is an invertible matrix and I is the identity matrix
    public void inverse() {
        if (!augmented) {
            if (matrix.length == matrix[0].length) {
                if (getDeterminant() != 0) {
                    double augmentedMatrix[][] = new double[matrix.length]
                            [matrix[0].length + matrix.length];
                    double inverseMatrix[][]
                            = new double[matrix.length][matrix[0].length];
                    for (int row = 0; row < matrix.length; row++)
                        for (int col = 0; col < matrix[0].length; col++)
                            augmentedMatrix[row][col] = matrix[row][col];
                    for (int i = 0; i < matrix.length; i++) {
                        augmentedMatrix[i][matrix[0].length + i] = 1;
                    }
                    matrix = augmentedMatrix;
                    reducedRowEchelon();
                    for (int row = 0; row < inverseMatrix.length; row++) {
                        for (int col = 0; col < inverseMatrix[0].length;
                             col++) {
                            inverseMatrix[row][col] = augmentedMatrix[row]
                                    [inverseMatrix[0].length + col];
                        }
                    }
                    matrix = inverseMatrix;
                } else
                    System.out.println("Matrix is not invertible");
            } else error("inverse", "square");
        } else error("inverse", "augment");
    }

    //--------------------------UTILITY FUNCTIONS-----------------------------//
    //FUNCTION LIST:
    //public void add(int row1, double scalar, int row2)
    //public void multiply(int scalar, int row)
    //public void swap(int row1, int row2)
    //public double[] createArr(int row)
    //public void replace(int row, double [] arr)
    //private boolean setPivot(int col, int bgn, int end)
    //private boolean hasPivot(int col, int bgn, int end)
    //private int [] getPivotColCoords()
    //private boolean hasSolution()
    //private void error(String function, String errorStr)
    //private void error(String function, String[] var)

    //Function: Add
    //@param row1       index of specified row
    //       scalar     value to be multiplied to row
    //       row2       index of specified row
    //Adds row1 multiplied by scalar to row2 and places result in row2
    //row1 * scalar + row2 => row2
    private void add(int row1, double scalar, int row2) {
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
    private void multiply(double scalar, int row) {
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
    private void swap(int row1, int row2) {
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
    private double[] createArr(int row) {
        double arr[] = new double[matrix[row].length];
        if (inBounds(row, matrix.length)) {
            for (int col = 0; col < matrix[row].length; col++) {
                arr[col] = matrix[row][col];
            }
        }
        return arr;
    }

    //Function: Replace
    //@param row        a specified index of row to be replaced
    //       arr        the array for which to replace the row
    //Replaces values in the row with corresponding values in the array
    private void replace(int row, double [] arr) {
        if (inBounds(row, matrix.length) && matrix[row].length == arr.length) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = arr[col];
            }
        }
    }

    //Function: Set Pivot
    //@param col        the column where the pivot is to be selected
    //       bgn        the begin interval to search the interval (inclusive)
    //       end        the end interval (exclusive)
    //Rules for finding the pivot:
    //1) If pivot of "1" is found, use it as pivot
    //2) If no "1" is found, use lowest whole number that is not "0"
    //3) If no whole value is found, use any number that is not 0
    private boolean setPivot(int col, int bgn, int end) {
        int pivotRow = end;
        for (int row = bgn; row < end; row++) {
            //Find pivot with leading 1
            if (abs(matrix[row][col]) == 1) {
                pivotRow = row;
                break;
            }
            //Find pivot with the smallest whole number
            if (matrix[row][col] != 0 && matrix[row][col] % 1 == 0) {
                if (pivotRow != end) {
                    if (abs(matrix[row][col]) < abs(matrix[pivotRow][col]))
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
        if (pivotRow != end && pivotRow != bgn) {
            swap(pivotRow, bgn);
            return true;
        }
        return false;
    }

    //Function: Has Pivot
    //@return           whether there is a pivot (non zero number) in the
    //                  specified column
    private boolean hasPivot(int col, int bgn, int end) {
        for (int row = bgn; row < end; row++)
            if (matrix[row][col] != 0) return true;
        return false;
    }

    //Function: Get Pivot Column Coordinates
    //@return       coordinates of pivots at each column (row index)
    //Acquire the location of pivots, start at top left corner
    //-If there is pivot, move right and move down by one
    //-If no pivot, only move right, set "no pivot" to pivotCol
    private int [] getPivotColCoords() {
        int [] pivotColCoords = new int [matrix[0].length];
        int rowCount = 0;
        for (int col = 0; col < matrix[0].length; col++) {
            if (rowCount < matrix.length &&
                    matrix[rowCount][col] != 0) {
                pivotColCoords[col] = rowCount;
                rowCount++;
            } else pivotColCoords[col] = -1;
        }
        return pivotColCoords;
    }

    //Function: Has Solution
    //@return       whether there is a solution to the RREF form of the matrix
    //              or not
    //Checks whether there is a "zero row" but with a constant that is not zero
    //If such row is found and matrix is in RREF, then system is inconsistent
    private boolean hasSolution() {
        for (int row = 0; row < matrix.length; row++) {
            if (matrix[row][matrix[0].length - 1] != 0) {
                boolean isZeroRow = true;
                for (int col = 0; col < matrix[0].length - 1; col++) {
                    if (matrix[row][col] != 0) isZeroRow = false;
                }
                if (isZeroRow) return false;
            }
        }
        return true;
    }

    //Function: Back Up Matrix
    //Stores the current matrix in a backup matrix (to be restored later)
    private void backupMatrix() {
        if (!backedup) {
            backupMatrix = copyArray(matrix);
            backedup = true;
        } else
            error("backupMatrix", "a version of the matrix has" +
                    "already been backed up");
    }

    //Function: Restore Matrix
    //Restores the matrix in backup
    private void restoreMatrix() {
        if (backedup) {
            matrix = backupMatrix;
            backedup = false;
        } else
            error("restoreMatrix",
                    "there was no backed up matrix");
    }

    //Function: Error (1)
    //@param function   the name of the function the error occurred in
    //       errorStr   the error message
    //Prints the name of the method that failed along with error message
    private void error(String function, String errorStr) {
        if (errorStr.equals("augment"))
            errorStr = "operation does not support augmented matrices";
        else if (errorStr.equals("dimension"))
            errorStr = "matrix has invalid dimensions for this operation";
        else if (errorStr.equals("square"))
            errorStr = "matrix is not square";
        System.err.print("Error occurred while executing method '" + function
                + "', " + errorStr);
    }

    //Function: Error (2)
    //@param function   the name of the function the error occurred in
    //       var        the names of the parameters of that function
    //Compiles the variable names into a error string and calls the primary
    //error function to print the message
    private void error(String function, String[] var) {
        String errorStr = "";
        for (int i = 0; i < var.length; i++) {
            if (i != 0) errorStr += " or ";
            errorStr += var[i];
        }
        errorStr += " may be out of bounds of the matrix";
        error(function, errorStr);
    }

    //--------------------------ASSIST FUNCTIONS------------------------------//
    //FUNCTION LIST:
    //public boolean inBounds(int a, int b, int upBound)
    //public boolean inBounds(int a, int upBound)
    //private double [][] copyArray(double [][] arr)

    //Function: In Bounds (1)
    //Checks if the values a and b are between 0 and upBound
    private boolean inBounds(int a, int b, int upBound) {
        return a >= 0 && b >= 0 && a < upBound && b < upBound;
    }

    //Function: In Bounds (2)
    //Checks if value a is between 0 and upBound
    private boolean inBounds(int a, int upBound) {
        return inBounds(a, 0, upBound);
    }

    //Function: Copy Array
    //@param arr    the array to be copied
    //@return       the copy of the array
    //Creates a new instance of the array in memory
    private double [][] copyArray(double [][] arr) {
        double arrCopy[][] = new double[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                arrCopy[i][j] = arr[i][j];
        return arrCopy;
    }

    private double abs(double num) {
        return num >= 0 ? num : -num;
    }

    //---------------------------MISC FUNCTIONS-------------------------------//
    //FUNCTION LIST:
    //public double [][] getMatrixCopy()
    //public String toString()

    //Function: Get Matrix
    //@return       the matrix array
    public double [][] getMatrixCopy() {
        return copyArray(matrix);
    }

    /*public void setMatrixArray(double [][] newMatrix) {
        int colBound;
        if (!augmented) colBound = matrix[0].length;
        else colBound = matrix[0].length - 1;
        if (newMatrix.length == matrix.length
                && newMatrix[0].length == colBound)
            for (int row = 0; row < matrix[0].length; row++)
                for (int col = 0; col < matrix[0].length; col++)
                    matrix[row][col] = newMatrix[row][col];
    }

    public void setMatrixVector(double [] vector) {
        if (augmented) {

        }
    }*/

    //Function: To String
    //Prints out the matrix in grid form
    public String toString() {
        String matrixStr = "";
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                if (augmented && col == matrix[0].length - 1) matrixStr += "|";
                matrixStr += matrix[row][col] + "\t";
            }
            matrixStr += "\n";
        }
        return matrixStr;
    }

    //------------------------------------------------------------------------//

}
