import java.util.HashSet;
import java.util.Iterator;

public class AFN <S>{

    private HashSet<Letter> alphabet;
    private States<S> setOfStates;
    private States<S> setOfInitialStates;
    private States<S> setOfFinalStates;
    private Transitions<S> transitionRelation;

    public AFN(HashSet alphabet, States<S> setOfStates, States<S> setOfInitialStates, States<S> setOfFinalStates, Transitions<S> transitionRelation){
        this.alphabet=alphabet;
        this.setOfStates=setOfStates;
        this.setOfInitialStates=setOfInitialStates;
        this.setOfFinalStates=setOfFinalStates;
        this.transitionRelation=transitionRelation;
    }


    public boolean recognize(Word w){
        States<S> s=setOfInitialStates;
        for(Letter l: w.getContain()){
            s=transitionRelation.successors(s,l);
            if(s.getSetofStates().isEmpty()){
                return false;
            }
        }
        Iterator<S> iterator=s.iterator();
        while(iterator.hasNext()){
            for(S f: setOfFinalStates.getSetofStates()){
                if(iterator.next().toString().equals(f.toString())){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean emptyLanguage(){
        if(setOfFinalStates.getSetofStates().isEmpty()){
           return true;
        }
        return false;
    }

    public boolean isDeterministic(){
        Iterator<S> states=setOfStates.iterator();
        while(states.hasNext()){
            S state=states.next();
            for(Letter l:  alphabet){
                if(transitionRelation.successor(state, l).getSetofStates().size()>1){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isComplete(){
        Iterator<S> states=setOfStates.iterator();
        while(states.hasNext()){
            S state=states.next();
            for(Letter l:  alphabet){
                if(transitionRelation.successor(state, l).getSetofStates().isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    public void complete(){
        if(!isComplete()){
            State well=new State("I");
            setOfStates.addState((S)well);
            Iterator<S> states=setOfStates.iterator();
            while(states.hasNext()){
                S state=states.next();
                for(Letter l:  alphabet){
                    if(transitionRelation.successor(state, l).getSetofStates().isEmpty()){
                        Transition newTransition=new Transition(state, l, well);
                        transitionRelation.addTransition(newTransition);
                    }
                }
            }
        }
    }

    public HashSet<Letter> getAlphabet() {
        return alphabet;
    }

    public States<S> getSetOfStates() {
        return setOfStates;
    }

    public States<S> getSetOfInitialStates() {
        return setOfInitialStates;
    }

    public States<S> getSetOfFinalStates() {
        return setOfFinalStates;
    }

    public Transitions<S> getTransitionRelation() {
        return transitionRelation;
    }

}
