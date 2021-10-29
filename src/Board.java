import java.util.ArrayList;

public class Board {
    Tile[][] tileArr;
    int nBordSize;

    int maxScore ;
    ArrayList<Tile> bestWord;
    Anchor currentAnchor;

    public Board(char[][] board){
        nBordSize = 15;
        tileArr = new Tile[nBordSize][nBordSize];

        for (int row = 0 ; row < nBordSize ; row ++){

            for (int col = 0 ; col < nBordSize ; col ++){
                char ch = board[row][col];
                if(ch == ' ')
                    tileArr[row][col] = new Tile(' ',0,1,1);
                else
                    tileArr[row][col] = new Tile(ch,AIScrable.g_wordTrie.pointsPerLetter.get(ch),1,1);
            }
        }
    }

    public Move processSolution(ArrayList<Tile> tiles){
        maxScore = 0;
        bestWord = new ArrayList<Tile>();
        if(foundAnchors().size() == 0){
            tileArr[6][5] = new Tile('e',1,1,1);
            Anchor an = new Anchor(6,5,tileArr[6][5], 6 , 0 ,false);
            ArrayList<Tile> inputTiles = new ArrayList<Tile>(tiles);
            inputTiles.add(an.anchorTile);
            findBestWord(inputTiles, new ArrayList<Tile>(), "", 0, an);
        }else{
            for (Anchor anchor : foundAnchors()){
                ArrayList<Tile> inputTiles = new ArrayList<Tile>(tiles);
                inputTiles.add(anchor.anchorTile);
                findBestWord(inputTiles, new ArrayList<Tile>(), "", 0, anchor);
            }
        }

        if (bestWord == null || bestWord.size() == 0){

            return null;
        } else {

            int startCol;
            int startRow;
            if (currentAnchor.across){
                startCol = currentAnchor.col - getAnchorPos(currentAnchor, bestWord);
                startRow = currentAnchor.row;
            } else {
                startCol = currentAnchor.col;
                startRow = currentAnchor.row - getAnchorPos(currentAnchor, bestWord);
            }

            return new Move(bestWord , startRow , startCol ,currentAnchor.across, maxScore );

        }

    }

    private void findBestWord(ArrayList<Tile> inputTiles, ArrayList<Tile> tilesToBeUsed, String currentWord, int score, Anchor anchor){
        for (int tileNo = 0 ; tileNo < inputTiles.size() ; tileNo++){
            Tile curTile = inputTiles.get(tileNo);
            if (curTile == null) break;
            if (checkValidPref(currentWord + curTile.letter)	){

                ArrayList<Tile> remainingTiles = new ArrayList<Tile>( inputTiles);
                ArrayList<Tile> tilesInWord = new ArrayList<Tile>(tilesToBeUsed);
                remainingTiles.remove(tileNo);
                tilesInWord.add(curTile);
                findBestWord(remainingTiles, tilesInWord ,currentWord  + curTile.letter, score + curTile.points , anchor);

                if (currentWord.length() >= 7){
                    score += 50;
                }



                //need to check if anchor is in the word before we propose it as an answer
                if (tilesToBeUsed.contains(anchor.anchorTile) || curTile.equals(anchor.anchorTile)){
                    if(checkValidWord(currentWord + curTile.letter)){
                        if (fitBoard(anchor, tilesInWord)){
                            if (maxScore < score + curTile.points){
                                maxScore =  score + curTile.points;
                                bestWord = tilesInWord;
                                currentAnchor = anchor;
                            }
                        }
                    }


                }
            }
        }
    }

    boolean checkValidPref(String prefix){

        if (AIScrable.g_wordTrie.searchPrefix(prefix)){
            return true;
        } else {
            return false;
        }

    }

    private int getAnchorPos(Anchor anchor, ArrayList<Tile> word){
        for (int c = 0 ; c < word.size() ; c++){
            if (word.get(c).letter == anchor.anchorTile.letter){
                return c;
            }
        }
        return -1000;
    }

    private boolean fitBoard(Anchor anchor, ArrayList<Tile> word){
        //check if word would cause spilling off the edge of the board
        int anchorPos = getAnchorPos(anchor, word);
        int prefixLength = anchorPos;
        int posfixLength = word.size() - anchorPos - 1 ;

        if (anchor.prefixCap >= prefixLength && anchor.postfixCap >= posfixLength){
            return true;
        } else {
            return false;
        }
    }

