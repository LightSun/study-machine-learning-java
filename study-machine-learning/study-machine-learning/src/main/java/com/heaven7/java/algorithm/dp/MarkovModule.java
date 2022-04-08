package com.heaven7.java.algorithm.dp;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.FissionVisitor;
import com.heaven7.java.visitor.ResultIndexedVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.item.IStringItem;

import java.util.*;

/**
 * 马尔可夫模型/ 隐马尔可夫模型: https://www.cnblogs.com/mantch/p/11203748.html
 * */
public class MarkovModule {

    public static final Comparator<Path> DEFAULT_PATH_COMPARATOR = new Comparator<Path>(){
        @Override
        public int compare(Path o1, Path o2) {
            return Float.compare(o1.probability, o2.probability);
        }
    };
    public static final Selector DEFAULT_SELECTOR = new Selector(){
        @Override
        public Path select(int step, List<Path> nextPaths, List<Path> multiplePaths) {
            List<MarkovModule.Path> all = new ArrayList<>(nextPaths);
            if(multiplePaths != null){
                all.addAll(multiplePaths);
            }
            return VisitServices.from(all).max(DEFAULT_PATH_COMPARATOR);
        }
    };

    private final List<Path> mSelectStates = new ArrayList<>();
    private final List<List<State>> statePaths;
    private final Walker walker;
    private int pathIndex = -1;

    public MarkovModule(List<List<State>> statePaths, Walker walker) {
        this.statePaths = statePaths;
        this.walker = walker;
    }

    public int getCurrentPathIndex() {
        return pathIndex;
    }
    public void reset(){
        mSelectStates.clear();
        pathIndex = -1;
    }

    public Path testWalkNext(Selector selector){
        return walkNext(selector, false);
    }
    public List<Path> walksNext(Selector selector){
        return walksNext(selector, true);
    }
    public List<Path> walksNext(Selector selector, boolean includeCurStateGroup){
        for (;;){
            if(walkNext(selector, includeCurStateGroup, true) == null){
                break;
            }
        }
        return mSelectStates;
    }

