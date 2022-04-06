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