    boolean checkValidWord(String word){
        if(AIScrable.g_wordTrie.searchWord(word))
            return true;
        return false;
    }

    ArrayList<Anchor> foundAnchors(){
        ArrayList<Anchor> anchors = new ArrayList<Anchor>();
        for (int row = 0 ; row < tileArr.length ; row ++){
            for (int col = 0 ; col < tileArr[0].length ; col ++){
                if (tileArr[row][col].letter != ' '){

                    int startCol = col;
                    int endCol = col;

                    //check how far left the word can go without collisions
                    if (col > 0 && tileArr[row][col - 1].letter < 53){
                        while (startCol > 0){
                            if (row != nBordSize - 1 && tileArr[row + 1][startCol - 1].letter != ' '){
                                break;
                            }
                            if ( row != 0 && tileArr[row - 1][startCol - 1].letter != ' '){
                                break;
                            }
                            if (startCol == 1){
                                startCol--;
                                break;
                            }
                            if (tileArr[row    ][startCol -2].letter != ' '){
                                break;
                            }
                            startCol--;
                        }
                    }

                    //check how far right the word can go without collisions
                    if (col < nBordSize - 1 && tileArr[row][col + 1].letter == ' '){
                        while (endCol < nBordSize -1 ){

                            if (row != nBordSize - 1 && tileArr[row + 1][endCol + 1].letter != ' '){
                                break;
                            }
                            if ( row != 0 && tileArr[row - 1][endCol + 1].letter != ' '){
                                break;
                            }
                            if (endCol == nBordSize - 2){
                                endCol++;
                                break;
                            }

                            if (tileArr[row    ][endCol + 2].letter != ' '){
                                break;
                            }

                            endCol++;
                        }
                    }

                    //add the horizontal anchors
                    if (col - startCol > 0 && endCol - col > 0){ // words that can go left or right
                        anchors.add(new Anchor(row, col, tileArr[row][col], col - startCol, endCol - col , true));
                    } else {
                        //if only one then we need to do additional checks
                        if (col - startCol > 0){
                            if (col < nBordSize - 1 && tileArr[row][col + 1].letter == ' '){  // words that can only go left
                                anchors.add(new Anchor(row, col, tileArr[row][col], col - startCol, endCol - col , true));
                            }
                        }
                        if (endCol - col > 0){
                            if (col > 0 && tileArr[row][col - 1 ].letter == ' '){ // words that can only go right
                                anchors.add(new Anchor(row, col, tileArr[row][col], col - startCol, endCol - col , true));
                            }
                        }
                    }

                    //check not at edges - have to re-think algorithm for edges.
                    int startRow = row;
                    int endRow = row;

                    //check how high the word can go without collisions
                    if (row > 0 && tileArr[row - 1][col].letter == ' '){
                        while (startRow > 0){
                            if (col < nBordSize - 1 && tileArr[startRow - 1][col + 1].letter != ' '){
                                break;
                            }
                            if (col > 0 && tileArr[startRow - 1][col - 1].letter != ' '){
                                break;
                            }
                            if (startRow == 1){
                                startRow--;
                                break;
                            }
                            if (tileArr[startRow - 2][col    ].letter != ' '){
                                break;
                            }
                            startRow--;
                        }

                    }

                    //check how low the word can go without collisions
                    if (row < nBordSize - 1 && tileArr[row + 1][col].letter == ' '){
                        while (endRow < nBordSize -1){
                            if (col < nBordSize - 1 && tileArr[endRow + 1][col + 1].letter != ' '){
                                break;
                            }
                            if (col > 0 &&	tileArr[endRow + 1][col - 1].letter != ' '){
                                break;
                            }
                            if (endRow == nBordSize - 2){
                                endRow++;
                                break;
                            }
                            if(tileArr[endRow + 2][col].letter != ' '){
                                break;
                            }

                            endRow++;
                        }
                    }


                    if (row - startRow > 0 && endRow - row > 0){
                        anchors.add(new Anchor(row, col, tileArr[row][col], row - startRow, endRow - row, false));
                    } else{//if only one then we need to do additional checks
                        if (row - startRow > 0){ //words that can only go up
                            if (row < nBordSize-1 && tileArr[row+1][col].letter == ' '){
                                anchors.add(new Anchor(row, col, tileArr[row][col], row - startRow, endRow - row, false));
                            }
                        }
                        if (endRow - row > 0){ //words that can only go down
                            if (row > 0 && tileArr[row-1][col].letter == ' '){
                                anchors.add(new Anchor(row, col, tileArr[row][col], row - startRow, endRow - row, false));
                            }
                        }
                    }


                }
            }
        }
        return anchors;
    }

