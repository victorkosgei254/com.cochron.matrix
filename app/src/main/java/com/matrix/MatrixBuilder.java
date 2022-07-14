package com.matrix;

public class MatrixBuilder {
    private Matrix matrix = new Matrix();
    private Thread threads[];
    private int rows, columns;

    MatrixBuilder dimensions(int row, int column) {
        matrix.setDimension(row, column);
        threads = new Thread[column];
        rows = row;
        columns = column;
        return this;
    }

    MatrixBuilder addID(String id) {
        matrix.setName(id);
        return this;
    }

    private void fillMatrix(double... elements) {
        for (int i = 0; i < columns; i++) {
            int k = i;

            Thread t = new Thread(new Runnable() {
                int mark = k;

                @Override
                public void run() {
                    for (int j = 0; j < rows; j++) {
                        if (mark < elements.length) {
                            matrix.addElement(j, k, elements[mark]);
                        }
                        mark = mark + rows;
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
    }

    MatrixBuilder addElements(double... elements) {
        fillMatrix(elements);
        return this;
    }

    MatrixBuilder addRows(int row, double... elements) {
        for (int i = 0; i <= columns; i++) {
            matrix.addElement(row, i, elements[i]);
        }
        return this;
    }

    MatrixBuilder addColumns(int column, double... elements) {
        for (int i = 0; i <= columns; i++) {
            matrix.addElement(i, column, elements[i]);
        }
        return this;
    }

    Matrix diagonalMatrix(int row, int column, double element) {
        matrix.setDimension(row, column);
        for (int i = 0; i < column; i++) {
            matrix.addElement(i, i, element);
        }
        return matrix;
    }

    Matrix diagonalMatrix(int row, int column, double... diagonalElements) {
        matrix.setDimension(row, column);
        for (int i = 0; i < column; i++) {
            matrix.addElement(i, i, diagonalElements[i]);
        }
        return matrix;
    }

    Matrix unityMatrix(int dimensions) {
        matrix.setDimension(dimensions, dimensions);
        for (int i = 0; i < dimensions; i++) {
            matrix.addElement(i, i, 1);
        }
        return matrix;
    }

    Matrix nullMatrix(int dimensions) {
        matrix.setDimension(dimensions, dimensions);
        for (int i = 0; i < dimensions; i++) {
            matrix.addElement(i, i, 0);
        }
        return matrix;
    }

    Matrix columnMatrix(int rows, double... rowElements) {
        matrix.setDimension(rows, 1);
        for (int i = 0; i < rows; i++) {
            matrix.addElement(i, 0, rowElements[i]);
        }
        return matrix;
    }

    Matrix rowMatrix(int columns, double... columnElements) {
        matrix.setDimension(1, columns);
        for (int i = 0; i < columns; i++) {
            matrix.addElement(0, i, columnElements[i]);
        }
        return matrix;
    }

    Matrix build() {
        return matrix;
    }
}