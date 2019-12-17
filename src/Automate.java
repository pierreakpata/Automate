import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * @author AKPATA Kodjo Pierre
 */
public class Automate {
    public static void main (String[] args) {
         // création de lettres
         Letter letter1 = new Letter("a");
         Letter letter2 = new Letter("b");

         // création d'états
         State state1 = new State("q_1");
         State state2 = new State("q_2");
         State state3 = new State("q_3");
         //State state4 = new State("q_4");

         // Création d'un objet de type States
         States<State> Q = new States<State>();
         Q.addState(state1);
         Q.addState(state2);
         Q.addState(state3);
         //Q.addState(state4);

         System.out.println(Q.toString());


         //création des transitions
         Transition t1 = new Transition(state1,letter1,state1);
         Transition t2 = new Transition(state1,letter1,state2);
         Transition t3 = new Transition(state1,letter2,state3);
         Transition t4 = new Transition(state3,letter2,state3);
         Transition t5 = new Transition(state3,letter1,state2);
         //Transition t6 = new Transition(state2, letter2, state4);
         //Transition t7 = new Transition(state4, letter1, state3);

         //création de l'ensemble des  transitions
         Transitions<State> Delta = new Transitions<State>();
         Delta.addTransition(t1);
         Delta.addTransition(t2);
         Delta.addTransition(t3);
         Delta.addTransition(t4);
         Delta.addTransition(t5);
         //Delta.addTransition(t6);
         //Delta.addTransition(t7);

        //affiche les successeurs par a de l'état q_1
        States E1 = Delta.successor(state1,letter1);
        System.out.println(E1.toString());

        //affiche les successeurs par a de l'état q_2
        States E2 = Delta.successor(state2,letter1);
        System.out.println(E2.toString());

        //affiche les successeurs par a de l'état q_3
        States E3 = Delta.successor(state3,letter1);
        System.out.println(E3.toString());

        //affiche les successeurs par b de {q_1, q_2}
        HashSet<State> H = new HashSet<State>();
        H.add(state1);
        H.add(state2);
        States<State> S1 = new States<State>(H);
        States E4 = Delta.successors(S1,letter2);
        System.out.println(S1.toString());
        System.out.println(E4.toString());

        //Création de l'automate
        HashSet<Letter> alphabet=new HashSet<>();
        alphabet.add(letter1);
        alphabet.add(letter2);

        States<State> initialStates=new States<>();
        initialStates.addState(state1);

        States<State> finalStates=new States<>();
        finalStates.addState(state3);

        AFN<State> afn=new AFN<>(alphabet, Q, initialStates, finalStates, Delta);

        //Test d'appartenance d'un mot au langage d'automate
        ArrayList<Letter> contain1=new ArrayList<>();
        ArrayList<Letter> contain2=new ArrayList<>();
        contain1.add(letter1);
        contain1.add(letter2);
        contain2.add(letter1);
        contain2.add(letter2);
        contain2.add(letter1);
        Word word1=new Word(contain1);
        Word word2=new Word(contain2);
        System.out.println("ab: "+afn.recognize(word1));
        System.out.println("aba: "+afn.recognize(word2));

        //Test de lanage vide
        System.out.println("Langage vide?: "+afn.emptyLanguage());

        //Test du déterminisme
        System.out.println(afn.isDeterministic());

        //Test du completisme de l'automate
        System.out.println(afn.isComplete());

        //Completation de l'automate
        /*afn.complete();
        System.out.println(afn.isComplete());*/

        //Test de la méthode accessible
        System.out.println(afn.reachable().toString());

        //Test de la méthode co-accessible
        System.out.println(afn.coreachable().toString());

        //Test de la méthode trim
        afn.trim();
        System.out.println("Nouvelles états: "+Q.toString());

        //affiche les successeurs par a de l'état q_1
        States d1 = Delta.successor(state1,letter1);
        System.out.println(d1.toString());

        //affiche les successeurs par a de l'état q_3
        States d2 = Delta.successor(state3,letter1);
        System.out.println(d2.toString());
    }
}
