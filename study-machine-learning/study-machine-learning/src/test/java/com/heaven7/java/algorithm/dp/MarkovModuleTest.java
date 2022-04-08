package com.heaven7.java.algorithm.dp;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MarkovModuleTest {

    List<MarkovModule.State> list1;
    List<MarkovModule.State> list2;
    Map<String,Float> proMap ;

    @Test
    public void test1(){
        prepare();
        MarkovModule module = new MarkovModule(new ArrayList<>(Arrays.asList(list1, list2)), new MarkovModule.Walker() {
            @Override
            public float computeWalkProbability(int step, MarkovModule.State cur, MarkovModule.State next) {
                if(cur == null){
                    return proMap.get(next.getDesc());
                }
                if(cur instanceof MarkovModule.MultipleState){
                    MarkovModule.State s = ((MarkovModule.MultipleState)cur).getLastState();
                    return proMap.get(s.getDesc() + "_" + next.getDesc());
                }
                return proMap.get(cur.getDesc() + "_" + next.getDesc());
            }
        });
        List<MarkovModule.Path> paths = module.walksNext(MarkovModule.DEFAULT_SELECTOR);
        String str = VisitServices.from(paths).mapString().joinToString(", ");
        System.out.println(str);
    }

    @Test
    public void testBackWalk(){
        //同样知晓这个模型，同样是这三件事，我想猜，这三天的天气是怎么样的。
        prepare();
        MarkovModule module = new MarkovModule(new ArrayList<>(Arrays.asList(list1, list2)), new MarkovModule.Walker() {
            @Override
            public float computeWalkProbability(int step, MarkovModule.State cur, MarkovModule.State next) {
                if(cur == null){
                    return proMap.get(next.getDesc());
                }
                if(cur instanceof MarkovModule.MultipleState){
                    MarkovModule.State s = ((MarkovModule.MultipleState)cur).getLastState();
                    return proMap.get(s.getDesc() + "_" + next.getDesc());
                }
                Float f = proMap.get(cur.getDesc() + "_" + next.getDesc());
                Throwables.checkNull(f);
                return f;
            }
        });
        //三天所做的行为分别是：散步、购物、打扫卫生
        String str = VisitServices.from(module.backWalk(Arrays.asList(new MarkovModule.SimpleState("Walk"),
                new MarkovModule.SimpleState("Shop"),
                new MarkovModule.SimpleState("Clean")
        ))).map(new ResultVisitor<MarkovModule.Path, String>() {
            @Override
            public String visit(MarkovModule.Path path, Object o) {
                return path.getLastState().getDesc();
            }
        }).joinToString(" --> ");
        System.out.println("best path: " + str); // Sunny --> Sunny --> Rainy
    }
    private void prepare(){
        list1 = Arrays.asList(new MarkovModule.SimpleState("Rainy"),
                new MarkovModule.SimpleState("Sunny")
        );
        list2 = Arrays.asList(new MarkovModule.SimpleState("Walk"),
                new MarkovModule.SimpleState("Shop"),
                new MarkovModule.SimpleState("Clean")
        );
        proMap = new HashMap<>();

        proMap.put("Rainy", 0.6f);
        proMap.put("Sunny", 0.4f);
        //
        proMap.put("Rainy_Rainy", 0.7f);
        proMap.put("Rainy_Sunny", 0.3f);
        proMap.put("Sunny_Sunny", 0.6f);
        proMap.put("Sunny_Rainy", 0.4f);
        //walk ,shop ,clean
        proMap.put("Rainy_Walk", 0.1f);
        proMap.put("Rainy_Shop", 0.4f);
        proMap.put("Rainy_Clean", 0.5f);

        proMap.put("Sunny_Walk", 0.6f);
        proMap.put("Sunny_Shop", 0.3f);
        proMap.put("Sunny_Clean", 0.1f);
    }
}
