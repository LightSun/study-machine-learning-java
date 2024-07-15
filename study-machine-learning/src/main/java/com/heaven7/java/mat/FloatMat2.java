package com.heaven7.java.mat;

import com.heaven7.java.visitor.ResultVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloatMat2 implements IMat2<Float> {

    private final float[][] mat;

    public FloatMat2(float[][] mat) {
        this.mat = mat;
    }
    public FloatMat2(int rowCount, int colCount, float defVal) {
        this.mat = new float[rowCount][colCount];
        for (int i = 0; i < rowCount; i++){
            for (int j = 0; j < colCount; j++){
                mat[i][j] = defVal;
            }
        }
    }
    @Override
    public List<Float> getLastColumn(){
        if(mat.length ==0){
            return Collections.emptyList();
        }
        return getColumn(mat[0].length - 1);
    }
    @Override
    public List<Float> getColumn(int col_index) {
        if(mat.length ==0){
            return Collections.emptyList();
        }
        if(col_index < 0){
            col_index = mat[0].length + col_index;
        }
        List<Float> list = new ArrayList<>();
        int size = mat.length;
        for (int i = 0 ; i < size ; i ++){
            list.add(mat[i][col_index]);
        }
        return list;
    }

    @Override
    public List<List<Float>> getColumns(List<Integer> col_Indexes) {
        if(mat.length ==0){
            return Collections.emptyList();
        }
        List<List<Float>> list = new ArrayList<>();
        final int size = mat.length;
        for(Integer idx : col_Indexes){
            List<Float> list1 = new ArrayList<>();
            for (int i = 0 ; i < size ; i ++){
                list1.add(mat[i][idx]);
            }
            list.add(list1);
        }
        return list;
    }
    @Override
    public List<Float> getColumns(int row_index, List<Integer> col_Indexes) {
        if(mat.length ==0){
            return Collections.emptyList();
        }
        row_index = MatUtils.getIndex(mat.length, row_index);
        //
        List<Float> ret = new ArrayList<>();
        float[] arr = mat[row_index];
        for(Integer i : col_Indexes){
            i = MatUtils.getIndex(arr.length, i);
            ret.add(arr[i]);
        }
        return ret;
    }

    @Override
    public List<Float> getRow(int row_index) {
        if(row_index < 0){
            row_index = mat.length + row_index;
        }
        List<Float> list1 = new ArrayList<>();
        for (int i = 0 ; i < mat[row_index].length ; i ++){
            list1.add(mat[row_index][i]);
        }
        return list1;
    }

    @Override
    public List<List<Float>> getRows(List<Integer> row_indexes) {
        List<List<Float>> ret = new ArrayList<>();
        for (int row_index : row_indexes){
            if(row_index < 0){
                row_index = mat.length + row_index;
            }
            List<Float> list1 = new ArrayList<>();
            for (int i = 0 ; i < mat[row_index].length ; i ++){
                list1.add(mat[row_index][i]);
            }
            ret.add(list1);
        }
        return ret;
    }
    @Override
    public Float getElement(int row_index, int col_index){
        if(mat.length == 0){
            return null;
        }
        if(row_index < 0){
            row_index = mat.length + row_index;
        }
        if(col_index < 0){
            col_index = mat[0].length + col_index;
        }
        return mat[row_index][col_index];
    }
    @Override
    public boolean setElement(int row_index, int col_index, Float val){
        if(mat.length == 0 || mat[0].length == 0){
            return false;
        }
        if(row_index < 0){
            row_index = mat.length + row_index;
        }
        if(col_index < 0){
            col_index = mat[0].length + col_index;
        }
        mat[row_index][col_index] = val;
        return true;
    }

    @Override
    public boolean readWriteElement(int row_index, int col_index, Object param, ResultVisitor<Float, Float> visitor) {
        if(mat.length == 0 || mat[0].length == 0){
            return false;
        }
        if(row_index < 0){
            row_index = mat.length + row_index;
        }
        if(col_index < 0){
            col_index = mat[0].length + col_index;
        }
        Float v = visitor.visit(mat[row_index][col_index], param);
        mat[row_index][col_index] = v;
        return true;
    }

    //--------------------------------------------------
    @Override
    public Object getMat() {
        return mat;
    }
    @Override
    public IMat2<Float> toWrapperMat(){
        List<List<Float>> ret = new ArrayList<>();
        for (int idx = 0 ; idx < mat.length ; idx ++ ){
            List<Float> list1 = new ArrayList<>();
            for (int i = 0 ; i < mat[idx].length ; i ++){
                list1.add(mat[idx][i]);
            }
            ret.add(list1);
        }
        return new Mat2<>(ret);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mat.length);
        if(mat.length > 0){
            sb.append("x").append(mat[0].length);
        }
        sb.append("[\n");
        for (int i = 0; i < mat.length; i++){
            sb.append("[");
            for (int j = 0, jsize = mat[i].length; j < jsize; j++){
                sb.append(mat[i][j]);
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
