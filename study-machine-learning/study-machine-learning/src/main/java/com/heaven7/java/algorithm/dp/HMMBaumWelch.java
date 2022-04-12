package com.heaven7.java.algorithm.dp;

import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// from https://github.com/hamzarawal/HMM-Baum-Welch-Algorithm/blob/master/baum-welch.py
// MarkovModule: Baum-Welch Algorithm
public class HMMBaumWelch {

    private List<List<Float>> transition;
    private List<List<Float>> emission;

    private List<String> states;
    private Map<String, Integer> states_dic;
    private Map<String, Integer> sequence_syms;
    private List<String> sequence;

    private List<String> test_sequence = Arrays.asList("3","3","1","1","2","2","3","1","3");
    private List<Float> start_probs;
    private List<Float> end_probs;

    public void forward_probs(){
        float[][] node_values_fwd = new float[states.size()][test_sequence.size()];
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
                        float v = node_values_fwd[k][i - 1] * emission.get(j).get(seq_sym_val) * transition.get(k).get(j);
                        values.add(v);
                    }
                    node_values_fwd[j][i] = VisitServices.from(values).pile(new PileVisitor<Float>() {
                        @Override
                        public Float visit(Object o, Float f1, Float f2) {
                            return f1 + f2;
                        }
                    });
                }
            }
            /*
             //node_values_fwd[:, -1] 获取最后一列生成数组
             //np.multiply: 对应元素相乘。内积
            * */
            //#end state value
            //end_state = np.multiply(node_values_fwd[:, -1], end_probs)
            //end_state_val = sum(end_state)

        }
    }

    public static class ForwardRet{
        float[][] node_values_fwd;
        float end_state_val;
    }
}