    public int getScore(){
        int nresult = 0;
        ArrayList<PlayedWordHisotry> newWords = collectNewWords();

        for (PlayedWordHisotry word : newWords){



            int score = word.score;//Dictionary.getWordScore(word.);

            //Scrabble.log.append(player.name + " plays the word " + word.word + " for " + score + " points\n");
            nresult +=score;
            if(word.word.length() >= 7)
                nresult += 50;
            //player.awardPoints(score);
        }

        return  nresult;
    }

    public ArrayList<PlayedWordHisotry> collectNewWords(){
        ArrayList<PlayedWordHisotry> old = AIScrable.wordList;
        ArrayList<PlayedWordHisotry> current = colectWordList();

        for (PlayedWordHisotry oldWord : old){
            for (PlayedWordHisotry curWord : current){
                if (oldWord.word.equals(curWord.word)){
                    current.remove(curWord);
                    break;
                }
            }
        }
        return current;
    }

    public ArrayList<PlayedWordHisotry> colectWordList(){
        ArrayList<PlayedWordHisotry> words = new ArrayList<>();
        words.addAll(collectVerticalWords());
        words.addAll(collectHorizontalWords());
        return words;
    }

    private ArrayList<PlayedWordHisotry> collectVerticalWords(){
        ArrayList<PlayedWordHisotry> vertical = new ArrayList<>();
        Tile[][] TileArr = tileArr;
        StringBuilder curWord = new StringBuilder();
        int multiplier = 1;
        int score = 0;
        for (int col = 0 ; col < nBordSize ; col++){
            for (int row = 0 ; row < nBordSize ; row++){
                if (TileArr[row][col].letter == ' '){
                    if (curWord.length() > 1){
                        vertical.add(new PlayedWordHisotry(curWord.toString(), score * multiplier));
                    }
                    curWord = new StringBuilder();
                    multiplier = 1;
                    score = 0;
                } else {
                    score += TileArr[row][col].points * tileArr[row][col].nLbonus;
                    multiplier *= tileArr[row][col].nWbonus;
                    curWord.append(TileArr[row][col].letter);
                }
            }
            if (curWord.length() > 1){
                vertical.add(new PlayedWordHisotry(curWord.toString(), score * multiplier));
            }
            curWord = new StringBuilder();
            multiplier = 1;
            score = 0;
        }
        return vertical;
    }

    private ArrayList<PlayedWordHisotry> collectHorizontalWords(){
        ArrayList<PlayedWordHisotry> horizontal = new ArrayList<>();
        Tile[][] TileArr = tileArr;
        StringBuilder curWord = new StringBuilder();
        int multiplier = 1;
        int score = 0;
        for (int row = 0 ; row < nBordSize ; row++){
            for (int col = 0 ; col < nBordSize ; col++){
                if (TileArr[row][col].letter == ' '){
                    if (curWord.length() > 1){
                        horizontal.add(new PlayedWordHisotry(curWord.toString(), score * multiplier));
                    }
                    curWord = new StringBuilder();
                    multiplier = 1;
                    score = 0;
                } else {
                    score += TileArr[row][col].points * tileArr[row][col].nLbonus;
                    multiplier *= tileArr[row][col].nWbonus;
                    curWord.append(TileArr[row][col].letter);
                }
            }
            if (curWord.length() > 1){
                horizontal.add(new PlayedWordHisotry(curWord.toString(), score * multiplier));
            }
            curWord = new StringBuilder();
            multiplier = 1;
            score = 0;
        }
        return horizontal;
    }
}
