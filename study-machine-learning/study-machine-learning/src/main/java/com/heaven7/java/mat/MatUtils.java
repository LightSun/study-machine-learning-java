package com.heaven7.java.mat;

public final class MatUtils{

    public static int getIndex(int size , int expect_index){
        return expect_index >= 0 ? expect_index : size + expect_index;
    }
}
