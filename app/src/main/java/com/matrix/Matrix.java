package com.matrix;

import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.multi.MultiInternalFrameUI;

public class Matrix {
    private int rows, columns;
    private String name;
    private double matrix[][];
    private Thread[] threads;
    private double[][] L;
    private double[][] U;

    public Matrix() {

    }

    public void setDimension(int row, int column) {
        rows = row;
        columns = column;
        matrix = new double[rows][columns];
        L = unity();
        U = new double[rows][columns];
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
        // System.out.printf("%d-%d= %4.2f\n", row, column, element);
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
        if (a.rows != rows || a.columns != columns)
            return null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = matrix[i][j] + a.matrix[i][j];
            }
        }

        return this;
    }

    Matrix subtractMatrix(Matrix a) {
        if (a.rows != rows || a.columns != columns)
            return null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = matrix[i][j] - a.matrix[i][j];
            }
        }

        return this;
    }

    Matrix multiplyBy(double factor) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = matrix[i][j] * factor;
            }
        }

        return this;
    }

    Matrix dotProduct(Matrix a) {
        if (columns != a.rows) {
            System.out.println("Returning null");
            return null;
        }

        double[][] newMatrix = new double[rows][a.columns];
        for (int i = 0; i < a.columns; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.println("--");
                System.out.println(i);
                System.out.println(matrix[j][i + 1]);
                System.out.println("***");
                newMatrix[j][i] = matrix[j][i] * a.matrix[i][i] + matrix[j][i + 1] * a.matrix[i + 1][i];
                System.out.println(matrix[j][i] * a.matrix[i][i] + matrix[j][i + 1] *
                        a.matrix[i + 1][i]);
                // System.out.printf("%8.2f - %8.2f - %8.2f - %8.2f\n", matrix[j][i],
                // a.matrix[i][i], matrix[j][i + 1],
                // a.matrix[i + 1][i]);
            }
        }

        matrix = newMatrix;
        return this;
    }

    Matrix decompose() {
        U = matrix;

        for (int i = 0; i < columns; i++) {
            for (int j = i + 1; j < rows; j++) {

                double residue = U[j][i] / U[i][i];

                for (int k = 0; k < columns; k++) {

                    U[j][k] = U[j][k] - residue * U[i][k];

                    L[j][i] = residue;
                }
            }
        }

        return this;
    }

    double[][] unity() {
        double[][] l = new double[rows][rows];
        for (int i = 0; i < rows; i++) {
            l[i][i] = 1;
        }
        return l;
    }

    double[] unityRow(int row) {
        double[] l = new double[row];
        for (int i = 0; i < rows; i++) {
            l[i] = 1;
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
        System.out.println("U");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%12.4f", U[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("L");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.printf("%12.4f", L[i][j]);
            }
            System.out.println();
        }
    }

}
