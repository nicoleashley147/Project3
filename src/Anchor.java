public class Anchor {

    int row;
    int col;
    Tile anchorTile;
    int prefixCap;
    int postfixCap;
    boolean across;

    public Anchor(int row, int col, Tile anchorTile, int prefixCap, int postfixCap, boolean across) {
        super();
        this.row = row;
        this.col = col;
        this.anchorTile = anchorTile;
        this.prefixCap = prefixCap;
        this.postfixCap = postfixCap;
        this.across = across;
    }
}