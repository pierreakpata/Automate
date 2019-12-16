import java.util.HashSet;
import java.util.Iterator;

/**
 * @author AKPATA Kodjo Pierre
 * @param <S>
 */
public class Transitions<S>
{
    private HashSet<Transition<S>> SetofTransitions; 
    
    /**
     * Constructeur d'objets de classe Transitions initialement vide
     */
    public Transitions()
    {
      this.SetofTransitions = new HashSet<Transition<S>>();  
    }
    
    /**
     * Constructeur d'objets de classe Transitions à partir d'un ensemble de transitions
     */
    public Transitions(HashSet<Transition<S>> T)
    {
      this.SetofTransitions = T;  
    }

    
    /**
     * Ajout d'une transition 
     */
    public void addTransition(Transition<S> t)
    {
      this.SetofTransitions.add(t);   
    }
    
    /**
     * Retourne l'ensemble des transitions représenté  
     */
    public HashSet<Transition<S>> getSetofTransitions()
    {
      return this.SetofTransitions;   
    }
    
    /**
     * Successeurs de l'état s et de la lettre a par les transitions
     */
    States<S> successor(S s, Letter a)
    {
     States<S> Targets = new States<S>(); 
     Iterator<Transition<S>> AllTransitions = this.SetofTransitions.iterator();
     while(AllTransitions.hasNext()){
         Transition<S> transition= AllTransitions.next();
         if(transition.getSource().toString().equals(s.toString()) &&  transition.getLabel().toString().equals(a.toString())){
             Targets.addState(transition.getTarget());
         }
     }
     return Targets; 
    }
    
 /**
     * successeurs de l'ensemble d'états S et de la lettre a par les 
     * transitions
     */  
    States<S> successors(States<S> set, Letter a)
    {
        States<S> Targets = new States<S>();
        Iterator<S> state=set.iterator();
        while(state.hasNext()){
            Targets.addAllStates(successor(state.next(), a));
        }
        return Targets;
    }
    
 }
