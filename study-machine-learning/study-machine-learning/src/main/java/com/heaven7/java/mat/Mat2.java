package com.heaven7.java.mat;

import com.heaven7.java.visitor.ResultVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mat2<T> implements IMat2<T> {

    private final List<List<T>> mat;

    public Mat2(List<List<T>> mat) {
        this.mat = mat;
    }
    public Mat2(int rowCount, int colCount, T defVal) {
        this.mat = new ArrayList<>();
        for (int i = 0; i < rowCount; i++){
            List<T> list = new ArrayList<>();
            for (int j = 0; j < colCount; j++){
                list.add(defVal);
            }
            mat.add(list);
        }
    }
    @Override
    public List<T> getLastColumn(){
        if(mat.size() ==0){
            return Collections.emptyList();
        }
        return getColumn(mat.get(0).size() - 1);
    }
    @Override
    public List<T> getColumn(int col_index){
        if(mat.size() ==0){
            return Collections.emptyList();
        }
        if(col_index < 0){
            col_index = mat.get(0).size() + col_index;
        }
        List<T> list = new ArrayList<>();
        int size = mat.get(0).size();
        for (int i = 0 ; i < size ; i ++){
            list.add(mat.get(i).get(col_index));
        }
        return list;
    }
    @Override
    public List<List<T>> getColumns(List<Integer> col_Indexes){
        if(mat.size() ==0){
            return Collections.emptyList();
        }
        List<List<T>> list = new ArrayList<>();
        final int size = mat.get(0).size();
        for(Integer col_index : col_Indexes){
            if(col_index < 0){
                col_index = size + col_index;
            }
            List<T> list1 = new ArrayList<>();
            for (int i = 0 ; i < size ; i ++){
                list1.add(mat.get(i).get(col_index));
            }
            list.add(list1);
        }
        return list;
    }

    @Override
    public List<T> getColumns(int row_index, List<Integer> col_Indexes) {
        if(mat.size() == 0){
            return Collections.emptyList();
        }
        row_index = MatUtils.getIndex(mat.size(), row_index);
        List<T> list1 = new ArrayList<>();
        List<T> ts = mat.get(row_index);
        for (int i: col_Indexes){
            i = MatUtils.getIndex(ts.size(), i);
            list1.add(ts.get(i));
        }
        return list1;
    }

    @Override
    public List<T> getRow(int index){
        return mat.get(index);
    }
    @Override
    public List<List<T>> getRows(List<Integer> row_indexes){
        List<List<T>> list = new ArrayList<>();
        for (Integer row_index : row_indexes){
            if(row_index < 0){
                row_index = mat.size() + row_index;
            }
            list.add(mat.get(row_index));
        }
        return list;
    }
    @Override
    public T getElement(int row_index, int col_index){
        if(mat.size() == 0){
            return null;
        }
        if(row_index < 0){
            row_index = mat.size() + row_index;
        }
        if(col_index < 0){
            col_index = mat.get(0).size() + col_index;
        }
        return mat.get(row_index).get(col_index);
    }
    @Override
    public boolean setElement(int row_index, int col_index, T val){
        if(mat.size() == 0 || mat.get(0).size() == 0){
            return false;
        }
        if(row_index < 0){
            row_index = mat.size() + row_index;
        }
        if(col_index < 0){
            col_index = mat.get(0).size() + col_index;
        }
        mat.get(row_index).set(col_index, val);
        return true;
    }
    @Override
    public boolean readWriteElement(int row_index, int col_index, Object param, ResultVisitor<T, T> visitor){
        if(mat.size() == 0 || mat.get(0).size() == 0){
            return false;
        }
        if(row_index < 0){
            row_index = mat.size() + row_index;
        }
        if(col_index < 0){
            col_index = mat.get(0).size() + col_index;
        }
        T tmp = mat.get(row_index).get(col_index);
        mat.get(row_index).set(col_index, visitor.visit(tmp, param));
        return true;
    }

    //-------------------------------------------
    @Override
    public Object getMat(){
        return mat;
    }
    @Override
    public IMat2<T> toWrapperMat() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mat.size());
        if(mat.size() > 0){
            sb.append("x").append(mat.get(0).size());
        }
        sb.append("[\n");
        for (int i = 0; i < mat.size(); i++){
            sb.append("[");
            for (int j = 0, jsize = mat.get(i).size(); j < jsize; j++){
                sb.append(mat.get(i).get(j));
                if(j != jsize -1){
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        }
        sb.append("]");
        return sb.toString();
    }
}
