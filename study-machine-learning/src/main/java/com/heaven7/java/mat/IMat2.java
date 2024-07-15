package com.heaven7.java.mat;

import com.heaven7.java.visitor.ResultVisitor;

import java.util.List;

public interface IMat2<T> {

    List<T> getLastColumn();
    List<T> getColumn(int column_index);

    List<List<T>> getColumns(List<Integer> col_Indexes);
    List<T> getColumns(int row_index, List<Integer> col_Indexes);

    List<T> getRow(int index);

    List<List<T>> getRows(List<Integer> row_indexes);

    Object getMat();

    IMat2<T> toWrapperMat();

    T getElement(int row_index, int col_index);
    boolean setElement(int row_index, int col_index, T val);
    boolean readWriteElement(int row_index, int col_index, Object param, ResultVisitor<T, T> visitor);
}
