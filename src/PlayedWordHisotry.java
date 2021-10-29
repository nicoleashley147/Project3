class PlayedWordHisotry {
    @Override
    public String toString() {
        return "PlayedWordHisotry [word=" + word + ", score=" + score + "]";
    }
    public PlayedWordHisotry(String word, int score) {
        this.word = word;
        this.score = score;
    }


    public boolean equals(PlayedWordHisotry other) {
        if (other.word == this.word){
            return true;
        }
        return false;

    }
    String word;
    int score;
}
