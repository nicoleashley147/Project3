import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

class WordDataTrie {
    private TriNode root;
    public static HashMap<Character, Integer> pointsPerLetter;

    public WordDataTrie(String path) {
        this.root = new TriNode();
        ReadWordList(path);
        setupLetterScores();
    }

    void ReadWordList(String fileName){
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String word;
            while ( (word  = br.readLine()) != null ){
                insertWord(word.toUpperCase());
            }
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void insertWord(String word) {
        if (word == null) return;
        TriNode cur = root;
        for (char c : word.toCharArray()){
            HashMap<Character, TriNode> child = cur.children;
            if (child.containsKey(c)){
                cur = child.get(c);
            } else{
                cur = new TriNode(c);
                child.put(c, cur);
            }
        }
        cur.isLeaf = true;
    }

    public Boolean searchWord(String word) {
        if (word == null|| word == "") return false;
        word = word.toUpperCase();
        TriNode cur = root;
        for (char c: word.toCharArray()){
            HashMap<Character, TriNode> child = cur.children;
            if (child.containsKey(c)){
                cur = child.get(c);
            } else{
                return false;
            }
        }
        if (cur.isLeaf){
            return true;
        } else {
            return false;
        }
    }

    public Boolean searchPrefix(String word) {
        if (word == null) return false;
        word = word.toUpperCase();
        TriNode cur = root;
        for (char c: word.toCharArray()){
            HashMap<Character, TriNode> child = cur.children;
            if (child.containsKey(c)){
                cur = child.get(c);
            } else{
                return false;
            }
        }
        return true;
    }

    public static int getWordScore(String word){
        int score = 0;
        for (char c : word.toCharArray()){
            score += pointsPerLetter.get(c);
        }
        return score;
    }

    void setupLetterScores(){
        pointsPerLetter = new HashMap<>();
        pointsPerLetter.put('a',1);
        pointsPerLetter.put('b',3);
        pointsPerLetter.put('c',3);
        pointsPerLetter.put('d',2);
        pointsPerLetter.put('e',1);
        pointsPerLetter.put('f',4);
        pointsPerLetter.put('g',2);
        pointsPerLetter.put('h',4);
        pointsPerLetter.put('i',1);
        pointsPerLetter.put('j',8);
        pointsPerLetter.put('k',5);
        pointsPerLetter.put('l',1);
        pointsPerLetter.put('m',3);
        pointsPerLetter.put('n',1);
        pointsPerLetter.put('o',1);
        pointsPerLetter.put('p',3);
        pointsPerLetter.put('q',10);
        pointsPerLetter.put('r',1);
        pointsPerLetter.put('s',1);
        pointsPerLetter.put('t',1);
        pointsPerLetter.put('u',1);
        pointsPerLetter.put('v',4);
        pointsPerLetter.put('w',4);
        pointsPerLetter.put('x',8);
        pointsPerLetter.put('y',4);
        pointsPerLetter.put('z',10);

    }

}
