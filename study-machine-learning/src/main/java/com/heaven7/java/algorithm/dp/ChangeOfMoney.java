package com.heaven7.java.algorithm.dp;

import java.util.Arrays;

//DP
public class ChangeOfMoney {

    /**
     * 给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。
     * 如果没有任何一种硬币组合能组成总金额，返回 -1
     * @param coins 面值的硬币
     * @param amount 金额
     * @return 最少的硬币数量
     */
    public static int coinChange(int[] coins, int amount) {
        /*
        1.dp[i]表示amount = i时，需要的硬币数量；填充dp[],因为dp[]最大为amount个，所以将其初始化为amount+1；

        2.动态方程：dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1)；当前金额i所使用的金币个数，
        是dp[i] 和i - coin[j]金额所使用的金币个数dp[i - coin[i]] + 1，这里加一是因为coin[i]，消耗一个金币。
        * */
        int max = amount + 1;
        //dp[i] 表示amount = i时，需要的硬币数量
        int[] dp = new int[amount + 1];//dp[0] 到 dp[amount]
        //填充dp[],因为dp[]最大为amount个，所以将其初始化为amount+1
        Arrays.fill(dp, max);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                //面值比金额小.
                if (coins[j] <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        int[] arr = {5, 2, 1};
        System.out.println(coinChange(arr, 9));
    }
}
