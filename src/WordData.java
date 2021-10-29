import java.util.ArrayList;

public class WordData {

    // Attributes
    private ArrayList<String> dictionaryWords;
    private static WordData instance;

    // file name
    private static final String FILE_NAME = "src/SOWPODS.txt";

    // instance
    public static WordData getInstance() {

        if(instance == null) {
            instance = new WordData(FILE_NAME);
        }

        return instance;

    }

    private WordData(String filename) {

        this.dictionaryWords = DataCollector.getWordsData(filename);

    }

    public ArrayList<String> getWordsData() {

        return dictionaryWords;

    }

    public void setWordsData(ArrayList<String> words) {

        this.dictionaryWords = words;

    }

    public boolean isWordExists(String word) {

        int currentIndex = dictionaryWords.size() / 2;
        int size = dictionaryWords.size();
        int initial = 0;
        boolean flag = false;

        do {

            int result = dictionaryWords.get(currentIndex).compareTo(word.toLowerCase());
            if(result > 0) {
                size = currentIndex;
                currentIndex /= 2;
            } else if(result < 0) {
                initial = currentIndex + 1;
                currentIndex = ((currentIndex + size) / 2);
            } else {
                flag = true;
                break;
            }

        } while (initial < size);

        return flag;

    }

}
