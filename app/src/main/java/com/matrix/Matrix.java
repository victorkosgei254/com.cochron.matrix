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
