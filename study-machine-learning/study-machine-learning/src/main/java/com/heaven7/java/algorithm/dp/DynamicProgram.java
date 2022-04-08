package com.heaven7.java.algorithm.dp;

/**
 * dynamic program: 动态规划主要利用局部最优解 来解决全局最优解的问题.
 * 如果 后面依赖 前面的结果： 则用前向传递. 比如 基因测序-序列比对.
 * 比如 最短路径问题。则逆向传递。
 */
public class DynamicProgram {

    //爬楼梯
    public static int climbStairs(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for(int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
/*
理解状态转移的概率：
假设有3个状态机：
    State 1: produce a = 0.8 , b = 0.1, c = 0.1
    State 2: produce a = 0.7 , b = 0.2, c = 0.1
    State 3: produce a = 0.5 , b = 0.3, c = 0.2
状态转移的概率：
   State 1:  1->2 = 0.3,   1->1 = 0.5,  1->3 = 0.2
   State 2:  2->3 = 0.5 ,  2->2 = 0.5
   State 3:  3->3 = 1

   则产生字符串 'aabc' 的概率：
   1123 ->  0.8 * 0.5(T) * 0.8 * 0.3(T) * 0.2 * 0.5(T) * 0.2
   1233 ->  0.8 * 0.3(T) * 0.7 * 0.5(T) * 0.3 * 1(T) * 0.2
   ...
 */
