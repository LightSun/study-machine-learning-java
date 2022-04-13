package com.heaven7.java.algorithm.dp;

import com.heaven7.java.mat.FloatMat2;
import com.heaven7.java.mat.Mat2;
import com.heaven7.java.mat.Mat3;
import com.heaven7.java.mat.MatUtils;
import com.heaven7.java.utils.ArrayUtils;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.*;

import static com.heaven7.java.visitor.PileVisitor.FLOAT_ADD;

/**
 * """
 * //from: https://github.com/hamzarawal/HMM-Baum-Welch-Algorithm
 * Baum-Welch 算法
 * 用 Python 实现 Baum-Welch (Forward-Backward) 算法的 Numpy 实现。
 *
 * 该算法可以针对任意数量的状态和观察结果运行。
 * 默认示例有两个状态 (H&C) 和三个可能的生成值（emission），即 1、2 和 3。
 * 以下是需要调整的矩阵/变量：
 *
 *     Transition：     包含初始转换概率（状态转移矩阵）。[0,0] 处的值是从 H 到 H 的转换，[1,1] 处的值是从 C 到 C 的转换。（注意默认示例）
 *     emission：       包含初始概率(生成概率矩阵)。[0,0] 处的值是从 H 观测符号 1的概率，而在 [0,1] 处的值是从 H 观测符号 2的概率。（注意默认示例）
 *     states_dic(not use)：     包含每个状态对应 数字/索引的字典。该值从 0 开始，上升到每个状态的状态数
 *     states:          状态
 *     sequence_syms：  简单的所有可能观察及其相应索引的字典。索引从 0 开始并随着每次观察而增加
 *     sequence：       是所有可能观察的列表
 *     test_sequence：  一个字符串，包含我们要训练矩阵的序列
 *     threshold value： 可以在代码的最后调整停止算法的阈值
 * """
 */
public class HMMBaumWelchDemo {

    private List<List<Float>> transition;
    private List<List<Float>> emission;

    private List<String> states;
    //private Map<String, Integer> states_dic;
    private Map<String, Integer> sequence_syms;
    private List<String> sequence;

    private List<String> test_sequence;
    private List<Float> start_probs;
    private List<Float> end_probs;

    @SuppressWarnings("unchecked")
    public HMMBaumWelchDemo(){
        //transition probabilities. 2个状态 各自转移的概率。
        transition = (List<List<Float>>) new FloatMat2(new float[][]{
                {0.8f, 0.1f},
                {0.1f, 0.8f},
        }).toWrapperMat().getMat();
        //Emission probabilities. 2个状态生成 3个符号（ 1, 2 ，3）的概率.
        emission = (List<List<Float>>) new FloatMat2(new float[][]{
                {0.1f, 0.2f, 0.7f},
                {0.7f, 0.2f, 0.1f},
        }).toWrapperMat().getMat();
        //#defining states and sequence symbols
        states = Arrays.asList("H", "C");
//        states_dic = new HashMap<>();
//        states_dic.put("H", 0);
//        states_dic.put("C", 1);
        //
        sequence_syms = new HashMap<>(); // 和emission列数相等
        sequence_syms.put("1", 0);
        sequence_syms.put("2", 1);
        sequence_syms.put("3", 2);
        sequence = Arrays.asList("1", "2", "3");
        // test sequence
        test_sequence = Arrays.asList("3","3","1","1","2","2","3","1","3");
        //#probabilities of going to end state
        end_probs = Arrays.asList(0.1f, 0.1f);
        //#probabilities of going from start state
        start_probs = Arrays.asList(0.5f, 0.5f);
    }

