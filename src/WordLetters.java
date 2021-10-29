public class WordLetters {

    private Hand lettersInHand;
    private Hand currentLettersInHand;

    public WordLetters() {

        lettersInHand = new Hand();
        currentLettersInHand = new Hand();

        init();

    }

    public Hand getLettersInHand() {

        return lettersInHand;

    }

    public Hand getCurrentLettersInHand() {

        return currentLettersInHand;

    }

    public void setLettersInHand(Hand lettersInHand) {

        this.lettersInHand = lettersInHand;

    }

    public void setCurrentLettersInHand(Hand currentLettersInHand) {

        this.currentLettersInHand = currentLettersInHand;

    }

    private void init() {

        for (int i = 0; i < 9; i++)
            lettersInHand.add('a');
        for (int i = 9; i < 11; i++)
            lettersInHand.add('b');
        for (int i = 11; i < 13; i++)
            lettersInHand.add('c');
        for (int i = 13; i < 17; i++)
            lettersInHand.add('d');
        for (int i = 17; i < 29; i++)
            lettersInHand.add('e');
        for (int i = 29; i < 31; i++)
            lettersInHand.add('f');
        for (int i = 31; i < 34; i++)
            lettersInHand.add('g');
        for (int i = 34; i < 36; i++)
            lettersInHand.add('h');
        for (int i = 36; i < 45; i++)
            lettersInHand.add('i');
        lettersInHand.add('j');
        lettersInHand.add('k');
        for (int i = 47; i < 51; i++)
            lettersInHand.add('l');
        for (int i = 51; i < 53; i++)
            lettersInHand.add('m');
        for (int i = 53; i < 59; i++)
            lettersInHand.add('n');
        for (int i = 59; i < 67; i++)
            lettersInHand.add('o');
        for (int i = 67; i < 69; i++)
            lettersInHand.add('p');
        lettersInHand.add('q');
        for (int i = 70; i < 76; i++)
            lettersInHand.add('r');
        for (int i = 76; i < 80; i++)
            lettersInHand.add('s');
        for (int i = 80; i < 86; i++)
            lettersInHand.add('t');
        for (int i = 86; i < 90; i++)
            lettersInHand.add('u');
        for (int i = 90; i < 92; i++)
            lettersInHand.add('v');
        for (int i = 92; i < 94; i++)
            lettersInHand.add('w');
        lettersInHand.add('x');
        for (int i = 95; i < 97; i++)
            lettersInHand.add('y');
        lettersInHand.add('z');
        for (int i = 98; i < 100; i++)
            lettersInHand.add(' ');

    }

}