    public Path walkNext(Selector selector, boolean saveState){
        return walkNext(selector, true, saveState);
    }
    @SuppressWarnings("unchecked")
    public Path walkNext(Selector selector, boolean includeCurStateGroup, boolean saveState){
        final int cur = pathIndex;
        final int next = pathIndex + 1;
        if(next >= statePaths.size()){
            return null;
        }
        final List<State> nextStates = statePaths.get(next);
        final State curState = cur >= 0 ? mSelectStates.get(cur).getLastState() : null;
        final List<State> curStates = cur >= 0 ? statePaths.get(cur) : null;
        //self-group then next
        List<Path> multiplePaths = null;
        if(curStates != null && includeCurStateGroup){
            multiplePaths = VisitServices.from(curStates).fission(nextStates, new FissionVisitor<State, Path>() {
                @Override
                public List<Path> visit(State curS, Object o) {
                    List<State> ls = (List<State>) o;
                    return VisitServices.from(ls).map(new ResultVisitor<State, Path>() {
                        @Override
                        public Path visit(State nextS, Object o) {
                            MultipleState s = new MultipleState(curS, nextS);
                            float pro = walker.computeWalkProbability(next, curState, curS) *
                                    walker.computeWalkProbability(next, curS, nextS);
                            return new Path(s, pro);
                        }
                    }).getAsList();
                }
            }).getAsList();
        }
        //next
        List<Path> nextPaths = VisitServices.from(nextStates).mapIndexed(null, new ResultIndexedVisitor<State, Path>() {
            @Override
            public Path visit(Object o, State state, int size, int index) {
                float pro = walker.computeWalkProbability(next, curState, state);
                return new Path(state, pro);
            }
        }).getAsList();
        Path selectPath = selector.select(next, nextPaths, multiplePaths);
        if(selectPath == null ){
            return null;
        }
        if(saveState){
            pathIndex ++;
            mSelectStates.add(selectPath);
        }
        return selectPath;
    }
    //隐含马尔可夫模型 -> 反推. (维特比算法(Viterbi))
    public List<Path> backWalk(List<State> actions){
        Throwables.checkEmpty(actions);
        //case ->action, case -> action , ...
        reset();
        //默认 statePaths[1] 表示动作状态
        int times = actions.size();
        List<Path> paths0 = VisitServices.from(statePaths.get(0)).map(new ResultVisitor<State, Path>() {
            @Override
            public Path visit(State state, Object o) {
                return new Path(state, walker.computeWalkProbability(0, null, state));
            }
        }).getAsList();
        final List<Path> lastPaths = new ArrayList<>(paths0);
        List<Path> proPaths = new ArrayList<>();
        for (int i = 0 ; i < times ; i ++){
            final State actionState = actions.get(i);
            final int id = i;
            final List<Path> tmpPaths = new ArrayList<>();
            Path maxPath = VisitServices.from(lastPaths).map(new ResultVisitor<Path, Path>() {
                @Override
                public Path visit(Path v, Object o) {
                    if(id > 0){
                        return VisitServices.from(new ArrayList<>(lastPaths)).map(new ResultVisitor<Path,Path>() {
                            @Override
                            public Path visit(Path path, Object o) {
                                float pro0 = v.probability * walker.computeWalkProbability(id, v.getLastState(), path.getLastState()) *
                                        walker.computeWalkProbability(id, path.getLastState(), actionState);
                                MultipleState ms = new MultipleState(Arrays.asList(v.getLastState(), path.getLastState()));
                                return new Path(ms, pro0);
                            }
                        }).max(DEFAULT_PATH_COMPARATOR);
                    }else{
                        final float pro = walker.computeWalkProbability(id, v.getLastState(), actionState);
                        return new Path(v.getLastState(), pro);
                    }
                }
            }).save(tmpPaths).max(DEFAULT_PATH_COMPARATOR);
            lastPaths.clear();
            lastPaths.addAll(tmpPaths);
            proPaths.add(maxPath);
        }
        return proPaths;
    }
    public static class Path implements IStringItem {
        public final State state;
        public final float probability;

        public Path(State state, float probability) {
            this.state = state;
            this.probability = probability;
        }
        public State getLastState(){
            return state instanceof MultipleState ? ((MultipleState) state).getLastState() : state;
        }
        @Override
        public String getString() {
            return state.getDesc();
        }
    }
    public interface State{
        String getDesc();
    }
    public static class MultipleState implements State{
        public List<State> states;
        public MultipleState(State s1, State s2) {
            states = Arrays.asList(s1, s2);
        }
        public MultipleState(List<State> s) {
            states = s;
        }
        public State getLastState(){
            return states.get(states.size() - 1);
        }
        @Override
        public String getDesc() {
            return VisitServices.from(states).map(new ResultVisitor<State, String>() {
                @Override
                public String visit(State state, Object o) {
                    return state.getDesc();
                }
            }).joinToString("*");
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MultipleState that = (MultipleState) o;
            return Objects.equals(states, that.states);
        }
        @Override
        public int hashCode() {
            return Objects.hash(states);
        }
    }
    public static class SimpleState implements State{
        private final String desc;
        public SimpleState(String desc) {
            this.desc = desc;
        }
        @Override
        public String getDesc() {
            return desc;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimpleState that = (SimpleState) o;
            return Objects.equals(desc, that.desc);
        }
        @Override
        public int hashCode() {
            return Objects.hash(desc);
        }
    }
    public interface Walker{
        float computeWalkProbability(int step, @Nullable State cur, State next);
    }
    public interface Selector{
        /**
         * select the path from sta next paths or multiple paths.
         * @param step the step index start from 0
         * @param nextPaths the nextPaths
         * @param multiplePaths the multiplePaths. which are from walk cur state group then to next
         * @return the select path.
         */
        Path select(int step, List<Path> nextPaths, @Nullable List<Path> multiplePaths);
    }
}