    public StepState forward_probs(){
        float[][] node_values_fwd = new float[states.size()][test_sequence.size()];
        FloatMat2 fm = new FloatMat2(node_values_fwd);
        for (int i = 0 ; i < test_sequence.size() ; i ++){
            String sequence_val = test_sequence.get(i);
            Integer seq_sym_val = sequence_syms.get(sequence_val);
            for (int j = 0 ; j < states.size() ; j ++){
                //# if first sequence value then do this
                if (i == 0){
                    //node_values_fwd[j, i] = start_probs[j] * emission[j, sequence_syms[sequence_val]]
                    node_values_fwd[j][i] = start_probs.get(j) * emission.get(j).get(seq_sym_val);
                }else{
                   /*
                    values = [node_values_fwd[k, i - 1] * emission[j, sequence_syms[sequence_val]] * transition[k, j] for k in
                    range(len(states))]
                    node_values_fwd[j, i] = sum(values)
                    */
                    List<Float> values = new ArrayList<>();
                    for(int k = 0 ; k < states.size() ; k ++){
                        //node_values_fwd[k][i - 1] = fm.getElement(k, i - 1)
                        float v = fm.getElement(k, i - 1) * emission.get(j).get(seq_sym_val) * transition.get(k).get(j);
                        values.add(v);
                    }
                    node_values_fwd[j][i] = VisitServices.from(values).pile(FLOAT_ADD);
                }
            }
        }
        /*
         //node_values_fwd[:, -1] 获取最后一列生成数组
         //np.multiply: 对应元素相乘。内积
        * */
        //#end state value
        //end_state = np.multiply(node_values_fwd[:, -1], end_probs)
        //end_state_val = sum(end_state)
        //fm.getLastColumn()
        List<Float> end_state = ArrayUtils.multiplyF(fm.getLastColumn(), end_probs);
        float end_state_val = VisitServices.from(end_state).pile(FLOAT_ADD);
        return new StepState(fm, end_state_val);
    }

    public StepState backward_probs(){
        // # node values stored during forward algorithm
        //node_values_bwd = np.zeros((len(states), len(test_sequence)))
        float[][] node_values_bwd = new float[states.size()][test_sequence.size()];
        FloatMat2 fm = new FloatMat2(node_values_bwd);
        for (int i = 1 ; i < test_sequence.size() + 1; i ++) {
            for (int j = 0; j < states.size(); j++) {
                if( i == 1){
                    fm.setElement(j, -1, end_probs.get(j));
                }else{
                    //values = [node_values_bwd[k, -i+1] * emission[k, sequence_syms[test_sequence[-i+1]] ] * transition[j, k] for k in range(len(states))]
                    // node_values_bwd[j, -i] = sum(values)
                    List<Float> values = new ArrayList<>();
                    String sequence_val = test_sequence.get(MatUtils.getIndex(test_sequence.size(), -i + 1));
                    Integer seq_idx = sequence_syms.get(sequence_val);
                    for(int k = 0 ; k < states.size() ; k ++){
                        float v = fm.getElement(k, - i + 1) * emission.get(k).get(seq_idx) * transition.get(j).get(k);
                        values.add(v);
                    }
                    fm.setElement(j, -i, VisitServices.from(values).pile(FLOAT_ADD));
                }
            }
        }
        /*
         start_state = [node_values_bwd[m,0] * emission[m, sequence_syms[test_sequence[0]]] for m in range(len(states))]
    start_state = np.multiply(start_state, start_probs)
    start_state_val = sum(start_state)
          */
        List<Float> start_state = new ArrayList<>();
        String sequence_val = test_sequence.get(0);
        Integer seq_idx = sequence_syms.get(sequence_val);
        for(int m = 0 ; m < states.size() ; m ++){
            float v = node_values_bwd[m][0] * emission.get(m).get(seq_idx);
            start_state.add(v);
        }
        start_state = ArrayUtils.multiplyF(start_state, start_probs);
        float start_state_val = VisitServices.from(start_state).pile(FLOAT_ADD);
        return new StepState(fm, start_state_val);
    }

    public Mat3<Float> si_probs(FloatMat2 forward, FloatMat2 backward, float forward_val){
        final int ssize = states.size();
        Mat3<Float> si_probs = new Mat3<Float>(ssize, test_sequence.size() - 1, ssize, 0f);
        Mat2<Float> transition = new Mat2<>(this.transition);
        Mat2<Float> emission = new Mat2<>(this.emission);
        float val;
        for(int i = 0; i < test_sequence.size() - 1; i ++){
            Integer seq_idx = sequence_syms.get(test_sequence.get(i + 1));
            for(int j = 0; j < ssize; j ++){
                Float f_val = forward.getElement(j, i);
                for(int k = 0 ; k <ssize ; k ++){
                    Float b_val = backward.getElement(k, i + 1);
                    val = f_val * b_val * transition.getElement(j, k) * emission.getElement(k, seq_idx) / forward_val;
                    // si_probabilities[j,i,k] = ( forward[j,i] * backward[k,i+1] * transition[j,k] * emission[k,sequence_syms[test_sequence[i+1]]] ) / forward_val
                    si_probs.setElement(j, i, k, val);
                }
            }
        }
        return si_probs;
    }

