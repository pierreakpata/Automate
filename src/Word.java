import java.util.ArrayList;
import java.util.Iterator;

public class Word {
    private ArrayList<Letter> contain;

    public ArrayList<Letter> getContain() {
        return contain;
    }

    Iterator<Letter> iterator(){
        return contain.iterator();
    }
}
