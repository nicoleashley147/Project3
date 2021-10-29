import java.util.ArrayList;

public class Hand {

    private ArrayList<Character> lettersInHand;

    public Hand() {

        this.lettersInHand = new ArrayList<>();

    }

    public void add(Character add) {

        this.lettersInHand.add(add);

    }

    public Character get(int index) {

        if(index >= 0 && index < lettersInHand.size()) {
            return this.lettersInHand.get(index);
        }else {
            return ' ';
        }

    }

    public Character remove(int index) {

        if(index >= 0 && index < lettersInHand.size()) {
            return this.lettersInHand.remove(index);
        }else {
            return ' ';
        }

    }

    public void set(int index, Character newValue) {

        if(index >= 0 && index < lettersInHand.size()) {
            this.lettersInHand.set(index, newValue);
        }

    }

    public void clear() {

        this.lettersInHand = new ArrayList<>();

    }

    public int size() {

        return this.lettersInHand.size();

    }

    public boolean remove(Character object) {

        return this.lettersInHand.remove(object);

    }

    public ArrayList<Character> getHandData(){

        return this.lettersInHand;

    }

}
