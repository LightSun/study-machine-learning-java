package com.heaven7.java.algorithm.align;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 布隆过滤器
 */
public class BloomFilterDemo {
    /**
     * 位数组的大小
     */
    private static  int SIZE;
    /**
     * 通过这个数组可以创建不同的哈希函数
     */
    private static  int[] SEEDS;
    /**
     * 位数组。数组中的元素只能是 0 或者 1
     */
    private BitSet bits;

    /**
     * 存放包含 hash 函数的类的数组
     */
    private SimpleHash[] func;

    private  Double autoClearRate;
    /**
     * 使用数量
     */
    private final AtomicInteger useCount = new AtomicInteger(0);
    /**
     * 静态内部类。hash 函数
     */
    public static class SimpleHash {

        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        /**
         * 计算 hash 值
         */
        public int hash(Object value) {
            int h;
            return (value == null) ? 0 : Math.abs(seed * (cap - 1) & ((h = value.hashCode()) ^ (h >>> 16)));
        }

    }

    /**
     * 误判率
     */
    public enum MisjudgmentRate {
        /**
         * 每个字符串分配4个位
         */
        VERY_SMALL(new int[] { 2, 3, 5, 7 }),
        /**
         * 每个字符串分配8个位
         */
        SMALL(new int[] { 2, 3, 5, 7, 11, 13, 17, 19 }),
        /**
         * 每个字符串分配16个位
         */
        MIDDLE(new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53 }),
        /**
         * 每个字符串分配32个位
         */
        HIGH(new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
                101, 103, 107, 109, 113, 127, 131 });

        private int[] seeds;

        private MisjudgmentRate(int[] seeds) {
            this.seeds = seeds;
        }

        public int[] getSeeds() {
            return seeds;
        }

        public void setSeeds(int[] seeds) {
            this.seeds = seeds;
        }

    }

    /**
     * 默认中等程序的误判率
     * @param dataCount 预期处理的数据规模，如预期用于处理1百万数据的查重，这里则填写1000000
     */
    public BloomFilterDemo(int dataCount){
        this(MisjudgmentRate.MIDDLE, dataCount, null);
    }

    /**
     *
     * @param rate 枚举类型的误判率
     * @param dataCount 预期处理的数据规模，如预期用于处理1百万数据的查重，这里则填写1000000
     * @param autoClearRate 自动清空过滤器内部信息的使用比率
     */
    public BloomFilterDemo(MisjudgmentRate rate, int dataCount, Double autoClearRate){
        long bitSize = rate.seeds.length * dataCount;
        if (bitSize < 0 || bitSize > Integer.MAX_VALUE) {
            throw new RuntimeException("位数太大溢出了，请降低误判率或者降低数据大小");
        }
        //n = dataCount
        SEEDS = rate.seeds;
        SIZE = (int) bitSize;
        //k = SEEDS.length
        func = new SimpleHash[SEEDS.length];
        for (int i = 0; i < SEEDS.length; i++) {
            func[i] = new SimpleHash(SIZE, SEEDS[i]);
        }
        //m = SIZE
        bits = new BitSet(SIZE);
        this.autoClearRate = autoClearRate;
    }

    /**
     * 添加元素到位数组
     */
    public void add(Object value) {

        checkNeedClear();

        if(!contains(value)){
	       for (SimpleHash f : func) {
	           bits.set(f.hash(value), true);
	       }
	       useCount.getAndIncrement();
        }
        
    }

    /**
     * 判断指定元素是否存在于位数组
     */
    public boolean contains(Object value) {
        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }

    /**
     * 检查是否需要清空
     */
    private void checkNeedClear() {
        if (autoClearRate != null) {
            if (getUseRate() >= autoClearRate) {
                synchronized (this) {
                    if (getUseRate() >= autoClearRate) {
                        bits.clear();
                        useCount.set(0);
                    }
                }
            }
        }
    }
    public double getUseRate() {
        return (double) useCount.intValue() / (double) SIZE;
    }

    public static void main(String[] args) {
        String value1 = "fffdfg";
        String value2 = "ddbgbfgbfbbgfb";
        BloomFilterDemo filter = new BloomFilterDemo(2<<24);
        System.out.println(filter.contains(value1));
        System.out.println(filter.contains(value2));
        filter.add(value1);
        filter.add(value2);
        System.out.println(filter.contains(value1));
        System.out.println(filter.contains(value2));
        Integer value11 = 13423;
        Integer value21 = 22131;
        System.out.println(filter.contains(value11));
        System.out.println(filter.contains(value21));
        filter.add(value11);
        filter.add(value21);
        System.out.println(filter.contains(value11));
        System.out.println(filter.contains(value21));

        System.out.println("----------");
        //compute k
        int seed_len = 12;
        long n = 1100000000L;
        long m = n * seed_len; //seed_len 这个数字越大，错误率越低
        long k = (long) Math.abs(Math.log(Math.pow(2, seed_len)));
       // long k2 = (long) Math.abs(Math.log(2) * seed_len); // = k
        System.out.println("k = " + k);
      //  System.out.println("k2 = " + k2);
        //incorrect p.
        double temp = 1 - Math.pow(Math.E, -(k*n * 1.0/m));
        float p = (float) Math.pow(temp, k);
        System.out.println( " p = " + p);
        /*
         k = 5            + 1
         p = 0.021679217  0.021577142
         */
    }
}
