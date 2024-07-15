package com.heaven7.java.mat;

import java.util.ArrayList;
import java.util.List;

public class Mat3<T> {

    private final List<List<List<T>>> mat;

    public Mat3(List<List<List<T>>> mat) {
        this.mat = mat;
    }
    public Mat3(int dim1, int dim2, int dim3, T defVal) {
        this.mat = new ArrayList<>();
        for (int i = 0; i < dim1; i++) {
            List<List<T>> list_dim2 = new ArrayList<>();
            for (int j = 0; j < dim2; j++) {
                List<T> list_dim3 = new ArrayList<>();
                for (int k = 0; k < dim3; k++) {
                    list_dim3.add(defVal);
                }
                list_dim2.add(list_dim3);
            }
            mat.add(list_dim2);
        }
    }
    public T getElement(int i, int j, int k){
        if(mat.size() == 0 || mat.get(0).size() == 0 || mat.get(0).get(0).size() == 0){
            return null;
        }
        i = MatUtils.getIndex(mat.size(), i);
        j = MatUtils.getIndex(mat.get(0).size(), j);
        k = MatUtils.getIndex(mat.get(0).get(0).size(), k);
        return mat.get(i).get(j).get(k);
    }

    public boolean setElement(int i, int j, int k, T val){
        if(mat.size() == 0 || mat.get(0).size() == 0 || mat.get(0).get(0).size() == 0){
            return false;
        }
        i = MatUtils.getIndex(mat.size(), i);
        j = MatUtils.getIndex(mat.get(0).size(), j);
        k = MatUtils.getIndex(mat.get(0).get(0).size(), k);
        mat.get(i).get(j).set(k, val);
        return true;
    }
}
