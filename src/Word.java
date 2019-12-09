import java.util.ArrayList;
import java.util.Iterator;

public class Word {
    private ArrayList<Letter> contain;

    public Word(ArrayList<Letter> contain){
        this.contain=contain;
    }

    public ArrayList<Letter> getContain() {
        return contain;
    }

    Iterator<Letter> iterator(){
        return contain.iterator();
    }
}
