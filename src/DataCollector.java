import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class DataCollector {

    // to get words data
    public static ArrayList<String> getWordsData(String filename){

        ArrayList<String> wordsData = new ArrayList<String>();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String word = "";
            while((word = reader.readLine()) != null) {
                wordsData.add(word);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return wordsData;

    }

}
