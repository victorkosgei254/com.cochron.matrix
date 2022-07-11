package com.matrix;

public class Matrix {
    private int rows, columns;
    private String name;
    private double matrix[][];
    private Thread[] threads;
    private double[][] L;
    private double[][] U;

    public Matrix() {

    }

    public void setDimension(int x, int y) {
        rows = x;
        columns = y;
        matrix = new double[rows][columns];
        L = unity();
        U = new double[rows][rows];
    }

    public void setName(String n) {
        name = n;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    double getElement(int row, int column) {
        return matrix[row][column];
    }

    double[][] getMatrix() {
        return matrix;
    }

    void addElement(int row, int column, double element) {
        matrix[row][column] = element;
    }

    Matrix transpose() {
        double[][] orginalMatrix = getMatrix();
        Matrix newMatrix = new Matrix();
        newMatrix.setDimension(columns, rows);

        threads = new Thread[columns];
        for (int i = 0; i < columns; i++) {
            int k = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < rows; j++) {
                        newMatrix.addElement(k, j, orginalMatrix[j][k]);
                        // newMatrix[k][j];
                    }
                }
            });

            threads[i] = t;
        }

        for (Thread t : threads)
            t.start();

        for (Thread t : threads)
            try {
                t.join();
            } catch (InterruptedException e) {
                ;
            }

        return newMatrix;
    }

    Matrix addMatrix(Matrix a) {
        rows += a.getRows();
        columns += a.getColumns();
        return this;
    }

    void decompose() {
        U[0] = matrix[0];
        for (int i = 1; i < columns; i++) {
            double[] prev = matrix[i - 1];
            int mark = 1;
            for (int j = 0; j < rows; j++) {

                // prev[j] / matrix[i][j];
                if (mark < rows)
                    L[i][j] = matrix[mark][j] / prev[j];
                mark++;
            }

        }
    }

    double[][] unity() {
        double[][] l = new double[rows][rows];
        for (int i = 0; i < rows; i++) {
            l[i][i] = 1;
        }
        return l;
    }

    public void showMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%8.2f", matrix[i][j]);
            }
            System.out.println();
        }
    }

    public void showLU() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%8.4f", L[i][j]);
            }
            System.out.println();
        }
    }

}
