package com.heaven7.java.algorithm.dp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 序列比对: 全局/局部
 * match.
 * gap open
 * gap extending
 * demo:
 ```
 LSPADK
 LTPEEK
 -> ------LSPADK
    LTPEEK------
 ```
 */
public class SequenceAlignment<T> {

    private Context context;
    private List<T> sequences1;
    private List<T> sequences2;

    public SequenceAlignment(List<T> sequences1, List<T> sequences2) {
        this.sequences1 = sequences1;
        this.sequences2 = sequences2;
    }

    public static void main(String[] args) {
        List<String> s1 = Arrays.asList("A", "A", "G");
        List<String> s2 = Arrays.asList("A", "G", "C");
        SequenceAlignment<String> sa = new SequenceAlignment<String>(s1, s2);
        Rule rule = new Rule();
        rule.match = 2;
        rule.gapOpen = -5;
        int[][] tab = sa.align(rule, new Comparator<String>() {
            @Override
            public int compare(String t1, String t2) {
                return t1.compareTo(t2);
            }
        });
        for (int i = 0 ; i < tab.length ; i ++){
            for (int j = 0 ; j < tab[i].length ; j ++){
                System.out.print(tab[i][j]);
                if( j != tab[i].length - 1){
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        /*
         0, -5, -10, -15
         -5, 2, -3, -8
         -10, -3, -3, -1
         -15, -8, -8, -6
         * */
    }
    public void simpleAlign(Rule rule){

    }

    public int[][] align(Rule rule, Comparator<T> c){
        /*
            s e q u e n c e s 1 ...
          s
          e
          q
          u
          e
          n
          c
          e
          s
          2
          .
          .
          .
         */
        int n = sequences1.size();
        int[][] tab = new int[n + 1][n +1];
        tab[0][0] = 0;
        //rows and cols : 1
        for (int j = 1 ; j < n + 1; j ++){
            tab[0][j] = rule.gapOpen + tab[0][j - 1];
            tab[j][0] = rule.gapOpen + tab[j- 1][0];
            // 局部比对增加下限0
            //tab[0][j] = Math.max(0, tab[0][j]);
            //tab[j][0] = Math.max(0, tab[j][0]);
        }
        int s1, s2, s3, v;
        for(int k = 1 ; k < n + 1; k ++){
            for(int i = 1 ; i < n + 1; i ++){
                //tab[k][i], tab[i][k]
                if(c.compare(sequences1.get(i-1), sequences2.get(k - 1)) == 0){
                    s1 = rule.match + tab[k-1][i-1];
                }else{
                    s1 = rule.gapOpen + tab[k-1][i-1];
                }
                s2 = rule.gapOpen + tab[k-1][i];
                s3 = rule.gapOpen + tab[k][i-1];
                v = Math.max(Math.max(s1, s2), s3);
                //局部比对增加下限0
                // v = Math.max(0, v);
                tab[k][i] =  v;
            }
        }
        return tab;
    }
    public static class Context{

    }
    public static class Rule{
        int match;
        int gapOpen;
        int gapExtending;
    }
}
