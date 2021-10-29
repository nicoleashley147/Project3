public class CurrentHand {

    // Attributes..
    private Hand playerHand;
    private Hand computerHand;
    private Hand currentComputerHand;
    private Hand currentPlayerHand;

    public CurrentHand() {

        playerHand = new Hand();
        computerHand = new Hand();
        currentComputerHand = new Hand();
        currentPlayerHand = new Hand();

    }

    public Hand getPlayerHand() {

        return playerHand;

    }

    public Hand getComputerHand() {

        return computerHand;

    }

    public Hand getCurrentComputerHand() {

        return currentComputerHand;

    }

    public Hand getCurrentPlayerHand() {

        return currentPlayerHand;

    }



}