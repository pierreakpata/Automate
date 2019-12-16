import java.util.HashSet;
import java.util.Iterator;

/**
 * @author AKPATA Kodjo Pierre
 * @param <S>
 */
public class AFN <S>{

    private HashSet<Letter> alphabet;
    private States<S> setOfStates;
    private States<S> setOfInitialStates;
    private States<S> setOfFinalStates;
    private Transitions<S> transitionRelation;

    /**
     * Constructor
     * @param alphabet
     * @param setOfStates
     * @param setOfInitialStates
     * @param setOfFinalStates
     * @param transitionRelation
     */
    public AFN(HashSet alphabet, States<S> setOfStates, States<S> setOfInitialStates, States<S> setOfFinalStates, Transitions<S> transitionRelation){
        this.alphabet=alphabet;
        this.setOfStates=setOfStates;
        this.setOfInitialStates=setOfInitialStates;
        this.setOfFinalStates=setOfFinalStates;
        this.transitionRelation=transitionRelation;
    }


    /**
     *
     * @param w
     * @return true if the word is recognized
     */
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

    /**
     *
     * @return true if language is empty
     */
    public boolean emptyLanguage(){
        if(setOfInitialStates.getSetofStates().isEmpty()){
            return true;
        }
        else if (setOfFinalStates.getSetofStates().isEmpty()){
           return true;
        }
        else if(!setOfInitialStates.getSetofStates().isEmpty() && !setOfFinalStates.getSetofStates().isEmpty()){
            States<S> s=setOfInitialStates;
            States<S> previous=new States<>();
            States<S> successor=new States<>();
            while(!s.getSetofStates().equals(previous.getSetofStates())){
                for(Letter l: alphabet){
                    successor.addAllStates(transitionRelation.successors(s,l));
                }
                previous=s;
                s=successor;
                successor=new States<>();
            }
            Iterator<S> iterator=s.iterator();
            while(iterator.hasNext()){
                for(S f: setOfFinalStates.getSetofStates()){
                    if(iterator.next().toString().equals(f.toString())){
                        return false;
                    }
                }
            }

        }
        return true;
    }

    /**
     *
     * @return true if automate is deterministic
     */
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

    /**
     *
     * @return true if automate is complete
     */
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

    /**
     * complete the automate
     */
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

    public States<S> reachable(){
        Iterator<S> iterator=setOfStates.iterator();
        States<S> results=new States<>();
        while(iterator.hasNext()){
            S state=iterator.next();
            if(!setOfInitialStates.getSetofStates().contains(state) && !setOfFinalStates.getSetofStates().contains(state)){
                for(Letter a: alphabet){
                    States<S> successors= transitionRelation.successors(setOfInitialStates, a);
                    States<S> previous=new States<>();
                    while(!successors.getSetofStates().equals(previous.getSetofStates())){
                        if(successors.getSetofStates().contains(state)){
                            results.addState(state);
                            break;
                        }
                        previous=successors;
                        successors=transitionRelation.successors(successors, a);
                    }
                }
            }
        }
        return results;
    }

    public States<S> coreachable(){
        Iterator<S> iterator=setOfStates.iterator();
        States<S> results=new States<>();
        while(iterator.hasNext()){
            S state=iterator.next();
            if(!setOfInitialStates.getSetofStates().contains(state) && !setOfFinalStates.getSetofStates().contains(state)){
                for(Letter a: alphabet){
                    States<S> successors= transitionRelation.successor(state, a);
                    States<S> previous=new States<>();
                    while(!successors.getSetofStates().equals(previous.getSetofStates())){
                       for(S s: successors.getSetofStates()){
                           if(setOfFinalStates.getSetofStates().contains(s)){
                               results.addState(state);
                           }
                       }
                        previous=successors;
                        successors=transitionRelation.successors(successors, a);
                    }
                }
            }
        }
        return results;
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
