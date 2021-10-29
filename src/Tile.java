
public class Tile {
    char letter;
    int points;
    int nLbonus;
    int nWbonus;

    public Tile(char letter, int points, int nbonus,int wbouse) {
        this.letter = letter;
        this.points = points;
        this.nLbonus = nbonus;
        this.nWbonus = wbouse;
    }

    public boolean isBlank(){
        if (letter == ' ') return true;
        return false;
    }

    public boolean isNumeric(){
        if (letter >= '0' && letter <= '4')return true;
        return false;
    }

    @Override
    public String toString() {
        String str = "";
        if(letter == ' '){
            if(nWbonus > 1)
                str += String.valueOf(nWbonus);
            else
                str += ".";
            if(nLbonus > 1)
                str += String.valueOf(nLbonus );
            else
                str += ".";

        }else{
            str = String.valueOf(letter) + " ";
        }
        return str + " ";
    }

}