    public Mat2<Float> gamma_probs(FloatMat2 forward, FloatMat2 backward, float forward_val){
        int ssize = states.size();
        Mat2<Float> gamma_probs = new Mat2<Float>(ssize, test_sequence.size(), 0f);
        for (int i = 0 ; i < test_sequence.size() ; i ++){
            for (int j = 0 ; j < ssize ; j ++){
                //#gamma_probabilities[j,i] = ( forward[j,i] * backward[j,i] * emission[j,sequence_syms[test_sequence[i]]] ) / forward_val
                //gamma_probabilities[j, i] = (forward[j, i] * backward[j, i]) / forward_val
                float val = forward.getElement(j, i) * backward.getElement(j, i) / forward_val;
                gamma_probs.setElement(j, i, val);
            }
        }
        return gamma_probs;
    }

    @SuppressWarnings("unchecked")
    public void run(int count){
        final ResultVisitor<Float, Float> vit_divide = new ResultVisitor<Float, Float>() {
            @Override
            public Float visit(Float t, Object p) {
                return t / (Float)p;
            }
        };
        final int ssize = states.size();
        for (int iteration = 0 ; iteration < count ; iteration ++){
            System.out.println("-------------------------------------");
            System.out.println("Iteration No: " + (iteration + 1));
            StepState forward_state = forward_probs();
            StepState backward_state = backward_probs();
            Mat3<Float> si_probs = si_probs(forward_state.fm, backward_state.fm, forward_state.end_state_val);
            Mat2<Float> gamma_probs = gamma_probs(forward_state.fm, backward_state.fm, forward_state.end_state_val);

            // #caclculating 'a' and 'b' matrices
            Mat2<Float> a = new Mat2<Float>(ssize, ssize, 0f);
            Mat2<Float> b = new Mat2<Float>(ssize, sequence_syms.size(), 0f);

            //# 'a' mat
            for (int j = 0 ; j < ssize ; j ++){
                for(int i = 0; i < ssize ; i ++){
                    for(int t = 0 ; t < test_sequence.size() - 1 ; t ++){
                        float v = a.getElement(j, i) + si_probs.getElement(j, t, i);
                        a.setElement(j, i, v) ;
                    }
                    List<Float> denomenator_a = new ArrayList<>();
                    for (int t_x = 0 ; t_x < test_sequence.size() - 1 ; t_x ++){
                        for (int i_x = 0 ; i_x < ssize ; i_x ++){
                            denomenator_a.add(si_probs.getElement(j, t_x, i_x));
                        }
                    }
                    Float total = VisitServices.from(denomenator_a).pile(FLOAT_ADD);
                    if(total == 0){
                        a.setElement(j, i, 0f);
                    }else{
                        a.readWriteElement(j, i, total, vit_divide);
                    }
                }
            }

            // 'b' mat
            for(int j = 0 ; j < ssize ; j ++ ){
                for (int i = 0 ; i < sequence.size() ; i ++){
                    List<Integer> indices = new ArrayList<>();
                    for(int t = 0 ; t < test_sequence.size() ; t ++){
                        if(test_sequence.get(t).equals(sequence.get(i))){
                            indices.add(t);
                        }
                    }
                    float numerator_b = VisitServices.from(gamma_probs.getColumns(j, indices)).pile(FLOAT_ADD);
                    float denomenator_b = VisitServices.from(gamma_probs.getRow(j)).pile(FLOAT_ADD);
                    if(denomenator_b == 0){
                        b.setElement(j, i, 0f);
                    }else{
                        b.setElement(j , i, numerator_b / denomenator_b);
                    }
                }
            }
            //print a, b
            System.out.println("Mat a: " + a);
            System.out.println("Mat b: " + b);
            transition = (List<List<Float>>) a.getMat();
            emission = (List<List<Float>>) b.getMat();

            StepState forward_state_new = forward_probs();
            System.out.println("New forward probability: " + forward_state_new.end_state_val);
            float diff = Math.abs(forward_state.end_state_val - forward_state_new.end_state_val);
            System.out.println("Difference in forward probability: " + diff);
            if(diff < 0.0000001){
                break;
            }
        }
    }

    public static void main(String[] args) {
        new HMMBaumWelchDemo().run(2000);
    }

    private static class StepState {
        FloatMat2 fm;
        float end_state_val;

        public StepState(FloatMat2 fm, float end_state_val) {
            this.fm = fm;
            this.end_state_val = end_state_val;
        }
    }
}
