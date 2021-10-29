import java.util.HashMap;

class TriNode {
    Character c;
    Boolean isLeaf = false;
    HashMap<Character, TriNode> children = new HashMap<>();
    public TriNode() {}
    public TriNode(Character c) {
        this.c = c;
    }
}