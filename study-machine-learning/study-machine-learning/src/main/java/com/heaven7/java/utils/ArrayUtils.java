package com.heaven7.java.utils;

import java.util.ArrayList;
import java.util.List;

public final class ArrayUtils {

    public static float[] multiplyF(float[] a, float[] b) {
        assert a.length == b.length;
        float[] ret = new float[a.length];
        for (int i = 0; i < ret.length; i++){
            ret[i] = a[i] * b[i];
        }
        return ret;
    }
    public static List<Float> multiplyF(List<Float> a, List<Float> b) {
        assert a.size() == b.size();
        List<Float> ret = new ArrayList<>();
        for (int i = 0; i < a.size(); i++){
            ret.add(a.get(i) * b.get(i));
        }
        return ret;
    }
}
