import java.util.ArrayList;

public class AIScrable {

    public static ArrayList<PlayedWordHisotry> wordList = new ArrayList<>();
    public static WordDataTrie g_wordTrie;
    public Board m_board;
    public static int mscore;

    public AIScrable(String dicpath){
        g_wordTrie = new WordDataTrie(dicpath);
    }

    public char[][] execute(char [][] curBoard) {

        ArrayList<Tile> rarcklst = new ArrayList<>();
        for (int i = 0; i < ScrabbleDriver.currentHand.getComputerHand().size(); i++) {

            char ch = ScrabbleDriver.currentHand.getComputerHand().get(i);
            rarcklst.add(new Tile(ch, g_wordTrie.pointsPerLetter.get(ch),1,1));

        }
        m_board = new Board(curBoard);
        wordList = m_board.colectWordList();
        mscore = 0;
        Move solution = m_board.processSolution(rarcklst);
        solution.execute(m_board.tileArr);

        int row = solution.startRow;
        int col = solution.startCol;

        for (Tile tile: solution.tiles){
            curBoard[row][col] = tile.letter;

            if (solution.across){
                col++;
            } else {
                row++;
            }

        }
        mscore = m_board.getScore();
        return curBoard;
    }


}
