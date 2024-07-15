package com.heaven7.java.algorithm.graph;

import java.util.*;

/**
 *
 * 最长子串问题:
 *      给定k个可以有重叠部分的短序列，要求得到包含所有短序列的最短长串
 * 应用： 基因测序，拼接碱基序列
 *       最短路径?
 */
/*
1946年，荷兰数学家尼古拉·德·布鲁因(Nicolaas de Bruijn)开始对“超串问题”感兴趣：找到一个最短的循环“超串”，它包含给定字母表中所有长度为k (k-mers)的可能“子串”。

在包含n个符号的字母表中存在nk  k-mers，例如，给定由A、T、G和C组成的字母表，这是43 = 64个三核苷酸。

如果我们的字母表是0和1，那么所有可能的3-mers都简单地由8个3位二进制数字给出:000，001，010，011，100，101，110，111。

 圆形超弦0001110100不仅包含所有的3-mer，而且尽可能短，因为它包含每个3-mer恰好一次。

 但是，在任意k值和任意字母的情况下，如何为所有k-mers构造这样的超弦呢?德布鲁因借用欧拉对哥尼斯堡桥问题的解来回答这个问题。

简而言之，构造一个图B(原始图称为de Bruijn图)，其中每个可能的(k-1)-mer都赋给一个节点：通过有向边连接一个(k-1)-mer和第二个(k-1)-mer，

如果这是一些k-mer，其前缀是前者，后缀是后者。de Bruijn图的边代表了所有可能的k-mer， B中的欧拉循环代表了一个最短的(循环的)超弦，它只包含一个k-mer。
 */
//https://blog.csdn.net/weixin_40695088/article/details/122294595
public class DeBruijnModule {

    private List<String> table = Arrays.asList(
        "0", "1"
    ); // 字符 字典
    private int kmer = 4;

    public List<KmerChild> generateKmerStrs(){
        List<KmerChild> first = generateKmerStrs0(null);
        for (int i = 1 ; i < kmer ; i ++){
            first = generateKmerStrs0(first);
        }
        return first;
    }
    private List<KmerChild> generateKmerStrs0(List<KmerChild> p){
        // table.size() ^ kmer
        List<KmerChild> ret = new ArrayList<>();
        if(p != null && p.size() > 0){
            for(KmerChild c : p){
                for(int i = 0 ; i < table.size() ; i ++){
                    KmerChild child = new KmerChild();
                    //
                    child.chs.addAll(c.chs);
                    child.chs.add(table.get(i));
                    ret.add(child);
                }
            }
        }else{
            for(int i = 0 ; i < table.size() ; i ++){
                KmerChild child = new KmerChild();
                child.chs.add(table.get(i));
                ret.add(child);
            }
        }
        return ret;
    }

    /** generate graph by k-1 */
    public Graph generateKmer_1_graph(){
        List<KmerChild> list = generateKmerStrs();
        HashMap<String, List<KmerChild>> left_map = new HashMap<>();
        HashMap<String, List<KmerChild>> right_map = new HashMap<>();
        HashMap<String, Node> node_map = new HashMap<>();
        for(int i = 0 ; i < list.size() ; i ++){
            KmerChild child = list.get(i);
            child.k_1();
            left_map.computeIfAbsent(child.k_1_left, (t)-> new ArrayList<>()).add(child);
            right_map.computeIfAbsent(child.k_1_right, (t)-> new ArrayList<>()).add(child);
            node_map.put(child.k_1_left, new Node(child.k_1_left));
            node_map.put(child.k_1_right, new Node(child.k_1_right));
        }
        ArrayList<Node> nodes = new ArrayList<>(node_map.values());
        for (Node n : nodes){
            // n.next
            List<KmerChild> cs = right_map.get(n.text);
            for (KmerChild c : cs ){
                n.addPre(c.getText(), node_map.get(c.k_1_left));
            }
            cs = left_map.get(n.text);
            for (KmerChild c : cs ){
                n.addNext(c.getText(), node_map.get(c.k_1_right));
            }
        }
        return new Graph(nodes);
    }

    public static void main(String[] args) {
        DeBruijnModule module = new DeBruijnModule();
//        List<KmerChild> list = module.generateKmerStrs();
//        for (KmerChild c : list){
//            System.out.println(c.toString());
//        }
        Graph graph = module.generateKmer_1_graph();
        for (int i = 0; i < graph.nodes.size(); i++) {
            Node node = graph.nodes.get(i);
            int nextSize = node.getNextSize();
            System.out.println(" ===== " + node.text + " =======");
            for (int j = 0; j < nextSize; j++) {
                System.out.printf("next: %s ---( %s )--> %s .\n", node.text, node.nextEdge.get(j), node.next.get(j).text);
            }
            int preSize = node.getPreSize();
            for (int j = 0; j < preSize; j++) {
                System.out.printf("pre: %s <---( %s )-- %s .\n", node.text, node.preEdge.get(j), node.pre.get(j).text);
            }
            System.out.println();
        }
        Node node = graph.findStartNode();
        StringBuilder sb = new StringBuilder();
        sb.append(node.text);


    }
    //---------------
    public static class KmerChild{
        List<String> chs = new ArrayList<>();
        String k_1_left;
        String k_1_right;
        String text;

