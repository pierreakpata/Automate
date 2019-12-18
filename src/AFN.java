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
            States<S> successors=setOfInitialStates;
            States<S> previous=new States<>();
            States<S> temp=new States<>();
            while(!successors.getSetofStates().equals(previous.getSetofStates())){
                for(Letter l: alphabet){
                    temp.addAllStates(transitionRelation.successors(successors,l));
                }
                previous=successors;
                successors=temp;
                temp=new States<>();
            }
           if(isFinally(successors)){
               return true;
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

    /**
     *
     * @return un ensemble d'état pour lesquels il existe un calcul à partir de l'état initial
     */
    public States<S> reachable(){
        Iterator<S> iterator=setOfStates.iterator();
        States<S> results=new States<>();
        while(iterator.hasNext()){
            S state=iterator.next();
            States<S> successors=setOfInitialStates;
            States<S> previous=new States<>();
            States<S> temp= new States<>();
            while(!successors.getSetofStates().equals(previous.getSetofStates())){
                for(Letter l: alphabet){
                    temp.addAllStates(transitionRelation.successors(successors,l));
                }
                previous=successors;
                successors=temp;
                temp=new States<>();
            }
            if(successors.getSetofStates().contains(state)){
                results.addState(state);
            }
        }
        return results;
    }

    /**
     *
     * @param s
     * @return la liste des successeurs d'un état  pour tout alphabet
     */
    private States<S> successorsOfStateForAllLetter(S s){
        States<S> successors=new States<>();
        for(Letter l: alphabet){
            successors.addAllStates(transitionRelation.successor(s,l));
        }
        return successors;
    }

    /**
     *
     * @param s
     * @return true s'il exist au moins un état final parmis les successeurs
     */
    private boolean isFinally(States<S> s){
        Iterator<S> iterator2=s.iterator();
        while(iterator2.hasNext()){
            for(S f: setOfFinalStates.getSetofStates()){
                if(iterator2.next().toString().equals(f.toString())){
                   return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return l'ensemble des états pour lesquels il existe un calcul jusqu'à l'état final
     */
    public States<S> coreachable(){
        Iterator<S> iterator=setOfStates.iterator();
        States<S> results=new States<>();
        while(iterator.hasNext()){
            S state=iterator.next();
            States<S> successors=successorsOfStateForAllLetter(state);
            States<S> previous=new States<>();
            States<S> temp= new States<>();
            while(!successors.getSetofStates().equals(previous.getSetofStates())){
                for(Letter l: alphabet){
                    temp.addAllStates(transitionRelation.successors(successors,l));
                }
                previous=successors;
                successors=temp;
                temp=new States<>();
            }
            if(isFinally(successors)){
                results.addState(state);
            }

        }
        return results;
    }

    /**
     * élimine les états inutiles et leurs transitions
     */
    public void trim(){
        States<S> accessible=reachable();
        States<S> coaccessible=coreachable();
        HashSet<S> deleteState=new HashSet<>();
        for(S s: setOfStates.getSetofStates()){
            if(!accessible.getSetofStates().contains(s) || !coaccessible.getSetofStates().contains(s)){
                HashSet<Transition<S>> transitions=transitionRelation.getSetofTransitions();
                HashSet<Transition<S>> temp=new HashSet<>();
                for(Transition t: transitions){
                    if(t.getSource().toString().equals(s.toString()) || t.getTarget().toString().equals(s.toString())){
                        temp.add(t);
                    }
                }
                transitions.removeAll(temp);
                deleteState.add(s);
            }
        }
        setOfStates.getSetofStates().removeAll(deleteState);
    }


    /**
     *
     * @return automate miroir
     */
    public AFN<S> mirror(){
        States<S> initialStates=setOfFinalStates;
        States<S> finalStates=setOfInitialStates;
        Transitions<S> transitions=new Transitions<>();
        for(Transition t: transitionRelation.getSetofTransitions()){
            transitions.addTransition(new Transition(t.getTarget(), t.getLabel(), t.getSource()));
        }
        AFN<S> afnMirror=new AFN(alphabet, setOfStates, initialStates, finalStates, transitions);
        return afnMirror;
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
