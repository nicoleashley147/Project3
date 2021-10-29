
import java.util.ArrayList;


public class Move {

    public Move(ArrayList<Tile> tiles, int startRow, int startCol, boolean across, int score) {
        this.tiles = tiles;
        this.startRow = startRow;
        this.startCol = startCol;
        this.across = across;
        this.score = score;
    }

    public Move(Move other) {
        this.tiles = other.tiles;
        this.startRow = other.startRow;
        this.startCol = other.startCol;
        this.across = other.across;
        this.score = other.score;
    }



    ArrayList<Tile> tiles;
    int startRow;
    int startCol;
    boolean across;
    int score;

    void execute(Tile[][] tileArr){

        int row = startRow;
        int col = startCol;

        for (Tile tile: tiles){
            tileArr[row][col].letter = tile.letter;
            tileArr[row][col].points = tile.points;
            if (across){
                col++;
            } else {
                row++;
            }

        }
    }

}