        public void k_1(){
            k_1_left = "";
            k_1_right = "";
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            int c = chs.size();
            for (int i = 0; i < c; i++) {
                if(i != c - 1){
                    sb1.append(chs.get(i));
                }
                if(i != 0){
                    sb2.append(chs.get(i));
                }
            }
            k_1_left = sb1.toString();
            k_1_right = sb2.toString();
        }

        public String getText(){
            if(text == null){
                StringBuilder sb = new StringBuilder();
                for (String str: chs){
                    sb.append(str);
                }
                text = sb.toString();
            }
            return text;
        }

        @Override
        public String toString() {
           return getText();
        }
    }

    public static class Node{
        String text;
        List<Node> next;
        List<Node> pre;
        List<String> nextEdge;
        List<String> preEdge;

        public Node(String s) {
            this.text = s;
        }
        public int getPreSize(){
            return pre != null ? pre.size() : 0;
        }
        public int getNextSize(){
            return next != null ? next.size() : 0;
        }
        public List<List<Node>> generateSequence(Node head, List<List<Node>> src) {
            if(src == null){
                List<Node> nodes = new ArrayList<>();
                nodes.add(this);
                List<List<Node>> _list = new ArrayList<>();
                _list.add(nodes);
                return generateSequence0(head, _list);
            }else{
                for(List<Node> _l : src){
                    _l.add(this);
                }
                return generateSequence0(head, src);
            }
        }
        //node 可以重复， 边不能重复
        public List<List<Node>> generateSequence0(Node head, List<List<Node>> ret) {
            int size = getNextSize();
            if(size == 0){
                return ret;
            }
            List<List<Node>> list = new ArrayList<>();
            for(int i = 0 ; i < size ; i ++){
                Node node = next.get(i);
                //TODO error, need change.
                if(node != head){
                    list.addAll(node.generateSequence(head, ret));
                }
            }
            if(list.isEmpty()){
                return ret;
            }
            return list;
        }
        static int _dis(Node src, Node dest){
            // 这里简便型。
            //return 1;
            //通用型
            String a = src.text;
            String b = dest.text;
            for (int i = 0; i < a.length(); i++)
            {
                if (a.length() - i <= b.length() &&
                        a.substring(i).equals(b.substring(0, a.length() - i + 1)))
                    return b.length() - a.length() + i;
            }
            return b.length();
        }

        public void addPre(String kmerText, Node _pre) {
            if(pre == null){
                pre = new ArrayList<Node>();
                preEdge = new ArrayList<>();
            }
            pre.add(_pre);
            preEdge.add(kmerText);
        }
        public void addNext(String kmerText, Node _next) {
            if(next == null){
                next = new ArrayList<Node>();
                nextEdge = new ArrayList<>();
            }
            next.add(_next);
            nextEdge.add(kmerText);
        }
        public int getEdgeCount() {
            return getPreSize() + getNextSize();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(text, node.text);
        }
        @Override
        public int hashCode() {
            return Objects.hash(text);
        }
    }
    public static class Graph{
        final List<Node> nodes;

        public Graph(List<Node> nodes) {
            this.nodes = nodes;
        }
        //may be multi
        public Node findStartNode(){
            int min = Integer.MAX_VALUE;
            Node head = null;
            for (Node n : nodes){
                int c = n.getEdgeCount();
                if(c < min){
                    min = c;
                    head = n;
                }
            }
            return head;
        }
        //多源，包含所有串的 最短字符串。
        //单源， 根据相交的节点 分段。然后再计算距离
        //要包含所有串。重叠的越多，最后的结果串越短. (可能会一些不必要的循环)
        //要包含所有串。a->b的距离 = b的长度 - 重叠的个数. 最短距离
        public List<Node> getMinPath(){
//            int minDistance = Integer.MAX_VALUE;
//            List<Node> ret = null;
//            for (Node n : nodes){
//            }
            return null;
        }
        public List<List<Node>> getGraphs(){
            List<List<Node>> ret = new ArrayList<>();
            for (Node n : nodes){
                ret.addAll(n.generateSequence(n, null));
            }
            return ret;
        }
    }
}
